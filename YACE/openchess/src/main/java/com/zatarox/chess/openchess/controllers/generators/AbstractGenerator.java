/*
 * Copyright 2014 Guillaume CHAUVET.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zatarox.chess.openchess.controllers.generators;

import com.zatarox.chess.openchess.models.materials.*;
import com.zatarox.chess.openchess.models.moves.Move;
import com.zatarox.chess.openchess.models.moves.MoveVisitable;
import com.zatarox.chess.openchess.models.moves.MovesFactorySingleton;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class AbstractGenerator implements Generator {

    // Board borders
    protected static final long b_d = 0x00000000000000ffL; // down
    protected static final long b_u = 0xff00000000000000L; // up
    protected static final long b_r = 0x0101010101010101L; // right
    protected static final long b_l = 0x8080808080808080L; // left

    protected static final long b3_d = 0x0000000000ffffffL; // down
    protected static final long b3_u = 0xffffff0000000000L; // up

    private final Piece type;
    private MovePonderingStrategy ponder;

    protected AbstractGenerator(Piece type, MovePonderingStrategy ponder) {
        assert type != null;
        assert ponder != null;
        this.type = type;
        this.ponder = ponder;
    }

    protected final MovePonderingStrategy getPonder() {
        return ponder;
    }

    abstract protected long squareAttacked(long square, int shift, long border);

    /**
     * Can't be final, for pawns generation...
     */
    @Override
    public Queue<Move> attacks(ChessBoard board, Square square) throws IllegalArgumentException {
        if (!board.isOccuped(square)) {
            throw new IllegalArgumentException("No attacker piece");
        }
        final BitBoard all = board.getSide(BoardSide.WHITE).getSnapshot();
        all.merge(board.getSide(BoardSide.BLACK).getSnapshot());
        final Stone stone = board.getStone(square);
        final BitBoard attacks = new BitBoard(coverage(square, all, board.getTurn()) & board.getSide(stone.getSide().flip()).getSnapshot().unwrap());
        final Queue<Move> result = new PriorityQueue<>();
        for (Square to : attacks) {
            final Move move = MovesFactorySingleton.getInstance().createCapture(square, to, board.getStone(to).getPiece());
            getPonder().compute(board, (MoveVisitable) move);
            result.add(move);
        }
        return result;
    }

    @Override
    public final boolean isEnPrise(ChessBoard board, Square square) throws IllegalArgumentException {
        if (!board.isOccuped(square)) {
            throw new IllegalArgumentException("No piece to check");
        }
        final BitBoard all = board.getSide(BoardSide.WHITE).getSnapshot();
        all.merge(board.getSide(BoardSide.BLACK).getSnapshot());
        final Stone stone = board.getStone(square);
        final BitBoard attacks = new BitBoard();
        for (Square index : board.getSide(stone.getSide().flip()).get(type)) {
            attacks.merge(new BitBoard(coverage(index, all, stone.getSide()) & board.getSide(stone.getSide()).getSnapshot().unwrap()));
        }
        return (attacks.unwrap() & square.toLong()) != 0;
    }

    /**
     * Can't be final, for pawns generation...
     */
    @Override
    public Queue<Move> fills(ChessBoard board, Square square) {
        final BitBoard all = board.getSide(BoardSide.WHITE).getSnapshot();
        all.merge(board.getSide(BoardSide.BLACK).getSnapshot());
        final BitBoard attacks = new BitBoard(coverage(square, all, board.getTurn()) & ~all.unwrap());
        final Queue<Move> result = new PriorityQueue<>();
        for (Square to : attacks) {
            final Move move = MovesFactorySingleton.getInstance().createNormal(square, to);
            getPonder().compute(board, (MoveVisitable) move);
            result.add(move);
        }
        return result;
    }

    @Override
    public final Queue<Move> attacks(ChessBoard board) {
        final Queue<Move> result = new LinkedList<>();
        for (Square index : board.getSide(board.getTurn()).get(type)) {
            result.addAll(attacks(board, index));
        }
        return result;
    }

    @Override
    public final Queue<Move> fills(ChessBoard board) {
        final Queue<Move> result = new PriorityQueue<>();
        for (Square index : board.getSide(board.getTurn()).get(type)) {
            result.addAll(fills(board, index));
        }
        return result;
    }

    @Override
    public final Queue<Move> alls(ChessBoard board) {
        final Queue<Move> result = attacks(board);
        result.addAll(fills(board));
        return result;
    }

    @Override
    public final Queue<Move> alls(ChessBoard board, Square square) {
        Queue<Move> result = attacks(board, square);
        result.addAll(fills(board, square));
        return result;
    }

    /**
     * Magic! coverage, very fast method
     *
     * @param index
     * @param all
     * @param color
     * @return
     */
    abstract long coverage(Square index, BitBoard all, BoardSide color);

    /**
     * Call it from final constructor for magic bitboard initialization.
     */
    protected void populate() {
        long square = 1;
        short i = 0;
        while (square != 0) {
            populate(i, square);
            square <<= 1;
            i++;
        }
    }

    abstract protected void populate(short index, long square);

}
