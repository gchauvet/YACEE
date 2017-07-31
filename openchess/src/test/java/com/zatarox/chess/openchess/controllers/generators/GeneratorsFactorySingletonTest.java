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
import org.junit.Before;

public final class GeneratorsFactorySingletonTest {

    private GeneratorsFactorySingleton generator;

    @Before
    public void setUp() {
        generator = GeneratorsFactorySingleton.getInstance();
    }

    @Test
    public void createPawnGenerator() {
        assertEquals(generator.from(Piece.PAWN).getClass(), PawnGenerator.class);
    }

    @Test
    public void createKnigthGenerator() {
        assertEquals(generator.from(Piece.KNIGHT).getClass(), KnightGenerator.class);
    }

    @Test
    public void createBishopGenerator() {
        assertEquals(generator.from(Piece.BISHOP).getClass(), BishopGenerator.class);
    }

    @Test
    public void createRookGenerator() {
        assertEquals(generator.from(Piece.ROOK).getClass(), RookGenerator.class);
    }

    @Test
    public void createQueenGenerator() {
        assertEquals(generator.from(Piece.QUEEN).getClass(), QueenGenerator.class);
    }

    @Test
    public void createKingGenerator() {
        assertEquals(generator.from(Piece.KING).getClass(), KingGenerator.class);
    }

}
