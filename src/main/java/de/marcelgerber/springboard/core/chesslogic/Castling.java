package de.marcelgerber.springboard.core.chesslogic;

import lombok.Getter;

/**
 * Class for representing the castling rights.
 */
public class Castling {

    /**
     * All castling rights can be stored in just 4 bits.
     * 00: short castling (kings' side)
     * 000: long castling (queens' side)
     */
    @Getter
    public enum CastlingValue {
        NO_CASTLING((byte) 0),
        WHITE_00((byte) 0b00000001),
        WHITE_000((byte) 0b00000010),
        BLACK_00((byte) 0b00000100),
        BLACK_000((byte) 0b00001000);

        private final byte value;

        CastlingValue(final byte value) {
            this.value = value;
        }
    }

    // All castling rights will be saved in a single byte
    private byte castlingRights;

    public Castling() {
        castlingRights = CastlingValue.NO_CASTLING.getValue();
    }

}
