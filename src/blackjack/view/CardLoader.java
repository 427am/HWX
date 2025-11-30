package blackjack.view;

import blackjack.model.Card;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.HashMap;

public class CardLoader {

    private HashMap<String, Image> imageMap;

    private Image goldChipImage;
    private Image redChipImage;
    private Image cardBackImage; // CardBack.png

    public CardLoader() {
        imageMap = new HashMap<String, Image>();
    }

    public Image getCardImage(Card c) {
        if (c == null) return null;

        String name = c.getCardName(); // e.g. "ASeal"
        String path = "images/cards/" + name + ".png";

        if (imageMap.containsKey(path)) {
            return imageMap.get(path);
        }

        Image img = new ImageIcon(path).getImage();
        imageMap.put(path, img);

        return img;
    }

    // load the back-of-card image once and reuse it
    public Image getCardBackImage() {
        if (cardBackImage == null) {
            cardBackImage = new ImageIcon("images/cards/CardBack.png").getImage();
        }
        return cardBackImage;
    }

    public Image getGoldChipImage() {
        if (goldChipImage == null) {
            goldChipImage = new ImageIcon("images/chips/goldChip.png").getImage();
        }
        return goldChipImage;
    }

    public Image getRedChipImage() {
        if (redChipImage == null) {
            redChipImage = new ImageIcon("images/chips/redChip.png").getImage();
        }
        return redChipImage;
    }
}
