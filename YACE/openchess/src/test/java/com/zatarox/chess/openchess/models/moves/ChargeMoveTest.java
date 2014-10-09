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

public class ChargeMoveTest {

    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("rnbqk1nr/pppp1ppp/8/4p3/1bB1P3/8/PPPP1PPP/RNBQK1NR w KQkq - 0 1");
        board = notation.create();
    }

    @Test(expected = AssertionError.class)
    public void moveWithNull() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCharge(null, null);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void noPieceToMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCharge(Square.E2, Square.E4);
        move.play(board);
    }

    @Test(expected = AssertionError.class)
    public void noMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCharge(Square.D2, Square.D2);
        move.play(board);
    }

    @Test(expected = SelfMateMoveException.class)
    public void pinnedPiece() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCharge(Square.D2, Square.D4);
        move.play(board);
    }

    @Test
    public void playCharge() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCharge(Square.F2, Square.F4);
        move.play(board);
        assertThat(board.getStone(Square.F4), equalTo(new Stone(Piece.PAWN, BoardSide.WHITE)));
        assertFalse(board.isOccuped(Square.F2));
        assertThat(board.getSide(BoardSide.WHITE).getEnpassant(), is(Square.F3));
        move.unplay(board);
        assertThat(board.getStone(Square.F2), equalTo(new Stone(Piece.PAWN, BoardSide.WHITE)));
        assertFalse(board.isOccuped(Square.F4));
        assertNull(board.getSide(BoardSide.WHITE).getEnpassant());
    }

    @Test(expected = IllegalMoveException.class)
    public void replayMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCharge(Square.A2, Square.A4);
        move.play(board);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void unplayedNotPlayedMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCharge(Square.A2, Square.A4);
        move.unplay(board);
    }

}
