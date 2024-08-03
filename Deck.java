import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        // Initialize the deck with UNO cards
        String[] colors = {"red", "blue", "green", "yellow"};
        String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] specials = {"skip", "reverse", "draw2"};
        for (String color : colors) {
            for (String number : numbers) {
                cards.add(new Card(color, number, false));
                if (!number.equals("0")) {
                    cards.add(new Card(color, number, false)); // Add a second instance for each number (except 0)
                }
            }
            for (String special : specials) {
                cards.add(new Card(color, special, true));
                cards.add(new Card(color, special, true)); // Add a second instance for each special card
            }
        }
        for (int i = 0; i < 4; i++) { // Wild cards
            cards.add(new Card("wild", "wild", true));
            cards.add(new Card("wild", "draw4", true));
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}
