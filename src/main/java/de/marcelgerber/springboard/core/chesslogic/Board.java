package de.marcelgerber.springboard.core.chesslogic;

import de.marcelgerber.springboard.core.chesslogic.pieces.*;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class for representing a logical (not persistent) Chess board.
 */
@Getter
public class Board {

    @Data
    private class StateInfo {
        private Castling castling;
        private Square enPassant;
        private Piece captured;
        private byte halfMoveCounter;

        public StateInfo(Castling castling, Square enPassant, Piece captured, byte halfMoveCounter) {
            this.castling = new Castling(castling);
            this.enPassant = new Square(enPassant);
            this.captured = captured;
            this.halfMoveCounter = halfMoveCounter;
        }
    }

    private final Piece[] pieces;
    private Color sideToMove;
    private Castling castling;
    private Square enPassant;
    private byte halfMoveCounter;
    private int plies;

    // All previous states of the board will be saved in here
    private final Stack<StateInfo> prevStates = new Stack<>();

    public Board() {
        pieces = new Piece[64];
        sideToMove = Color.WHITE;
        castling = new Castling();
        enPassant = new Square(SquareValue.NONE);
        halfMoveCounter = 0;
        plies = 0;

        init();
    }

    /**
     * Returns the piece standing on this index
     *
     * @param index Index of square
     * @return Piece
     */
    public Piece getPiece(final byte index) {
        return pieces[index];
    }

    /**
     * Returns the piece standing on this square
     *
     * @param square Square
     * @return Piece
     */
    public Piece getPiece(final Square square) {
        return pieces[square.getIndex()];
    }

    /**
     * Returns the piece standing on this square, but checks if the square is valid.
     * If not it returns the NullPieces' instance
     *
     * @param square Square
     * @return Piece
     */
    public Piece getPieceOrNullPiece(Square square) {
        if(square.getValue() == SquareValue.NONE) {
            return NullPiece.getInstance();
        }
        return getPiece(square.getIndex());
    }

    /**
     * Returns the king's square based on the color
     *
     * @param color Color
     * @return Square
     */
    private Square getKingSquare(Color color) {
        for (byte index = 0; index < 64; index++) {
            Piece piece = getPiece(index);

            if(piece instanceof King && piece.getColor() == color) {
                return new Square(index);
            }
        }
        return new Square(SquareValue.NONE);
    }

    /**
     * Places a piece on the board
     *
     * @param index Index of square
     * @param piece Piece
     */
    private void placePiece(byte index, Piece piece) {
        pieces[index] = piece;
    }

    /**
     * Removes a piece from the board
     *
     * @param index Index of square
     */
    private void removePiece(byte index) {
        pieces[index] = NullPiece.getInstance();
    }

    /**
     * Returns 'true' if the piece standing on the index is a NullPiece
     *
     * @param index Index of square
     * @return boolean
     */
    public boolean isEmpty(byte index) {
        return pieces[index] instanceof NullPiece;
    }

    /**
     * Returns 'true' if the piece standing on the index is a NullPiece
     *
     * @param square Square
     * @return boolean
     */
    public boolean isEmpty(Square square) {
        return pieces[square.getIndex()] instanceof NullPiece;
    }

    /**
     * Returns 'true' if all indices of the array are empty on the board
     *
     * @param indices Indices of squares
     * @return boolean
     */
    public boolean areEmpty(byte[] indices) {
        for(byte index : indices) {
            if(!isEmpty(index)) return false;
        }
        return true;
    }

    /**
     * Returns 'true' if any squares' indices are attacked by the current opponent
     *
     * @param indices squares' indices
     * @return boolean
     */
    public boolean areAttacked(byte[] indices) {
        List<Square> attackedSquares = getAttackedSquares(sideToMove.getOpposite());

        for(Square square : attackedSquares) {
            for(byte index : indices) {
                if(square.getIndex() == index) return true;
            }
        }
        return false;
    }

    /**
     * Returns 'true' when a king is standing on the provided square
     *
     * @param square Square
     * @return boolean
     */
    public boolean isKing(Square square) {
        return getPiece(square) instanceof King;
    }

    /**
     * Returns 'true' if the piece standing on the provided square has the same color as the provided piece
     *
     * @param square Square
     * @param piece Piece
     * @return boolean
     */
    public boolean isFriendly(Square square, Piece piece) {
        return getPiece(square).getColor() == piece.getColor();
    }

