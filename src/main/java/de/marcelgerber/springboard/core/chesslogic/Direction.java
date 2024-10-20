package de.marcelgerber.springboard.core.chesslogic;

import lombok.Getter;

/**
 * Enum representing a direction on a chess board using LERF mapping (see Square.java)
 */
public enum Direction {
    // Ray Directions
    NORTH((byte) 8),
    WEST((byte) -1),
    SOUTH((byte) -8),
    EAST((byte) 1),
    NORTH_EAST((byte) 9),
    NORTH_WEST((byte) 7),
    SOUTH_WEST((byte) -9),
    SOUTH_EAST((byte) -7),
    NONE((byte) 0),

    // Knight Directions
    KNIGHT_NORTH_NORTH_WEST((byte) 15),
    KNIGHT_NORTH_NORTH_EAST((byte) 17),
    KNIGHT_NORTH_EAST_EAST((byte) 10),
    KNIGHT_SOUTH_EAST_EAST((byte) -6),
    KNIGHT_SOUTH_SOUTH_EAST((byte) -15),
    KNIGHT_SOUTH_SOUTH_WEST((byte) -17),
    KNIGHT_SOUTH_WEST_WEST((byte) -10),
    KNIGHT_NORTH_WEST_WEST((byte) 6);

    @Getter
    private final byte value;

    Direction(byte value) {
        this.value = value;
    }
}
