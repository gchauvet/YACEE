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
import org.junit.Test;
import static org.junit.Assert.*;

public final class MovesFactorySingletonTest {

    @Test
    public void testCreateNormal() {
        final MovesFactorySingleton mover = MovesFactorySingleton.getInstance();
        assertEquals(mover.createNormal(Square.A1, Square.A2).getClass(), BasicMove.class);
        assertEquals(mover.createCapture(Square.A1, Square.A2, Piece.PAWN).getClass(), CaptureMove.class);
        assertEquals(mover.createCastle(Castle.SHORT, BoardSide.WHITE).getClass(), CastleMove.class);
        assertEquals(mover.createEnpassant(Square.A1, Square.A1).getClass(), EnPassantMove.class);
        assertEquals(mover.createPromotion(Square.A1, Square.A1, Piece.PAWN).getClass(), PromotionMove.class);
    }

}
