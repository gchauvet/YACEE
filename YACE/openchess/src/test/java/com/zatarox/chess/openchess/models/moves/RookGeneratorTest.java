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

public class RookGeneratorTest {

    private Notation notation;

    @Before
    public void setUp() {
        notation = new ForsythEdwardsNotation("r3k2r/pp1p1pbp/2n3p1/8/8/2NR1N2/PPP2PPP/5RK1 w - - 0 1");
    }

    @Test
    public void attacks() {
        final ChessBoard board = notation.create();
        final Generator instance = new RookGenerator();
        final List<Move> attacks = instance.attacks(board, Square.D3);
        assertThat(attacks.size(), is(1));
        assertThat(attacks, hasItems((Move) new CaptureMove(Square.D3, Square.D7, Piece.PAWN)));
    }
    
    @Test
    public void fills() {
        final ChessBoard board = notation.create();
        final Generator instance = new RookGenerator();
        final List<Move> fills = instance.fills(board, Square.D3);
        assertThat(fills.size(), is(6));
        assertThat(fills, hasItems(
            (Move) new BasicMove(Square.D3, Square.D1),
            new BasicMove(Square.D3, Square.D2),
            new BasicMove(Square.D3, Square.E3),
            new BasicMove(Square.D3, Square.D4),
            new BasicMove(Square.D3, Square.D5),
            new BasicMove(Square.D3, Square.D6)
        ));
    }

}
