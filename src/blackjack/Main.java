package blackjack;

import blackjack.model.*;
import blackjack.view.*;

import javax.swing.*;
import java.util.ArrayList;

public class Main 
{
    public static void main(String[] args)
    {
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new Player("Player 1", 30, 14)); // $100 each
        players.add(new Player("Player 2", 30, 14));
        players.add(new Player("Player 3", 30, 14));
        players.add(new Player("Player 4", 30, 14));

        Dealer dealer = new Dealer();
        Deck deck = new Deck();
        GameState gameState = new GameState(players, dealer, deck);

        JFrame window = new JFrame("FSU Blackjack");
        TableView table = new TableView(players, dealer, gameState);
        window.add(table);
        window.setSize(1200, 800);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        //sets cards to face down at start
        table.pressPlayAgain();
    }
}