    /**
     * Returns 'true' if the piece standing on the provided square has the opposite color as the provided piece
     *
     * @param square Square
     * @param piece Piece
     * @return boolean
     */
    public boolean isOpponent(Square square, Piece piece) {
        Piece targetPiece = getPiece(square);
        return (!(targetPiece instanceof NullPiece) && targetPiece.getColor() != piece.getColor());
    }

    /**
     * Returns 'true' if the piece standing on the provided square is a NullPiece
     * or the provided piece is an enemy piece
     *
     * @param square Square
     * @param piece Piece
     * @return boolean
     */
    public boolean isEmptyOrOpponent(Square square, Piece piece) {
        Piece targetPiece = getPiece(square);
        return targetPiece instanceof NullPiece || targetPiece.getColor() != piece.getColor();
    }

    /**
     * Returns 'true' if en passant is possible for a pawn standing on the provided square
     *
     * @param to Square
     * @param piece Piece (needs to be a pawn)
     * @return boolean
     */
    private boolean isEnPassantPossible(Square to, Piece piece) {
        Square east = Square.add(to, Direction.EAST);
        Square west = Square.add(to, Direction.WEST);

        Piece neighborEast = getPieceOrNullPiece(east);
        Piece neighborWest = getPieceOrNullPiece(west);

        return (neighborEast instanceof Pawn && neighborEast.getColor() != piece.getColor()) ||
        (neighborWest instanceof Pawn && neighborWest.getColor() != piece.getColor());
    }

    /**
     * Plays a move on the board
     *
     * @param move Move
     */
    public void makeMove(Move move) {
        Square from = move.getFrom();
        Square to = move.getTo();
        MoveType moveType = move.getMoveType();

        Piece moved = getPiece(from);
        Piece captured = getPiece(to);

        Castling castling = new Castling(this.castling);
        Square enPassantSquare = new Square(this.enPassant);

        StateInfo stateInfo = new StateInfo(castling, enPassantSquare, captured, this.halfMoveCounter);
        prevStates.push(stateInfo);

        this.halfMoveCounter++;
        this.plies++;

        if(this.enPassant.getValue() != SquareValue.NONE) {
            this.enPassant.setValue(SquareValue.NONE);
        }

        // Played move is a capture
        if(!(captured instanceof NullPiece)) {
            this.halfMoveCounter = 0;
            removePiece(to.getIndex());

            if(captured instanceof Rook) {
                CastlingValue castlingValue = Castling.fromRookSourceIndex(to.getIndex());
                this.castling.unSet(castlingValue);
            }
        }

        if(this.castling.has(sideToMove)) {
            if(moved instanceof King) {
                this.castling.unSet(sideToMove);
            }
            else if(moved instanceof Rook) {
                CastlingValue castlingValue = Castling.fromRookSourceIndex(from.getIndex());
                this.castling.unSet(castlingValue);
            }
        }

        if(moved instanceof Pawn) {
            this.halfMoveCounter = 0;
            // Double push
            if(Math.abs(from.getIndex() - to.getIndex()) == 16) {
                if(isEnPassantPossible(to, moved)) {
                    int enPassantIndex = to.getIndex() ^ 8;
                    this.enPassant = new Square((byte) enPassantIndex);
                }
            }
        }

        if(moveType == MoveType.CASTLING) {
            CastlingValue castlingValue = Castling.fromKingTargetIndex(to.getIndex());
            byte startingRookIndex = Castling.getRookSourceIndex(castlingValue);
            byte endingRookIndex = Castling.getRookTargetIndex(castlingValue);

            Piece rook = getPiece(startingRookIndex);

            // Remove rook and king
            removePiece(startingRookIndex);
            removePiece(from.getIndex());

            // Place rook and king at new positions
            placePiece(endingRookIndex, rook);
            placePiece(to.getIndex(), moved);
        }
        else if(moveType == MoveType.PROMOTION) {
            PieceType promotionType = move.getPromotion();
            Piece promotionPiece = promotionType.getPiece(sideToMove);

            removePiece(from.getIndex());
            placePiece(to.getIndex(), promotionPiece);
        }
        else {
            removePiece(from.getIndex());
            placePiece(to.getIndex(), moved);
        }

        if(moveType == MoveType.ENPASSANT) {
            int enPassantIndex = to.getIndex() ^ 8;
            removePiece((byte) enPassantIndex);
        }

        sideToMove = sideToMove.getOpposite();
    }

