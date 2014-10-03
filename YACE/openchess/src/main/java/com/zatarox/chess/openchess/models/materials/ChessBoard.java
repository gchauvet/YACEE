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

public final class ChessBoard implements Serializable {

    private final EnumMap<BoardSide, Player> sides = new EnumMap<>(BoardSide.class);
    private BoardSide turn;
    private short halfmove = 0;
    private short fullmove = 0;

    public ChessBoard() {
        for (BoardSide trait : BoardSide.values()) {
            sides.put(trait, new Player());
        }
    }

    /**
     * Clear chessboard
     */
    public void clear() {
        for (BoardSide trait : BoardSide.values()) {
            for (Piece piece : Piece.values()) {
                getSide(trait).get(piece).clear();
            }
        }
    }

    public Player getSide(BoardSide side) {
        return sides.get(side);
    }

    public short getHalfmove() {
        return halfmove;
    }

    public short getFullmove() {
        return fullmove;
    }

    public void setFullmove(short fullmove) {
        this.fullmove = fullmove;
    }

    public void setHalfmove(short halfmove) {
        this.halfmove = halfmove;
    }

    /**
     * @param square Square to check.
     * @return True if occuped.
     */
    public boolean isOccuped(Square square) {
        for (BoardSide t : BoardSide.values()) {
            for (Piece p : Piece.values()) {
                if (getSide(t).get(p).isOccuped(square)) {
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
                if (getSide(t).get(p).isOccuped(square)) {
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
                if (getSide(t).get(p).isOccuped(square)) {
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
            if (getSide(color).get(piece).getSize() > 0) {
                throw new IllegalArgumentException("King already defined");
            }
        }
        getSide(color).get(piece).set(square);
    }

    public void unsetPiece(Square square) {
        for (BoardSide t : BoardSide.values()) {
            for (Piece p : Piece.values()) {
                getSide(t).get(p).unset(square);
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
            result.merge(getSide(color).get(piece));
        }
        return result;
    }

    public BitBoard getSnapshot(Piece piece) {
        BitBoard result = new BitBoard(getSide(BoardSide.WHITE).get(piece));
        result.merge(getSide(BoardSide.BLACK).get(piece));
        return result;
    }

}
