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

public class BishopGeneratorTest {

    private Generator instance;
    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("k7/8/P1N5/8/2K1n3/6p1/5bB1/8 w - - 0 1");
        instance = GeneratorsFactorySingleton.getInstance().from(Piece.BISHOP);
        board = notation.create();
    }

    @Test
    public void attacksBishopG2() {
        final Queue<Move> attacks = instance.attacks(board, Square.G2);
        assertThat(attacks.size(), is(1));
        assertThat(attacks, hasItems(MovesFactorySingleton.getInstance().createCapture(Square.G2, Square.E4, Piece.KNIGHT)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void attackFromEmptySquare() {
        assertTrue(instance.attacks(board, Square.F3).isEmpty());
    }

    @Test
    public void enPriseNe4ByBg2() {
        assertTrue(instance.isEnPrise(board, Square.E4));
    }

    @Test
    public void notEnPriseKa8ByBg2() {
        assertFalse(instance.isEnPrise(board, Square.A8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void squareEnPriseb6ByBf2() {
        assertTrue(instance.isEnPrise(board, Square.B6));
    }

    @Test
    public void attacksBishopF2() {
        final Queue<Move> attacks = instance.attacks(board, Square.F2);
        assertTrue(attacks.isEmpty());
    }

    @Test
    public void fillBishopF2() {
        final Queue<Move> fills = instance.fills(board, Square.F2);
        assertThat(fills.size(), is(7));
        assertThat(fills, hasItems(
                MovesFactorySingleton.getInstance().createNormal(Square.F2, Square.G1),
                MovesFactorySingleton.getInstance().createNormal(Square.F2, Square.E1),
                MovesFactorySingleton.getInstance().createNormal(Square.F2, Square.E3),
                MovesFactorySingleton.getInstance().createNormal(Square.F2, Square.D4),
                MovesFactorySingleton.getInstance().createNormal(Square.F2, Square.C5),
                MovesFactorySingleton.getInstance().createNormal(Square.F2, Square.B6),
                MovesFactorySingleton.getInstance().createNormal(Square.F2, Square.A7)
        ));
    }

    @Test
    public void fillBishopG2() {
        final Queue<Move> fills = instance.fills(board, Square.G2);
        assertThat(fills.size(), is(4));
        assertThat(fills, hasItems(
                MovesFactorySingleton.getInstance().createNormal(Square.G2, Square.H1),
                MovesFactorySingleton.getInstance().createNormal(Square.G2, Square.F1),
                MovesFactorySingleton.getInstance().createNormal(Square.G2, Square.F3),
                MovesFactorySingleton.getInstance().createNormal(Square.G2, Square.H3)
        ));
    }

}
