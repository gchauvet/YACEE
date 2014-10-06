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
package com.zatarox.chess.openchess.models.moves.generators;

import com.zatarox.chess.openchess.models.materials.Piece;

abstract class AbstractSliderGenerator extends AbstractGenerator {

    public AbstractSliderGenerator(Piece type, MovePonderingStrategy ponder) {
        super(type, ponder);
    }

    @Override
    final protected long squareAttacked(long square, int shift, long border) {
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

    final protected long squareAttackedMask(long square, int shift, long border) {
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

    /**
     * Attacks for sliding pieces
     */
    final protected long checkSquareAttacked(long square, long all, int shift, long border) {
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

    /**
     * Fills pieces from a mask. Neccesary for magic generation variable bits is
     * the mask bytes number index goes from 0 to 2^bits
     */
    final protected long generatePieces(int index, int bits, long mask) {
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

    final protected int magicTransform(long b, long magic, byte bits) {
        return (int) ((b * magic) >>> (64 - bits));
    }

}
