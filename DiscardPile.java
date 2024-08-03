import java.util.LinkedList;

public class DiscardPile {
    private LinkedList<Card> pile;

    public DiscardPile() {
        pile = new LinkedList<>();
    }

    public void addCard(Card card) {
        pile.addLast(card);
    }

    public Card peek() {
        return pile.isEmpty() ? null : pile.getLast();
    }
}
