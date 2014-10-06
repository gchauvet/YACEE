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
import com.zatarox.chess.openchess.models.moves.Move;
import java.util.PriorityQueue;
import java.util.Queue;

final class QueenGenerator implements Generator {

    @Override
    public Queue<Move> attacks(ChessBoard board, Square square) {
        final Generator rooker = GeneratorsFactorySingleton.getInstance().from(Piece.ROOK);
        final Generator bishoper = GeneratorsFactorySingleton.getInstance().from(Piece.BISHOP);
        final Queue<Move> result = rooker.attacks(board, square);
        result.addAll(bishoper.attacks(board, square));
        return result;
    }

    @Override
    public Queue<Move> fills(ChessBoard board, Square square) {
        final Generator rooker = GeneratorsFactorySingleton.getInstance().from(Piece.ROOK);
        final Generator bishoper = GeneratorsFactorySingleton.getInstance().from(Piece.BISHOP);
        final Queue<Move> result = rooker.fills(board, square);
        result.addAll(bishoper.fills(board, square));
        return result;
    }

    @Override
    public final Queue<Move> attacks(ChessBoard board) {
        final Queue<Move> result = new PriorityQueue<>();
        for (Square index : board.getSide(board.getTurn()).get(Piece.QUEEN)) {
            result.addAll(attacks(board, index));
        }
        return result;
    }

    @Override
    public final Queue<Move> alls(ChessBoard board) {
        final Queue<Move> result = new PriorityQueue<>();
        for (Square index : board.getSide(board.getTurn()).get(Piece.QUEEN)) {
            result.addAll(fills(board, index));
        }
        return result;
    }

    @Override
    public Queue<Move> alls(ChessBoard board, Square square) {
        Queue<Move> result = attacks(board, square);
        result.addAll(fills(board, square));
        return result;
    }

}
