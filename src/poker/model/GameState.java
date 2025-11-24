package poker.model;

import java.util.ArrayList;

public class GameState {

    // phase constants - track what part of the game it is
    private static final int PHASE_PREFLOP  = 0;
    private static final int PHASE_FLOP     = 1;
    private static final int PHASE_TURN     = 2;
    private static final int PHASE_RIVER    = 3;
    private static final int PHASE_SHOWDOWN = 4;

    // game data
    private ArrayList<Player> players; // list of players
    private CommunityCards community; // cards that dealer deals
    private Deck deck; // deck of cards

    // track whose turn it is and what the state is
    private int currentPlayerIndex; 
    private boolean waitingForPlayerStart;
    private boolean waitingForAction;
    private boolean waitingForEndTurn;

    // Pot and betting info
    private int pot;         // full pot total
    private int potGold;     // dollars from gold chips $1
    private int potRed;      // dollars from red chips $5

    private int currentBet;  // highest bet in  round

    // player round info
    private int[] playerBets;       // total money each player bet this round
    private int[] playerGoldRound;  // amount of gold chip dollars used
    private int[] playerRedRound;   // amount of red chip dollars used
    private boolean[] hasActed;     // has player acted this round or not

    private boolean anyBetThisRound;

    // Messages + winner
    // feel free to change this 
    private String lastMessage;
    private String winnerName;

    // Game phase
    private int phase;

    
    public GameState(ArrayList<Player> players, CommunityCards community, Deck deck) {
        this.players = players;
        this.community = community;
        this.deck = deck;

        int n = players.size();
        playerBets = new int[n];
        playerGoldRound = new int[n];
        playerRedRound = new int[n];
        hasActed = new boolean[n];

        resetWholeGameState();
    }

    
    private void clearMessage() {
        lastMessage = null;
    }

    private boolean playerIsFolded(int i) {
        return players.get(i).isFolded();
    }

    private int totalPlayers() {
        return players.size();
    }

    // getters
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public boolean isWaitingForPlayerStart() { return waitingForPlayerStart; }
    public boolean isWaitingForAction() { return waitingForAction; }
    public boolean isWaitingForEndTurn() { return waitingForEndTurn; }
    public boolean isShowdownPhase() { return phase == PHASE_SHOWDOWN; }

    public int getPot() { return pot; }
    public int getPotGold() { return potGold; }
    public int getPotRed() { return potRed; }

    public int getCurrentBet() { return currentBet; }
    public String getWinnerName() { return winnerName; }
    public String getLastMessage() { return lastMessage; }

    public int getPlayerRoundTotal(int i) { return i < 0 ? 0 : playerBets[i]; }
    public int getPlayerRoundGold(int i)  { return i < 0 ? 0 : playerGoldRound[i]; }
    public int getPlayerRoundRed(int i)   { return i < 0 ? 0 : playerRedRound[i]; }

    // starting a new hand
    public void startNewHand() {

        // reset pot and bets
        pot = 0;
        potGold = 0;
        potRed = 0;
        currentBet = 0;
        phase = PHASE_PREFLOP;
        winnerName = null;

        // reset each player state
        for (int i = 0; i < players.size(); i++) {
            playerBets[i] = 0;
            playerGoldRound[i] = 0;
            playerRedRound[i] = 0;
            hasActed[i] = false;
            players.get(i).resetForNewHand();
        }

        // new deck + community reset
        deck = new Deck();
        community.reset();

        // deal hole cards
        for (Player p : players)
            p.setHoleCards(deck.dealCard(), deck.dealCard());

        // set turn flow
        currentPlayerIndex = 0;
        waitingForPlayerStart = true;
        waitingForAction = false;
        waitingForEndTurn = false;

        anyBetThisRound = false;
        clearMessage();
    }

    // reset FULL game state (constructor)
    private void resetWholeGameState() {
        pot = 0;
        potGold = 0;
        potRed = 0;
        currentBet = 0;

        anyBetThisRound = false;

        currentPlayerIndex = 0;
        waitingForPlayerStart = true;
        waitingForAction = false;
        waitingForEndTurn = false;

        lastMessage = null;
        winnerName = null;
        phase = PHASE_PREFLOP;
    }

