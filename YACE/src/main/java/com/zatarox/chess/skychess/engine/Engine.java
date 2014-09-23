/*
 * Copyright 2014 Guillaume Chauvet.
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

import chesspresso.move.Move;
import com.zatarox.chess.skychess.tables.TranspositionTable;
import com.zatarox.chess.skychess.tables.TranspositionTable.Flag;

public class Engine implements Runnable {

    private final Board game;
    private short depth;
    private int engineTime;
    private int engineIncrement;
    private int moveTime;
    private boolean ponder;
    private short bestMove;
    private boolean stop;

    public Engine(Board game) {
        this.game = game;
    }

    public short getDepth() {
        return depth;
    }

    public void setDepth(short depth) {
        this.depth = depth;
    }

    public int getEngineIncrement() {
        return engineIncrement;
    }

    public void setEngineIncrement(int engineIncrement) {
        this.engineIncrement = engineIncrement;
    }

    public int getEngineTime() {
        return engineTime;
    }

    public void setEngineTime(int engineTime) {
        this.engineTime = engineTime;
    }

    public int getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(int moveTime) {
        this.moveTime = moveTime;
    }

    public boolean isPonder() {
        return ponder;
    }

    public void setPonder(boolean ponder) {
        this.ponder = ponder;
    }

    @Override
    public void run() {
        stop = false;
        bestMove = Move.NO_MOVE;
        bestMove = search();
    }

    private short search() {
        double best = Double.NEGATIVE_INFINITY;
        short move = Move.NO_MOVE;
        for (final short m : game.getAllMoves()) {
            if (game.play(m)) {
                final double score = -negascout((short) (depth - 1), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                game.unplay();
                if (score > best) {
                    best = score;
                    move = m;
                }
            }
        }
        return move;
    }

    public void stop() {
        this.stop = true;
    }

    public short getBestMove() {
        return bestMove;
    }

    private double quiesce(double alpha, double beta) {
        double stand_pat = game.evaluate();
        if (stand_pat >= beta) {
            return beta;
        } else if (alpha < stand_pat) {
            alpha = stand_pat;
        }

        for (final short move : game.getAllCapturingMoves()) {
            if (game.play(move)) {
                final double score = -quiesce(-beta, -alpha);
                game.unplay();

                if (score >= beta) {
                    return beta;
                } else if (score > alpha) {
                    alpha = score;
                }
            }
        }
        return alpha;
    }

    private double negascout(short depth, double alpha, double beta) {
        double score = Double.NEGATIVE_INFINITY;
        short bestMove_ = Move.NO_MOVE;
        Flag flag = Flag.ALPHA;
        if (TranspositionTable.getInstance().has(game)) {
            if (TranspositionTable.getInstance().getDepth(game) > depth) {
                alpha = TranspositionTable.getInstance().getEval(game);
            }
        } else {
            double b = beta;
            if (depth > 0 && !stop) {
                boolean first = true;
                for (final short move : game.getAllMoves()) {
                    if (game.play(move)) {
                        score = -negascout((short) (depth - 1), -b, -alpha);
                        if ((score > alpha) && (score < b) && !first) {
                            score = -negascout((short) (depth - 1), -beta, -alpha);
                        }
                        game.unplay();
                        first = false;
                        alpha = Math.max(alpha, score);
                        if (alpha >= beta) {
                            flag = Flag.EXACT;
                            bestMove_ = move;
                            break;
                        }
                        b = alpha + 1;
                    }
                }
            } else {
                alpha = quiesce(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
            TranspositionTable.getInstance().put(game, depth, flag, score, bestMove_);
        }
        return alpha;
    }
}
