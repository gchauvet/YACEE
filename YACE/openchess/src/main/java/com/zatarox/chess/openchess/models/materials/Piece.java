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

public enum Piece {

    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    public float getPonderation() {
        float result;
        switch (this) {
            case PAWN:
                result = 1f;
                break;
            case BISHOP:
                result = 3.33f;
                break;
            case KNIGHT:
                result = 3.20f;
                break;
            case ROOK:
                result = 5.10f;
                break;
            case QUEEN:
                result = 8.80f;
                break;
            case KING:
                result = 16f * 10.24f;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

}
