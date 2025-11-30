package blackjack.view;

import blackjack.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TableView extends JPanel
{
    //game data
    private ArrayList<Player> players;
    private Dealer dealer;
    private CardLoader loader;
    private GameState gameState;

    //per-player betting controls
    private java.util.List<JButton> goldPlusButtons = new ArrayList<>();
    private java.util.List<JButton> goldMinusButtons = new ArrayList<>();
    private java.util.List<JButton> redPlusButtons = new ArrayList<>();
    private java.util.List<JButton> redMinusButtons = new ArrayList<>();
    private java.util.List<JLabel> chipsLabels = new ArrayList<>();
    private java.util.List<JLabel> betLabels = new ArrayList<>();
    private JButton placeBetButton;
    private JLabel statusLabel;
    private int bettingIndex = 0;

    //buttons
    private JButton startButton;
    private JButton playAgainButton;
    private JButton hitButton;
    private JButton standButton;
    

    //constructor
    public TableView(ArrayList<Player> players, Dealer dealer, GameState gameState)
    {
        this.players = players;
        this.dealer = dealer;
        this.gameState = gameState;
        this.loader = new CardLoader();
        setBackground(new Color(0, 100, 0));
        setLayout(null); // we manually place buttons
        initButtons();
    }

    private void initButtons()
    {
        startButton = new JButton("Start");
        startButton.setBounds(750, 700, 100, 50);
        add(startButton);
        hitButton = new JButton("Hit");
        hitButton.setBounds(500, 700, 80, 50);
        add(hitButton);
        standButton = new JButton("Stand");
        standButton.setBounds(600, 700, 80, 50);
        add(standButton);
        placeBetButton = new JButton("Place Bet");
        placeBetButton.setBounds(400, 700, 100, 50);
        add(placeBetButton);
        playAgainButton = new JButton("Play Again");
        playAgainButton.setBounds(850, 700, 100, 50);
        add(playAgainButton);
        statusLabel = new JLabel(gameState.getStatus());
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setBounds(50, 10, 800, 28);
        add(statusLabel);
        //start disabled until all bets placed
        startButton.setEnabled(false);
        //action buttons disabled until round starts
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        bettingIndex = 0;
        updateBettingUI();
        //player betting controls
        int areaStartX = 50;
        int areaWidth = 1100;
        int count = Math.max(1, players.size());
        int spacing = areaWidth / count;
        for(int i=0; i<players.size(); i++)
        {
            final int idx = i;
            Player p = players.get(i);
            int px = areaStartX + i * spacing;
            //per-player labels and buttons
            //chips
            JLabel chipsLabel = new JLabel("G: " + p.getGoldChips() + "                   R: " + p.getRedChips());
            chipsLabel.setForeground(Color.WHITE);
            chipsLabel.setBounds(px, 600, 140, 20);
            add(chipsLabel);
            chipsLabels.add(chipsLabel);
            //bets
            JLabel betLabel = new JLabel("Bet G:" + p.getBetGold() + "                   R:" + p.getBetRed());
            betLabel.setForeground(Color.WHITE);
            betLabel.setBounds(px, 620, 140, 20);
            add(betLabel);
            betLabels.add(betLabel);
            //plus gold
            JButton gPlus = new JButton("+");
            gPlus.setBounds(px, 640, 40, 24);
            add(gPlus);
            goldPlusButtons.add(gPlus);
            //minus gold
            JButton gMinus = new JButton("-");
            gMinus.setBounds(px + 46, 640, 40, 24);
            add(gMinus);
            goldMinusButtons.add(gMinus);
            //red plus
            JButton rPlus = new JButton("+");
            rPlus.setBounds(px + 92, 640, 40, 24);
            add(rPlus);
            redPlusButtons.add(rPlus);
            //red minus
            JButton rMinus = new JButton("-");
            rMinus.setBounds(px + 138, 640, 40, 24);
            add(rMinus);
            redMinusButtons.add(rMinus);
            //action listeners for player buttons
            //plus gold
            gPlus.addActionListener(e -> 
            {
                Player pp = players.get(idx);
                if(pp.getBetGold() < pp.getGoldChips())
                {
                    pp.setBetGold(pp.getBetGold() + 1);
                    betLabels.get(idx).setText("Bet G:" + pp.getBetGold() + "                   R:" + pp.getBetRed());
                    chipsLabels.get(idx).setText("G: " + pp.getGoldChips() + "                   R: " + pp.getRedChips());
                    repaint();
                }
            });
            //minus gold
            gMinus.addActionListener(e ->
            {
                Player pp = players.get(idx);
                if(pp.getBetGold() > 0)
                {
                    pp.setBetGold(pp.getBetGold() - 1);
                    betLabels.get(idx).setText("Bet G:" + pp.getBetGold() + "                   R:" + pp.getBetRed());
                    repaint();
                }
            });
            //plus red
            rPlus.addActionListener(e ->
            {
                Player pp = players.get(idx);
                if(pp.getBetRed() < pp.getRedChips())
                {
                    pp.setBetRed(pp.getBetRed() + 1);
                    betLabels.get(idx).setText("Bet G:" + pp.getBetGold() + "                   R:" + pp.getBetRed());
                    repaint();
                }
            });
            //minus red
            rMinus.addActionListener(e ->
            {
                Player pp = players.get(idx);
                if(pp.getBetRed() > 0)
                {
                    pp.setBetRed(pp.getBetRed() - 1);
                    betLabels.get(idx).setText("Bet G:" + pp.getBetGold() + "                   R:" + pp.getBetRed());
                    repaint();
                }
            });
        }
        //action listeners for main buttons
        //start button
        startButton.addActionListener(e -> 
        {
            if(!gameState.allBetsPlaced()) 
            {
                JOptionPane.showMessageDialog(this, "All players must place a bet before starting the round.");
                return;
            }
            gameState.startNewRound();
            statusLabel.setText(gameState.getStatus());
            startButton.setEnabled(false);
            hitButton.setEnabled(true);
            standButton.setEnabled(true);
            if(placeBetButton != null)
                placeBetButton.setEnabled(false);
            updateBettingUI();
            updateComponentPositions();
            repaint();
        });
        //bet button
        placeBetButton.addActionListener(e ->
        {
            if(bettingIndex < 0 || bettingIndex >= players.size()) 
                return;
            Player player = players.get(bettingIndex);
            player.setBetPlaced(true);
            if(bettingIndex < betLabels.size())
                betLabels.get(bettingIndex).setText("Bet G:" + player.getBetGold() + "                   R:" + player.getBetRed());
            if(bettingIndex < chipsLabels.size())
                chipsLabels.get(bettingIndex).setText("G: " + player.getGoldChips() + "                   R: " + player.getRedChips());
            int next = bettingIndex + 1;
            while(next < players.size() && players.get(next).isBetPlaced()) 
                next++;
            if(next >= players.size())
                bettingIndex = -1;
            else
                bettingIndex = next;
            updateBettingUI();
            if(gameState.allBetsPlaced())
            {
                startButton.setEnabled(true);
                statusLabel.setText("All bets placed. Click Start to deal.");
                if(placeBetButton != null)
                    placeBetButton.setEnabled(false);
            }
            else
            {
                if(bettingIndex >= 0)
                    statusLabel.setText("Bet placed. Next: " + players.get(bettingIndex).getName());
            }
            repaint();
        });
        //hit button
        hitButton.addActionListener(e ->
        {
            gameState.playerHit();
            statusLabel.setText(gameState.getStatus());
            updateComponentPositions();
            repaint();
        });
        //stand button
        standButton.addActionListener(e ->
        {
            gameState.playerStand();
            statusLabel.setText(gameState.getStatus());
            updateComponentPositions();
            repaint();
        });
        //play again button
        playAgainButton.addActionListener(e -> 
        {
            gameState.resetWholeGameState();
            statusLabel.setText(gameState.getStatus());
            startButton.setEnabled(false);
            bettingIndex = 0;
            updateBettingUI();
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            updateComponentPositions();
            repaint();
        });
    }

    public void pressPlayAgain()
    {
        if(playAgainButton != null)
            playAgainButton.doClick();
    }

    private void updateBettingUI()
    {
        for(int i=0; i<players.size(); i++)
        {
            Player player = players.get(i);
            boolean placed = player.isBetPlaced();
            //enable betting
            if(i < goldPlusButtons.size())
                goldPlusButtons.get(i).setEnabled(!placed);
            if(i < goldMinusButtons.size())
                goldMinusButtons.get(i).setEnabled(!placed);
            if(i < redPlusButtons.size())
                redPlusButtons.get(i).setEnabled(!placed);
            if(i < redMinusButtons.size())
                redMinusButtons.get(i).setEnabled(!placed);
            if(i < betLabels.size())
                betLabels.get(i).setText("Bet G:" + player.getBetGold() + "                   R:" + player.getBetRed());
        }
        //place bet button
        boolean placeBetEnabled = false;
        if(bettingIndex >= 0 && bettingIndex < players.size())
        {
            placeBetEnabled = !players.get(bettingIndex).isBetPlaced();
        }
        if(placeBetButton != null)
            placeBetButton.setEnabled(placeBetEnabled);
    }

    private void updateComponentPositions()
    {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        //spacing for players
        int areaStartX = 50;
        int areaWidth = Math.max(600, panelWidth - 100);
        int spacing = areaWidth / Math.max(1, players.size());
        int cardH = 180;
        int baseY = Math.max(120, panelHeight / 2 + 20);
        int chipsY = baseY + cardH + 15;
        //player ui
        for(int i=0; i<players.size(); i++)
        {
            int playerX = areaStartX + i * spacing;
            if(i < chipsLabels.size())
            {
                Player p = players.get(i);
                int totalDollars = p.getGoldChips() + p.getRedChips() * 5;
                chipsLabels.get(i).setText("Total: $" + totalDollars + "  G:" + p.getGoldChips() + " R:" + p.getRedChips());
                chipsLabels.get(i).setBounds(playerX, chipsY, 220, 20);
            }
            if(i < betLabels.size())
            {
                betLabels.get(i).setBounds(playerX, chipsY + 20, 160, 20);
            }
            //plus minus buttons
            if(i < goldPlusButtons.size())
            {
                goldPlusButtons.get(i).setBounds(playerX, chipsY + 70, 40, 24);
            }
            if(i < goldMinusButtons.size())
            {
                goldMinusButtons.get(i).setBounds(playerX + 46, chipsY + 70, 40, 24);
            }
            if(i < redPlusButtons.size())
            {
                redPlusButtons.get(i).setBounds(playerX + 92, chipsY + 70, 40, 24);
            }
            if(i < redMinusButtons.size())
            {
                redMinusButtons.get(i).setBounds(playerX + 138, chipsY + 70, 40, 24);
            }
        }

        if(statusLabel != null)
        {
            int labelW = Math.min(800, panelWidth - 100);
            int labelH = 28;
            //middle of screen
            int labelX = Math.max(0, (panelWidth - labelW) / 2);
            int labelY = Math.max(10, (panelHeight / 2) - 30); 
            statusLabel.setBounds(labelX, labelY, labelW, labelH);
        }

        //main buttons
        int bottomY = Math.max(panelHeight - 60, chipsY + 80);
        int gap = 20;
        int wHit = 80, wStand = 80, wPlace = 100, wStart = 100, wPlay = 100;
        int totalButtonsW = wHit + wStand + wPlace + wStart + wPlay + gap * 4;
        int buttonsStartX = Math.max(20, (panelWidth - totalButtonsW) / 2);
        if(hitButton != null)
            hitButton.setBounds(buttonsStartX, bottomY, wHit, 50);
        if(standButton != null)
            standButton.setBounds(buttonsStartX + wHit + gap, bottomY, wStand, 50);
        if(placeBetButton != null)
            placeBetButton.setBounds(buttonsStartX + wHit + gap + wStand + gap, bottomY, wPlace, 50);
        if(startButton != null)
            startButton.setBounds(buttonsStartX + wHit + gap + wStand + gap + wPlace + gap, bottomY, wStart, 50);
        if(playAgainButton != null)
            playAgainButton.setBounds(buttonsStartX + wHit + gap + wStand + gap + wPlace + gap + wStart + gap, bottomY, wPlay, 50);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        updateComponentPositions();
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int areaStartX = 50;
        int areaWidth = Math.max(600, panelWidth - 100);
        int spacing = areaWidth / players.size();
        int cardW = 120;
        int cardH = 180;
        int gap = 2;
        paintDealer(g2, panelWidth, cardW, cardH, gap);
        paintPlayers(g2, panelWidth, panelHeight, areaStartX, areaWidth, spacing, cardW, cardH, gap);
    }

    private void paintDealer(Graphics2D g2, int panelWidth, int cardW, int cardH, int gap)
    {
        //formatting
        int dealerY = 30;
        g2.drawString("Dealer:", panelWidth/2 - 30, dealerY);
        int dCount = dealer.getHand().size();
        int dCardW = cardW;
        int dGap = Math.max(10, gap);
        int dTotalW = Math.max(dCardW, dCount * dCardW + Math.max(0, dCount - 1) * dGap);
        int dStartX = (panelWidth - dTotalW) / 2;
        Image back = loader.getCardBackImage();
        boolean revealSecond = gameState.isDealerSecondRevealed();
        List<Card> dHand = dealer.getHand();
        //hide dealer cards during betting
        if(!gameState.isPlayersRevealed())
        {
            int backs = 2;
            int backTotal = backs * dCardW + (backs - 1) * dGap;
            int start = (panelWidth - backTotal) / 2;
            for(int i=0; i<backs; i++)
            {
                if(back != null)
                    g2.drawImage(back, start + i * (dCardW + dGap), dealerY + 10, dCardW, cardH, this);
                else
                    g2.drawRect(start + i * (dCardW + dGap), dealerY + 10, dCardW, cardH);
            }
        }
        else
        {
            int dx = dStartX;
            for(int i=0; i<dHand.size(); i++)
            {
                Card c = dHand.get(i);
                //hide second card during round
                if(i == 1 && !revealSecond) 
                {
                    if(back != null)
                        g2.drawImage(back, dx, dealerY + 10, dCardW, cardH, this);
                    else
                        g2.drawRect(dx, dealerY + 10, dCardW, cardH);
                }
                else
                {
                    Image img = loader.getCardImage(c);
                    if(img != null)
                    {
                        g2.drawImage(img, dx, dealerY + 10, dCardW, cardH, this);
                    }
                    else
                    {
                        g2.drawRect(dx, dealerY + 10, dCardW, cardH);
                        g2.drawString(c.toString(), dx + 5, dealerY + cardH/2);
                    }
                }
                dx += dCardW + dGap;
            }
        }
    }

    private void paintPlayers(Graphics2D g2, int panelWidth, int panelHeight, int areaStartX, int areaWidth, int spacing, int cardW, int cardH, int gap)
    {
        int baseY = Math.max(120, panelHeight / 2 + 20);
        for(int i=0; i<players.size(); i++)
        {
            Player p = players.get(i);
            int colStart = areaStartX + i * spacing;
            int colCenter = colStart + spacing / 2;
            //clip keeps cards in certain spaces
            Shape oldClip = g2.getClip();
            int clipMarginLeft = 2;
            int clipMarginRight = 60;
            int clipX = Math.max(0, colStart - clipMarginLeft);
            int clipY = baseY + 10;
            int clipW = Math.max(0, spacing + clipMarginLeft + clipMarginRight);
            int clipH = cardH + 80;
            g2.setClip(new Rectangle(clipX, clipY, clipW, clipH));
            g2.drawString(p.getName() + " (Chips: $" + p.getTotalMoney() + ")", colStart, baseY);
            //hide cards during betting
            if(!gameState.isPlayersRevealed())
            {
                Image back = loader.getCardBackImage();
                int backs = 2;
                int totalW = backs * cardW + (backs - 1) * gap;
                int startX = colCenter - totalW / 2;
                int cardY = baseY + 20;
                for(int b=0; b<backs; b++)
                {
                    if(back != null)
                        g2.drawImage(back, startX + b * (cardW + gap), cardY, cardW, cardH, this);
                    else
                        g2.drawRect(startX + b * (cardW + gap), cardY, cardW, cardH);
                }
            }
            //draw cards
            else
            {
                java.util.List<Card> hand = p.getHand();
                int n = hand.size();
                int colAvailable = spacing - 20;
                int baseCardW = cardW;
                int minCardW = 40;
                int minGap = 1;
                int desiredGap = gap;
                int localCardW = baseCardW;
                int localGap = desiredGap;
                //dynamic sizing
                if(n > 0) 
                {
                    int preferredTotal = n * baseCardW + (n - 1) * desiredGap;
                    if(preferredTotal <= colAvailable)
                    {
                        localCardW = baseCardW;
                        localGap = desiredGap;
                    }
                    else
                    {
                        int totalWithMinGap = n * baseCardW + (n - 1) * minGap;
                        if(totalWithMinGap <= colAvailable)
                        {
                            //shrink gap to fit
                            localCardW = baseCardW;
                            int candidateGap = (colAvailable - n * localCardW) / (n - 1);
                            localGap = Math.max(minGap, Math.min(desiredGap, candidateGap));
                        }
                        else
                        {
                            //shrink card to fit
                            int candidateCardW = (colAvailable - (n - 1) * minGap) / n;
                            if(candidateCardW < minCardW)
                                candidateCardW = minCardW;
                            localCardW = Math.min(baseCardW, candidateCardW);
                            if(n > 1)
                            {
                                int candidateGap2 = (colAvailable - n * localCardW) / (n - 1);
                                localGap = Math.max(minGap, candidateGap2);
                            }
                            else
                                localGap = desiredGap;
                        }
                    }
                }
                int totalW = (n > 0) ? (n * localCardW + (n - 1) * localGap) : localCardW;
                int colLeftBound = colStart + 5;
                int colRightBound = colStart + spacing - 5;
                int startX;
                //if hand fits within column, center it; otherwise left-align so cards grow to the right
                if(totalW <= (colRightBound - colLeftBound))
                    startX = colCenter - totalW / 2;
                else 
                    startX = colLeftBound;
                int cardY = baseY + 20;
                int cardX = startX;
                for(Card c : hand)
                {
                    Image img = loader.getCardImage(c);
                    if (img != null)
                        g2.drawImage(img, cardX, cardY, localCardW, cardH, this);
                    else
                    {     
                        g2.drawRect(cardX, cardY, localCardW, cardH);
                        g2.drawString(c.toString(), cardX + 5, cardY + cardH/2);
                    }
                    cardX += localCardW + localGap;
                }
            }
            //draw chips
            int chipsX = colStart + 20;
            int chipsY = baseY + cardH + 50;
            Image gold = loader.getGoldChipImage();
            Image red = loader.getRedChipImage();
            if(gold != null)
                g2.drawImage(gold, chipsX, chipsY, 40, 40, this);
            if(red != null)
                g2.drawImage(red, chipsX + 95, chipsY, 40, 40, this);
            //restore original clip
            g2.setClip(oldClip);
        }
    }
}
