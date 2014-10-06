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

final class KingGenerator extends AbstractPushGenerator {

    private final long[] king = new long[64];

    public KingGenerator(MovePonderingStrategy ponder) {
        super(Piece.KING, ponder);
        populate();
    }

    @Override
    protected void populate(short index, long square) {
        king[index] = squareAttacked(square, +8, b_u)
                | squareAttacked(square, -8, b_d)
                | squareAttacked(square, -1, b_r)
                | squareAttacked(square, +1, b_l)
                | squareAttacked(square, +9, b_u | b_l)
                | squareAttacked(square, +7, b_u | b_r)
                | squareAttacked(square, -7, b_d | b_l)
                | squareAttacked(square, -9, b_d | b_r);
    }

    @Override
    protected long coverage(Square index, BitBoard all, BoardSide turn) {
        return king[index.ordinal()];
    }

}
