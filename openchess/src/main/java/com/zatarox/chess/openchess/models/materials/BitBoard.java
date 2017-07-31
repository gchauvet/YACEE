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

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

public final class BitBoard implements Serializable, Iterable<Square> {

    private long board = 0;

    public void clear() {
        board = 0;
    }

    public BitBoard() {
    }

    public BitBoard(long board) {
        this.board = board;
    }

    public BitBoard(BitBoard bitboard) {
        this.board = bitboard.board;
    }

    /**
     * @return Number of pieces of the board
     */
    public int getSize() {
        return Long.bitCount(board);
    }

    /**
     * @param square Square to check
     * @return True if occuped
     */
    public boolean isOccuped(Square square) {
        return (board & square.toBitMask()) != 0;
    }

    /**
     * @param squares Squares used by pieces on this bitboard
     */
    void set(Set<Square> squares) {
        for (Square square : squares) {
            set(square);
        }
    }

    /**
     * @param square Add a piece on this bitboard
     */
    void set(Square square) {
        board |= square.toBitMask();
    }

    /**
     * @param square Remove piece on the bitboard
     */
    void unset(Square square) {
        board &= ~square.toBitMask();
    }

    @Override
    public int hashCode() {
        return Long.valueOf(board).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof BitBoard) {
            BitBoard cast = (BitBoard) obj;
            result = board == cast.board;
        }
        return result;
    }

    /**
     * @param board board to append
     */
    public void merge(BitBoard board) {
        this.board |= board.board;
    }

    /**
     * @return Internat bitboard representation
     */
    public long unwrap() {
        return board;
    }

    public boolean isEmpty() {
        return board == 0;
    }

    @Override
    public Iterator<Square> iterator() {
        return new Iterator<Square>() {

            private final BitBoard bits = new BitBoard(BitBoard.this);

            @Override

            public boolean hasNext() {
                return bits.getSize() > 0;
            }

            @Override
            public Square next() {
                Square result = Square.values()[Long.numberOfTrailingZeros(bits.board)];
                bits.unset(result);
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Square.Rank r : Square.Rank.reverse()) {
            for (Square.File f : Square.File.values()) {
                result.append((Square.from(f, r).toBitMask() & board) != 0 ? 'X' : '.');
                result.append(' ');
            }
            result.append("\n");
        }
        return result.toString();
    }

}
