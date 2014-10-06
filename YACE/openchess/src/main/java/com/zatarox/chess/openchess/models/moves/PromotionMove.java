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

public final class PromotionMove extends AbstractMove {

    private final Piece promotion;
    private Piece captured = null;

    public PromotionMove(Square from, Square to, Piece promotion) {
        super(from, to);
        this.promotion = promotion;
    }

    public Piece getPromotion() {
        return promotion;
    }

    @Override
    protected void doPlay(ChessBoard board) throws IllegalMoveException {
        final BoardSide color = board.getStone(getFrom()).getSide();
        board.getSide(color.flip()).get(Piece.PAWN).unset(getFrom());
        if (board.isOccuped(getTo())) {
            captured = board.getStone(getTo()).getPiece();
            board.getSide(color.flip()).get(captured).unset(getTo());
        }
        board.getSide(color).get(promotion).set(getTo());
    }

    @Override
    protected void doUnplay(ChessBoard board) throws IllegalMoveException {
        final BoardSide color = board.getStone(getTo()).getSide();
        board.getSide(color).get(Piece.PAWN).set(getFrom());
        if (captured != null) {
            board.getSide(color.flip()).get(captured).unset(getTo());
            captured = null;
        }
        board.getSide(color).get(promotion).unset(getTo());
    }
    
    @Override
    public void accept(MoveVisitor visitor) {
        visitor.visit(this);
    }

}
