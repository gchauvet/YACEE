/*
 * Copyright 2014 Romain.
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

import com.zatarox.chess.openchess.models.moves.IllegalMoveException;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public final class ChessBoardTest {

    @Test
    public void testClear() {
        ChessBoard instance = new ChessBoard();
        instance.clear();
        int count = 0;
        for (BoardSide trait : BoardSide.values()) {
            for (Piece piece : Piece.values()) {
                count += instance.get(trait, piece).getSize();
            }
        }
        assertThat(count, is(0));
    }

    @Test
    public void testSetPiece() throws IllegalMoveException {
        BoardSide color = BoardSide.WHITE;
        Piece piece = Piece.ROOK;
        Square square = Square.C7;
        ChessBoard instance = new ChessBoard();
        instance.setPiece(color, piece, square);
        assertTrue(instance.get(color, piece).isOccuped(square));
        assertThat(instance.getSide(square), is(BoardSide.WHITE));
    }

    @Test
    public void testUnsetPiece() throws IllegalMoveException {
        Square square = Square.H2;
        ChessBoard instance = new ChessBoard();
        instance.setPiece(BoardSide.WHITE, Piece.PAWN, square);
        assertTrue(instance.isOccuped(square));
        instance.unsetPiece(square);
        assertFalse(instance.isOccuped(square));
    }

    @Test
    public void testTurn() {
        ChessBoard instance = new ChessBoard();
        BoardSide expResult = BoardSide.BLACK;
        instance.setTurn(expResult);
        assertEquals(expResult, instance.getTurn());
        expResult = expResult.flip();
        instance.setTurn(expResult);
        assertEquals(expResult, instance.getTurn());
    }
    
    @Test
    public void testSnapshot() throws IllegalMoveException {
        final ChessBoard instance = new ChessBoard();
        instance.setPiece(BoardSide.WHITE, Piece.PAWN, Square.A1);
        instance.setPiece(BoardSide.WHITE, Piece.KNIGHT, Square.A2);
        instance.setPiece(BoardSide.BLACK, Piece.ROOK, Square.H4);
        instance.setPiece(BoardSide.BLACK, Piece.QUEEN, Square.C7);
        instance.setPiece(BoardSide.BLACK, Piece.BISHOP, Square.G1);
        final BitBoard white = instance.getSnapshot(BoardSide.WHITE);
        assertThat(white.getSize(), is(2));
        assertTrue(white.isOccuped(Square.A1));
        assertTrue(white.isOccuped(Square.A2));
        final BitBoard black = instance.getSnapshot(BoardSide.BLACK);
        assertThat(black.getSize(), is(3));
        assertTrue(black.isOccuped(Square.H4));
        assertTrue(black.isOccuped(Square.C7));
        assertTrue(black.isOccuped(Square.G1));
    }
    
    @Test
    public void testSnapshotForPieces() throws IllegalMoveException {
        final ChessBoard instance = new ChessBoard();
        instance.setPiece(BoardSide.WHITE, Piece.PAWN, Square.A1);
        instance.setPiece(BoardSide.BLACK, Piece.PAWN, Square.H4);
        instance.setPiece(BoardSide.WHITE, Piece.ROOK, Square.A2);
        instance.setPiece(BoardSide.BLACK, Piece.ROOK, Square.C7);
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
