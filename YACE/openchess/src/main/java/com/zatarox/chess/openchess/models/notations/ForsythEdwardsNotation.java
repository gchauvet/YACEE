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
import java.util.LinkedList;
import java.util.List;
import org.codehaus.jparsec.*;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;

public final class ForsythEdwardsNotation implements Notation {

    static final class StoneExpression extends Stone {

        private final short skip;

        public StoneExpression(Piece piece, BoardSide side) {
            super(piece, side);
            this.skip = 1;
        }

        public StoneExpression(short skip) {
            this.skip = skip;
        }

        public StoneExpression() {
            this((short) 1);
        }

        public boolean hasPiece() {
            return getPiece() != null;
        }

        public short getSkip() {
            return skip;
        }

    }

    static final class TokenStoneExpression implements Map<Void, StoneExpression> {

        private final StoneExpression result;

        public TokenStoneExpression(BoardSide side, Piece piece) {
            this.result = new StoneExpression(piece, side);
        }

        public TokenStoneExpression(char skip) {
            this((short) (skip - '0'));
            assert Character.isDigit(skip);
        }

        public TokenStoneExpression(short skip) {
            assert skip >= 0 && skip <= 7;
            this.result = new StoneExpression(skip);
        }

        @Override
        public StoneExpression map(Void from) {
            return result;
        }
    }

    static final class RankStoneExpression implements Iterable<File> {

        private final EnumMap<File, StoneExpression> pieces = new EnumMap<>(File.class);

        public void put(StoneExpression piece, File index) {
            pieces.put(index, piece);
        }

        public StoneExpression get(File index) {
            return pieces.get(index);
        }

        @Override
        public Iterator<File> iterator() {
            return pieces.keySet().iterator();
        }
    }

    static final class RankExpression implements Map<List<StoneExpression>, RankStoneExpression> {

        @Override
        public RankStoneExpression map(List<StoneExpression> from) {
            final RankStoneExpression result = new RankStoneExpression();
            short i = 0;

            for (StoneExpression stone : from) {
                if (stone.hasPiece()) {
                    result.put(stone, File.values()[i]);
                    i++;
                } else {
                    i += stone.getSkip();
                }
            }
            return result;
        }

    }

    static final class BoardsideExpression implements Map<Void, BoardSide> {

        private final BoardSide result;

        public BoardsideExpression(BoardSide result) {
            this.result = result;
        }

        @Override
        public BoardSide map(Void from) {
            return result;
        }

    }

    static final class CastleExpression implements Map<Void, Pair<BoardSide, Castle>> {

        private final Pair<BoardSide, Castle> result;

        public CastleExpression(BoardSide side, Castle castle) {
            this.result = new Pair<>(side, castle);
        }

        @Override
        public Pair<BoardSide, Castle> map(Void from) {
            return result;
        }
    }

    static final class ShortExpression implements Map<String, Short> {

        @Override
        public Short map(String from) {
            return Short.valueOf(from);
        }

    }

    static final class FileExpression implements Map<Token, File> {

        @Override
        public File map(Token from) {
            return File.values()['a' - ((String) from.value()).charAt(0)];
        }

    }

    static final class EPRankExpression implements Map<Token, Rank> {

        @Override
        public Rank map(Token from) {
            return Rank.values()[Short.valueOf((String) from.value())];
        }

    }

    static Parser<StoneExpression> createStoneParser(BoardSide side) {
        final EnumMap<Piece, Character> pieces = new EnumMap(Piece.class);
        pieces.put(Piece.PAWN, 'P');
        pieces.put(Piece.KNIGHT, 'N');
        pieces.put(Piece.BISHOP, 'B');
        pieces.put(Piece.ROOK, 'R');
        pieces.put(Piece.QUEEN, 'Q');
        pieces.put(Piece.KING, 'K');

        List<Parser<StoneExpression>> result = new LinkedList<>();
        for (Piece p : pieces.keySet()) {
            char c = pieces.get(p);
            c = side == BoardSide.WHITE ? Character.toUpperCase(c) : Character.toLowerCase(c);
            result.add(Scanners.isChar(c).map(new TokenStoneExpression(side, p)));
        }
        return Parsers.or(result);
    }

