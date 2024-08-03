import java.util.Scanner;

public class UNOGAME {
    private Game game;

    public static void main(String[] args) {
        UNOGAME unoGame = new UNOGAME();
        unoGame.setupGame();
        unoGame.playGame();
    }

    // Initialize game settings and start the game
    private void setupGame() {
        game = new Game();

        // Create exactly 4 players
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= 4; i++) {
            System.out.println("Enter name for Player " + i + ":");
            String name = scanner.nextLine();
            Player player = new Player(name);
            game.addPlayer(player);
        }

        game.startGame();
    }

    // Play the game until a player wins
    private void playGame() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Player currentPlayer = game.getCurrentPlayer();

            System.out.println("It's " + currentPlayer.getName() + "'s turn.");
            System.out.println("Top card on discard pile: " + game.getDiscardPile().peek());
            System.out.println("Your hand: " + currentPlayer.getHand());

            boolean validMove = false;
            while (!validMove) {
                System.out.println("Enter the card you want to play (e.g., 'red 5') or 'draw' to draw a card:");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("draw")) {
                    currentPlayer.drawCard(game.getDeck());
                    validMove = true;
                } else {
                    Card playedCard = parseCardInput(input);
                    if (playedCard != null) {
                        if (currentPlayer.getHand().contains(playedCard)) {
                            if (canPlayCard(playedCard)) {
                                currentPlayer.playCard(playedCard, game.getDiscardPile());

                                // Handle wild and wild draw 4 cards
                                if (playedCard.getNumber().equals("wild") || playedCard.getNumber().equals("draw4")) {
                                    String newColor = chooseColor(scanner);
                                    game.getDiscardPile().peek().setColor(newColor);
                                }

                                validMove = true;
                            } else {
                                System.out.println("Card cannot be played on top of the discard pile. Try again.");
                            }
                        } else {
                            System.out.println("Card is not in hand. Try again.");
                        }
                    } else {
                        System.out.println("Invalid card input. Please enter a valid card description (e.g., 'red 5').");
                    }
                }
            }

            game.checkwin();
            game.checkSpecialCard(game.getDiscardPile().peek());
            game.nextTurn();
        }
    }

    // Helper method to parse card input
    private Card parseCardInput(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            return null;
        }
        String color = parts[0].trim().toLowerCase();
        String number = parts[1].trim().toLowerCase();

        // Create a Card object; assuming the second part is either a number or special card type
        boolean isSpecial = number.equals("skip") || number.equals("reverse") || number.equals("draw2") || number.equals("wild") || number.equals("draw4");

        // Validate color and number/type
        if (!isValidColor(color) || !isValidTypeOrNumber(number)) {
            return null;
        }

        return new Card(color, number, isSpecial);
    }

    // Check if the color is valid
    private boolean isValidColor(String color) {
        return color.equals("red") || color.equals("blue") || color.equals("green") || color.equals("yellow") || color.equals("wild");
    }

    // Check if the type/number is valid
    private boolean isValidTypeOrNumber(String typeOrNumber) {
        return typeOrNumber.equals("0") || typeOrNumber.equals("1") || typeOrNumber.equals("2") || typeOrNumber.equals("3") || typeOrNumber.equals("4") || typeOrNumber.equals("5") || typeOrNumber.equals("6") || typeOrNumber.equals("7") || typeOrNumber.equals("8") || typeOrNumber.equals("9") || typeOrNumber.equals("skip") || typeOrNumber.equals("reverse") || typeOrNumber.equals("draw2") || typeOrNumber.equals("wild") || typeOrNumber.equals("draw4");
    }

    // Check if the card can be played on top of the discard pile
    private boolean canPlayCard(Card card) {
        Card topCard = game.getDiscardPile().peek();
        if (topCard == null) return true; // If no top card, any card can be played
        return card.getColor().equals(topCard.getColor()) || card.getNumber().equals(topCard.getNumber()) || card.isSpecial();
    }

    // Prompt the player to choose a new color for wild cards
    private String chooseColor(Scanner scanner) {
        String[] colors = {"red", "blue", "green", "yellow"};
        String newColor;
        while (true) {
            System.out.println("Choose a color for the wild card (red, blue, green, yellow):");
            newColor = scanner.nextLine().trim().toLowerCase();
            for (String color : colors) {
                if (color.equals(newColor)) {
                    return newColor;
                }
            }
            System.out.println("Invalid color. Please choose again.");
        }
    }
}
