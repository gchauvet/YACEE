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

public class CaptureMoveTest {

    private ChessBoard board;

    @Before
    public void setUp() {
        final Notation notation = new ForsythEdwardsNotation("4k3/1p4pp/2p5/8/q3r2Q/3p3P/1P4PK/4R3 b - - 0 1");
        board = notation.create();
    }

    @Test(expected = AssertionError.class)
    public void moveWithNull() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.A1, Square.A2, null);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void noPieceToMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.A2, Square.B3, Piece.PAWN);
        move.play(board);
    }

    @Test(expected = AssertionError.class)
    public void noMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createNormal(Square.H7, Square.H7);
        move.play(board);
    }

    @Test(expected = SelfMateMoveException.class)
    public void playPinnedRookE4() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.E4, Square.H4, Piece.QUEEN);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void playUnvalidCaptureRookE4() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.E4, Square.A4, Piece.QUEEN);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void playNoCaptureQueenH4() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.H4, Square.G4, Piece.PAWN);
        move.play(board);
    }

    @Test(expected = IllegalMoveException.class)
    public void playNoPieceCapturer() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCapture(Square.B4, Square.B2, Piece.PAWN);
        move.play(board);
    }
}
