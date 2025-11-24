package poker.model;

public class HandRank implements Comparable<HandRank> {

    // 8 = Straight Flush, 7 = Four of a Kind, 6 = Full House,
    // 5 = Flush, 4 = Straight, 3 = Three of a Kind,
    // 2 = Two Pair, 1 = One Pair, 0 = High Card
    private int category;
    private int[] tiebreakers; // ranks in descending order for tie-breaking

    public HandRank(int category, int[] tiebreakers) {
        this.category = category;
        this.tiebreakers = tiebreakers;
    }

    public int getCategory() {
        return category;
    }

    public int[] getTiebreakers() {
        return tiebreakers;
    }

    @Override
    public int compareTo(HandRank other) {
        if (other == null) return 1;

        if (this.category != other.category) {
            return this.category - other.category;
        }

        int len = Math.min(this.tiebreakers.length, other.tiebreakers.length);
        for (int i = 0; i < len; i++) {
            if (this.tiebreakers[i] != other.tiebreakers[i]) {
                return this.tiebreakers[i] - other.tiebreakers[i];
            }
        }
        return 0;
    }
}
