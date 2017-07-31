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
package com.zatarox.chess.openchess.controllers.generators;

import com.zatarox.chess.openchess.models.materials.*;
import com.zatarox.chess.openchess.models.moves.Move;
import java.util.PriorityQueue;
import java.util.Queue;

public final class GeneratorFacade implements Generator {

    private static final GeneratorFacade INSTANCE = new GeneratorFacade();

    /**
     * Discover attacks to squares using magics: cheap version
     *
     * @param board The chessboard
     * @param index Square to check
     * @return
     */
    @Override
    public boolean isEnPrise(ChessBoard board, Square index) throws IllegalArgumentException {
        boolean result = false;
        for (Piece p : Piece.values()) {
            if (GeneratorsFactorySingleton.getInstance().from(p).isEnPrise(board, index)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public Queue<Move> attacks(ChessBoard board) {
        final Queue<Move> result = new PriorityQueue<>();
        for (Square square : board.getSide(board.getTurn())) {
            result.addAll(attacks(board, square));
        }
        return result;
    }

    @Override
    public Queue<Move> attacks(ChessBoard board, Square square) {
        final Stone stone = board.getStone(square);
        return GeneratorsFactorySingleton.getInstance().from(stone.getPiece()).attacks(board, square);
    }

    @Override
    public Queue<Move> fills(ChessBoard board, Square square) {
        final Stone stone = board.getStone(square);
        return GeneratorsFactorySingleton.getInstance().from(stone.getPiece()).fills(board, square);
    }

    @Override
    public Queue<Move> fills(ChessBoard board) {
        final Queue<Move> result = new PriorityQueue<>();
        for (Square square : board.getSide(board.getTurn())) {
            result.addAll(fills(board, square));
        }
        return result;
    }

    @Override
    public Queue<Move> alls(ChessBoard board) {
        final Queue<Move> result = attacks(board);
        result.addAll(fills(board));
        return result;
    }

    @Override
    public Queue<Move> alls(ChessBoard board, Square square) {
        final Stone stone = board.getStone(square);
        return GeneratorsFactorySingleton.getInstance().from(stone.getPiece()).alls(board, square);
    }

    public static GeneratorFacade getInstance() {
        return INSTANCE;
    }

}
