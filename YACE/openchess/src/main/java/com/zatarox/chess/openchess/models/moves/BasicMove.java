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

import com.zatarox.chess.openchess.models.moves.exceptions.IllegalMoveException;
import com.zatarox.chess.openchess.models.materials.*;

 public class BasicMove extends AbstractMove {

    public BasicMove(Square from, Square to) {
        super(from, to);
    }

    @Override
    protected void doPlay(ChessBoard board) throws IllegalMoveException {
        final Stone stone = board.getStone(getFrom());
        final BitBoard bitboard = board.getSide(stone.getSide()).get(stone.getPiece());
        bitboard.unset(getFrom());
        bitboard.set(getTo());
    }

    @Override
    protected void doUnplay(ChessBoard board) throws IllegalMoveException {
        final Stone stone = board.getStone(getTo());
        if (stone == null) {
            throw new IllegalMoveException("Piece not found");
        }
        final BitBoard bitboard = board.getSide(stone.getSide()).get(stone.getPiece());
        bitboard.unset(getTo());
        bitboard.set(getFrom());
    }

    @Override
    public void accept(MoveVisitor visitor) {
        visitor.visit(this);
    }

}
