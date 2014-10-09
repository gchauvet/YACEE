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
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class EnPassantMoveTest extends AbstractMoveTest {

    public EnPassantMoveTest() {
        super("8/6bb/8/R7/2pPppk1/4P3/P7/K7 b - d3 0 1");
    }

    @Test(expected = AssertionError.class)
    public void moveWithNull() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createEnpassant(null, null);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void noPieceToMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createEnpassant(Square.B4, Square.C3);
        move.play(board);
    }

    @Test(expected = AssertionError.class)
    public void noMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createEnpassant(Square.C4, Square.C4);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void playNoPieceToCapture() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createEnpassant(Square.B4, Square.C3);
        move.play(board);
    }

    @Test
    public void playMoveWithMate() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createEnpassant(Square.C4, Square.D3);
        move.play(board);
        assertThat(board.getStone(Square.D3), equalTo(new Stone(Piece.PAWN, BoardSide.BLACK)));
        assertFalse(board.isOccuped(Square.C4));
        assertFalse(board.getSide(BoardSide.WHITE).isEnpassant());
        move.unplay(board);
        assertThat(board.getStone(Square.C4), equalTo(new Stone(Piece.PAWN, BoardSide.BLACK)));
        assertFalse(board.isOccuped(Square.D3));
        assertThat(board.getSide(BoardSide.WHITE).getEnpassant(), is(Square.D3));
    }

    @Test(expected = IllegalMoveException.class)
    public void replayMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createEnpassant(Square.C4, Square.C3);
        move.play(board);
        move.play(board);
    }
}
