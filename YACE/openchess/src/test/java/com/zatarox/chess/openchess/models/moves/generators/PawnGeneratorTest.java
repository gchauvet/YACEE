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
package com.zatarox.chess.openchess.models.moves.generators;

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
import org.junit.Ignore;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class PawnGeneratorTest {

    private Notation notation;
    private Generator instance;

    @Before
    public void setUp() {
        notation = new ForsythEdwardsNotation("8/6bb/8/8/R1pP2k1/4P3/P7/K7 b - d3 0 1");
        instance = new PawnGenerator();
    }

    @Test
    public void fillsPawnE4() {
        final ChessBoard board = notation.create();
        final Queue<Move> fills = instance.fills(board, Square.E3);
        assertThat(fills.size(), is(1));
        assertThat(fills, hasItems(MovesFactorySingleton.getInstance().createNormal(Square.E3, Square.E4)));
    }

    @Test
    @Ignore
    public void fillsPawnC4() {
        final ChessBoard board = notation.create();
        final Queue<Move> fills = instance.fills(board, Square.C4);
        assertThat(fills.size(), is(2));
        assertThat(fills, hasItems(MovesFactorySingleton.getInstance().createNormal(Square.C4, Square.C3),
                MovesFactorySingleton.getInstance().createEnpassant(Square.C4, board.getSide(BoardSide.BLACK).getEnpassant())
        ));
    }

}