    /**
     * Undo the last played move on the board
     *
     * @param move The last played Move
     */
    public void unmakeMove(Move move) {
        StateInfo stateInfo = prevStates.pop();

        this.castling = stateInfo.getCastling();
        this.enPassant = stateInfo.getEnPassant();
        this.halfMoveCounter = stateInfo.getHalfMoveCounter();
        Piece captured = stateInfo.getCaptured();

        sideToMove = sideToMove.getOpposite();
        this.plies--;

        Square from = move.getFrom();
        Square to = move.getTo();
        MoveType moveType = move.getMoveType();

        if(moveType == MoveType.CASTLING) {
            CastlingValue castlingValue = Castling.fromKingTargetIndex(to.getIndex());
            byte startingRookIndex = Castling.getRookSourceIndex(castlingValue);
            byte endingRookIndex = Castling.getRookTargetIndex(castlingValue);

            Piece rook = getPiece(endingRookIndex);
            Piece king = getPiece(to.getIndex());

            // Remove rook and king
            removePiece(endingRookIndex);
            removePiece(to.getIndex());

            // Place rook and king at old positions
            placePiece(startingRookIndex, rook);
            placePiece(from.getIndex(), king);

            return;
        }

        if(moveType == MoveType.PROMOTION) {
            Pawn pawn = new Pawn(sideToMove);

            removePiece(to.getIndex());
            placePiece(from.getIndex(), pawn);

            if(!(captured instanceof NullPiece)) {
                placePiece(to.getIndex(), captured);
            }
            return;
        }

        Piece moved = getPiece(to.getIndex());
        removePiece(to.getIndex());
        placePiece(from.getIndex(), moved);

        if(moveType == MoveType.ENPASSANT) {
            Pawn pawn = new Pawn(sideToMove.getOpposite());
            byte pawnIndex = (byte) (enPassant.getIndex() ^ 8);

            placePiece(pawnIndex, pawn);
            return;
        }

        if(!(captured instanceof NullPiece)) {
            placePiece(to.getIndex(), captured);
        }
    }

    /**
     * Returns a list of squares that are currently attacked by the provided color
     *
     * @return List of attacked squares
     */
    public List<Square> getAttackedSquares(Color color) {
        List<Square> attackedSquares = new ArrayList<>();

        for(byte index = 0; index < 64; index++) {
            Piece piece = getPiece(index);
            if(piece.getColor() != color) continue;

            attackedSquares.addAll(piece.getAttackedSquares(this, new Square(index)));
        }
        return attackedSquares;
    }

    /**
     * Returns a list of squares that are currently attacked by the side to move
     *
     * @return List of attacked squares
     */
    public List<Square> getAttackedSquares() {
        return getAttackedSquares(sideToMove);
    }

    /**
     * Returns 'true' when there is currently a check
     *
     * @return boolean
     */
    public boolean isCheck() {
        Square kingSquare = getKingSquare(sideToMove.getOpposite());

        for(Square attacked : getAttackedSquares()) {
            if (kingSquare.getValue() == attacked.getValue()) return true;
        }
        return false;
    }

    /**
     * Returns all current pseudo legal moves
     *
     * @return List of Moves
     */
    public List<Move> getPseudoLegalMoves() {
        List<Move> pseudoLegalMoves = new ArrayList<>();

        for(byte index = 0; index < 64; index++) {
            Piece piece = getPiece(index);
            if(piece.getColor() != sideToMove) continue;

            pseudoLegalMoves.addAll(piece.getPseudoLegalMoves(this, new Square(index)));
        }
        return pseudoLegalMoves;
    }

    /**
     * Returns all current legal moves
     *
     * @return List of Moves
     */
    public List<Move> getLegalMoves() {
        List<Move> pseudoLegalMoves = getPseudoLegalMoves();
        List<Move> legalMoves = new ArrayList<>();

        for(Move move : pseudoLegalMoves) {
            makeMove(move);
            if(!isCheck()) {
                legalMoves.add(move);
            }
            unmakeMove(move);
        }
        return legalMoves;
    }

