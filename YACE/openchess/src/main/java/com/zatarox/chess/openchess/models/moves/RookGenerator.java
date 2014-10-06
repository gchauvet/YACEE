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

final class RookGenerator extends AbstractSliderGenerator {

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

    private long[] rook = new long[64];
    private long[] rookMask = new long[64];
    private long[][] rookMagic = new long[64][];

    /**
     * without magic bitboards, too expensive, but neccesary for magic
     * generation
     */
    private long getRookShiftAttacks(long square, long all) {
        return checkSquareAttacked(square, all, +8, b_u)
                | checkSquareAttacked(square, all, -8, b_d)
                | checkSquareAttacked(square, all, -1, b_r)
                | checkSquareAttacked(square, all, +1, b_l);
    }

    public RookGenerator() {
        super(Piece.ROOK);
        populate();
    }

    @Override
    protected void populate(short index, long square) {
        rook[index] = squareAttacked(square, +8, b_u)
                | squareAttacked(square, -8, b_d)
                | squareAttacked(square, -1, b_r)
                | squareAttacked(square, +1, b_l);

        rookMask[index] = squareAttackedMask(square, +8, b_u)
                | squareAttackedMask(square, -8, b_d)
                | squareAttackedMask(square, -1, b_r)
                | squareAttackedMask(square, +1, b_l);
        // And now generate magics			
        int rookPositions = (1 << rookShiftBits[index]);
        rookMagic[index] = new long[rookPositions];
        for (int j = 0; j < rookPositions; j++) {
            long pieces = generatePieces(j, rookShiftBits[index], rookMask[index]);
            int magicIndex = magicTransform(pieces, rookMagicNumber[index], rookShiftBits[index]);
            rookMagic[index][magicIndex] = getRookShiftAttacks(square, pieces);
        }
    }

    @Override
    protected long coverage(Square index, BitBoard all, BoardSide turn) {
        int i = magicTransform(all.unwrap() & rookMask[index.ordinal()], rookMagicNumber[index.ordinal()], rookShiftBits[index.ordinal()]);
        return rookMagic[index.ordinal()][i];
    }

}
