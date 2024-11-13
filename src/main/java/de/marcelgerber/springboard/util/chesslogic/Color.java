package de.marcelgerber.springboard.util.chesslogic;

/**
 * Represents a color in a chess game
 */
public enum Color {
    WHITE,
    BLACK,
    NONE;

    private static final Color[] legalColors = new Color[]{WHITE, BLACK};

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

    /**
     * Converts a string to a Color
     *
     * @param string Color as a String
     * @return Color
     */
    public static Color fromString(String string) {
        for(Color c : legalColors) {
            if(c.toString().equalsIgnoreCase(string)) {
                return c;
            }
        }
        return NONE;
    }
}
