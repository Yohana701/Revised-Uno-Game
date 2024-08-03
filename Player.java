import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void drawCard(Deck deck) {
        Card drawnCard = deck.drawCard();
        if (drawnCard != null) {
            hand.add(drawnCard);
        }
    }

    public void playCard(Card card, DiscardPile discardPile) {
        hand.remove(card);
        discardPile.addCard(card);
    }

    public boolean hasWon() {
        return hand.isEmpty();
    }
}
