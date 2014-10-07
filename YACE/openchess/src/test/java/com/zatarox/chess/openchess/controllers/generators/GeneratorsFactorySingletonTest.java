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
package com.zatarox.chess.openchess.controllers.generators;

import com.zatarox.chess.openchess.models.materials.Piece;
import org.junit.Test;
import static org.junit.Assert.*;

public final class GeneratorsFactorySingletonTest {

    @Test
    public void testBuild() {
        final GeneratorsFactorySingleton generator = GeneratorsFactorySingleton.getInstance();
        assertEquals(generator.from(Piece.PAWN).getClass(), PawnGenerator.class);
        assertEquals(generator.from(Piece.KNIGHT).getClass(), KnightGenerator.class);
        assertEquals(generator.from(Piece.BISHOP).getClass(), BishopGenerator.class);
        assertEquals(generator.from(Piece.ROOK).getClass(), RookGenerator.class);
        assertEquals(generator.from(Piece.QUEEN).getClass(), QueenGenerator.class);
        assertEquals(generator.from(Piece.KING).getClass(), KingGenerator.class);
    }

}
