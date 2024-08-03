import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private Deck deck;
    private DiscardPile discardPile;
    private int currentPlayerIndex;
    private boolean gameDirection; // true for clockwise, false for counter-clockwise

    public Game() {
        players = new ArrayList<>();
        deck = new Deck();
        discardPile = new DiscardPile();
        deck.shuffle();
        currentPlayerIndex = 0;
        gameDirection = true; // Start with clockwise direction
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Deck getDeck() {
        return deck;
    }

    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    public void startGame() {
        // Deal 7 cards to each player
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.drawCard(deck);
            }
        }

        // Start the discard pile with one card
        discardPile.addCard(deck.drawCard());
    }

    public void nextTurn() {
        if (gameDirection) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }

    public void checkSpecialCard(Card card) {
        if (card.isSpecial()) {
            switch (card.getNumber()) {
                case "skip":
                    nextTurn();
                    break;
                case "reverse":
                    gameDirection = !gameDirection;
                    break;
                case "draw2":
                    int nextPlayerIndex = (currentPlayerIndex + (gameDirection ? 1 : -1) + players.size()) % players.size();
                    Player nextPlayer = players.get(nextPlayerIndex);
                    nextPlayer.drawCard(deck);
                    nextPlayer.drawCard(deck);
                    break;
                case "wild":
                    // Implement wild card logic if needed
                    break;
                case "draw4":
                    nextPlayerIndex = (currentPlayerIndex + (gameDirection ? 1 : -1) + players.size()) % players.size();
                    nextPlayer = players.get(nextPlayerIndex);
                    nextPlayer.drawCard(deck);
                    nextPlayer.drawCard(deck);
                    nextPlayer.drawCard(deck);
                    nextPlayer.drawCard(deck);
                    // Implement color change logic if needed
                    break;
            }
        }
    }

    public void checkwin() {
        for (Player player : players) {
            if (player.hasWon()) {
                System.out.println(player.getName() + " has won!");
                System.exit(0);
            }
        }
    }
}