    /**
     * Returns the boards' full move counter
     *
     * @return int
     */
    public int getFullMoveCounter() {
        return 1 + plies / 2;
    }

    /**
     * Returns the current board position as a FEN string
     *
     * @return FEN string
     */
    public String getFen() {
        StringBuilder fen = new StringBuilder();

        for(byte rank = 7; rank >= 0; rank--) {
            int emptySquares = 0;

            for(byte file = 0; file < 8; file++) {
                byte index = (byte) (rank * 8 + file);
                Piece piece = getPiece(index);

                if(piece == NullPiece.getInstance()) {
                    emptySquares++;
                }
                else {
                    if(emptySquares > 0) {
                        fen.append(emptySquares);
                        emptySquares = 0;
                    }
                    fen.append(piece.getChar());
                }
            }

            if(emptySquares > 0) {
                fen.append(emptySquares);
            }

            if(rank > 0) {
                fen.append("/");
            }
        }

        // Append side to move
        char colorToMove = this.sideToMove == Color.WHITE ? 'w' : 'b';
        fen.append(" ");
        fen.append(colorToMove);

        // Append castling rights
        fen.append(" ");
        fen.append(this.castling.toString());

        // Append en passant square
        fen.append(" ");
        fen.append(this.enPassant.toString());

        // Append half and full move counter
        fen.append(" ");
        fen.append(this.halfMoveCounter);
        fen.append(" ");
        fen.append(getFullMoveCounter());

        return fen.toString();
    }

    /**
     * Sets the board position based on the provided FEN string
     *
     * @param fen String
     */
    public void setFen(String fen) {
        reset();

        String[] split = fen.split(" ");
        String pieces = split[0];
        String sideToMove = split.length > 1 ? split[1] : "w";
        String castling = split.length > 2 ? split[2] : "-";
        String enPassant = split.length > 3 ? split[3] : "-";
        String halfMove = split.length > 4 ? split[4] : "0";
        String fullMove = split.length > 5 ? split[5] : "1";

        this.sideToMove = sideToMove.equals("w") ? Color.WHITE : Color.BLACK;
        this.enPassant = new Square(enPassant);
        this.halfMoveCounter = Byte.parseByte(halfMove);
        this.plies = Integer.parseInt(fullMove);
        this.plies = this.plies * 2 - 2;

        if(this.sideToMove == Color.BLACK) this.plies++;

        byte index = 56;
        for(char c : pieces.toCharArray()) {
            if (c == '/') {
                index -= 16;
                continue;
            }

            if(Character.isDigit(c)) {
                index += (byte) (c - '0');
                continue;
            }

            Piece piece = Piece.fromChar(c);
            placePiece(index, piece);
            index++;
        }

        if(castling.equals("-")) return;

        for(char c : castling.toCharArray()) {
            switch (c) {
                case 'K' -> this.castling.set(CastlingValue.WHITE_00);
                case 'Q' -> this.castling.set(CastlingValue.WHITE_000);
                case 'k' -> this.castling.set(CastlingValue.BLACK_00);
                case 'q' -> this.castling.set(CastlingValue.BLACK_000);
            }
        }
    }

    /**
     * Initializes the board with empty pieces
     */
    private void init() {
        for(byte i = 0; i < 64; i++) {
            pieces[i] = NullPiece.getInstance();
        }
    }

    /**
     * Resets all the boards' data
     */
    public void reset() {
        init();
        this.sideToMove = Color.WHITE;
        this.castling.reset();
        this.enPassant.setValue(SquareValue.NONE);
        this.halfMoveCounter = 0;
        this.plies = 0;
        this.prevStates.clear();
    }

    /**
     * Prints the board
     */
    public void print() {
        StringBuilder stringBuilder = new StringBuilder("\n---------------------------------\n");
        byte index = 56;

        for (byte i = 0; i < 8; i++) {
            for (byte j = 0; j < 8; j++) {
                stringBuilder.append("| ");
                stringBuilder.append(getPiece(index).getChar());
                stringBuilder.append(" ");

                index++;
            }

            stringBuilder.append("|\n");
            stringBuilder.append("---------------------------------\n");
            index -= 16;
        }
        System.out.println(stringBuilder);
    }

}
