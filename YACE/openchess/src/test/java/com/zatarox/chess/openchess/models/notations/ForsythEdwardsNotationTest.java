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
import com.zatarox.chess.openchess.models.notations.ForsythEdwardsNotation.*;
import java.util.List;
import org.codehaus.jparsec.functors.Pair;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static org.junit.Assert.*;

public class ForsythEdwardsNotationTest {

    @Test
    public void emptyRank() {
        final RankStoneExpression result = ForsythEdwardsNotation.createRankParser().parse("8");
        assertFalse(result.iterator().hasNext());
    }

    @Test
    public void notEmptyRank() {
        final RankStoneExpression result = ForsythEdwardsNotation.createRankParser().parse("1k6");
        assertThat(result.get(File.B), equalTo(new Stone(Piece.KING, BoardSide.BLACK)));
    }

    @Test
    public void playerTurn() {
        assertThat(ForsythEdwardsNotation.createTurnParser().parse("w"), is(BoardSide.WHITE));
        assertThat(ForsythEdwardsNotation.createTurnParser().parse("b"), is(BoardSide.BLACK));
    }

    @Test
    public void emptyCasteling() {
        assertTrue(ForsythEdwardsNotation.createCastelingParser().parse("-").isEmpty());
    }
    
    @Test
    public void fullCasteling() {
        final List<Pair<BoardSide, Castle>> result = ForsythEdwardsNotation.createCastelingParser().parse("KQkq");
        for (BoardSide side : BoardSide.values()) {
            for (Castle castle : Castle.values()) {
                assertThat(result, hasItems(new Pair<>(side, castle)));
            }
        }
    }
    
    @Test
    public void enPassant() {
        assertNull(ForsythEdwardsNotation.createEnPassantParser().parse("-"));
        assertThat(ForsythEdwardsNotation.createFileParser().parse("c"), is(File.C));
        assertThat(ForsythEdwardsNotation.createEnPassantParser().parse("d3"), is(Square.D3));
    }

    @Test
    public void createDefaultBoard() {
        final Notation instance = new ForsythEdwardsNotation();
        final ChessBoard board = instance.create();
        assertThat(board.getHashing().hashCode64(), is(not(0L)));
        assertNotNull(board);
        assertFalse(board.isEmpty());
        // Check chessboard side
        // Whites
        for (File file : File.values()) {
            for (Rank rank : new Rank[]{Rank._1, Rank._2}) {
                assertThat(board.getStone(Square.from(file, rank)).getSide(), is(BoardSide.WHITE));
            }
        }
        // Blacks
        for (File file : File.values()) {
            for (Rank rank : new Rank[]{Rank._8, Rank._7}) {
                assertThat(board.getStone(Square.from(file, rank)).getSide(), is(BoardSide.BLACK));
            }
        }

        // Check middle is empty
        for (File file : File.values()) {
            for (Rank rank : new Rank[]{Rank._3, Rank._4, Rank._5, Rank._6}) {
                assertTrue(!board.isOccuped(Square.from(file, rank)));
            }
        }

        // Check pieces
        // Pawns
        for (File file : File.values()) {
            assertThat(board.getStone(Square.from(file, Rank._2)), equalTo(new Stone(Piece.PAWN, BoardSide.WHITE)));
            assertThat(board.getStone(Square.from(file, Rank._7)), equalTo(new Stone(Piece.PAWN, BoardSide.BLACK)));
        }

        // Rooks
        assertThat(board.getStone(Square.from(File.A, Rank._1)), equalTo(new Stone(Piece.ROOK, BoardSide.WHITE)));
        assertThat(board.getStone(Square.from(File.A, Rank._8)), equalTo(new Stone(Piece.ROOK, BoardSide.BLACK)));
        assertThat(board.getStone(Square.from(File.A.mirror(), Rank._1)), equalTo(new Stone(Piece.ROOK, BoardSide.WHITE)));
        assertThat(board.getStone(Square.from(File.A.mirror(), Rank._8)), equalTo(new Stone(Piece.ROOK, BoardSide.BLACK)));

        // Knights
        assertThat(board.getStone(Square.from(File.B, Rank._1)), equalTo(new Stone(Piece.KNIGHT, BoardSide.WHITE)));
        assertThat(board.getStone(Square.from(File.B, Rank._8)), equalTo(new Stone(Piece.KNIGHT, BoardSide.BLACK)));
        assertThat(board.getStone(Square.from(File.B.mirror(), Rank._1)), equalTo(new Stone(Piece.KNIGHT, BoardSide.WHITE)));
        assertThat(board.getStone(Square.from(File.B.mirror(), Rank._8)), equalTo(new Stone(Piece.KNIGHT, BoardSide.BLACK)));

        // Bishops
        assertThat(board.getStone(Square.from(File.C, Rank._1)), equalTo(new Stone(Piece.BISHOP, BoardSide.WHITE)));
        assertThat(board.getStone(Square.from(File.C, Rank._8)), equalTo(new Stone(Piece.BISHOP, BoardSide.BLACK)));
        assertThat(board.getStone(Square.from(File.C.mirror(), Rank._1)), equalTo(new Stone(Piece.BISHOP, BoardSide.WHITE)));
        assertThat(board.getStone(Square.from(File.C.mirror(), Rank._8)), equalTo(new Stone(Piece.BISHOP, BoardSide.BLACK)));

        // Asymetric pieces
        // Queen
        assertThat(board.getStone(Square.D1), equalTo(new Stone(Piece.QUEEN, BoardSide.WHITE)));
        assertThat(board.getStone(Square.D8), equalTo(new Stone(Piece.QUEEN, BoardSide.BLACK)));

        // King
        assertThat(board.getStone(Square.E1), equalTo(new Stone(Piece.KING, BoardSide.WHITE)));
        assertThat(board.getStone(Square.E8), equalTo(new Stone(Piece.KING, BoardSide.BLACK)));

        assertThat(board.getTurn(), is(BoardSide.WHITE));
        assertThat(board.getHalfmove(), is((short) 0));
        assertThat(board.getFullmove(), is((short) 1));

        assertThat(board.getSide(BoardSide.WHITE).getCastles(), hasItems(Castle.values()));
        assertThat(board.getSide(BoardSide.BLACK).getCastles(), hasItems(Castle.values()));
    }

