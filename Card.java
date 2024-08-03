import java.util.Objects;

public class Card {
    private String color;
    private String number;
    private boolean isSpecial;

    public Card(String color, String number, boolean isSpecial) {
        this.color = color;
        this.number = number;
        this.isSpecial = isSpecial;
    }

    public String getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public boolean isSpecial() {
        return isSpecial;
    }
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color + " " + number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return color.equals(card.color) && number.equals(card.number);
    }

    @Override
    public int hashCode() {
        return color.hashCode() * 31 + number.hashCode();
    }
}
