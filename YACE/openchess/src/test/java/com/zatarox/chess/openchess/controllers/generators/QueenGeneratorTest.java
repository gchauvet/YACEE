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

public class QueenGeneratorTest {

    private Generator instance;
    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("1k4q1/1p2Rprp/p1p5/2Pp4/1P1Q3P/6P1/P5K1/8 w - - 0 1");
        instance = GeneratorsFactorySingleton.getInstance().from(Piece.QUEEN);
        board = notation.create();
    }

    @Test
    public void attacksQueenD4() {
        final Queue<Move> attacks = instance.attacks(board, Square.D4);
        assertThat(attacks.size(), is(2));
        assertThat(attacks, hasItems(
                MovesFactorySingleton.getInstance().createCapture(Square.D4, Square.G7, Piece.ROOK),
                MovesFactorySingleton.getInstance().createCapture(Square.D4, Square.D5, Piece.PAWN)
        ));
    }

    @Test
    public void enPriseQd4Rg7() {
        assertTrue(instance.isEnPrise(board, Square.G7));
    }
    
    @Test
    public void enPriseQd4pf7() {
        assertFalse(instance.isEnPrise(board, Square.F7));
    }

    @Test
    public void attacksQueenG8() {
        final Queue<Move> attacks = instance.attacks(board, Square.G8);
        assertTrue(attacks.isEmpty());
    }

    @Test
    public void fillQueenD4() {
        final Queue<Move> fills = instance.fills(board, Square.D4);
        assertThat(fills.size(), is(15));
        assertThat(fills, hasItems(
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.C4),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.E4),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.F4),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.G4),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.D3),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.D2),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.D1),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.C3),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.B2),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.A1),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.E3),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.F2),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.G1),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.E5),
                MovesFactorySingleton.getInstance().createNormal(Square.D4, Square.F6)
        ));
    }

    @Test
    public void fillQueenG8() {
        final Queue<Move> fills = instance.fills(board, Square.G8);
        assertThat(fills.size(), is(5));
        assertThat(fills, hasItems(
                MovesFactorySingleton.getInstance().createNormal(Square.G8, Square.H8),
                MovesFactorySingleton.getInstance().createNormal(Square.G8, Square.C8),
                MovesFactorySingleton.getInstance().createNormal(Square.G8, Square.D8),
                MovesFactorySingleton.getInstance().createNormal(Square.G8, Square.E8),
                MovesFactorySingleton.getInstance().createNormal(Square.G8, Square.F8)
        ));
    }

}
