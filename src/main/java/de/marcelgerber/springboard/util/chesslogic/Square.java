package de.marcelgerber.springboard.util.chesslogic;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for representing a square on the board
 */
@Data
@AllArgsConstructor
public class Square {

    private SquareValue value;

    // Copy constructor
    public Square(Square square) {
        this.value = square.getValue();
    }

    public Square(final byte index) {
        value = SquareValue.get(index);
    }

    public Square(String string) {
        if(string.equals("-")) {
            value = SquareValue.NONE;
            return;
        }

        byte index = (byte) ((string.charAt(0) - 'a') + ((string.charAt(1) - '1') * 8));
        value = SquareValue.get(index);
    }

    /**
     * Adds a direction to the square and returns a new Square
     *
     * @param square Square
     * @param direction Direction
     * @return Square
     */
    public static Square add(Square square, Direction direction) {
        byte targetIndex = (byte) (square.getIndex() + direction.getValue());
        Square targetSquare = new Square(targetIndex);

        if(Math.abs(square.getFileIndex() - targetSquare.getFileIndex()) > 2) {
            return new Square(SquareValue.NONE);
        }
        return targetSquare;
    }

    /**
     * Returns the index of the square in a LERF mapping
     *
     * @return byte
     */
    public byte getIndex() {
        return (byte) value.ordinal();
    }

    /**
     * Returns the file index of the square
     *
     * @return byte
     */
    public byte getFileIndex() {
        return (byte) (getIndex() & 7);
    }

    /**
     * Returns the rank index of the square
     *
     * @return byte
     */
    public byte getRankIndex() {
        return (byte) (getIndex() >> 3);
    }

    /**
     * Converts the Square to a string
     *
     * @return String
     */
    @Override
    public String toString() {
        if(value == SquareValue.NONE) return "-";

        String string = "";

        char file = (char) ('a' + getFileIndex());
        char rank = (char) ('1' + getRankIndex());

        string += file;
        string += rank;
        return string;
    }

    /**
     * Returns the rank index based on the provided character
     *
     * @return byte
     */
    public static byte getRankIndex(char c) {
        if(c >= '1' && c <= '8') {
            return (byte) (c - '1');
        }
        return -1;
    }

    /**
     * Returns the file index based on the provided character
     *
     * @return byte
     */
    public static byte getFileIndex(char c) {
        if (c >= 'a' && c <= 'h') {
            return (byte) (c - 'a');
        }
        return -1;
    }

}
