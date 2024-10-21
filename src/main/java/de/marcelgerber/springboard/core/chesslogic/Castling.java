package de.marcelgerber.springboard.core.chesslogic;

import lombok.Getter;

/**
 * Class for representing the castling rights.
 */
public class Castling {

    // All castling rights will be saved in a single byte
    @Getter
    private byte castlingRights;

    private static final CastlingValue[] blackCastlings = { CastlingValue.BLACK_00, CastlingValue.BLACK_000 };
    private static final CastlingValue[] whiteCastlings = { CastlingValue.WHITE_00, CastlingValue.WHITE_000 };

    // These squares need to be empty
    private static final byte[] black00EmptySquares = { 61, 62 };
    private static final byte[] black000EmptySquares = { 57, 58, 59 };

    private static final byte[] white00EmptySquares = { 5, 6 };
    private static final byte[] white000EmptySquares = { 1, 2, 3 };

    // These squares are not allowed to be attacked by the enemy
    private static final byte[] black00NotAttacked = { 61, 62 };
    private static final byte[] black000NotAttacked = { 58, 59 };

    private static final byte[] white00NotAttacked = { 5, 6 };
    private static final byte[] white000NotAttacked = { 2, 3 };

    public Castling() {
        castlingRights = CastlingValue.NO_CASTLING.getValue();
    }

    // Copy constructor
    public Castling(Castling castling) {
        castlingRights = castling.getCastlingRights();
    }

    /**
     * Sets a castling right
     *
     * @param castlingValue CastlingValue
     */
    public void set(CastlingValue castlingValue) {
        castlingRights |= castlingValue.getValue();
    }

    /**
     * Unsets a castling right
     *
     * @param castlingValue CastlingValue
     */
    public void unSet(CastlingValue castlingValue) {
        castlingRights &= (byte) ~castlingValue.getValue();
    }

    /**
     * Unsets all castling rights for a color
     *
     * @param color Color
     */
    public void unSet(Color color) {
        switch (color) {
            case Color.WHITE:
                unSet(CastlingValue.WHITE_00);
                unSet(CastlingValue.WHITE_000);
                return;
            case Color.BLACK:
                unSet(CastlingValue.BLACK_00);
                unSet(CastlingValue.BLACK_000);
                return;
            default:
                throw new IllegalStateException("Unexpected value: " + color);
        }
    }

    /**
     * Returns 'true' if the provided castling right is set
     *
     * @param castlingValue CastlingValue
     * @return boolean
     */
    public boolean has(CastlingValue castlingValue) {
        return (castlingRights & castlingValue.getValue()) == castlingValue.getValue();
    }

    /**
     * Returns 'true' if the provided color still has both castling rights
     *
     * @param color Color
     * @return boolean
     */
    public boolean has(Color color) {
        for(CastlingValue castlingValue : getCastlings(color)) {
            if(has(castlingValue)) return true;
        }
        return false;
    }

    /**
     * Returns 'true' if there are no more castling rights set
     *
     * @return boolean
     */
    public boolean hasNoCastling() {
        return castlingRights == CastlingValue.NO_CASTLING.getValue();
    }

    /**
     * Returns array of castling rights for a provided color
     *
     * @param color Color
     * @return Array of CastlingValue
     */
    public static CastlingValue[] getCastlings(Color color) {
        return switch (color) {
            case Color.BLACK -> blackCastlings;
            case Color.WHITE -> whiteCastlings;
            default -> throw new IllegalStateException("Unexpected value: " + color);
        };
    }

    /**
     * Returns the kings' target square index based off the provided castling right
     *
     * @param castlingValue CastlingValue
     * @return byte
     */
    public static byte getKingTargetIndex(CastlingValue castlingValue) {
        return switch (castlingValue) {
            case CastlingValue.BLACK_00 -> 62;
            case CastlingValue.BLACK_000 -> 58;
            case CastlingValue.WHITE_00 -> 6;
            case CastlingValue.WHITE_000 -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + castlingValue);
        };
    }

    /**
     * Returns the rooks' source square index based off the provided castling right
     *
     * @param castlingValue CastlingValue
     * @return byte
     */
    public static byte getRookSourceIndex(CastlingValue castlingValue) {
        return switch (castlingValue) {
            case CastlingValue.WHITE_00 -> 7;
            case CastlingValue.WHITE_000 -> 0;
            case CastlingValue.BLACK_00 -> 63;
            case CastlingValue.BLACK_000 -> 56;
            default -> throw new IllegalStateException("Unexpected value: " + castlingValue);
        };
    }

    /**
     * Returns the rooks' target square index based off the provided castling right
     *
     * @param castlingValue CastlingValue
     * @return byte
     */
    public static byte getRookTargetIndex(CastlingValue castlingValue) {
        return switch (castlingValue) {
            case CastlingValue.WHITE_00 -> 5;
            case CastlingValue.WHITE_000 -> 3;
            case CastlingValue.BLACK_00 -> 61;
            case CastlingValue.BLACK_000 -> 59;
            default -> throw new IllegalStateException("Unexpected value: " + castlingValue);
        };
    }

    /**
     * Returns the castling right based on the provided kings' target square index
     *
     * @param index Kings' target square index
     * @return CastlingValue
     */
    public static CastlingValue fromKingTargetIndex(byte index) {
        return switch (index) {
            case 2 -> CastlingValue.WHITE_000;
            case 6 -> CastlingValue.WHITE_00;
            case 58 -> CastlingValue.BLACK_000;
            case 62 -> CastlingValue.BLACK_00;
            default -> CastlingValue.NO_CASTLING;
        };
    }

    /**
     * Returns the castling right based on the provided rooks' source square index
     *
     * @param index Rooks' source square index
     * @return CastlingValue
     */
    public static CastlingValue fromRookSourceIndex(byte index) {
        return switch (index) {
            case 0 -> CastlingValue.WHITE_000;
            case 7 -> CastlingValue.WHITE_00;
            case 56 -> CastlingValue.BLACK_000;
            case 63 -> CastlingValue.BLACK_00;
            default -> CastlingValue.NO_CASTLING;
        };
    }

    /**
     * Returns an array of squares' indices that have to be empty
     *
     * @param castlingValue CastlingValue
     * @return array of squares indices
     */
    public static byte[] getEmptySquares(CastlingValue castlingValue) {
        return switch (castlingValue) {
            case CastlingValue.BLACK_00 -> black00EmptySquares;
            case CastlingValue.BLACK_000 -> black000EmptySquares;
            case CastlingValue.WHITE_00 -> white00EmptySquares;
            case CastlingValue.WHITE_000 -> white000EmptySquares;
            default -> throw new IllegalStateException("Unexpected value: " + castlingValue);
        };
    }

    /**
     * Returns an array of squares' indices that are not allowed to be attacked by the opponent
     *
     * @param castlingValue CastlingValue
     * @return array of squares indices
     */
    public static byte[] getNotAttackedSquares(CastlingValue castlingValue) {
        return switch (castlingValue) {
            case CastlingValue.BLACK_00 -> black00NotAttacked;
            case CastlingValue.BLACK_000 -> black000NotAttacked;
            case CastlingValue.WHITE_00 -> white00NotAttacked;
            case CastlingValue.WHITE_000 -> white000NotAttacked;
            default -> throw new IllegalStateException("Unexpected value: " + castlingValue);
        };
    }

}
