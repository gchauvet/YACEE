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

final class KnightGenerator extends AbstractGenerator {

    private final long[] knight;

    public KnightGenerator() {
        super(Piece.KNIGHT);
        knight = new long[64];

        long square = 1;
        byte i = 0;
        while (square != 0) {
            knight[i] = squareAttackedAux(square, +17, b2_u | b_l)
                    | squareAttackedAux(square, +15, b2_u | b_r)
                    | squareAttackedAux(square, -15, b2_d | b_l)
                    | squareAttackedAux(square, -17, b2_d | b_r)
                    | squareAttackedAux(square, +10, b_u | b2_l)
                    | squareAttackedAux(square, +6, b_u | b2_r)
                    | squareAttackedAux(square, -6, b_d | b2_l)
                    | squareAttackedAux(square, -10, b_d | b2_r);
            square <<= 1;
            i++;
        }
    }

    @Override
    protected long attacks(Square index, BitBoard all) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
