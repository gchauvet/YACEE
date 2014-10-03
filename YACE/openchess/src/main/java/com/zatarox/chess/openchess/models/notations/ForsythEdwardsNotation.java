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
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jparsec.*;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;
import org.codehaus.jparsec.util.Lists;

public final class ForsythEdwardsNotation implements Notation {

    private final class StoneExpression extends Stone {

        private final short skip;

        public StoneExpression(Piece piece, BoardSide side) {
            super(piece, side);
            this.skip = 1;
        }

        public StoneExpression(short skip) {
            super(null, null);
            this.skip = skip;
        }

        public boolean hasPiece() {
            return getPiece() != null;
        }

        public short getSkip() {
            return skip;
        }

    }

    private final class TokenStoneExpression implements Map<Void, StoneExpression> {

        private final StoneExpression result;

        public TokenStoneExpression(BoardSide side, Piece piece) {
            this.result = new StoneExpression(piece, side);
        }

        @Override
        public StoneExpression map(Void from) {
            return result;
        }
    }

    private final class TokenSkipStoneExpression implements Map<Token, StoneExpression> {

        @Override
        public StoneExpression map(Token from) {
            return new StoneExpression(Short.valueOf((String) from.value()));
        }
    }

    private final class RankStones implements Iterable<StoneExpression> {

        private List<StoneExpression> pieces = Lists.arrayList(8);

        public void put(StoneExpression piece, short index) {
            pieces.add(index, piece);
        }

        @Override
        public Iterator<StoneExpression> iterator() {
            return pieces.iterator();
        }
    }

    private final class RankExpression implements Map<List<StoneExpression>, RankStones> {

        @Override
        public RankStones map(List<StoneExpression> from) {
            short i = 0;
            RankStones result = new RankStones();
            for (StoneExpression stone : from) {
                if (stone.hasPiece()) {
                    result.put(stone, i);
                    i++;
                } else {
                    i += stone.getSkip();
                }
            }
            return result;
        }

    }

    private final class EmptyRankExpression implements Map<Void, RankStones> {

        @Override
        public RankStones map(Void from) {
            return new RankStones();
        }

    }

    private final class BoardsideExpression implements Map<Void, BoardSide> {

        final BoardSide result;

        public BoardsideExpression(BoardSide result) {
            this.result = result;
        }

        @Override
        public BoardSide map(Void from) {
            return result;
        }

    }

    private final class CastleExpression implements Map<Void, Pair<BoardSide, Castle>> {

        private Pair<BoardSide, Castle> result;

        public CastleExpression(BoardSide side, Castle castle) {
            this.result = new Pair<>(side, castle);
        }

        @Override
        public Pair<BoardSide, Castle> map(Void from) {
            return result;
        }
    }

    private final class ShortExpression implements Map<String, Short> {

        @Override
        public Short map(String from) {
            return Short.valueOf(from);
        }

    }

    private final class FileExpression implements Map<Token, File> {

        @Override
        public File map(Token from) {
            return File.values()['a' - ((String) from.value()).charAt(0)];
        }

    }

    private final class EPRankExpression implements Map<Token, Rank> {

        @Override
        public Rank map(Token from) {
            return Rank.values()[Short.valueOf((String) from.value())];
        }

    }

    private Parser<StoneExpression> createStoneParser(BoardSide side) {
        final EnumMap<Piece, Character> pieces = new EnumMap(Piece.class);
        pieces.put(Piece.PAWN, 'P');
        pieces.put(Piece.KNIGHT, 'N');
        pieces.put(Piece.BISHOP, 'B');
        pieces.put(Piece.ROOK, 'R');
        pieces.put(Piece.QUEEN, 'Q');
        pieces.put(Piece.KING, 'K');

        Parser<StoneExpression> result = null;
        for (Piece p : pieces.keySet()) {
            char c = pieces.get(p);
            c = side == BoardSide.WHITE ? Character.toUpperCase(c) : Character.toLowerCase(c);
            Parser<StoneExpression> parse = Scanners.isChar(c).map(new TokenStoneExpression(side, p));
            if (result == null) {
                result = parse;
            } else {
                result = result.or(parse);
            }
        }
        return result;
    }

    private final Parser<List<Object>> parser;
    private final String notation;

