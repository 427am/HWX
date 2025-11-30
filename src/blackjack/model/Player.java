package blackjack.model;

import java.util.ArrayList;

public class Player
{

    private String name;
    private ArrayList<Card> hand;

    private int goldChips; // $1 each
    private int redChips;  // $5 each
    private int betGold;
    private int betRed;
    private boolean betPlaced;

    private boolean busted;
    private boolean stood;
    private boolean doubled;
    private boolean blackjack;

    public Player(String name, int goldChips, int redChips) {
        this.name = name;
        this.goldChips = goldChips;
        this.redChips = redChips;
        this.busted = false;
        this.stood = false;
        this.doubled = false;
        this.betPlaced = false;
    }

    public void setHoleCards(Card c1, Card c2) {
        this.getHand().add(c1);
        this.getHand().add(c2);
    }

    public ArrayList<Card> getHand() {
        if (hand == null) {
            hand = new ArrayList<>();
        }
        return hand;
    }

    public void clearHand() {
        getHand().clear();
    }

    public String getName() {
        return name;
    }

    public int getGoldChips() {
        return goldChips;
    }

    public int getRedChips() {
        return redChips;
    }

    public int getBetGold() {
        return betGold;
    }

    public int getBetRed() {
        return betRed;
    }

    public int getBetAmount() {
        return betGold + betRed * 5;
    }

    public void setGoldChips(int amount) {
        goldChips = amount;
    }

    public void setRedChips(int amount) {
        redChips = amount;
    }

    public void setBetGold(int amount) {
        betGold = amount;
    }

    public void setBetRed(int amount) {
        betRed = amount;
    }

    public void clearBet() {
        betGold = 0;
        betRed = 0;
    }

    public boolean isBetPlaced() {return betPlaced;}
    public void setBetPlaced(boolean placed) {betPlaced = placed;}

    // total money in dollars
    public int getTotalMoney() {
        return goldChips * 1 + redChips * 5;
    }

    public int getChips() {
        return getTotalMoney();
    }

    public boolean isBusted() {
        return busted;
    }

    public boolean isStood() {
        return stood;
    }

    public boolean isDoubled() {
        return doubled;
    }

    public void bust() {
        busted = true;
    }

    public void stand() {
        stood = true;
    }

    public void doubleDown() {
        doubled = true;
    }

    public boolean hasBlackjack() {
        return blackjack;
    }

    public void setBlackjack(boolean bj) {
        blackjack = bj;
    }

    public void winChips(int red, int gold) {
        redChips += red;
        goldChips += gold;
    }

    public void loseChips(int red, int gold) {
        redChips -= red;
        goldChips -= gold;
    }

    public void resetForNewHand() {
        busted = false;
        stood = false;
        doubled = false;
        blackjack = false;
        getHand().clear();
        betPlaced = false;
    }
}
