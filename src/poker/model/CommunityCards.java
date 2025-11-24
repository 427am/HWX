package poker.model;

import java.util.ArrayList;

public class CommunityCards {
    private ArrayList<Card> comCards;

    public CommunityCards() {
        comCards = new ArrayList<Card>();
    }

    public void reset() {
        comCards.clear();
    }

    public void dealFlop(Deck deck) {
        comCards.add(deck.dealCard());
        comCards.add(deck.dealCard());
        comCards.add(deck.dealCard());
    }

    public void dealTurn(Deck deck) {
        comCards.add(deck.dealCard());
    }

    public void dealRiver(Deck deck) {
        comCards.add(deck.dealCard());
    }

    public ArrayList<Card> getCards() {
        return comCards;
    }

}
