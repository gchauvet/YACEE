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
package com.zatarox.chess.openchess.models.moves;

import com.zatarox.chess.openchess.models.materials.*;
import java.util.List;

final class QueenGenerator extends AbstractGenerator {

    @Override
    public List<Move> attacks(ChessBoard board, Square square) {
        final Generator rooker = GeneratorsFactorySingleton.getInstance().build(Piece.ROOK);
        final Generator bishoper = GeneratorsFactorySingleton.getInstance().build(Piece.BISHOP);
        final List<Move> result = rooker.attacks(board, square);
        result.addAll(bishoper.attacks(board, square));
        return result;
    }

    @Override
    public List<Move> fills(ChessBoard board, Square square) {
        final Generator rooker = GeneratorsFactorySingleton.getInstance().build(Piece.ROOK);
        final Generator bishoper = GeneratorsFactorySingleton.getInstance().build(Piece.BISHOP);
        final List<Move> result = rooker.fills(board, square);
        result.addAll(bishoper.fills(board, square));
        return result;
    }

}
