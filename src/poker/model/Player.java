package poker.model;

public class Player {

    private String name;
    private Card card1;
    private Card card2;

    private int goldChips; // $1 each
    private int redChips;  // $5 each

    private boolean folded;

    public Player(String name, int goldChips, int redChips) {
        this.name = name;
        this.goldChips = goldChips;
        this.redChips = redChips;
        this.folded = false;
    }

    public void setHoleCards(Card c1, Card c2) {
        this.card1 = c1;
        this.card2 = c2;
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
        return card2;
    }

    public void clearCards() {
        card1 = null;
        card2 = null;
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

    public void setGoldChips(int amount) {
        goldChips = amount;
    }

    public void setRedChips(int amount) {
        redChips = amount;
    }

    // total money in dollars
    public int getTotalMoney() {
        return goldChips * 1 + redChips * 5;
    }

    public int getChips() {
        return getTotalMoney();
    }

    public boolean isFolded() {
        return folded;
    }

    public void fold() {
        folded = true;
    }

    public void resetForNewHand() {
        folded = false;
        clearCards();
    }
}
