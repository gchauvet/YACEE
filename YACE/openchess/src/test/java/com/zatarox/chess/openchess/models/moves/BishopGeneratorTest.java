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

public class BishopGeneratorTest {

    private Notation notation;

    @Before
    public void setUp() {
        notation = new ForsythEdwardsNotation("k7/8/P1N5/8/2K1n3/6p1/5bB1/8 w - - 0 1");
    }

    @Test
    public void attacksBishopG2() {
        final ChessBoard board = notation.create();
        final Generator instance = new BishopGenerator();
        final List<Move> attacks = instance.attacks(board, Square.G2);
        assertThat(attacks.size(), is(1));
        assertThat(attacks, hasItems((Move) new CaptureMove(Square.G2, Square.E4, Piece.KNIGHT)));
    }

    @Test
    public void attacksBishopF2() {
        final ChessBoard board = notation.create();
        final Generator instance = new BishopGenerator();
        final List<Move> attacks = instance.attacks(board, Square.F2);
        assertTrue(attacks.isEmpty());
    }

    @Test
    public void fillBishopF2() {
        final ChessBoard board = notation.create();
        final Generator instance = new BishopGenerator();
        final List<Move> fills = instance.fills(board, Square.F2);
        assertThat(fills.size(), is(7));
        assertThat(fills, hasItems(
                (Move) new BasicMove(Square.F2, Square.G1),
                new BasicMove(Square.F2, Square.E1),
                new BasicMove(Square.F2, Square.E3),
                new BasicMove(Square.F2, Square.D4),
                new BasicMove(Square.F2, Square.C5),
                new BasicMove(Square.F2, Square.B6),
                new BasicMove(Square.F2, Square.A7)
        ));
    }

    @Test
    public void fillBishopG2() {
        final ChessBoard board = notation.create();
        final Generator instance = new BishopGenerator();
        final List<Move> fills = instance.fills(board, Square.G2);
        assertThat(fills.size(), is(4));
        assertThat(fills, hasItems(
                (Move) new BasicMove(Square.G2, Square.H1),
                new BasicMove(Square.G2, Square.F1),
                new BasicMove(Square.G2, Square.F3),
                new BasicMove(Square.G2, Square.H3)
        ));
    }

}