    /**
     * https://chessprogramming.wikispaces.com/Forsyth-Edwards+Notation
     * http://jparsec.codehaus.org/manual/faq.html
     */
    public ForsythEdwardsNotation() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public ForsythEdwardsNotation(String notation) {
        //// Piece positions
        // Terminals
        final Parser<List<RankStones>> ranks;
        {
            final Parser<StoneExpression> whites = createStoneParser(BoardSide.WHITE);
            final Parser<StoneExpression> blacks = createStoneParser(BoardSide.BLACK);
            final Parser<StoneExpression> digit17 = Scanners.among("1234567").token().map(new TokenSkipStoneExpression());
            final Parser<StoneExpression> piece = whites.or(blacks);

            // Ranks
            final Parser<RankStones> skipRank = Scanners.isChar('8').map(new EmptyRankExpression());
            final Parser<RankStones> rank8 = digit17.or(piece).many1().map(new RankExpression()).or(skipRank);
            ranks = rank8.sepBy(Scanners.isChar('/'));
        }
        // Player
        final Parser<BoardSide> side = Scanners.isChar('w').map(new BoardsideExpression(BoardSide.WHITE)).or(Scanners.isChar('b').map(new BoardsideExpression(BoardSide.BLACK)));
        // Casteling
        final Parser<List<Pair<BoardSide, Castle>>> castling;
        {
            final Parser<Pair<BoardSide, Castle>> whiteShortCastle = Scanners.isChar('K').map(new CastleExpression(BoardSide.WHITE, Castle.SHORT));
            final Parser<Pair<BoardSide, Castle>> whiteLongCastle = Scanners.isChar('Q').map(new CastleExpression(BoardSide.WHITE, Castle.LONG));
            final Parser<Pair<BoardSide, Castle>> blackShortCastle = Scanners.isChar('k').map(new CastleExpression(BoardSide.BLACK, Castle.SHORT));
            final Parser<Pair<BoardSide, Castle>> blackLongCastle = Scanners.isChar('q').map(new CastleExpression(BoardSide.BLACK, Castle.LONG));

            castling = Scanners.isChar('-').map(new Map<Void, List<Pair<BoardSide, Castle>>>() {

                @Override
                public List<Pair<BoardSide, Castle>> map(Void from) {
                    return Collections.emptyList();
                }

            }).or(Parsers.list(Arrays.asList(whiteShortCastle, whiteLongCastle, blackShortCastle, blackLongCastle)));
        }
        // En passant
        final Parser<File> fileLetter = Scanners.among("abcdefgh").token().map(new FileExpression());
        final Parser<Rank> epRank = Scanners.among("36").token().map(new EPRankExpression());
        final Parser<Square> epsquare = Parsers.tuple(fileLetter, epRank).map(new Map<Pair<File, Rank>, Square>() {
            @Override
            public Square map(Pair<File, Rank> from) {
                return Square.get(from.b, from.a);
            }
        });
        final Parser<Square> enpassant = Scanners.isChar('-').map(new Map<Void, Square>() {
            @Override
            public Square map(Void from) {
                return null;
            }
        }).or(epsquare);
        // Halfmove Clock
        final Parser<Short> halfmove = Scanners.INTEGER.map(new ShortExpression());
        // Fullmove counter
        final Parser<Short> fullmove = Scanners.INTEGER.map(new ShortExpression());

        // Parser
        this.parser = Parsers.list(
                Arrays.asList(
                        ranks.followedBy(Scanners.WHITESPACES),
                        side.followedBy(Scanners.WHITESPACES),
                        castling.followedBy(Scanners.WHITESPACES),
                        enpassant.followedBy(Scanners.WHITESPACES),
                        halfmove.followedBy(Scanners.WHITESPACES),
                        fullmove)
        );
        this.notation = notation;
    }

    @Override
    public ChessBoard create() {
        final List<Object> parsed = parser.parse(notation);
        final ChessBoard result = new ChessBoard();
        short r = 7;
        for (RankStones rank : ((List<RankStones>) parsed.get(0))) {
            short f = 0;
            for (StoneExpression p : rank) {
                if (p.hasPiece()) {
                    result.getSide(p.getSide()).get(p.getPiece()).set(Square.get(Rank.values()[r], File.values()[f]));
                }
                f += p.getSkip();
            }
            r--;
        }
        result.setTurn((BoardSide) parsed.get(1));

        for (Pair<BoardSide, Castle> castle : (List<Pair<BoardSide, Castle>>) parsed.get(2)) {
            result.getSide(castle.a).getCastles().add(castle.b);
        }

        result.getSide(result.getTurn()).setEnpassant((Square) parsed.get(3));
        result.setHalfmove((short) parsed.get(4));
        result.setFullmove((short) parsed.get(5));
        return result;
    }

    @Override
    public String toString(ChessBoard board) {
        throw new UnsupportedOperationException();
    }
}
