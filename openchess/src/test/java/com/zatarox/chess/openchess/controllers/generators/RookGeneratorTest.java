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

public class RookGeneratorTest {

    private Generator instance;
    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("r3k2r/pp1p1pbp/2n3p1/8/8/2NR1N2/PPP2PPP/5RK1 w - - 0 1");
        instance = GeneratorsFactorySingleton.getInstance().from(Piece.ROOK);
        board = notation.create();
    }

    @Test
    public void attacks() {
        final Queue<Move> attacks = instance.attacks(board, Square.D3);
        assertThat(attacks.size(), is(1));
        assertThat(attacks, hasItems(MovesFactorySingleton.getInstance().createCapture(Square.D3, Square.D7, new Stone(Piece.PAWN, BoardSide.BLACK))));
    }

    @Test
    public void enPriseRd3pd7() {
        assertTrue(instance.isEnPrise(board, Square.D7));
    }

    @Test
    public void enPriseRd3nc6() {
        assertFalse(instance.isEnPrise(board, Square.C6));
    }
    
    @Test
    public void enPriseRd3Nf3() {
        assertFalse(instance.isEnPrise(board, Square.F3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void notEnPriseRd3d5() {
        assertTrue(instance.isEnPrise(board, Square.D5));
    }

    @Test
    public void fills() {
        final Queue<Move> fills = instance.fills(board, Square.D3);
        assertThat(fills.size(), is(6));
        assertThat(fills, hasItems(
                MovesFactorySingleton.getInstance().createNormal(Square.D3, Square.D1),
                MovesFactorySingleton.getInstance().createNormal(Square.D3, Square.D2),
                MovesFactorySingleton.getInstance().createNormal(Square.D3, Square.E3),
                MovesFactorySingleton.getInstance().createNormal(Square.D3, Square.D4),
                MovesFactorySingleton.getInstance().createNormal(Square.D3, Square.D5),
                MovesFactorySingleton.getInstance().createNormal(Square.D3, Square.D6)
        ));
    }

}
