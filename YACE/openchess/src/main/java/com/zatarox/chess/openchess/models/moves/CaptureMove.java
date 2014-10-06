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

public final class CaptureMove extends BasicMove {

    private final Piece captured;

    public CaptureMove(Square from, Square to, Piece captured) {
        super(from, to);
        assert captured != null;
        this.captured = captured;
    }

    public Piece getCaptured() {
        return captured;
    }

    @Override
    protected void doPlay(ChessBoard board) throws IllegalMoveException {
        board.getSide(board.getTurn().flip()).get(captured).unset(getTo());
        super.doPlay(board);
    }

    @Override
    protected void doUnplay(ChessBoard board) throws IllegalMoveException {
        super.doUnplay(board);
        board.getSide(board.getTurn().flip()).get(captured).set(getTo());
    }

    @Override
    public boolean equals(Object o) {
        boolean result = super.equals((BasicMove) o);
        if(o instanceof CaptureMove) {
            CaptureMove other = (CaptureMove) o;
            result &= other.captured == captured;
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7 * 67 + captured.hashCode();
        return hash ^ super.hashCode();
    }
    
    @Override
    public void accept(MoveVisitor visitor) {
        visitor.visit(this);
    }
}
