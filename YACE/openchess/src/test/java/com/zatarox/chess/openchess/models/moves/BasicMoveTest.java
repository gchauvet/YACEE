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

public class BasicMoveTest {

    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("k1r5/8/P1N5/8/2K1n3/6p1/5bB1/8 w - - 0 1");
        board = notation.create();
    }

    @Test(expected = AssertionError.class)
    public void moveWithNull() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(null, null);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void noPieceToMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(Square.H7, Square.H4);
        move.play(board);
    }

    @Test(expected = AssertionError.class)
    public void noMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(Square.H7, Square.H7);
        move.play(board);
    }

    @Test(expected = SelfMateMoveException.class)
    public void pinnedPiece() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(Square.C6, Square.E5);
        move.play(board);
    }

    @Test
    public void playBishop() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(Square.F2, Square.A7);
        move.play(board);
        assertThat(board.getStone(Square.A7), equalTo(new Stone(Piece.BISHOP, BoardSide.BLACK)));
        assertFalse(board.isOccuped(Square.F2));
        move.unplay(board);
        assertTrue(board.isOccuped(Square.F2));
        assertThat(board.getStone(Square.F2), equalTo(new Stone(Piece.BISHOP, BoardSide.BLACK)));
    }

    @Test(expected = SelfMateMoveException.class)
    public void playIllegalKing() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(Square.C4, Square.C5);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void replayMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(Square.C4, Square.B3);
        move.play(board);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void unplayedNotPlayedMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(Square.C4, Square.C5);
        move.unplay(board);
    }

}
