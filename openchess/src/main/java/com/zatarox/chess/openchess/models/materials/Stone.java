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

public class Stone implements Serializable {

    private final Piece piece;
    private final BoardSide side;

    public Stone() {
        this.piece = null;
        this.side = null;
    }

    public Stone(Piece piece, BoardSide side) {
        assert piece != null;
        assert side != null;
        this.piece = piece;
        this.side = side;
    }

    public Piece getPiece() {
        return piece;
    }

    public BoardSide getSide() {
        return side;
    }

    @Override
    public int hashCode() {
        return piece.hashCode() ^ side.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Stone) {
            final Stone other = (Stone) obj;
            result = other.piece == piece && other.side == side;
        }
        return result;
    }

    @Override
    public String toString() {
        String result;
        if (piece != null & side != null) {
            result = side.name() + " " + piece.name();
        } else {
            result = super.toString();
        }
        return result;
    }
}
