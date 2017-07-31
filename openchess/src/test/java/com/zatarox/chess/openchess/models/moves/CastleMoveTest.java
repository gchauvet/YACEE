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
import static org.junit.matchers.JUnitMatchers.hasItems;

public class CastleMoveTest extends AbstractMoveTest {

    public CastleMoveTest() {
        super("r3k2r/ppp1bppp/2np1q1n/1B2pb2/4P3/2NPBN2/PPPQ1PPP/R3K2R b KQkq - 0 1");
    }

    @Test(expected = AssertionError.class)
    public void castWithNull() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCastle(null, BoardSide.WHITE);
        move.play(board);
    }

    @Test
    public void whiteShortCastle() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCastle(Castle.SHORT, BoardSide.WHITE);
        move.play(board);
        assertTrue(board.getSide(BoardSide.WHITE).getCastles().isEmpty());
        assertThat(board.getStone(Square.G1), equalTo(new Stone(Piece.KING, BoardSide.WHITE)));
        assertThat(board.getStone(Square.F1), equalTo(new Stone(Piece.ROOK, BoardSide.WHITE)));
        assertFalse(board.isOccuped(Square.E1));
        assertFalse(board.isOccuped(Square.H1));
        move.unplay(board);
        assertThat(board.getSide(BoardSide.BLACK).getCastles(), hasItems(Castle.LONG, Castle.SHORT));
        assertFalse(board.isOccuped(Square.G1));
        assertFalse(board.isOccuped(Square.F1));
        assertThat(board.getStone(Square.E1), equalTo(new Stone(Piece.KING, BoardSide.WHITE)));
        assertThat(board.getStone(Square.H1), equalTo(new Stone(Piece.ROOK, BoardSide.WHITE)));
    }

    @Test
    public void whiteLongCastle() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCastle(Castle.LONG, BoardSide.WHITE);
        move.play(board);
        assertTrue(board.getSide(BoardSide.WHITE).getCastles().isEmpty());
        assertThat(board.getStone(Square.C1), equalTo(new Stone(Piece.KING, BoardSide.WHITE)));
        assertThat(board.getStone(Square.D1), equalTo(new Stone(Piece.ROOK, BoardSide.WHITE)));
        assertFalse(board.isOccuped(Square.E1));
        assertFalse(board.isOccuped(Square.A1));
        move.unplay(board);
        assertThat(board.getSide(BoardSide.BLACK).getCastles(), hasItems(Castle.LONG, Castle.SHORT));
        assertFalse(board.isOccuped(Square.C1));
        assertFalse(board.isOccuped(Square.D1));
        assertThat(board.getStone(Square.E1), equalTo(new Stone(Piece.KING, BoardSide.WHITE)));
        assertThat(board.getStone(Square.A1), equalTo(new Stone(Piece.ROOK, BoardSide.WHITE)));
    }

    @Test
    public void blackShortCastle() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCastle(Castle.SHORT, BoardSide.BLACK);
        move.play(board);
        assertTrue(board.getSide(BoardSide.BLACK).getCastles().isEmpty());
        assertThat(board.getStone(Square.G8), equalTo(new Stone(Piece.KING, BoardSide.BLACK)));
        assertThat(board.getStone(Square.F8), equalTo(new Stone(Piece.ROOK, BoardSide.BLACK)));
        assertFalse(board.isOccuped(Square.E8));
        assertFalse(board.isOccuped(Square.H8));
        move.unplay(board);
        assertThat(board.getSide(BoardSide.BLACK).getCastles(), hasItems(Castle.LONG, Castle.SHORT));
        assertFalse(board.isOccuped(Square.G8));
        assertFalse(board.isOccuped(Square.F8));
        assertThat(board.getStone(Square.E8), equalTo(new Stone(Piece.KING, BoardSide.BLACK)));
        assertThat(board.getStone(Square.H8), equalTo(new Stone(Piece.ROOK, BoardSide.BLACK)));
    }

    @Test
    public void blackLongCastle() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCastle(Castle.LONG, BoardSide.BLACK);
        move.play(board);
        assertTrue(board.getSide(BoardSide.BLACK).getCastles().isEmpty());
        assertThat(board.getStone(Square.C8), equalTo(new Stone(Piece.KING, BoardSide.BLACK)));
        assertThat(board.getStone(Square.D8), equalTo(new Stone(Piece.ROOK, BoardSide.BLACK)));
        assertFalse(board.isOccuped(Square.E8));
        assertFalse(board.isOccuped(Square.A8));
        move.unplay(board);
        assertThat(board.getSide(BoardSide.BLACK).getCastles(), hasItems(Castle.LONG, Castle.SHORT));
        assertFalse(board.isOccuped(Square.C8));
        assertFalse(board.isOccuped(Square.D8));
        assertThat(board.getStone(Square.E8), equalTo(new Stone(Piece.KING, BoardSide.BLACK)));
        assertThat(board.getStone(Square.A8), equalTo(new Stone(Piece.ROOK, BoardSide.BLACK)));
    }

    @Test(expected = IllegalMoveException.class)
    public void replayMove() throws IllegalMoveException, SelfMateMoveException {
        final Move move = MovesFactorySingleton.getInstance().createCastle(Castle.LONG, BoardSide.BLACK);
        move.play(board);
        move.play(board);
    }
}
