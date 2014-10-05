/*
 * Copyright 2014 Romain.
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

final class RookGenerator extends AbstractGenerator {

    private static final long r2_d = 0x000000000000ff00L; // rank 2 down
    private static final long r2_u = 0x00ff000000000000L; // up

    private static final long r3_d = 0x0000000000ff0000L; // rank 3 down
    private static final long r3_u = 0x0000ff0000000000L; // up

    // Magic numbers generated with MagicNumbersGen
    private final long rookMagicNumber[] = {
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

    private final byte rookShiftBits[] = {
        12, 11, 11, 11, 11, 11, 11, 12,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        12, 11, 11, 11, 11, 11, 11, 12
    };

    private static long[] rook;
    private static long[] rookMask;
    private static long[][] rookMagic;

    /**
     * without magic bitboards, too expensive, but neccesary for magic
     * generation
     */
    private long getRookShiftAttacks(long square, long all) {
        return checkSquareAttackedAux(square, all, +8, b_u)
                | checkSquareAttackedAux(square, all, -8, b_d)
                | checkSquareAttackedAux(square, all, -1, b_r)
                | checkSquareAttackedAux(square, all, +1, b_l);
    }

    public RookGenerator() {
        super(Piece.ROOK);
        rook = new long[64];
        rookMask = new long[64];
        rookMagic = new long[64][];
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
            // And now generate magics			
            int rookPositions = (1 << rookShiftBits[i]);
            rookMagic[i] = new long[rookPositions];
            for (int j = 0; j < rookPositions; j++) {
                long pieces = generatePieces(j, rookShiftBits[i], rookMask[i]);
                int magicIndex = magicTransform(pieces, rookMagicNumber[i], rookShiftBits[i]);
                rookMagic[i][magicIndex] = getRookShiftAttacks(square, pieces);
            }
            square <<= 1;
            i++;
        }
    }

    @Override
    protected long attacks(Square index, BitBoard all) {
        int i = magicTransform(all.unwrap() & rookMask[index.ordinal()], rookMagicNumber[index.ordinal()], rookShiftBits[index.ordinal()]);
        return rookMagic[index.ordinal()][i];
    }

}
