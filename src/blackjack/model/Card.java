package blackjack.model;

public class Card {
    private String rank; // card # or letter
    private String suit; // seal, fsu, spear, or face

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    // full card name - ex: 4Spear
    public String getCardName() {
        return rank + suit;
    }

    public String toString() {
        return rank + " of " + suit;
    }
}
