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
        board.setFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        board.print();

        int nodes = perft(3);
        System.out.println(nodes);
    }

    private int perft(int depth) {
        if(depth == 0) return 1;

        int nodes = 0;
        List<Move> legalMoves = board.getPseudoLegalMoves();

        for(Move move : legalMoves) {
            board.makeMove(move);

            if(!board.isCheck()) {
                nodes += perft(depth - 1);
            }

            board.unmakeMove(move);
        }
        return nodes;
    }

}
