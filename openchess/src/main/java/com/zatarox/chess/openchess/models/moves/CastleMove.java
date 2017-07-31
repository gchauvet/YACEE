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
import java.util.EnumSet;
import java.util.Set;

public final class CastleMove extends AbstractMove {

    private final Castle castle;
    private Set<Castle> rigths = EnumSet.noneOf(Castle.class);

    private static Square castleFrom(BoardSide trait) {
        return trait == BoardSide.WHITE ? Square.E1 : Square.E8;
    }

    @Override
    public float getScore() {
        return 5000;
    }

    private static Square castleTo(BoardSide trait, Castle castle) {
        assert trait != null;
        assert castle != null;
        Square result;
        switch (trait) {
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
        final Stone stone = board.getStone(getFrom());
        switch (castle) {
            case SHORT:
                if (stone.getSide() == BoardSide.WHITE) {
                    move(board, Square.E1, Square.G1);
                    move(board, Square.H1, Square.F1);
                } else {
                    move(board, Square.E8, Square.G8);
                    move(board, Square.H8, Square.F8);
                }
                break;
            case LONG:
                if (stone.getSide() == BoardSide.WHITE) {
                    move(board, Square.E1, Square.C1);
                    move(board, Square.A1, Square.D1);
                } else {
                    move(board, Square.E8, Square.C8);
                    move(board, Square.A8, Square.D8);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        final Set<Castle> castles = board.getSide(stone.getSide()).getCastles();
        rigths.addAll(castles);
        castles.clear();
    }

    @Override
    protected void doUnplay(ChessBoard board) throws IllegalMoveException {
        final Stone stone = board.getStone(getTo());
        switch (castle) {
            case SHORT:
                if (stone.getSide() == BoardSide.WHITE) {
                    unmove(board, Square.E1, Square.G1);
                    unmove(board, Square.H1, Square.F1);
                } else {
                    unmove(board, Square.E8, Square.G8);
                    unmove(board, Square.H8, Square.F8);
                }
                break;
            case LONG:
                if (stone.getSide() == BoardSide.WHITE) {
                    unmove(board, Square.E1, Square.C1);
                    unmove(board, Square.A1, Square.D1);
                } else {
                    unmove(board, Square.E8, Square.C8);
                    unmove(board, Square.A8, Square.D8);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        final Set<Castle> castles = board.getSide(stone.getSide()).getCastles();
        castles.addAll(rigths);
        rigths.clear();
    }

    @Override
    public void accept(MoveVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        String result = "O-O";
        if (castle == Castle.LONG) {
            result += "-O";
        }
        return result;
    }

}
