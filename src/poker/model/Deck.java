package poker.model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;
    private int index;

    public Deck() {
        cards = new ArrayList<Card>();
        createDeck();
        shuffle();
        index = 0;
    }

    private void createDeck() {
        String[] ranks = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        String[] suits = {"Seal","Fsu","Spear","Face"};

        for(String suit: suits) {
            for(String rank : ranks) {
                cards.add(new Card(rank,suit));
            }
        }
    }

    private void shuffle() {
        Collections.shuffle(cards);
        index = 0;
    }

    public Card dealCard() {
        if (index < cards.size()) {
            Card c = cards.get(index);
            index++;
            return c;
        }
        return null;
    }
}