    @Test
    public void testEmptyChessboard() {
        final Notation fen = new ForsythEdwardsNotation("8/8/8/8/8/8/8/8 b - - 0 2");
        final ChessBoard board = fen.create();
        assertThat(board.getHashing().hashCode64(), is(0L));
        assertTrue(board.isEmpty());
        assertThat(board.getTurn(), is(BoardSide.BLACK));
        assertThat(board.getHalfmove(), is((short) 0));
        assertThat(board.getFullmove(), is((short) 2));
    }

    @Test
    public void testOnlyKings() {
        final Notation fen = new ForsythEdwardsNotation("8/3k4/8/8/8/1K6/8/8 b - - 0 12");
        final ChessBoard board = fen.create();
        assertThat(board.getHashing().hashCode64(), is(not(0L)));
        assertFalse(board.isEmpty());
        assertThat(board.getStone(Square.D7), equalTo(new Stone(Piece.KING, BoardSide.BLACK)));
        assertThat(board.getStone(Square.B3), equalTo(new Stone(Piece.KING, BoardSide.WHITE)));

        assertThat(board.getTurn(), is(BoardSide.BLACK));
        assertThat(board.getHalfmove(), is((short) 0));
        assertThat(board.getFullmove(), is((short) 12));
    }

    @Test
    public void testRookBattery() {
        final Notation fen = new ForsythEdwardsNotation("r3k2r/pp1p1pbp/2n3p1/8/8/2NR1N2/PPP2PPP/5RK1 w - - 0 1");
        final ChessBoard board = fen.create();
        assertThat(board.getHashing().hashCode64(), is(not(0L)));
        // Pawns
        assertThat(board.getStone(Square.from(File.B, Rank._2)), is(new Stone(Piece.PAWN, BoardSide.WHITE)));

        assertTrue(board.getSide(BoardSide.WHITE).getCastles().isEmpty());
        assertTrue(board.getSide(BoardSide.BLACK).getCastles().isEmpty());
        assertThat(board.getTurn(), is(BoardSide.WHITE));
        assertThat(board.getHalfmove(), is((short) 0));
        assertThat(board.getFullmove(), is((short) 1));
    }

}
