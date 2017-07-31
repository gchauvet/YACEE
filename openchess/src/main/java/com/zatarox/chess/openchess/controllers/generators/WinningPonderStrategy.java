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

import com.zatarox.chess.openchess.models.materials.ChessBoard;
import com.zatarox.chess.openchess.models.materials.Piece;
import com.zatarox.chess.openchess.models.moves.*;

/**
 * Default implementation for moves pondering.
 * USe winning captures/promotions ordering.
 */
public final class WinningPonderStrategy implements MovePonderingStrategy {

    @Override
    public void compute(final ChessBoard board, final MoveVisitable move) {
        move.accept(new MoveVisitor() {
            @Override
            public void visit(BasicMove visit) {
                visit.setScore(board.getStone(visit.getFrom()).getPiece().getPonderation());
            }

            @Override
            public void visit(CaptureMove visit) {
                visit.setScore(visit.getCaptured().getPiece().getPonderation() * 10 - board.getStone(visit.getFrom()).getPiece().getPonderation());
            }

            @Override
            public void visit(CastleMove visit) {
                visit.setScore(5000);
            }

            @Override
            public void visit(ChargeMove visit) {
                visit.setScore(Piece.PAWN.getPonderation() * 2);
            }

            @Override
            public void visit(EnPassantMove visit) {
                visit.setScore(Piece.PAWN.getPonderation() * 2);
            }

            @Override
            public void visit(PromotionMove visit) {
                visit.setScore(visit.getPromotion().getPonderation() * 10);
            }
        });
    }

}
