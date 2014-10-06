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

public final class CastleMove extends AbstractMove {

    private final Castle castle;
    
    private static Square castleFrom(BoardSide trait) {
        return trait == BoardSide.WHITE ? Square.E1 : Square.E8;
    }

    @Override
    public float getScore() {
        return 5000;
    }
    
    private static Square castleTo(BoardSide trait, Castle castle) {
        Square result;
        switch(trait) {
            case WHITE:
                result = castle == Castle.LONG ? Square.C1 : Square.G1;
                break;
            case BLACK:
                result = castle == Castle.LONG ? Square.C8 : Square.G8;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

    public CastleMove(Castle castle, BoardSide trait) {
        super(castleFrom(trait), castleTo(trait, castle));
        this.castle = castle;
    }
    
    @Override
    protected void doPlay(ChessBoard board) throws IllegalMoveException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doUnplay(ChessBoard board) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void accept(MoveVisitor visitor) {
        visitor.visit(this);
    }
    
}
