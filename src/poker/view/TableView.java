package poker.view;

import poker.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TableView extends JPanel {

    // game data
    private ArrayList<Player> players;
    private CommunityCards communityCards;
    private CardLoader loader;
    private GameState gameState;

    // buttons
    private JButton startButton, foldButton, callButton, betButton, checkButton, endTurnButton;
    private JButton bet1Button, bet5Button, betPotButton;
    private JButton playAgainButton;

    // base width and height for dynamic scaling
    private static final double BASE_WIDTH = 1440.0;
    private static final double BASE_HEIGHT = 900.0;

    // constructor
    public TableView(ArrayList<Player> players, CommunityCards community, GameState gameState) {

        this.players = players;
        this.communityCards = community;
        this.gameState = gameState;
        this.loader = new CardLoader();

        setBackground(new Color(0, 100, 0));
        setLayout(null); // we manually place buttons

        initButtons();
        updateButtonVisibility();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutButtons();
                repaint();
            }
        });
    }

    // scaling helper
    private double getScale() {
        if (getWidth() <= 0 || getHeight() <= 0) return 1.0;
        double scaleX = getWidth() / BASE_WIDTH;
        double scaleY = getHeight() / BASE_HEIGHT;
        return Math.min(scaleX, scaleY);
    }

    // show message if exists
    private void showMessageIfNeeded() {
        String msg = gameState.getLastMessage();
        if (msg != null && !msg.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    // create and set up buttons
    private void initButtons() {

        startButton = new JButton("Start");
        foldButton = new JButton("Fold");
        callButton = new JButton("Call");
        betButton = new JButton("Bet");
        checkButton = new JButton("Check");
        endTurnButton = new JButton("End Turn");

        bet1Button = new JButton("$1");
        bet5Button = new JButton("$5");
        betPotButton = new JButton("Pot");

        playAgainButton = new JButton("Play Again");

        add(startButton); add(foldButton); add(callButton);
        add(betButton); add(checkButton); add(endTurnButton);
        add(bet1Button); add(bet5Button); add(betPotButton);
        add(playAgainButton);

        bet1Button.setVisible(false);
        bet5Button.setVisible(false);
        betPotButton.setVisible(false);
        playAgainButton.setVisible(false);

        // button actions
        startButton.addActionListener(e -> {
            gameState.startGame();
            updateButtonVisibility();
            showMessageIfNeeded();
            repaint();
        });

        foldButton.addActionListener(e -> {
            gameState.handleFold();
            gameState.doneWithAction();
            updateButtonVisibility();
            showMessageIfNeeded();
            repaint();
        });

        callButton.addActionListener(e -> {
            gameState.handleCall();
            gameState.doneWithAction();
            updateButtonVisibility();
            showMessageIfNeeded();
            repaint();
        });

        betButton.addActionListener(e -> {
            bet1Button.setVisible(true);
            bet5Button.setVisible(true);
            betPotButton.setVisible(true);
            repaint();
        });

        checkButton.addActionListener(e -> {
            gameState.handleCheck();

            if ("You may only call, fold, or bet.".equals(gameState.getLastMessage())) {
                updateButtonVisibility();
                showMessageIfNeeded();
                repaint();
                return;
            }

            gameState.doneWithAction();
            updateButtonVisibility();
            showMessageIfNeeded();
            repaint();
        });

        endTurnButton.addActionListener(e -> {
            gameState.endTurn(players.size());
            updateButtonVisibility();
            showMessageIfNeeded();
            repaint();
        });

        bet1Button.addActionListener(e -> handleBet(1));
        bet5Button.addActionListener(e -> handleBet(5));

        betPotButton.addActionListener(e -> {
            int potAmount = Math.max(gameState.getPot(), 1);
            handleBet(potAmount);
        });

        playAgainButton.addActionListener(e -> {
            gameState.startNewHand();
            updateButtonVisibility();
            repaint();
        });
    }

    // shared helper for making a bet
    private void handleBet(int amount) {
        gameState.handleBetAmount(amount);
        gameState.doneWithAction();

        bet1Button.setVisible(false);
        bet5Button.setVisible(false);
        betPotButton.setVisible(false);

        updateButtonVisibility();
        showMessageIfNeeded();
        repaint();
    }

    // show/hide buttons based on game state
    private void updateButtonVisibility() {

        if (gameState.isShowdownPhase()) {
            startButton.setVisible(false);
            foldButton.setVisible(false);
            callButton.setVisible(false);
            betButton.setVisible(false);
            checkButton.setVisible(false);
            endTurnButton.setVisible(false);
            bet1Button.setVisible(false);
            bet5Button.setVisible(false);
            betPotButton.setVisible(false);
            playAgainButton.setVisible(true);
            return;
        }

        if (gameState.isWaitingForPlayerStart()) {
            startButton.setVisible(true);
            foldButton.setVisible(false);
            callButton.setVisible(false);
            betButton.setVisible(false);
            checkButton.setVisible(false);
            endTurnButton.setVisible(false);
            bet1Button.setVisible(false);
            bet5Button.setVisible(false);
            betPotButton.setVisible(false);
            playAgainButton.setVisible(false);
        }

        else if (gameState.isWaitingForAction()) {
            startButton.setVisible(false);
            foldButton.setVisible(true);
            callButton.setVisible(true);
            betButton.setVisible(true);
            checkButton.setVisible(true);
            endTurnButton.setVisible(false);
            playAgainButton.setVisible(false);
        }

        else if (gameState.isWaitingForEndTurn()) {
            startButton.setVisible(false);
            foldButton.setVisible(false);
            callButton.setVisible(false);
            betButton.setVisible(false);
            checkButton.setVisible(false);
            endTurnButton.setVisible(true);
            bet1Button.setVisible(false);
            bet5Button.setVisible(false);
            betPotButton.setVisible(false);
            playAgainButton.setVisible(false);
        }

        else {
            startButton.setVisible(false);
            foldButton.setVisible(false);
            callButton.setVisible(false);
            betButton.setVisible(false);
            checkButton.setVisible(false);
            endTurnButton.setVisible(false);
            bet1Button.setVisible(false);
            bet5Button.setVisible(false);
            betPotButton.setVisible(false);
            playAgainButton.setVisible(false);
        }
    }

    // button layout
    private void layoutButtons() {

        int w = getWidth();
        int h = getHeight();

        int centerX = w / 2;
        int centerY = h / 2;

        startButton.setBounds(centerX - 60, centerY - 80, 120, 40);

        int actionW = 100, actionH = 40, spacing = 10;
        int totalWidth = actionW * 4 + spacing * 3;
        int leftX = centerX - totalWidth / 2;
        int actionY = centerY - (actionH + 40);

        foldButton.setBounds(leftX, actionY, actionW, actionH);
        callButton.setBounds(leftX + (actionW + spacing), actionY, actionW, actionH);
        betButton .setBounds(leftX + 2 * (actionW + spacing), actionY, actionW, actionH);
        checkButton.setBounds(leftX + 3 * (actionW + spacing), actionY, actionW, actionH);

        endTurnButton.setBounds(centerX - 70, centerY - 80, 140, 40);

        int betRowY = actionY + actionH + 10;
        bet1Button .setBounds(centerX - 160, betRowY, 60, 30);
        bet5Button .setBounds(centerX -  30, betRowY, 60, 30);
        betPotButton.setBounds(centerX +  80, betRowY, 60, 30);

        playAgainButton.setBounds(centerX - 80, centerY - 22, 160, 45);
    }

    // paint everything
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double scale = getScale();

        drawCurrentPlayerLabel(g, scale);
        drawPot(g, scale);
        drawCommunityCards(g, scale);
        drawPlayers(g, scale);
    }

    // draw current player label
    private void drawCurrentPlayerLabel(Graphics g, double scale) {
        if (players.isEmpty()) return;
        if (gameState.isShowdownPhase()) return;

        int index = gameState.getCurrentPlayerIndex();
        if (index < 0 || index >= players.size()) return;

        String name = players.get(index).getName();

        g.setFont(new Font("Arial", Font.BOLD,
                Math.max(12, (int)(24 * scale))));
        g.setColor(Color.WHITE);

        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(name);

        int x = (getWidth() - width) / 2;
        int y = (getHeight() / 2) - (int)(120 * scale);

        g.drawString(name, x, y);
    }

    // draw pot and chips
    private void drawPot(Graphics g, double scale) {

        if (gameState.getPot() <= 0) return;

        int gold = gameState.getPotGold();
        int red  = gameState.getPotRed();

        int chipW = (int)(120 * scale);
        int chipH = (int)(140 * scale);
        int spacing = (int)(5 * scale);
        int margin = (int)(20 * scale);

        int right = getWidth() - margin;
        int groupW = chipW * 2 + spacing;
        int startX = right - groupW;
        int y = margin + (int)(20 * scale);

        Image goldImg = loader.getGoldChipImage();
        Image redImg  = loader.getRedChipImage();

        g.setFont(new Font("Arial", Font.BOLD, Math.max(10, (int)(18 * scale))));
        g.setColor(Color.WHITE);

        String label = "POT";
        int textWidth = g.getFontMetrics().stringWidth(label);

        int labelX = startX + (groupW / 2) - (textWidth / 2);
        int labelY = y + (int)(15 * scale);

        g.drawString(label, labelX, labelY);

        if (goldImg != null)
            g.drawImage(goldImg, startX, y, chipW, chipH, null);
        if (redImg != null)
            g.drawImage(redImg, startX + chipW + spacing, y, chipW, chipH, null);

        int numberY = y + chipH + (int)(15 * scale);

        g.setFont(new Font("Arial", Font.PLAIN,
                Math.max(9, (int)(14 * scale))));

        g.drawString(String.valueOf(gold),
                startX + chipW/2 - (int)(5 * scale),
                numberY);

        g.drawString(String.valueOf(red),
                startX + chipW + spacing + chipW/2 - (int)(5 * scale),
                numberY);
    }

    // draw community cards
    private void drawCommunityCards(Graphics g, double scale) {

        int total = 5;
        ArrayList<Card> cards = communityCards.getCards();

        int cardW = (int)(240 * scale);
        int cardH = (int)(320 * scale);
        int spacing = (int)(-80 * scale);

        int rowWidth = total * cardW + (total - 1) * spacing;
        int x = (getWidth() - rowWidth) / 2;
        int y = (int)(getHeight() * 0.02);

        for (int i = 0; i < total; i++) {

            Image img;

            if (i < cards.size() && cards.get(i) != null)
                img = loader.getCardImage(cards.get(i));
            else
                img = loader.getCardBackImage();

            if (img != null)
                g.drawImage(img, x, y, cardW, cardH, null);

            x += cardW + spacing;
        }
    }

    // draw players
    private void drawPlayers(Graphics g, double scale) {

        if (players.isEmpty()) return;

        int count = players.size();
        int section = getWidth() / count;

        int cardW = (int)(200 * scale);
        int cardH = (int)(280 * scale);

        int panelW = (int)(300 * scale);
        int panelH = (int)(350 * scale);

        int chipW = (int)(140 * scale);
        int chipH = (int)(160 * scale);
        int chipSpacing = (int)(-70 * scale);
        int chipYOffset = (int)(175 * scale);

        int nameFontSize = Math.max(10, (int)(16 * scale));
        int chipFontSize = Math.max(9,  (int)(14 * scale));


        int yBase = (int)(getHeight() * 0.65);

        int currentPlayer = gameState.getCurrentPlayerIndex();
        boolean waitingAction = gameState.isWaitingForAction();
        boolean waitingEnd    = gameState.isWaitingForEndTurn();
        boolean showdown      = gameState.isShowdownPhase();

        int betChipW = (int)(120 * scale);
        int betChipH = (int)(140 * scale);
        int betChipSpacing = (int)(-10 * scale);

        for (int i = 0; i < count; i++) {
            g.setFont(new Font("Arial", Font.PLAIN, nameFontSize));

            Player p = players.get(i);

            int centerX = i * section + (section / 2);
            int leftX = centerX - (panelW / 2);

            // panel background
            if (p.isFolded()) g.setColor(new Color(20,20,20,180));
            else              g.setColor(new Color(30,30,30,140));

            g.fillRoundRect(leftX, yBase - (int)(40 * scale),
                    panelW, panelH, (int)(20 * scale), (int)(20 * scale));

            g.setColor(Color.WHITE);
            g.drawString(p.getName(), leftX + (int)(15 * scale), yBase);
            g.drawString("Total: $" + p.getTotalMoney(),
                    leftX + (int)(15 * scale), yBase + (int)(20 * scale));

            boolean showCards =
                    !p.isFolded() &&
                    (showdown || (i == currentPlayer && (waitingAction || waitingEnd)));

            Image c1 = null, c2 = null;

            if (!p.isFolded()) {
                if (showCards) {
                    c1 = loader.getCardImage(p.getCard1());
                    c2 = loader.getCardImage(p.getCard2());
                } else {
                    c1 = loader.getCardBackImage();
                    c2 = loader.getCardBackImage();
                }
            }

            int cardY = yBase - 10;
            int card1X = leftX + (int)(scale);
            int card2X = leftX + panelW - (int)(scale) - cardW;

            if (c1 != null) g.drawImage(c1, card1X, cardY, cardW, cardH, null);
            if (c2 != null) g.drawImage(c2, card2X, cardY, cardW, cardH, null);

            // draw bet chips
            int goldBet = gameState.getPlayerRoundGold(i);
            int redBet  = gameState.getPlayerRoundRed(i);

            if (goldBet > 0 || redBet > 0) {

                int groupWidth = betChipW * 2 + betChipSpacing;
                int betStartX = centerX - (groupWidth / 2);
                int betY = yBase - (int)(200 * scale);

                Image goldImg = loader.getGoldChipImage();
                Image redImg  = loader.getRedChipImage();

                if (goldImg != null)
                    g.drawImage(goldImg, betStartX, betY, betChipW, betChipH, null);

                if (redImg != null)
                    g.drawImage(redImg, betStartX + betChipW + betChipSpacing,
                            betY, betChipW, betChipH, null);

                int labelY = betY + betChipH + (int)(-2 * scale);

                g.setFont(new Font("Arial", Font.PLAIN, chipFontSize));
                g.setColor(Color.WHITE);

                int goldChipCount = goldBet;     // 1 gold = $1
                int redChipCount  = redBet / 5;  // 1 red = $5

                g.drawString(String.valueOf(goldChipCount),
                        betStartX + betChipW/2 - (int)(5 * scale),
                        labelY);

                g.drawString(String.valueOf(redChipCount),
                        betStartX + betChipW + betChipSpacing + betChipW/2 - (int)(5 * scale),
                        labelY);
            }

            // draw chip stacks
            int stackWidth = chipW * 2 + chipSpacing;
            int stackX = centerX - (stackWidth / 2);
            int stackY = cardY + chipYOffset + 10;

            Image goldStack = loader.getGoldChipImage();
            Image redStack  = loader.getRedChipImage();

            if (goldStack != null)
                g.drawImage(goldStack, stackX, stackY, chipW, chipH, null);

            if (redStack != null)
                g.drawImage(redStack,
                        stackX + chipW + chipSpacing,
                        stackY, chipW, chipH, null);

            int chipLabelY = stackY + (int)(125 * scale);

            g.setFont(new Font("Arial", Font.PLAIN, chipFontSize));
            g.setColor(Color.WHITE);

            g.drawString(String.valueOf(p.getGoldChips()),
                    stackX + chipW/2 - (int)(5 * scale),
                    chipLabelY);

            g.drawString(String.valueOf(p.getRedChips()),
                    stackX + chipW + chipSpacing + chipW/2 - (int)(5 * scale),
                    chipLabelY);
        }
    }

    public void refresh() {
        repaint();
    }
}
