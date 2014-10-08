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
package com.zatarox.chess.openchess.models.moves;

import com.zatarox.chess.openchess.models.materials.*;
import com.zatarox.chess.openchess.models.moves.exceptions.IllegalMoveException;
import com.zatarox.chess.openchess.models.moves.exceptions.SelfMateMoveException;
import com.zatarox.chess.openchess.models.notations.ForsythEdwardsNotation;
import com.zatarox.chess.openchess.models.notations.Notation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CastleMoveTest {

    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 0 1");
        board = notation.create();
    }

    @Test(expected = AssertionError.class)
    public void castWithNull() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCastle(null, BoardSide.WHITE);
        move.play(board);
    }
    
    @Test(expected = AssertionError.class)
    public void playCastle() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCastle(Castle.SHORT, BoardSide.WHITE);
        move.play(board);
        assertThat(board.getStone(Square.G1), equalTo(new Stone(Piece.KING, BoardSide.WHITE)));
        assertThat(board.getStone(Square.F1), equalTo(new Stone(Piece.ROOK, BoardSide.WHITE)));
        assertFalse(board.isOccuped(Square.E1));
        assertFalse(board.isOccuped(Square.H1));
        move.unplay(board);
        assertFalse(board.isOccuped(Square.G1));
        assertFalse(board.isOccuped(Square.F1));
        assertThat(board.getStone(Square.E1), equalTo(new Stone(Piece.KING, BoardSide.WHITE)));
        assertThat(board.getStone(Square.H1), equalTo(new Stone(Piece.ROOK, BoardSide.WHITE)));
    }
}
