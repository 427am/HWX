package blackjack.model;

public class Dealer extends Player
{
    public Dealer()
    {
        super("Dealer", 10000, 1000);
    }

    //Dealer specific methods
    public void play(Deck deck)
    {
        HandEvaluator evaluator = new HandEvaluator();
        while(evaluator.evaluateBestHand(getHand()) < 17)
        {
            this.getHand().add(deck.dealCard());
        }
    }
}
