package de.marcelgerber.springboard.chesslogic;

import de.marcelgerber.springboard.core.chesslogic.Board;
import de.marcelgerber.springboard.core.chesslogic.Move;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PerftTests {

    private Board board;

    @Test
    public void run() {
        board = new Board();
        board.setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        board.print();

        int nodes = perft(4);
        System.out.println(nodes);
    }

    private int perft(int depth) {
        if(depth == 0) return 1;

        int nodes = 0;
        List<Move> legalMoves = board.getPseudoLegalMoves();

        for(Move move : legalMoves) {
//            System.out.println(move.toString());
            board.makeMove(move);

            if(!board.isCheck()) {
//                board.print();
                nodes += perft(depth - 1);
            }

            board.unmakeMove(move);
        }
        return nodes;
    }

}
