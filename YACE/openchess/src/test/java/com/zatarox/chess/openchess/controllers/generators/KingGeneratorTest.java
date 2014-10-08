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

import com.zatarox.chess.openchess.models.materials.*;
import com.zatarox.chess.openchess.models.moves.Move;
import com.zatarox.chess.openchess.models.moves.MovesFactorySingleton;
import com.zatarox.chess.openchess.models.notations.ForsythEdwardsNotation;
import com.zatarox.chess.openchess.models.notations.Notation;
import java.util.Queue;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class KingGeneratorTest {

    private Generator instance;
    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("2q2r1k/p3b2B/bp2pn1Q/8/3P4/8/PP1B1PPP/6K1 w - - 0 1");
        instance = GeneratorsFactorySingleton.getInstance().from(Piece.KING);
        board = notation.create();
    }

    @Test
    public void attacks() {
        final Queue<Move> attacks = instance.attacks(board, Square.H8);
        assertThat(attacks.size(), is(1));
        assertThat(attacks, hasItems(MovesFactorySingleton.getInstance().createCapture(Square.H8, Square.H7, Piece.BISHOP)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void attackFromEmptySquare() {
        assertTrue(instance.attacks(board, Square.G7).isEmpty());
    }

    @Test
    public void enPrisekh8Bh7() {
        assertTrue(instance.isEnPrise(board, Square.H7));
    }

    @Test
    public void enPrisekh8Qh6() {
        assertFalse(instance.isEnPrise(board, Square.H6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void enPrisekh8g8() {
        assertTrue(instance.isEnPrise(board, Square.G8));
    }

    @Test
    public void fills() {
        final Queue<Move> fills = instance.fills(board, Square.H8);
        assertThat(fills.size(), is(2));
        assertThat(fills, hasItems(
                MovesFactorySingleton.getInstance().createNormal(Square.H8, Square.G8),
                MovesFactorySingleton.getInstance().createNormal(Square.H8, Square.G7)
        ));
    }

}
