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

import com.zatarox.chess.openchess.models.materials.*;

public class GeneratorFacade {

    private static final GeneratorFacade INSTANCE = new GeneratorFacade();

    /**
     * Discover attacks to squares using magics: cheap version
     *
     * @param board
     * @param index
     * @param color
     * @return
     */
    public boolean isSquareAttacked(ChessBoard board, Square index, BoardSide color) {
        final BitBoard others = board.getSnapshot(color);
        final BitBoard all = new BitBoard(others);
        all.merge(board.getSnapshot(color.flip()));

        boolean result = false;
        for (Piece p : Piece.values()) {
            if (!GeneratorsFactorySingleton.getInstance().build(Piece.ROOK).attacks(board, index).isEmpty()) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    public static GeneratorFacade getInstance() {
        return INSTANCE;
    }

}
