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

public class PawnGeneratorTest {

    private Generator instance;
    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("8/6bb/8/R7/2pPppk1/4P3/P7/K7 b - d3 0 1");
        instance = GeneratorsFactorySingleton.getInstance().from(Piece.PAWN);
        board = notation.create();
    }

    @Test
    public void enPrisef4() {
        assertTrue(instance.isEnPrise(board, Square.F4));
    }

    @Test
    public void notEnPrisepe4() {
        assertFalse(instance.isEnPrise(board, Square.E4));
    }

    @Test
    public void enPrisePd4() {
        assertTrue(instance.isEnPrise(board, Square.D4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void noPieceEnPrise() {
        assertFalse(instance.isEnPrise(board, Square.E5));
    }

    @Test
    public void fillsPawnD4() {
        final Queue<Move> fills = instance.fills(board, Square.D4);
        assertThat(fills.size(), is(1));
        assertThat(fills, hasItems(MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.D5)));
    }

    @Test
    public void fillsPawnC4() {
        final Queue<Move> fills = instance.fills(board, Square.C4);
        assertThat(fills.size(), is(1));
        assertThat(fills, hasItems(MovesFactorySingleton.getInstance().createNormal(Square.C4, Square.C3)));
    }

    @Test
    public void attacksPawnC4() {
        final Queue<Move> fills = instance.attacks(board, Square.C4);
        fills.addAll(instance.attacks(board, Square.E4));
        assertThat(fills.size(), is(2));
        assertThat(fills, hasItems(
                MovesFactorySingleton.getInstance().createEnpassant(Square.C4, Square.D3),
                MovesFactorySingleton.getInstance().createEnpassant(Square.E4, Square.D3)
        ));
    }

    @Test
    public void fillsPawnA2() {
        final Queue<Move> fills = instance.fills(board, Square.A2);
        assertThat(fills.size(), is(2));
        assertThat(fills, hasItems(
                MovesFactorySingleton.getInstance().createNormal(Square.A2, Square.A3),
                MovesFactorySingleton.getInstance().createCharge(Square.A2, Square.A4)));
    }

}
