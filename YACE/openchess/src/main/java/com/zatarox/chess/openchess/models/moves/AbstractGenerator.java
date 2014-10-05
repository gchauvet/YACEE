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
    private static final long b_d = 0x00000000000000ffL; // down
    private static final long b_u = 0xff00000000000000L; // up
    private static final long b_r = 0x0101010101010101L; // right
    private static final long b_l = 0x8080808080808080L; // left

    // Board borders (2 squares),for the knight
    private static final long b2_d = 0x000000000000ffffL; // down
    private static final long b2_u = 0xffff000000000000L; // up
    private static final long b2_r = 0x0303030303030303L; // right
    private static final long b2_l = 0xC0C0C0C0C0C0C0C0L; // left

    private static final long b3_d = 0x0000000000ffffffL; // down
    private static final long b3_u = 0xffffff0000000000L; // up

    private static final long r2_d = 0x000000000000ff00L; // rank 2 down
    private static final long r2_u = 0x00ff000000000000L; // up

    private static final long r3_d = 0x0000000000ff0000L; // rank 3 down
    private static final long r3_u = 0x0000ff0000000000L; // up

    public final static byte rookShiftBits[] = {
        12, 11, 11, 11, 11, 11, 11, 12,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        12, 11, 11, 11, 11, 11, 11, 12
    };

    public final static byte bishopShiftBits[] = {
        6, 5, 5, 5, 5, 5, 5, 6,
        5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5,
        6, 5, 5, 5, 5, 5, 5, 6
    };

    // Magic numbers generated with MagicNumbersGen
    public final static long rookMagicNumber[] = {
        0x1080108000400020L, 0x40200010004000L, 0x100082000441100L, 0x480041000080080L, 0x100080005000210L,
        0x100020801000400L, 0x280010000800200L, 0x100008020420100L, 0x400800080400020L, 0x401000402000L,
        0x100801000200080L, 0x801000800800L, 0x800400080080L, 0x800200800400L, 0x1000200040100L, 0x4840800041000080L,
        0x20008080004000L, 0x404010002000L, 0x808010002000L, 0x828010000800L, 0x808004000800L, 0x14008002000480L,
        0x40002100801L, 0x20001004084L, 0x802080004000L, 0x200080400080L, 0x810001080200080L, 0x10008080080010L,
        0x4000080080040080L, 0x40080020080L, 0x1000100040200L, 0x80008200004124L, 0x804000800020L, 0x804000802000L,
        0x801000802000L, 0x2000801000800804L, 0x80080800400L, 0x80040080800200L, 0x800100800200L, 0x8042000104L,
        0x208040008008L, 0x10500020004000L, 0x100020008080L, 0x2000100008008080L, 0x200040008008080L, 0x8020004008080L,
        0x1000200010004L, 0x100040080420001L, 0x80004000200040L, 0x200040100140L, 0x20004800100040L, 0x100080080280L,
        0x8100800400080080L, 0x8004020080040080L, 0x9001000402000100L, 0x40080410200L, 0x208040110202L, 0x800810022004012L,
        0x1000820004011L, 0x1002004100009L, 0x41001002480005L, 0x81000208040001L, 0x4000008201100804L, 0x2841008402L
    };

    public final static long bishopMagicNumber[] = {
        0x1020041000484080L, 0x20204010a0000L, 0x8020420240000L, 0x404040085006400L, 0x804242000000108L,
        0x8901008800000L, 0x1010110400080L, 0x402401084004L, 0x1000200810208082L, 0x20802208200L,
        0x4200100102082000L, 0x1024081040020L, 0x20210000000L, 0x8210400100L, 0x10110022000L, 0x80090088010820L,
        0x8001002480800L, 0x8102082008200L, 0x41001000408100L, 0x88000082004000L, 0x204000200940000L,
        0x410201100100L, 0x2000101012000L, 0x40201008200c200L, 0x10100004204200L, 0x2080020010440L,
        0x480004002400L, 0x2008008008202L, 0x1010080104000L, 0x1020001004106L, 0x1040200520800L, 0x8410000840101L,
        0x1201000200400L, 0x2029000021000L, 0x4002400080840L, 0x5000020080080080L, 0x1080200002200L,
        0x4008202028800L, 0x2080210010080L, 0x800809200008200L, 0x1082004001000L, 0x1080202411080L,
        0x840048010101L, 0x40004010400200L, 0x500811020800400L, 0x20200040800040L, 0x1008012800830a00L,
        0x1041102001040L, 0x11010120200000L, 0x2020222020c00L, 0x400002402080800L, 0x20880000L,
        0x1122020400L, 0x11100248084000L, 0x210111000908000L, 0x2048102020080L, 0x1000108208024000L,
        0x1004100882000L, 0x41044100L, 0x840400L, 0x4208204L, 0x80000200282020cL, 0x8a001240100L, 0x2040104040080L
    };

    protected static final long[] rook;
    // The same but with border removed for magic bitboards
    protected static final long[] rookMask;
    protected static final long[][] rookMagic;
    protected static final long[] bishop;
    protected static final long[] bishopMask;
    protected static final long[][] bishopMagic;
    protected static final long[] knight;
    protected static final long[] king;
    protected static final long[] pawnDownwards;
    protected static final long[] pawnUpwards;

    private final Piece type;

    private static long squareAttackedAux(long square, int shift, long border) {
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

    private static long squareAttackedAuxSlider(long square, int shift, long border) {
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

    private static long squareAttackedAuxSliderMask(long square, int shift, long border) {
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

    static {
        rook = new long[64];
        rookMask = new long[64];
        rookMagic = new long[64][];
        bishop = new long[64];
        bishopMask = new long[64];
        bishopMagic = new long[64][];
        knight = new long[64];
        king = new long[64];
        pawnDownwards = new long[64];
        pawnUpwards = new long[64];

        long square = 1;
        byte i = 0;
        while (square != 0) {
            rook[i] = squareAttackedAuxSlider(square, +8, b_u)
                    | squareAttackedAuxSlider(square, -8, b_d)
                    | squareAttackedAuxSlider(square, -1, b_r)
                    | squareAttackedAuxSlider(square, +1, b_l);

            rookMask[i] = squareAttackedAuxSliderMask(square, +8, b_u)
                    | squareAttackedAuxSliderMask(square, -8, b_d)
                    | squareAttackedAuxSliderMask(square, -1, b_r)
                    | squareAttackedAuxSliderMask(square, +1, b_l);

            bishop[i] = squareAttackedAuxSlider(square, +9, b_u | b_l)
                    | squareAttackedAuxSlider(square, +7, b_u | b_r)
                    | squareAttackedAuxSlider(square, -7, b_d | b_l)
                    | squareAttackedAuxSlider(square, -9, b_d | b_r);

            bishopMask[i] = squareAttackedAuxSliderMask(square, +9, b_u | b_l)
                    | squareAttackedAuxSliderMask(square, +7, b_u | b_r)
                    | squareAttackedAuxSliderMask(square, -7, b_d | b_l)
                    | squareAttackedAuxSliderMask(square, -9, b_d | b_r);

            knight[i] = squareAttackedAux(square, +17, b2_u | b_l)
                    | squareAttackedAux(square, +15, b2_u | b_r)
                    | squareAttackedAux(square, -15, b2_d | b_l)
                    | squareAttackedAux(square, -17, b2_d | b_r)
                    | squareAttackedAux(square, +10, b_u | b2_l)
                    | squareAttackedAux(square, +6, b_u | b2_r)
                    | squareAttackedAux(square, -6, b_d | b2_l)
                    | squareAttackedAux(square, -10, b_d | b2_r);

            pawnUpwards[i] = squareAttackedAux(square, 7, b_u | b_r)
                    | squareAttackedAux(square, 9, b_u | b_l);

            pawnDownwards[i] = squareAttackedAux(square, -7, b_d | b_l)
                    | squareAttackedAux(square, -9, b_d | b_r);

            king[i] = squareAttackedAux(square, +8, b_u)
                    | squareAttackedAux(square, -8, b_d)
                    | squareAttackedAux(square, -1, b_r)
                    | squareAttackedAux(square, +1, b_l)
                    | squareAttackedAux(square, +9, b_u | b_l)
                    | squareAttackedAux(square, +7, b_u | b_r)
                    | squareAttackedAux(square, -7, b_d | b_l)
                    | squareAttackedAux(square, -9, b_d | b_r);

            // And now generate magics			
            int rookPositions = (1 << rookShiftBits[i]);
            rookMagic[i] = new long[rookPositions];
            for (int j = 0; j < rookPositions; j++) {
                long pieces = generatePieces(j, rookShiftBits[i], rookMask[i]);
                int magicIndex = magicTransform(pieces, rookMagicNumber[i], rookShiftBits[i]);
                rookMagic[i][magicIndex] = getRookShiftAttacks(square, pieces);
            }

            int bishopPositions = (1 << bishopShiftBits[i]);
            bishopMagic[i] = new long[bishopPositions];
            for (int j = 0; j < bishopPositions; j++) {
                long pieces = generatePieces(j, bishopShiftBits[i], bishopMask[i]);
                int magicIndex = magicTransform(pieces, bishopMagicNumber[i], bishopShiftBits[i]);
                bishopMagic[i][magicIndex] = getBishopShiftAttacks(square, pieces);
            }

            square <<= 1;
            i++;
        }
    }

    protected static int magicTransform(long b, long magic, byte bits) {
        return (int) ((b * magic) >>> (64 - bits));
    }

    /**
     * Fills pieces from a mask. Neccesary for magic generation variable bits is
     * the mask bytes number index goes from 0 to 2^bits
     */
    private static long generatePieces(int index, int bits, long mask) {
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
     * without magic bitboards, too expensive, but neccesary for magic
     * generation
     */
    private static long getRookShiftAttacks(long square, long all) {
        return checkSquareAttackedAux(square, all, +8, b_u)
                | checkSquareAttackedAux(square, all, -8, b_d)
                | checkSquareAttackedAux(square, all, -1, b_r)
                | checkSquareAttackedAux(square, all, +1, b_l);
    }

    private static long getBishopShiftAttacks(long square, long all) {
        return checkSquareAttackedAux(square, all, +9, b_u | b_l)
                | checkSquareAttackedAux(square, all, +7, b_u | b_r)
                | checkSquareAttackedAux(square, all, -7, b_d | b_l)
                | checkSquareAttackedAux(square, all, -9, b_d | b_r);
    }

    /**
     * Attacks for sliding pieces
     */
    private static long checkSquareAttackedAux(long square, long all, int shift, long border) {
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
    public final List<Move> attacks(ChessBoard board) {
        List<Move> result = new LinkedList<>();
        for (Square index : board.getSide(board.getTurn()).get(type)) {
            result.addAll(attacks(board, index));
        }
        return result;
    }

    @Override
    public final List<Move> alls(ChessBoard board) {
        List<Move> result = new LinkedList<>();
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

}