    // turn flow
    public void startGame() {
        clearMessage();
        waitingForPlayerStart = false;
        waitingForAction = true;
        waitingForEndTurn = false;
    }

    public void doneWithAction() {
        waitingForPlayerStart = false;
        waitingForAction = false;
        waitingForEndTurn = true;
        hasActed[currentPlayerIndex] = true;
    }

    public void endTurn(int count) {
        clearMessage();

        // betting round complete?
        if (isBettingRoundComplete()) {
            handleEndOfBettingRound();
            return;
        }

        if (count == 0) return;

        // move to next non folded player
        int next = currentPlayerIndex;
        for (int i = 0; i < count; i++) {
            next = (next + 1) % count;
            if (!playerIsFolded(next)) {
                currentPlayerIndex = next;
                break;
            }
        }

        // reset turn flow
        waitingForPlayerStart = true;
        waitingForAction = false;
        waitingForEndTurn = false;
    }

    // betting round complete?
    private boolean isBettingRoundComplete() {

        // count active players
        int active = 0;
        for (Player p : players)
            if (!p.isFolded()) active++;

        if (active <= 1) return true;

        boolean someoneActed = false;

        // every active player must have acted & matched the bet
        for (int i = 0; i < players.size(); i++) {

            if (playerIsFolded(i)) continue;

            if (!hasActed[i]) return false;

            someoneActed = true;

            if (playerBets[i] != currentBet)
                return false;
        }

        return someoneActed;
    }

    // handle end of round
    private void handleEndOfBettingRound() {

        int activeCount = 0;
        int lastActive = -1;

        // count active players
        for (int i = 0; i < totalPlayers(); i++) {
            if (!playerIsFolded(i)) {
                activeCount++;
                lastActive = i;
            }
        }

        // auto win if only one player remains (everyone folded)
        if (activeCount <= 1) {
            if (lastActive >= 0) awardPotToSinglePlayer(lastActive);
            moveToShowdown();
            return;
        }

        // move to next phase
        switch (phase) {

            case PHASE_PREFLOP:
                community.dealFlop(deck);
                phase = PHASE_FLOP;
                break;

            case PHASE_FLOP:
                community.dealTurn(deck);
                phase = PHASE_TURN;
                break;

            case PHASE_TURN:
                community.dealRiver(deck);
                phase = PHASE_RIVER;
                break;

            case PHASE_RIVER:
                doShowdown();
                return;
        }

        resetRoundBets();
        moveToFirstActivePlayer();
    }

    private void moveToShowdown() {
        phase = PHASE_SHOWDOWN;
        resetRoundBets();
        waitingForPlayerStart = false;
        waitingForAction = false;
        waitingForEndTurn = false;
    }

    private void moveToFirstActivePlayer() {
        for (int i = 0; i < totalPlayers(); i++) {
            if (!playerIsFolded(i)) {
                currentPlayerIndex = i;
                break;
            }
        }

        waitingForPlayerStart = true;
        waitingForAction = false;
        waitingForEndTurn = false;
    }

    private void resetRoundBets() {
        for (int i = 0; i < playerBets.length; i++) {
            playerBets[i] = 0;
            playerGoldRound[i] = 0;
            playerRedRound[i] = 0;
            hasActed[i] = false;
        }
        currentBet = 0;
        anyBetThisRound = false;
        clearMessage();
    }

    // showdown
    private void doShowdown() {

        // no winner logic implemented yet
        winnerName = null;

        pot = 0;
        potGold = 0;
        potRed = 0;

        moveToShowdown();
    }

    private void awardPotToSinglePlayer(int i) {

        Player p = players.get(i);

        int total = pot;

        int red = total / 5;  // red chips ($5)
        int gold = total % 5; // gold chips ($1)

        p.setRedChips(p.getRedChips() + red);
        p.setGoldChips(p.getGoldChips() + gold);

        pot = 0;
        potGold = 0;
        potRed = 0;
    }

