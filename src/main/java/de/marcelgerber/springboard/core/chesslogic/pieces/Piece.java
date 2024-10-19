package de.marcelgerber.springboard.core.chesslogic.pieces;

import de.marcelgerber.springboard.core.chesslogic.Color;
import lombok.Getter;

public abstract class Piece {

    @Getter
    private final Color color;

    protected Piece(final Color color) {
        this.color = color;
    }

}
