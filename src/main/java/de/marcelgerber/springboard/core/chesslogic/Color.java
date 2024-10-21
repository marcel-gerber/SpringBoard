package de.marcelgerber.springboard.core.chesslogic;

/**
 * Represents a color in a chess game
 */
public enum Color {
    WHITE,
    BLACK,
    NONE;

    /**
     * Returns the opposite Color
     *
     * @return Color
     */
    public Color getOpposite() {
        return switch(this) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
            case NONE -> NONE;
        };
    }
}
