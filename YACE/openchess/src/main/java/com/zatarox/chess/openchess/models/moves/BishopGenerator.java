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

final class BishopGenerator extends AbstractGenerator {

    private final byte bishopShiftBits[] = {
        6, 5, 5, 5, 5, 5, 5, 6,
        5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5,
        6, 5, 5, 5, 5, 5, 5, 6
    };

    private static long bishopMagicNumber[] = {
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

    private final long[] bishop;
    private final long[] bishopMask;
    private final long[][] bishopMagic;

    private long getBishopShiftAttacks(long square, long all) {
        return checkSquareAttackedAux(square, all, +9, b_u | b_l)
                | checkSquareAttackedAux(square, all, +7, b_u | b_r)
                | checkSquareAttackedAux(square, all, -7, b_d | b_l)
                | checkSquareAttackedAux(square, all, -9, b_d | b_r);
    }

    public BishopGenerator() {
        super(Piece.BISHOP);
        bishop = new long[64];
        bishopMask = new long[64];
        bishopMagic = new long[64][];
        
        long square = 1;
        byte i = 0;
        while (square != 0) {
            bishop[i] = squareAttackedAuxSlider(square, +9, b_u | b_l)
                    | squareAttackedAuxSlider(square, +7, b_u | b_r)
                    | squareAttackedAuxSlider(square, -7, b_d | b_l)
                    | squareAttackedAuxSlider(square, -9, b_d | b_r);

            bishopMask[i] = squareAttackedAuxSliderMask(square, +9, b_u | b_l)
                    | squareAttackedAuxSliderMask(square, +7, b_u | b_r)
                    | squareAttackedAuxSliderMask(square, -7, b_d | b_l)
                    | squareAttackedAuxSliderMask(square, -9, b_d | b_r);
            
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

    @Override
    protected long attacks(Square index, BitBoard all) {
        int i = magicTransform(all.unwrap() & bishopMask[index.ordinal()], bishopMagicNumber[index.ordinal()], bishopShiftBits[index.ordinal()]);
        return bishopMagic[index.ordinal()][i];
    }

}