    static Parser<RankStoneExpression> createRankParser() {
        final Parser<StoneExpression> whites = createStoneParser(BoardSide.WHITE);
        final Parser<StoneExpression> blacks = createStoneParser(BoardSide.BLACK);
        List<Parser<StoneExpression>> stones = new LinkedList<>();
        for (char number = '0'; number <= '7'; number++) {
            stones.add(Scanners.isChar(number).map(new TokenStoneExpression(number)));
        }
        final Parser<StoneExpression> digit17 = Parsers.or(stones);

        // Ranks
        final Parser<RankStoneExpression> skipRank = Scanners.isChar('8').map(new Map<Void, RankStoneExpression>() {
            @Override
            public RankStoneExpression map(Void from) {
                return new RankStoneExpression();
            }
        });
        return Parsers.or(digit17, blacks, whites).times(1, 8).map(new RankExpression()).or(skipRank);
    }

    static Parser<List<RankStoneExpression>> createBoardParser() {
        return createRankParser().sepBy(Scanners.isChar('/'));
    }

    static Parser<List<Pair<BoardSide, Castle>>> createCastelingParser() {
        final Parser<Pair<BoardSide, Castle>> whiteShortCastle = Scanners.isChar('K').map(new CastleExpression(BoardSide.WHITE, Castle.SHORT));
        final Parser<Pair<BoardSide, Castle>> whiteLongCastle = Scanners.isChar('Q').map(new CastleExpression(BoardSide.WHITE, Castle.LONG));
        final Parser<Pair<BoardSide, Castle>> blackShortCastle = Scanners.isChar('k').map(new CastleExpression(BoardSide.BLACK, Castle.SHORT));
        final Parser<Pair<BoardSide, Castle>> blackLongCastle = Scanners.isChar('q').map(new CastleExpression(BoardSide.BLACK, Castle.LONG));

        return Scanners.isChar('-').map(new Map<Void, List<Pair<BoardSide, Castle>>>() {
            @Override
            public List<Pair<BoardSide, Castle>> map(Void from) {
                return Collections.emptyList();
            }
        }).or(Parsers.list(Arrays.asList(whiteShortCastle, whiteLongCastle, blackShortCastle, blackLongCastle)));
    }

    static Parser<BoardSide> createTurnParser() {
        return Scanners.isChar('w').map(new BoardsideExpression(BoardSide.WHITE)).or(Scanners.isChar('b').map(new BoardsideExpression(BoardSide.BLACK)));
    }

    static Parser<Square> createEnPassantParser() {
        final Parser<File> fileLetter = Scanners.among("abcdefgh").token().map(new FileExpression());
        final Parser<Rank> epRank = Scanners.among("36").token().map(new EPRankExpression());
        final Parser<Square> epsquare = Parsers.tuple(fileLetter, epRank).map(new Map<Pair<File, Rank>, Square>() {
            @Override
            public Square map(Pair<File, Rank> from) {
                return Square.from(from.a, from.b);
            }
        });
        return Scanners.isChar('-').map(new Map<Void, Square>() {
            @Override
            public Square map(Void from) {
                return null;
            }
        }).or(epsquare);
    }

    static Parser<Short> createShortParser() {
        return Scanners.INTEGER.map(new ShortExpression());
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
        this.parser = Parsers.list(Arrays.asList(createBoardParser().followedBy(Scanners.WHITESPACES),
                createTurnParser().followedBy(Scanners.WHITESPACES),
                createCastelingParser().followedBy(Scanners.WHITESPACES),
                createEnPassantParser().followedBy(Scanners.WHITESPACES),
                createShortParser().followedBy(Scanners.WHITESPACES),
                createShortParser())
        );
        this.notation = notation;
    }

    @Override
    public ChessBoard create() {
        final List<Object> parsed = parser.parse(notation);
        final ChessBoard result = new ChessBoard();
        short r = 7;
        for (RankStoneExpression rank : ((List<RankStoneExpression>) parsed.get(0))) {
            for (File f : rank) {
                final StoneExpression p = rank.get(f);
                result.getSide(p.getSide()).get(p.getPiece()).set(Square.from(f, Rank.values()[r]));
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
