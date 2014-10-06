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
package com.zatarox.chess.openchess.models.moves.generators;

import com.zatarox.chess.openchess.models.materials.Piece;
import java.util.EnumMap;
import java.util.Map;

public class GeneratorsFactorySingleton {

    private static final GeneratorsFactorySingleton INSTANCE = new GeneratorsFactorySingleton();

    private final Map<Piece, Generator> generators = new EnumMap<>(Piece.class);

    private GeneratorsFactorySingleton() {
    }

    public Generator from(Piece piece) {
        if (!generators.containsKey(piece)) {
            Generator result;
            switch (piece) {
                case PAWN:
                    result = new PawnGenerator();
                    break;
                case KNIGHT:
                    result = new KnightGenerator();
                    break;
                case BISHOP:
                    result = new BishopGenerator();
                    break;
                case ROOK:
                    result = new RookGenerator();
                    break;
                case QUEEN:
                    result = new QueenGenerator();
                    break;
                case KING:
                    result = new KingGenerator();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            generators.put(piece, result);
        }
        return generators.get(piece);
    }

    public static GeneratorsFactorySingleton getInstance() {
        return INSTANCE;
    }

}
