/*
 * Copyright 2014 Guillaume.
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
import com.zatarox.chess.openchess.models.notations.ForsythEdwardsNotation;
import com.zatarox.chess.openchess.models.notations.Notation;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class QueenGeneratorTest {

    private Notation notation;
    private Generator instance;

    @Before
    public void setUp() {
        notation = new ForsythEdwardsNotation("1k4q1/1p2Rprp/p1p5/2Pp4/1P1Q3P/6P1/P5K1/8 w - - 0 1");
        instance = new QueenGenerator();
    }

    @Test
    public void attacksQueenD4() {
        final ChessBoard board = notation.create();
        final List<Move> attacks = instance.attacks(board, Square.D4);
        assertThat(attacks.size(), is(2));
        assertThat(attacks, hasItems(
            (Move) new CaptureMove(Square.D4, Square.G7, Piece.ROOK),
            new CaptureMove(Square.D4, Square.D5, Piece.PAWN)
        ));
    }

    @Test
    public void attacksQueenG8() {
        final ChessBoard board = notation.create();
        final List<Move> attacks = instance.attacks(board, Square.G8);
        assertTrue(attacks.isEmpty());
    }

    @Test
    public void fillQueenD4() {
        final ChessBoard board = notation.create();
        final List<Move> fills = instance.fills(board, Square.D4);
        assertThat(fills.size(), is(15));
        assertThat(fills, hasItems(
            (Move) new BasicMove(Square.D4, Square.C4),
            new BasicMove(Square.D4, Square.E4),
            new BasicMove(Square.D4, Square.F4),
            new BasicMove(Square.D4, Square.G4),
            new BasicMove(Square.D4, Square.D3),
            new BasicMove(Square.D4, Square.D2),
            new BasicMove(Square.D4, Square.D1), 
            new BasicMove(Square.D4, Square.C3),
            new BasicMove(Square.D4, Square.B2),
            new BasicMove(Square.D4, Square.A1),
            new BasicMove(Square.D4, Square.E3),
            new BasicMove(Square.D4, Square.F2),
            new BasicMove(Square.D4, Square.G1),
            new BasicMove(Square.D4, Square.E5),
            new BasicMove(Square.D4, Square.F6)
        ));
    }

    @Test
    public void fillQueenG8() {
        final ChessBoard board = notation.create();
        final List<Move> fills = instance.fills(board, Square.G8);
        assertThat(fills.size(), is(5));
        assertThat(fills, hasItems(
            (Move) new BasicMove(Square.G8, Square.H8),
            new BasicMove(Square.G8, Square.C8),
            new BasicMove(Square.G8, Square.D8),
            new BasicMove(Square.G8, Square.E8),
            new BasicMove(Square.G8, Square.F8)
        ));
    }

}
