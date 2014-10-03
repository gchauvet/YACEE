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

import com.zatarox.chess.openchess.models.materials.*;
import com.zatarox.chess.openchess.models.materials.Square.File;
import com.zatarox.chess.openchess.models.materials.Square.Rank;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.hasItems;
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
                assertThat(board.getStone(Square.get(rank, file)).getSide(), is(BoardSide.WHITE));
            }
        }
        // Blacks
        for (File file : File.values()) {
            for (Rank rank : new Rank[]{Rank._8, Rank._7}) {
                assertThat(board.getStone(Square.get(rank, file)).getSide(), is(BoardSide.BLACK));
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
            assertThat(board.getStone(Square.get(Rank._2, file)).getPiece(), is(Piece.PAWN));
            assertThat(board.getStone(Square.get(Rank._7, file)).getPiece(), is(Piece.PAWN));
        }
        
        // Rooks
        assertThat(board.getStone(Square.get(Rank._1, File.A)).getPiece(), is(Piece.ROOK));
        assertThat(board.getStone(Square.get(Rank._8, File.A)).getPiece(), is(Piece.ROOK));
        assertThat(board.getStone(Square.get(Rank._1, File.A.mirror())).getPiece(), is(Piece.ROOK));
        assertThat(board.getStone(Square.get(Rank._8, File.A.mirror())).getPiece(), is(Piece.ROOK));
        
        // Knights
        assertThat(board.getStone(Square.get(Rank._1, File.B)).getPiece(), is(Piece.KNIGHT));
        assertThat(board.getStone(Square.get(Rank._8, File.B)).getPiece(), is(Piece.KNIGHT));
        assertThat(board.getStone(Square.get(Rank._1, File.B.mirror())).getPiece(), is(Piece.KNIGHT));
        assertThat(board.getStone(Square.get(Rank._8, File.B.mirror())).getPiece(), is(Piece.KNIGHT));
        
        // Bishops
        assertThat(board.getStone(Square.get(Rank._1, File.C)).getPiece(), is(Piece.BISHOP));
        assertThat(board.getStone(Square.get(Rank._8, File.C)).getPiece(), is(Piece.BISHOP));
        assertThat(board.getStone(Square.get(Rank._1, File.C.mirror())).getPiece(), is(Piece.BISHOP));
        assertThat(board.getStone(Square.get(Rank._8, File.C.mirror())).getPiece(), is(Piece.BISHOP));
        
        
        // Asymetric pieces
        // Queen
        assertThat(board.getStone(Square.get(Rank._1, File.D)).getPiece(), is(Piece.QUEEN));
        assertThat(board.getStone(Square.get(Rank._8, File.D)).getPiece(), is(Piece.QUEEN));
        
        // King
        assertThat(board.getStone(Square.get(Rank._1, File.E)).getPiece(), is(Piece.KING));
        assertThat(board.getStone(Square.get(Rank._8, File.E)).getPiece(), is(Piece.KING));

        assertThat(board.getTurn(), is(BoardSide.WHITE));
        assertThat(board.getHalfmove(), is((short) 0));
        
        assertThat(board.getSide(BoardSide.WHITE).getCastles(), hasItems(Castle.values()));
        assertThat(board.getSide(BoardSide.BLACK).getCastles(), hasItems(Castle.values()));
        
    }

}
