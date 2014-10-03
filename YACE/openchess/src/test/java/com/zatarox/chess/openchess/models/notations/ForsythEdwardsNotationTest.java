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
package com.zatarox.chess.openchess.models.notations;

import com.zatarox.chess.openchess.models.materials.BoardSide;
import com.zatarox.chess.openchess.models.materials.ChessBoard;
import com.zatarox.chess.openchess.models.materials.Piece;
import com.zatarox.chess.openchess.models.materials.Square;
import com.zatarox.chess.openchess.models.materials.Square.File;
import com.zatarox.chess.openchess.models.materials.Square.Rank;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ForsythEdwardsNotationTest {

    @Test
    public void createDefaultBoard() {
        ForsythEdwardsNotation instance = new ForsythEdwardsNotation();
        ChessBoard board = instance.create();
        assertNotNull(board);
        // Check chessboard side
        // Whites
        for (File file : File.values()) {
            for (Rank rank : new Rank[]{Rank._1, Rank._2}) {
                assertThat(board.getSide(Square.get(rank, file)), is(BoardSide.WHITE));
            }
        }
        // Blacks
        for (File file : File.values()) {
            for (Rank rank : new Rank[]{Rank._8, Rank._7}) {
                assertThat(board.getSide(Square.get(rank, file)), is(BoardSide.BLACK));
            }
        }

        // Check middle is empty
        for (File file : File.values()) {
            for (Rank rank : new Rank[]{Rank._3, Rank._4, Rank._5, Rank._6}) {
                assertTrue(!board.isOccuped(Square.get(rank, file)));
            }
        }

        // Check pieces
        // Pawns
        for (File file : File.values()) {
            assertThat(board.getPiece(Square.get(Rank._2, file)), is(Piece.PAWN));
            assertThat(board.getPiece(Square.get(Rank._7, file)), is(Piece.PAWN));
        }
        
        // Rooks
        assertThat(board.getPiece(Square.get(Rank._1, File.A)), is(Piece.ROOK));
        assertThat(board.getPiece(Square.get(Rank._8, File.A)), is(Piece.ROOK));
        assertThat(board.getPiece(Square.get(Rank._1, File.A.mirror())), is(Piece.ROOK));
        assertThat(board.getPiece(Square.get(Rank._8, File.A.mirror())), is(Piece.ROOK));
        
        // Knights
        assertThat(board.getPiece(Square.get(Rank._1, File.B)), is(Piece.KNIGHT));
        assertThat(board.getPiece(Square.get(Rank._8, File.B)), is(Piece.KNIGHT));
        assertThat(board.getPiece(Square.get(Rank._1, File.B.mirror())), is(Piece.KNIGHT));
        assertThat(board.getPiece(Square.get(Rank._8, File.B.mirror())), is(Piece.KNIGHT));
        
        // Bishops
        assertThat(board.getPiece(Square.get(Rank._1, File.C)), is(Piece.BISHOP));
        assertThat(board.getPiece(Square.get(Rank._8, File.C)), is(Piece.BISHOP));
        assertThat(board.getPiece(Square.get(Rank._1, File.C.mirror())), is(Piece.BISHOP));
        assertThat(board.getPiece(Square.get(Rank._8, File.C.mirror())), is(Piece.BISHOP));
        
        
        // Asymetric pieces
        // Queen
        assertThat(board.getPiece(Square.get(Rank._1, File.D)), is(Piece.QUEEN));
        assertThat(board.getPiece(Square.get(Rank._8, File.D)), is(Piece.QUEEN));
        
        // King
        assertThat(board.getPiece(Square.get(Rank._1, File.E)), is(Piece.KING));
        assertThat(board.getPiece(Square.get(Rank._8, File.E)), is(Piece.KING));

    }

}
