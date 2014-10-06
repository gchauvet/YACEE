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
package com.zatarox.chess.openchess.models.moves;

import com.zatarox.chess.openchess.models.materials.*;
import java.util.PriorityQueue;
import java.util.Queue;

final class PawnGenerator extends AbstractPushGenerator {

    private final long[] pawnDownwards = new long[64];
    private final long[] pawnUpwards = new long[64];

    public PawnGenerator() {
        super(Piece.PAWN);
        populate();
    }

    @Override
    public Queue<Move> fills(ChessBoard board, Square square) {
        final BitBoard all = board.getSnapshot(BoardSide.WHITE);
        all.merge(board.getSnapshot(BoardSide.BLACK));
        final long mask = squareAttacked(1L << square.ordinal(), 8 * (board.getTurn() == BoardSide.WHITE ? -1 : 1), b_u);
        final BitBoard attacks = new BitBoard(mask & ~all.unwrap());
        final Queue<Move> result = new PriorityQueue<>();
        if (!attacks.isEmpty()) {
            result.add(MovesFactorySingleton.getInstance().createNormal(square, attacks.iterator().next()));
            attacks.clear();
            if (square.getRankIndex() == Square.Rank._2 || square.getRankIndex() == Square.Rank._7) {
                attacks.merge(new BitBoard(mask << 7));
            }
            if (!attacks.isEmpty()) {
                result.add(MovesFactorySingleton.getInstance().createCharge(square, attacks.iterator().next()));
            }
        }
        return result;
    }

    @Override
    public Queue<Move> attacks(ChessBoard board, Square square) {
        final Queue<Move> result = super.attacks(board, square);
        final BoardSide attacker = board.getStone(square).getSide();
        if (board.getSide(attacker).isEnpassant()) {
            result.add(MovesFactorySingleton.getInstance().createEnpassant(square, board.getSide(attacker).getEnpassant()));
        }
        return result;
    }

    @Override
    protected void populate(short index, long square) {
        pawnUpwards[index] = squareAttacked(square, 7, b_u | b_r)
                | squareAttacked(square, 9, b_u | b_l);
        pawnDownwards[index] = squareAttacked(square, -7, b_d | b_l)
                | squareAttacked(square, -9, b_d | b_r);
    }

    @Override
    protected long coverage(Square index, BitBoard all, BoardSide color) {
        return color == BoardSide.BLACK ? pawnUpwards[index.ordinal()] : pawnDownwards[index.ordinal()];
    }

}
