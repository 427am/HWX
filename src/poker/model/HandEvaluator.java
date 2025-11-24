package poker.model;

import java.util.ArrayList;
import java.util.Arrays;

public class HandEvaluator {

    public static HandRank evaluateBestHand(ArrayList<Card> cards) {
        if (cards == null || cards.size() < 5) return null;

        int n = cards.size();
        Card[] arr = cards.toArray(new Card[n]);

        HandRank best = null;

        for (int a = 0; a < n - 4; a++) {
            for (int b = a + 1; b < n - 3; b++) {
                for (int c = b + 1; c < n - 2; c++) {
                    for (int d = c + 1; d < n - 1; d++) {
                        for (int e = d + 1; e < n; e++) {
                            Card[] five = new Card[5];
                            five[0] = arr[a];
                            five[1] = arr[b];
                            five[2] = arr[c];
                            five[3] = arr[d];
                            five[4] = arr[e];

                            HandRank hr = evaluate5CardHand(five);
                            if (best == null || hr.compareTo(best) > 0) {
                                best = hr;
                            }
                        }
                    }
                }
            }
        }

        return best;
    }

    private static HandRank evaluate5CardHand(Card[] cards) {
        int[] ranks = new int[5];
        String[] suits = new String[5];

        for (int i = 0; i < 5; i++) {
            ranks[i] = rankToValue(cards[i].getRank());
            suits[i] = cards[i].getSuit();
        }

        // sort ranks descending
        Arrays.sort(ranks);
        reverse(ranks);

        boolean isFlush = isFlush(suits);
        boolean isStraight = false;
        int straightHigh = 0;

        boolean hasDup = false;
        for (int i = 0; i < 4; i++) {
            if (ranks[i] == ranks[i + 1]) {
                hasDup = true;
                break;
            }
        }

        if (!hasDup) {
            if (ranks[0] == 14 && ranks[1] == 5 && ranks[2] == 4 && ranks[3] == 3 && ranks[4] == 2) {
                isStraight = true;
                straightHigh = 5;
            } else if (ranks[0] == ranks[1] + 1 &&
                       ranks[1] == ranks[2] + 1 &&
                       ranks[2] == ranks[3] + 1 &&
                       ranks[3] == ranks[4] + 1) {
                isStraight = true;
                straightHigh = ranks[0];
            }
        }

        int[] count = new int[15];
        for (int r : ranks) {
            count[r]++;
        }

        int fourRank = 0;
        int threeRank = 0;
        ArrayList<Integer> pairRanks = new ArrayList<Integer>();
        ArrayList<Integer> singles = new ArrayList<Integer>();

        for (int r = 14; r >= 2; r--) {
            if (count[r] == 4) {
                fourRank = r;
            } else if (count[r] == 3) {
                if (threeRank == 0) threeRank = r;
            } else if (count[r] == 2) {
                pairRanks.add(r);
            } else if (count[r] == 1) {
                singles.add(r);
            }
        }

        if (isFlush && isStraight) {
            int[] tiebreak = new int[] { straightHigh };
            return new HandRank(8, tiebreak);
        }

        if (fourRank != 0) {
            int kicker = 0;
            for (int r : singles) {
                if (r != fourRank) {
                    kicker = r;
                    break;
                }
            }
            int[] tiebreak = new int[] { fourRank, kicker };
            return new HandRank(7, tiebreak);
        }

        if (threeRank != 0 && !pairRanks.isEmpty()) {
            int pairRank = pairRanks.get(0);
            int[] tiebreak = new int[] { threeRank, pairRank };
            return new HandRank(6, tiebreak);
        }

        if (isFlush) {
            int[] tiebreak = ranks.clone();
            return new HandRank(5, tiebreak);
        }

        if (isStraight) {
            int[] tiebreak = new int[] { straightHigh };
            return new HandRank(4, tiebreak);
        }

        if (threeRank != 0) {
            ArrayList<Integer> kickers = new ArrayList<Integer>();
            for (int r : singles) {
                if (r != threeRank) {
                    kickers.add(r);
                }
            }
            while (kickers.size() < 2) {
                kickers.add(0);
            }
            int[] tiebreak = new int[] { threeRank, kickers.get(0), kickers.get(1) };
            return new HandRank(3, tiebreak);
        }

        if (pairRanks.size() >= 2) {
            int highPair = pairRanks.get(0);
            int lowPair = pairRanks.get(1);
            int kicker = 0;
            for (int r : singles) {
                if (r != highPair && r != lowPair) {
                    kicker = r;
                    break;
                }
            }
            int[] tiebreak = new int[] { highPair, lowPair, kicker };
            return new HandRank(2, tiebreak);
        }

        if (pairRanks.size() == 1) {
            int pairRank = pairRanks.get(0);
            ArrayList<Integer> kickers = new ArrayList<Integer>();
            for (int r : singles) {
                if (r != pairRank) {
                    kickers.add(r);
                }
            }
            while (kickers.size() < 3) {
                kickers.add(0);
            }
            int[] tiebreak = new int[] { pairRank, kickers.get(0), kickers.get(1), kickers.get(2) };
            return new HandRank(1, tiebreak);
        }

        int[] tiebreak = ranks.clone();
        return new HandRank(0, tiebreak);
    }

    private static boolean isFlush(String[] suits) {
        String first = suits[0];
        for (int i = 1; i < suits.length; i++) {
            if (!first.equals(suits[i])) return false;
        }
        return true;
    }

    private static int rankToValue(String r) {
        if (r == null) return 0;
        r = r.toUpperCase();
        if (r.equals("A")) return 14;
        if (r.equals("K")) return 13;
        if (r.equals("Q")) return 12;
        if (r.equals("J")) return 11;
        if (r.equals("10")) return 10;
        try {
            int v = Integer.parseInt(r);
            if (v >= 2 && v <= 10) return v;
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    private static void reverse(int[] arr) {
        int i = 0;
        int j = arr.length - 1;
        while (i < j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
            i++;
            j--;
        }
    }
}
