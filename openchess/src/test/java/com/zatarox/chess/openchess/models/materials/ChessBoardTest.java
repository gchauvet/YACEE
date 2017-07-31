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
package com.zatarox.chess.openchess.models.materials;

import com.zatarox.chess.openchess.models.moves.exceptions.IllegalMoveException;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;

public final class ChessBoardTest {

    private ChessBoard instance;
    
    @Before
    public void setUp() {
        instance = new ChessBoard();
    }
    
    @Test
    public void testClear() {
        int count = 0;
        for (BoardSide trait : BoardSide.values()) {
            for (Piece piece : Piece.values()) {
                count += instance.getSide(trait).get(piece).getSize();
            }
        }
        assertThat(count, is(0));
        assertThat(instance.getHashing().hashCode64(), is(0L));
    }

    @Test
    public void testSetPiece() throws IllegalMoveException {
        BoardSide color = BoardSide.WHITE;
        Piece piece = Piece.ROOK;
        Square square = Square.C7;
        assertThat(instance.getHashing().hashCode64(), is(0L));
        instance.setPiece(square, new Stone(piece, color));
        assertThat(instance.getHashing().hashCode64(), is(not(0L)));
        assertTrue(instance.getSide(color).get(piece).isOccuped(square));
        assertThat(instance.getStone(square).getSide(), is(BoardSide.WHITE));
    }

    @Test
    public void testUnsetPiece() throws IllegalMoveException {
        Square square = Square.H2;
        instance.setPiece(square, new Stone(Piece.PAWN, BoardSide.WHITE));
        assertTrue(instance.isOccuped(square));
        assertThat(instance.getHashing().hashCode64(), is(not(0L)));
        instance.unsetPiece(square);
        assertFalse(instance.isOccuped(square));
        assertThat(instance.getHashing().hashCode64(), is(0L));
    }

    @Test
    public void testTurn() {
        BoardSide expResult = BoardSide.BLACK;
        instance.setTurn(expResult);
        assertEquals(expResult, instance.getTurn());
        expResult = expResult.flip();
        instance.setTurn(expResult);
        assertEquals(expResult, instance.getTurn());
    }
    
    @Test
    public void testSnapshot() throws IllegalMoveException {
        instance.setPiece(Square.A1, new Stone(Piece.PAWN, BoardSide.WHITE));
        instance.setPiece(Square.A2, new Stone(Piece.KNIGHT, BoardSide.WHITE));
        instance.setPiece(Square.H4, new Stone(Piece.ROOK, BoardSide.BLACK));
        instance.setPiece(Square.C7, new Stone(Piece.QUEEN, BoardSide.BLACK));
        instance.setPiece(Square.G1, new Stone(Piece.BISHOP, BoardSide.BLACK));
        final BitBoard white = instance.getSide(BoardSide.WHITE).getSnapshot();
        assertThat(white.getSize(), is(2));
        assertTrue(white.isOccuped(Square.A1));
        assertTrue(white.isOccuped(Square.A2));
        final BitBoard black = instance.getSide(BoardSide.BLACK).getSnapshot();
        assertThat(black.getSize(), is(3));
        assertTrue(black.isOccuped(Square.H4));
        assertTrue(black.isOccuped(Square.C7));
        assertTrue(black.isOccuped(Square.G1));
    }
    
    @Test
    public void testSnapshotForPieces() throws IllegalMoveException {
        instance.setPiece(Square.A1, new Stone(Piece.PAWN, BoardSide.WHITE));
        instance.setPiece(Square.H4, new Stone(Piece.PAWN, BoardSide.BLACK));
        instance.setPiece(Square.A2, new Stone(Piece.ROOK, BoardSide.WHITE));
        instance.setPiece(Square.C7, new Stone(Piece.ROOK, BoardSide.BLACK));
        final BitBoard pawns = instance.getSnapshot(Piece.PAWN);
        assertThat(pawns.getSize(), is(2));
        assertTrue(pawns.isOccuped(Square.A1));
        assertTrue(pawns.isOccuped(Square.H4));
        final BitBoard rooks = instance.getSnapshot(Piece.ROOK);
        assertThat(rooks.getSize(), is(2));
        assertTrue(rooks.isOccuped(Square.A2));
        assertTrue(rooks.isOccuped(Square.C7));
    }

}
