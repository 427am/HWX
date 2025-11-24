package poker;

import poker.model.*;
import poker.view.*;

import javax.swing.JFrame;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("Player 1", 30, 14)); // $100 each
        players.add(new Player("Player 2", 30, 14));
        players.add(new Player("Player 3", 30, 14));
        players.add(new Player("Player 4", 30, 14));

        CommunityCards community = new CommunityCards();
        Deck deck = new Deck();

        GameState gameState = new GameState(players, community, deck);
        gameState.startNewHand();

        JFrame window = new JFrame("FSU Texas Hold'em");
        TableView table = new TableView(players, community, gameState);

        window.add(table);
        window.setSize(1200, 800);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
