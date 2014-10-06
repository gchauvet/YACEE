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
import com.zatarox.chess.openchess.models.materials.Square.Rank;

public class ChargeMove extends BasicMove {

    public ChargeMove(Square from, Square to) {
        super(from, to);
    }

    @Override
    protected void doPlay(ChessBoard board) throws IllegalMoveException {
        super.doPlay(board);
        board.getSide(board.getTurn()).setEnpassant(Square.from(getFrom().getFileIndex(),
                Rank.values()[getTo().getRankIndex().ordinal() + 8 * (board.getTurn() == BoardSide.WHITE ? -1 : 1)]));
    }

    @Override
    protected void doUnplay(ChessBoard board) throws IllegalMoveException {
        super.doUnplay(board);
        board.getSide(board.getTurn()).setEnpassant(null);
    }

}
