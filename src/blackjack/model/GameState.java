package blackjack.model;

import java.util.*;
import javax.swing.*;


public class GameState 
{
    // game data
    private ArrayList<Player> players; // list of players
    private Dealer dealer; // dealer
    private Deck deck; // deck of cards

    private int currentPlayerIndex = 0;
    private boolean roundInProgress = false;
    private boolean waitingForPlayerStart;
    private boolean waitingForAction;
    private boolean waitingForEndTurn;
    private String statusMessage = "Welcome to Blackjack, Player 1 please place a bet.";
    private boolean playersRevealed = false;
    private boolean dealerSecondRevealed = false;

    public GameState(ArrayList<Player> players, Dealer dealer, Deck deck) 
    {
        this.players = players;
        this.dealer = dealer;
        this.deck = deck;
    }

    //getters
    public ArrayList<Player> getPlayers() {return players;}
    public Deck getDeck() {return deck;}
    public int getCurrentPlayerIndex() {return currentPlayerIndex;}
    public boolean isRoundInProgress() {return roundInProgress;}
    public boolean isWaitingForPlayerStart() {return waitingForPlayerStart;}
    public boolean isWaitingForAction() {return waitingForAction;}
    public boolean isWaitingForEndTurn() {return waitingForEndTurn;}

    public void startNewRound() 
    {
        roundInProgress = true;
        waitingForPlayerStart = false;
        waitingForAction = true;
        waitingForEndTurn = false;
        currentPlayerIndex = -1;
        deck = new Deck();

        //prepare for new hand
        for(Player p : players)
        {
            p.resetForNewHand();
            p.setHoleCards(deck.dealCard(), deck.dealCard());
        }

        dealer.resetForNewHand();
        dealer.getHand().add(deck.dealCard());
        dealer.getHand().add(deck.dealCard());
        playersRevealed = true;
        dealerSecondRevealed = false;
        HandEvaluator initialEvaluation = new HandEvaluator();
        for(Player p : players) //auto-stand players with blackjack
        {
            int total = initialEvaluation.evaluateBestHand(p.getHand());
            if(total == 21)
                p.stand();
        }
        advanceToNextPlayer();
    }

    public Player getCurrentPlayer()
    {
        if(players == null || players.size() == 0)
            return null;
        return players.get(currentPlayerIndex);
    }

    public void playerHit()
    {
        Player p = getCurrentPlayer();
        if(p == null || !roundInProgress) 
            return;
        p.getHand().add(deck.dealCard());
        HandEvaluator eval = new HandEvaluator();
        int total = eval.evaluateBestHand(p.getHand());
        if(total > 21)
        {
            p.bust();
            JOptionPane.showMessageDialog(null, p.getName() + " busted.");
            advanceToNextPlayer();
        }
        else if(total == 21)
        {
            p.stand();
            JOptionPane.showMessageDialog(null, p.getName() + " has 21");
            advanceToNextPlayer();
        } 
        else
        {
            setStatus(p.getName() + " hits. Total: " + total);
        }
    }

    public void playerStand()
    {
        Player p = getCurrentPlayer();
        if (p == null || !roundInProgress) return;
        p.stand();
        JOptionPane.showMessageDialog(null, p.getName() + " stands.");;
        advanceToNextPlayer();
    }

    private void advanceToNextPlayer()
    {
        int start = currentPlayerIndex;
        do
        {
            currentPlayerIndex++;
            if(currentPlayerIndex >= players.size()) //all players done
            {
                dealer.play(deck);
                setStatus("Dealer's turn...");
                settleRound();
                roundInProgress = false;
                return;
            }
            Player next = players.get(currentPlayerIndex);
            if(!next.isBusted() && !next.isStood()) //move to next player
            {
                HandEvaluator eval = new HandEvaluator();
                int total = eval.evaluateBestHand(next.getHand());
                setStatus(next.getName() + "'s turn. Total: " + total);
                return;
            }
        }
        while(currentPlayerIndex != start);
    }

    private void settleRound()
    {
        HandEvaluator evaluator = new HandEvaluator();
        int dealerTotal = evaluator.evaluateBestHand(dealer.getHand());
        dealerSecondRevealed = true;
        playersRevealed = true;
        StringBuilder winners = new StringBuilder("Round Results:\n");
        for(Player p : players)
        {
            int playerTotal = evaluator.evaluateBestHand(p.getHand());
            int reds = p.getBetAmount() % 5;
            int golds = p.getBetAmount() / 5;
            if(p.isBusted()) //player busted
            {
                p.loseChips(reds, golds);
                winners.append(p.getName()).append(" - Busted\n");
            }
            else if(playerTotal > dealerTotal && playerTotal <= 21) //player wins
            {
                p.winChips(reds, golds);
                winners.append(p.getName()).append(" - Wins\n");
            }
            else if(playerTotal < dealerTotal) //player loses
            {
                p.loseChips(reds, golds);
                winners.append(p.getName()).append(" - Loses\n");
            }
            else //push
            {
                winners.append(p.getName()).append(" - Push\n");
            }
            p.clearBet();
        }
        winners.append("Dealer: ").append(dealerTotal);
        setStatus("Round over. Dealer: " + dealerTotal);
        //winner message
        JOptionPane.showMessageDialog(null, winners.toString());
    }

    public String getStatus() {return statusMessage;}
    public void setStatus(String s) {statusMessage = s;}
    public boolean isPlayersRevealed() {return playersRevealed;}
    public boolean isDealerSecondRevealed() {return dealerSecondRevealed;}

    public boolean allBetsPlaced() 
    {
        for(Player p : players)
        {
            if(!p.isBetPlaced())
            {
                return false;
            }
        }
        return true;
    }

    public void resetWholeGameState() 
    {
        roundInProgress = false;
        waitingForPlayerStart = false;
        waitingForAction = false;
        waitingForEndTurn = false;
        currentPlayerIndex = 0;
        deck = new Deck();
        for(Player p : players) 
        {
            p.resetForNewHand();
            p.clearBet();
            p.setBetPlaced(false);
        }
        playersRevealed = false;
        dealerSecondRevealed = false;
    }
}
