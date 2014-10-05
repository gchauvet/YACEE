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
package com.zatarox.chess.openchess.models.moves;

import com.zatarox.chess.openchess.models.materials.*;
import java.util.LinkedList;
import java.util.List;

abstract class AbstractSlideGenerator extends AbstractGenerator {

    protected AbstractSlideGenerator(Piece type) {
        super(type);
    }

    @Override
    public final List<Move> attacks(ChessBoard board, Square square) {
        final BitBoard all = board.getSnapshot(BoardSide.WHITE);
        all.merge(board.getSnapshot(BoardSide.BLACK));
        final BitBoard attacks = new BitBoard(attacks(square, all) & board.getSnapshot(board.getStone(square).getSide().flip()).unwrap());
        final List<Move> result = new LinkedList<>();
        for (Square to : attacks) {
            result.add(MovesFactorySingleton.getInstance().createCapture(square, to, board.getStone(to).getPiece()));
        }
        return result;
    }

    @Override
    public final List<Move> fills(ChessBoard board, Square square) {
        final BitBoard all = board.getSnapshot(BoardSide.WHITE);
        all.merge(board.getSnapshot(BoardSide.BLACK));
        final BitBoard attacks = new BitBoard(attacks(square, all) & ~all.unwrap());
        final List<Move> result = new LinkedList<>();
        for (Square to : attacks) {
            result.add(MovesFactorySingleton.getInstance().createNormal(square, to));
        }
        return result;
    }

    /**
     * Magic! attacks, very fast method
     *
     * @param index
     * @param all
     * @return
     */
    abstract protected long attacks(Square index, BitBoard all);

}
