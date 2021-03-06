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

public class CaptureMoveTest extends AbstractMoveTest {

    public CaptureMoveTest() {
        super("4k3/1p4pp/2p5/8/q3r2Q/3p3P/1P4PK/4R3 b - - 0 1");
    }

    @Test(expected = AssertionError.class)
    public void moveWithNull() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.A1, Square.A2, null);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void noPieceToMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.A2, Square.B3, new Stone(Piece.PAWN, BoardSide.WHITE));
        move.play(board);
    }

    @Test(expected = AssertionError.class)
    public void noMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(Square.H7, Square.H7);
        move.play(board);
    }

    @Test(expected = SelfMateMoveException.class)
    public void playPinnedRookE4() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.E4, Square.H4, new Stone(Piece.QUEEN, BoardSide.WHITE));
        move.play(board);
    }

    @Test
    public void unplayPinnedRookE4() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.E4, Square.H4, new Stone(Piece.QUEEN, BoardSide.WHITE));
        try {
            move.play(board);
        } catch (SelfMateMoveException ex) {
            move.unplay(board);
        }
        assertThat(board.getStone(Square.E4), equalTo(new Stone(Piece.ROOK, BoardSide.BLACK)));
        assertThat(board.getStone(Square.H4), equalTo(new Stone(Piece.QUEEN, BoardSide.WHITE)));
    }

    @Test(expected = IllegalMoveException.class)
    public void playUnvalidCaptureRookE4() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.E4, Square.A4, new Stone(Piece.QUEEN, BoardSide.WHITE));
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void playNoCaptureQueenH4() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.H4, Square.G4, new Stone(Piece.PAWN, BoardSide.WHITE));
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void playNoPieceToCapture() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.B4, Square.B2, new Stone(Piece.PAWN, BoardSide.WHITE));
        move.play(board);
    }

    @Test
    public void playMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.H4, Square.H7, new Stone(Piece.PAWN, BoardSide.WHITE));
        move.play(board);
    }

    @Test
    public void playMoveWithMate() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.H4, Square.E4, new Stone(Piece.ROOK, BoardSide.WHITE));
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void replayMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.H4, Square.H7, new Stone(Piece.PAWN, BoardSide.WHITE));
        move.play(board);
        move.play(board);
    }
}
