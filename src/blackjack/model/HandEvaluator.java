package blackjack.model;

import java.util.*;

public class HandEvaluator
{
    public int evaluateBestHand(List<Card> hand)
    {
        int total = 0;
        int aceCount = 0;
        if(hand == null)
            return 0;
        //counting aces
        for(Card card : hand)
        {
            String rank = card.getRank();
            if(rank.equals("A"))
            {
                aceCount++;
                total += 1;
            }
            else if(rank.equals("K") || rank.equals("Q") || rank.equals("J"))
            {
                total += 10;
            }
            else
            {
                total += rankToValue(rank);
            }
        }
        // Try to count an ace as 11
        if(aceCount > 0 && total + 10 <= 21)
        {
            total += 10;
        }
        
        return total;
    }

    private static int rankToValue(String r) 
    {
        if (r == null)
            return 0;
        r = r.toUpperCase();
        if (r.equals("A") ||r.equals("K") || (r.equals("Q"))
            || (r.equals("J")) ||(r.equals("10"))) return 10;
        try {
            int v = Integer.parseInt(r);
            if (v >= 2 && v <= 10) return v;
        } catch (NumberFormatException e) {
        }
        return 0;
    }
}
