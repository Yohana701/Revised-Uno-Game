import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

// Card class representing an individual card
class Card {
    private String color;
    private int value;
    private boolean isSpecial;

    // Special card types
    public static final int SKIP = -1;
    public static final int REVERSE = -2;
    public static final int DRAW_TWO = -3;
    public static final int WILD = -4;
    public static final int WILD_DRAW_FOUR = -5;

    public Card(String color, int value) {
        this.color = color;
        this.value = value;
        this.isSpecial = value < 0; // Special cards are denoted by negative values
    }

    public String getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    @Override
    public String toString() {
        if (isSpecial) {
            switch (value) {
                case SKIP: return color + " Skip";
                case REVERSE: return color + " Reverse";
                case DRAW_TWO: return color + " Draw Two";
                case WILD: return "Wild";
                case WILD_DRAW_FOUR: return "Wild Draw Four";
                default: return color + " Special";
            }
        } else {
            return color + " " + value;
        }
    }
}

// Deck class representing a deck of cards
class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initialize();
        shuffle();
    }

    private void initialize() {
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        for (String color : colors) {
            for (int value = 0; value <= 9; value++) {
                cards.add(new Card(color, value));
                if (value != 0) { // Skip adding another 0 card
                    cards.add(new Card(color, value));
                }
            }
            // Add special cards
            cards.add(new Card(color, Card.SKIP));
            cards.add(new Card(color, Card.REVERSE));
            cards.add(new Card(color, Card.DRAW_TWO));
        }
        // Add wild cards
        cards.add(new Card("", Card.WILD));
        cards.add(new Card("", Card.WILD_DRAW_FOUR));
        cards.add(new Card("", Card.WILD));
        cards.add(new Card("", Card.WILD_DRAW_FOUR));
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        } else {
            return null; // Deck is empty
        }
    }

    public Card getTopCard() {
        if (!cards.isEmpty()) {
            return cards.get(cards.size() - 1);
        } else {
            return null;
        }
    }
}

// Player class representing a player
class Player {
    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public boolean playCard(Card card, Card topCard) {
        if (card.isSpecial() || card.getColor().equals(topCard.getColor()) || card.getValue() == topCard.getValue()) {
            hand.remove(card);
            return true;
        }
        return false;
    }

    public void displayHand() {
        System.out.println(name + "'s Hand:");
        for (Card card : hand) {
            System.out.println(card);
        }
        System.out.println();
    }

    public boolean hasWon() {
        return hand.isEmpty();
    }

    public boolean hasCard() {
        return !hand.isEmpty();
    }

    public Card drawCardFromDeck(Deck deck) {
        Card card = deck.drawCard();
        if (card != null) {
            addCard(card);
        }
        return card;
    }

    public List<Card> getHand() {
        return hand;
    }
}

// Main game class
public class UnoGame {
    public static void main(String[] args) {
        int numPlayers = 4;
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
        players.add(new Player("Player 4"));

        Deck deck = new Deck();
        Card topCard = deck.drawCard(); // First card on the discard pile

        // Deal 7 cards to each player
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                Card card = deck.drawCard();
                if (card != null) {
                    player.addCard(card);
                }
            }
        }

        boolean gameOn = true;
        int currentPlayerIndex = 0;
        boolean clockwise = true;

        Scanner scanner = new Scanner(System.in);

        while (gameOn) {
            Player currentPlayer = players.get(currentPlayerIndex);

            System.out.println("Current top card: " + topCard);
            currentPlayer.displayHand();

            System.out.println(currentPlayer.name + ", it's your turn.");
            System.out.println("Choose an action: (P)lay card or (D)raw card");
            String action = scanner.nextLine();

            if (action.equalsIgnoreCase("P")) {
                System.out.println("Enter the index of the card you want to play (0 to " + (currentPlayer.getHand().size() - 1) + "):");
                int cardIndex = Integer.parseInt(scanner.nextLine());

                if (cardIndex >= 0 && cardIndex < currentPlayer.getHand().size()) {
                    Card cardToPlay = currentPlayer.getHand().get(cardIndex);

                    if (currentPlayer.playCard(cardToPlay, topCard)) {
                        topCard = cardToPlay;

                        if (cardToPlay.isSpecial()) {
                            handleSpecialCard(cardToPlay, players, currentPlayerIndex, clockwise);
                        }

                        if (currentPlayer.hasWon()) {
                            System.out.println(currentPlayer.name + " wins!");
                            gameOn = false;
                            break;
                        }
                    } else {
                        System.out.println("Invalid card. Try again.");
                    }
                } else {
                    System.out.println("Invalid index. Try again.");
                }
            } else if (action.equalsIgnoreCase("D")) {
                currentPlayer.drawCardFromDeck(deck);
                if (currentPlayer.playCard(currentPlayer.getHand().get(currentPlayer.getHand().size() - 1), topCard)) {
                    topCard = currentPlayer.getHand().remove(currentPlayer.getHand().size() - 1);
                }
            }

            // Check for UNO
            if (currentPlayer.getHand().size() == 1) {
                System.out.println(currentPlayer.name + " says UNO!");
            }

            // Move to the next player
            if (clockwise) {
                currentPlayerIndex = (currentPlayerIndex + 1) % numPlayers;
            } else {
                currentPlayerIndex = (currentPlayerIndex - 1 + numPlayers) % numPlayers;
            }
        }

        scanner.close();
    }

    private static void handleSpecialCard(Card card, List<Player> players, int currentPlayerIndex, boolean clockwise) {
        switch (card.getValue()) {
            case Card.SKIP:
                currentPlayerIndex = (currentPlayerIndex + (clockwise ? 1 : -1) + players.size()) % players.size();
                break;
            case Card.REVERSE:
                clockwise = !clockwise;
                break;
            case Card.DRAW_TWO:
                int nextPlayerIndex = (currentPlayerIndex + (clockwise ? 1 : -1) + players.size()) % players.size();
                for (int i = 0; i < 2; i++) {
                    players.get(nextPlayerIndex).drawCardFromDeck(deck);
                }
                break;
            case Card.WILD:
                break;
            case Card.WILD_DRAW_FOUR:
                nextPlayerIndex = (currentPlayerIndex + (clockwise ? 1 : -1) + players.size()) % players.size();
                for (int i = 0; i < 4; i++) {
                    players.get(nextPlayerIndex).drawCardFromDeck(deck);
                }
                break;
        }
    }
}
