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
import java.util.*;

public class Player implements Serializable {
    private final Map<Piece, BitBoard> pieces = new EnumMap(Piece.class);
    private final Set<Castle> castles = EnumSet.noneOf(Castle.class);
    private Square enpassant;

    public Player() {
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
    
    public boolean isEnpassant() {
        return enpassant != null;
    }

    public Square getEnpassant() {
        return enpassant;
    }

    public void setEnpassant(Square enpassant) {
        this.enpassant = enpassant;
    }
    
}
