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

import com.zatarox.chess.openchess.models.materials.*;

final class KnightGenerator extends AbstractPushGenerator {

    // Board borders (2 squares),for the knight
    private static final long b2_d = 0x000000000000ffffL; // down
    private static final long b2_u = 0xffff000000000000L; // up
    private static final long b2_r = 0x0303030303030303L; // right
    private static final long b2_l = 0xC0C0C0C0C0C0C0C0L; // left
    private final long[] knight = new long[64];

    public KnightGenerator(MovePonderingStrategy ponder) {
        super(Piece.KNIGHT, ponder);
        populate();
    }

    @Override
    protected void populate(short index, long square) {
        knight[index] = squareAttacked(square, +17, b2_u | b_l)
                | squareAttacked(square, +15, b2_u | b_r)
                | squareAttacked(square, -15, b2_d | b_l)
                | squareAttacked(square, -17, b2_d | b_r)
                | squareAttacked(square, +10, b_u | b2_l)
                | squareAttacked(square, +6, b_u | b2_r)
                | squareAttacked(square, -6, b_d | b2_l)
                | squareAttacked(square, -10, b_d | b2_r);
    }

    @Override
    protected long coverage(Square index, BitBoard all, BoardSide turn) {
        return knight[index.ordinal()];
    }

}
