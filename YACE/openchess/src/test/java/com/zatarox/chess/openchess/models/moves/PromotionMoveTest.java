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

public class PromotionMoveTest {

    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("3n2nr/P3Pqpp/2k5/8/8/8/2B3PP/6K1 w - - 0 74");
        board = notation.create();
    }

    @Test(expected = AssertionError.class)
    public void promotionWithNull() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createPromotion(null, null, null);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void noPieceToMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createPromotion(Square.B7, Square.B8, Piece.QUEEN);
        move.play(board);
    }

    @Test(expected = AssertionError.class)
    public void noMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createPromotion(Square.B7, Square.B7, Piece.KNIGHT);
        move.play(board);
    }

    @Test
    public void playPromoteWithMate() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createPromotion(Square.E7, Square.D8, Piece.KNIGHT);
        move.play(board);
        assertThat(board.getStone(Square.D8), equalTo(new Stone(Piece.KNIGHT, BoardSide.WHITE)));
        assertFalse(board.isOccuped(Square.E7));
        move.unplay(board);
        assertThat(board.getStone(Square.D8), equalTo(new Stone(Piece.KNIGHT, BoardSide.BLACK)));
        assertThat(board.getStone(Square.E7), equalTo(new Stone(Piece.PAWN, BoardSide.WHITE)));
    }

    @Test
    public void playPromote() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createPromotion(Square.A7, Square.A8, Piece.QUEEN);
        move.play(board);
        assertThat(board.getStone(Square.A8), equalTo(new Stone(Piece.QUEEN, BoardSide.WHITE)));
        assertFalse(board.isOccuped(Square.A7));
        move.unplay(board);
        assertThat(board.getStone(Square.A7), equalTo(new Stone(Piece.PAWN, BoardSide.WHITE)));
        assertFalse(board.isOccuped(Square.A8));
    }

    @Test(expected = IllegalMoveException.class)
    public void replayMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.E7, Square.D8, new Stone(Piece.KNIGHT, BoardSide.WHITE));
        move.play(board);
        move.play(board);
    }
}