    // chip logic
    private int takeChips(int index, int amount) {

        if (amount <= 0) return 0;

        Player p = players.get(index);

        int total = p.getTotalMoney();
        if (total <= 0) {
            lastMessage = "You do not have enough chips.";
            return 0;
        }

        int toSpend = Math.min(amount, total);

        int red = p.getRedChips();
        int gold = p.getGoldChips();
        int spent = 0;

        // spend $5 chips first
        int useRed = Math.min(red, toSpend / 5);
        spent += useRed * 5;
        toSpend -= useRed * 5;
        red -= useRed;

        // spend $1 chips next
        int useGold = Math.min(gold, toSpend);
        spent += useGold;
        toSpend -= useGold;
        gold -= useGold;

        // update players chip counts
        p.setRedChips(red);
        p.setGoldChips(gold);

        // update round tracking
        playerBets[index] += spent;
        playerGoldRound[index] += useGold;
        playerRedRound[index] += useRed * 5;

        // update pot
        pot += spent;
        potGold += useGold;
        potRed += useRed;

        return spent;
    }

    // fold action
    public void handleFold() {
        clearMessage();
        Player p = players.get(currentPlayerIndex);

        p.fold();
        lastMessage = p.getName() + " folded.";
    }

    // call action
    public void handleCall() {

        clearMessage();
        Player p = players.get(currentPlayerIndex);

        if (p.isFolded()) {
            lastMessage = "You already folded.";
            return;
        }

        int alreadyBet = playerBets[currentPlayerIndex];

        // preflop forced call
        if (phase == PHASE_PREFLOP && currentBet == 0 && !anyBetThisRound) {

            int needed = 2;
            int paid = takeChips(currentPlayerIndex, needed);

            if (paid <= 0) {
                lastMessage = "Not enough chips to call $2.";
                return;
            }

            currentBet = paid;
            anyBetThisRound = true;
            lastMessage = p.getName() + " calls $2 to start the betting.";
            return;
        }

        // regular call
        int needed = currentBet - alreadyBet;

        if (needed <= 0) {
            lastMessage = "You already matched the bet.";
            return;
        }

        int paid = takeChips(currentPlayerIndex, needed);

        if (paid <= 0) {
            lastMessage = "Not enough chips to call.";
            return;
        }

        if (paid < needed)
            lastMessage = p.getName() + " goes all-in for $" + paid + ".";
        else
            lastMessage = p.getName() + " calls $" + needed + ".";
    }

    // bet action
    public void handleBetAmount(int amount) {

        clearMessage();
        Player p = players.get(currentPlayerIndex);

        if (p.isFolded()) {
            lastMessage = "You already folded.";
            return;
        }

        if (amount <= 0) {
            lastMessage = "Bet must be positive.";
            return;
        }

        int alreadyBet = playerBets[currentPlayerIndex];

        // determine what the new target bet should be
        int targetBet = (currentBet == 0)
                ? amount
                : currentBet + amount;

        int needed = targetBet - alreadyBet;

        if (needed <= 0) {
            lastMessage = "Your bet must exceed current bet.";
            return;
        }

        // try to pay the bet
        int paid = takeChips(currentPlayerIndex, needed);

        if (paid <= 0) {
            lastMessage = "Not enough chips to bet.";
            return;
        }

        // update the highest bet
        int newBet = playerBets[currentPlayerIndex];
        if (newBet > currentBet)
            currentBet = newBet;

        anyBetThisRound = true;

        lastMessage = p.getName() + " bets a total of $" + newBet + " this round.";
    }

    // check action
    public void handleCheck() {

        clearMessage();
        Player p = players.get(currentPlayerIndex);

        if (p.isFolded()) {
            lastMessage = "You already folded.";
            return;
        }

        int alreadyBet = playerBets[currentPlayerIndex];

        // preflop cannot check unless forced bet happened
        if (phase == PHASE_PREFLOP && currentBet == 0 && !anyBetThisRound) {
            lastMessage = "You may only call, fold, or bet.";
            return;
        }

        // cannot check behind a raise
        if (currentBet > 0 && alreadyBet < currentBet) {
            lastMessage = "You may only call, fold, or bet.";
            return;
        }

        lastMessage = p.getName() + " checks.";
    }
}
