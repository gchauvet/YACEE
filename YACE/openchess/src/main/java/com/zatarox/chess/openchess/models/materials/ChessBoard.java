/*
 * Copyright 2014 Guillaume.
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

import java.io.Serializable;
import java.util.EnumMap;

public class ChessBoard implements Serializable {

    private class Side implements Serializable {

        private EnumMap<Piece, BitBoard> pieces = new EnumMap(Piece.class);

        public BitBoard get(Piece piece) {
            return pieces.get(piece);
        }
    }

    private EnumMap<Trait, Side> sides = new EnumMap<Trait, Side>(Trait.class);
    private Trait turn;

    public BitBoard get(Trait trait, Piece piece) {
        return sides.get(trait).get(piece);
    }

    /**
     * @param square Square to check.
     * @return True if occuped.
     */
    public boolean isOccuped(Square square) {
        for (Trait t : Trait.values()) {
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
        for (Trait t : Trait.values()) {
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
    public Trait getSide(Square square) {
        for (Trait t : Trait.values()) {
            for (Piece p : Piece.values()) {
                if (get(t, p).isOccuped(square)) {
                    return t;
                }
            }
        }
        return null;
    }

    public void setPiece(Trait color, Piece piece, Square square) {
        if (isOccuped(square)) {
            throw new IllegalArgumentException("A piece already defined for square " + square.name());
        }
        if (piece == Piece.KING) {
            if (get(color, piece).getSize() > 0) {
                throw new IllegalArgumentException("King already defined");
            }
        }
        get(color, piece).set(square);
    }
    
    public void unsetPiece(Square square) {
        for (Trait t : Trait.values()) {
            for (Piece p : Piece.values()) {
                get(t, p).unset(square);
            }
        }
    }

    public Trait getTurn() {
        return turn;
    }

    public void setTurn(Trait turn) {
        this.turn = turn;
    }

}
