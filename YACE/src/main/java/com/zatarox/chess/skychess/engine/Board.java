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
package com.zatarox.chess.skychess.engine;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;
import com.fluxchess.jcpi.models.GenericMove;
import com.zatarox.chess.skychess.tables.RepetitionTable;
import java.io.Serializable;
import java.util.List;

public final class Board implements Serializable {

    public enum Side {

        WHITE,
        BLACK
    };

    Position game = new Position();
    private Evaluator evaluator = new DefaultEvaluator();

    public void reset() {
        game.set(Position.createInitialPosition());
    }

    public void reset(String fen) {
        game.set(new Position(fen));
    }

    public Side getPlayer() {
        return Chess.isWhitePly(game.getPlyNumber()) ? Side.WHITE : Side.BLACK;
    }
    
    public long hashCode64() {
        return game.getHashCode();
    }

    @Override
    public int hashCode() {
        return ((Long) hashCode64()).hashCode();
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public double evaluate() {
        return evaluator.evaluate(this);
    }

    public short[] getAllMoves() {
        return game.getAllMoves();
    }

    public short[] getAllCapturingMoves() {
        return game.getAllCapturingMoves();
    }

    public short[] getNonCapturingMoves() {
        return game.getAllNonCapturingMoves();
    }

    public boolean play(List<GenericMove> moves) {
        for (GenericMove move : moves) {
            int promotion;
            if (move.promotion != null) {
                switch (move.promotion) {
                    case QUEEN:
                        promotion = Chess.QUEEN;
                        break;
                    case ROOK:
                        promotion = Chess.ROOK;
                        break;
                    case BISHOP:
                        promotion = Chess.BISHOP;
                        break;
                    case KNIGHT:
                        promotion = Chess.KNIGHT;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } else {
                promotion = Chess.NO_PIECE;
            }
            final short ply = game.getMove(Chess.strToSqi(move.from.toString()), Chess.strToSqi(move.to.toString()), promotion);
            if (!play(ply)) {
                return false;
            }
            RepetitionTable.getInstance().recordRep(this);
        }
        return true;
    }

    public boolean play(short move) {
        boolean result = true;
        try {
            game.doMove(move);
            if(!game.isLegal()) {
                unplay();
                result = false;
            }
        } catch (IllegalMoveException ex) {
            result = false;
        }
        return result;
    }

    public boolean unplay() {
        return game.undoMove();
    }

}
