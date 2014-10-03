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

import com.zatarox.chess.openchess.models.moves.IllegalMoveException;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class ChessBoard implements Serializable {

    private class Side implements Serializable {

        private final Map<Piece, BitBoard> pieces = new EnumMap(Piece.class);
        private final Set<Castle> castles = EnumSet.allOf(Castle.class);

        public Side() {
            for (Piece piece : Piece.values()) {
                pieces.put(piece, new BitBoard());
            }
        }

        public BitBoard get(Piece piece) {
            return pieces.get(piece);
        }

        public void setCastles(Set<Castle> castles) {
            this.castles.clear();
            this.castles.addAll(castles);
        }

        public Set<Castle> getCastles() {
            return castles;
        }

    }

    private final EnumMap<BoardSide, Side> sides = new EnumMap<>(BoardSide.class);
    private BoardSide turn;

    public ChessBoard() {
        for (BoardSide trait : BoardSide.values()) {
            sides.put(trait, new Side());
        }
    }

    /**
     * Clear chessboard
     */
    public void clear() {
        for (BoardSide trait : BoardSide.values()) {
            for (Piece piece : Piece.values()) {
                get(trait, piece).clear();
            }
        }
    }

    public BitBoard get(BoardSide trait, Piece piece) {
        return sides.get(trait).get(piece);
    }

    /**
     * @param square Square to check.
     * @return True if occuped.
     */
    public boolean isOccuped(Square square) {
        for (BoardSide t : BoardSide.values()) {
            for (Piece p : Piece.values()) {
                if (get(t, p).isOccuped(square)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param square Square to check
     * @return The piece type on the square
     */
    public Piece getPiece(Square square) {
        for (BoardSide t : BoardSide.values()) {
            for (Piece p : Piece.values()) {
                if (get(t, p).isOccuped(square)) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * @param square Square to check
     * @return The color on the piece
     */
    public BoardSide getSide(Square square) {
        for (BoardSide t : BoardSide.values()) {
            for (Piece p : Piece.values()) {
                if (get(t, p).isOccuped(square)) {
                    return t;
                }
            }
        }
        return null;
    }

    public void setPiece(BoardSide color, Piece piece, Square square) throws IllegalMoveException {
        if (isOccuped(square)) {
            throw new IllegalMoveException("A piece already defined for square " + square.name());
        }
        if (piece == Piece.KING) {
            if (get(color, piece).getSize() > 0) {
                throw new IllegalArgumentException("King already defined");
            }
        }
        get(color, piece).set(square);
    }

    public void unsetPiece(Square square) {
        for (BoardSide t : BoardSide.values()) {
            for (Piece p : Piece.values()) {
                get(t, p).unset(square);
            }
        }
    }

    public BoardSide getTurn() {
        return turn;
    }

    public void setTurn(BoardSide turn) {
        this.turn = turn;
    }

    public BitBoard getSnapshot(BoardSide color) {
        BitBoard result = new BitBoard();
        for (Piece piece : Piece.values()) {
            result.merge(get(color, piece));
        }
        return result;
    }

    public BitBoard getSnapshot(Piece piece) {
        BitBoard result = new BitBoard(get(BoardSide.WHITE, piece));
        result.merge(get(BoardSide.BLACK, piece));
        return result;
    }

}
