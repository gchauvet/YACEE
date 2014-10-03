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

final class PromotionMove extends AbstractMove {

    private final Piece promotion;
    private Piece captured = null;

    public PromotionMove(Square from, Square to, Piece promotion) {
        super(from, to);
        this.promotion = promotion;
    }

    @Override
    protected void doPlay(ChessBoard board) throws IllegalMoveException {
        board.getSide(board.getTurn()).get(Piece.PAWN).unset(getFrom());
        if (board.isOccuped(getTo())) {
            captured = board.getStone(getTo()).getPiece();
            board.getSide(board.getTurn()).get(captured).unset(getTo());
        }
        board.getSide(board.getTurn()).get(promotion).set(getTo());
    }

    @Override
    protected void doUnplay(ChessBoard board) throws IllegalMoveException {
        board.getSide(board.getTurn()).get(Piece.PAWN).set(getFrom());
        if (captured != null) {
            board.getSide(board.getTurn()).get(captured).unset(getTo());
            captured = null;
        }
        board.getSide(board.getTurn()).get(promotion).unset(getTo());
    }

}
