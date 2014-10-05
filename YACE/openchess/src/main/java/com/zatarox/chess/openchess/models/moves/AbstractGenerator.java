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
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractGenerator implements Generator {

    // Board borders
    protected static final long b_d = 0x00000000000000ffL; // down
    protected static final long b_u = 0xff00000000000000L; // up
    protected static final long b_r = 0x0101010101010101L; // right
    protected static final long b_l = 0x8080808080808080L; // left

    // Board borders (2 squares),for the knight
    protected static final long b2_d = 0x000000000000ffffL; // down
    protected static final long b2_u = 0xffff000000000000L; // up
    protected static final long b2_r = 0x0303030303030303L; // right
    protected static final long b2_l = 0xC0C0C0C0C0C0C0C0L; // left

    protected static final long b3_d = 0x0000000000ffffffL; // down
    protected static final long b3_u = 0xffffff0000000000L; // up

    private final Piece type;

    protected long squareAttackedAux(long square, int shift, long border) {
        if ((square & border) == 0) {
            if (shift > 0) {
                square <<= shift;
            } else {
                square >>>= -shift;
            }
            return square;
        }
        return 0;
    }

    protected long squareAttackedAuxSlider(long square, int shift, long border) {
        long ret = 0;
        while ((square & border) == 0) {
            if (shift > 0) {
                square <<= shift;
            } else {
                square >>>= -shift;
            }
            ret |= square;
        }
        return ret;
    }

    protected long squareAttackedAuxSliderMask(long square, int shift, long border) {
        long ret = 0;
        while ((square & border) == 0) {
            if (shift > 0) {
                square <<= shift;
            } else {
                square >>>= -shift;
            }
            if ((square & border) == 0) {
                ret |= square;
            }
        }
        return ret;
    }

    protected int magicTransform(long b, long magic, byte bits) {
        return (int) ((b * magic) >>> (64 - bits));
    }

    /**
     * Fills pieces from a mask. Neccesary for magic generation variable bits is
     * the mask bytes number index goes from 0 to 2^bits
     */
    protected long generatePieces(int index, int bits, long mask) {
        int i;
        long lsb;
        long result = 0L;
        for (i = 0; i < bits; i++) {
            lsb = mask & (-mask);
            mask ^= lsb; // Deactivates lsb bit of the mask to get next bit next time
            if ((index & (1 << i)) != 0) {
                result |= lsb; // if bit is set to 1
            }
        }
        return result;
    }

    /**
     * Attacks for sliding pieces
     */
    protected long checkSquareAttackedAux(long square, long all, int shift, long border) {
        long ret = 0;
        while ((square & border) == 0) {
            if (shift > 0) {
                square <<= shift;
            } else {
                square >>>= -shift;
            }
            ret |= square;
            // If we collide with other piece
            if ((square & all) != 0) {
                break;
            }
        }
        return ret;
    }

    protected AbstractGenerator(Piece type) {
        this.type = type;
    }

    @Override
    public final List<Move> attacks(ChessBoard board, Square square) {
        final BitBoard all = board.getSnapshot(BoardSide.WHITE);
        all.merge(board.getSnapshot(BoardSide.BLACK));
        final BitBoard attacks = new BitBoard(attacks(square, all) & board.getSnapshot(board.getStone(square).getSide().flip()).unwrap());
        final List<Move> result = new LinkedList<>();
        for (Square to : attacks) {
            result.add(MovesFactorySingleton.getInstance().createCapture(square, to, board.getStone(to).getPiece()));
        }
        return result;
    }

    @Override
    public final List<Move> fills(ChessBoard board, Square square) {
        final BitBoard all = board.getSnapshot(BoardSide.WHITE);
        all.merge(board.getSnapshot(BoardSide.BLACK));
        final BitBoard attacks = new BitBoard(attacks(square, all) & ~all.unwrap());
        final List<Move> result = new LinkedList<>();
        for (Square to : attacks) {
            result.add(MovesFactorySingleton.getInstance().createNormal(square, to));
        }
        return result;
    }

    @Override
    public final List<Move> attacks(ChessBoard board) {
        final List<Move> result = new LinkedList<>();
        for (Square index : board.getSide(board.getTurn()).get(type)) {
            result.addAll(attacks(board, index));
        }
        return result;
    }

    @Override
    public final List<Move> alls(ChessBoard board) {
        final List<Move> result = new LinkedList<>();
        for (Square index : board.getSide(board.getTurn()).get(type)) {
            result.addAll(fills(board, index));
        }
        return result;
    }

    @Override
    public final List<Move> alls(ChessBoard board, Square square) {
        List<Move> result = attacks(board, square);
        result.addAll(fills(board, square));
        return result;
    }

    /**
     * Magic! attacks, very fast method
     *
     * @param index
     * @param all
     * @return
     */
    abstract protected long attacks(Square index, BitBoard all);

}
