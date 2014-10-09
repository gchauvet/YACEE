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

import java.nio.ByteBuffer;
import java.security.SecureRandom;

public final class ZobristHashStrategy implements HashStrategy {

    private long hash;
    private final long[][][] keys = new long[BoardSide.values().length][Square.values().length][Piece.values().length];

    public ZobristHashStrategy() {
        final SecureRandom sr = new SecureRandom();
        for (BoardSide side : BoardSide.values()) {
            for (Square square : Square.values()) {
                final byte[] rndBytes = new byte[8];
                for (Piece piece : Piece.values()) {
                    sr.nextBytes(rndBytes);
                    keys[side.ordinal()][square.ordinal()][piece.ordinal()] = ByteBuffer.wrap(rndBytes).asLongBuffer().get();
                }
            }
        }
    }

    private long get(Stone stone, Square square) {
        return keys[stone.getSide().ordinal()][square.ordinal()][stone.getPiece().ordinal()];
    }

    @Override
    public void add(Stone stone, Square square) {
        hash ^= get(stone, square);
    }

    @Override
    public void remove(Stone stone, Square square) {
        hash ^= get(stone, square);
    }

    @Override
    public long hashCode64() {
        return hash;
    }

}
