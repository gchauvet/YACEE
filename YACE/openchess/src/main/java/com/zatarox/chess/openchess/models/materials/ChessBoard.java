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

import com.zatarox.chess.openchess.models.materials.Square.File;
import com.zatarox.chess.openchess.models.materials.Square.Rank;
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

    /**
     * @return Check if chessboard is empty
     */
    public boolean isEmpty() {
        boolean result = true;
        for (BoardSide trait : BoardSide.values()) {
            for (Piece piece : Piece.values()) {
                result &= getSide(trait).get(piece).isEmpty();
            }
        }
        return result;
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
     * @return The stone (piece+color) type on the square
     */
    public Stone getStone(Square square) {
        for (BoardSide t : BoardSide.values()) {
            for (Piece p : Piece.values()) {
                if (getSide(t).get(p).isOccuped(square)) {
                    return new Stone(p, t);
                }
            }
        }
        return null;
    }

    public void setPiece(Square square, Stone stone) throws IllegalMoveException {
        if (isOccuped(square)) {
            throw new IllegalMoveException("A piece already defined for square " + square.name());
        }
        if (stone.getPiece() == Piece.KING) {
            if (getSide(stone.getSide()).get(stone.getPiece()).getSize() > 0) {
                throw new IllegalArgumentException("King already defined");
            }
        }
        getSide(stone.getSide()).get(stone.getPiece()).set(square);
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Rank r : Rank.reverse()) {
            for (File f : File.values()) {
                Stone stone = getStone(Square.from(f, r));
                if (stone != null) {
                    final char p = stone.getPiece() == Piece.KNIGHT ? 'N' : stone.getPiece().name().charAt(0);
                    result.append(stone.getSide() == BoardSide.WHITE ? Character.toUpperCase(p) : Character.toLowerCase(p));
                } else {
                    result.append('.');
                }
                result.append(' ');
            }
            result.append("\n");
        }
        return result.toString();
    }

}
