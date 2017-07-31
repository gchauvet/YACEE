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
package com.zatarox.chess.skychess.engine;

import com.zatarox.chess.skychess.tables.TranspositionTable;
import java.io.Serializable;
import chesspresso.move.Move;

public class DefaultChessEngine implements ChessEngine, Serializable {

    private Board board;
    private short depth;
    private int engineTime;
    private int engineIncrement;
    private int moveTime;
    private boolean ponder;
    private boolean stop;

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public short getDepth() {
        return depth;
    }

    @Override
    public void setDepth(short depth) {
        this.depth = depth;
    }

    @Override
    public int getEngineIncrement() {
        return engineIncrement;
    }

    @Override
    public void setEngineIncrement(int engineIncrement) {
        this.engineIncrement = engineIncrement;
    }

    @Override
    public int getEngineTime() {
        return engineTime;
    }

    @Override
    public void setEngineTime(int engineTime) {
        this.engineTime = engineTime;
    }

    @Override
    public int getMoveTime() {
        return moveTime;
    }

    @Override
    public void setMoveTime(int moveTime) {
        this.moveTime = moveTime;
    }

    @Override
    public boolean isPonder() {
        return ponder;
    }

    @Override
    public void setPonder(boolean ponder) {
        this.ponder = ponder;
    }

    private short search() {
        double best = Double.NEGATIVE_INFINITY;
        short move = Move.NO_MOVE;
        for (final short m : board.getAllMoves()) {
            if (board.play(m)) {
                final double score = iterativeSearch();
                board.unplay();
                if (score > best) {
                    best = score;
                    move = m;
                }
            }
        }
        return move;
    }

    @Override
    public void stop() {
        this.stop = true;
    }

    private double quiesce(double alpha, double beta) {
        double stand_pat = board.evaluate();
        if (stand_pat >= beta) {
            return beta;
        } else if (alpha < stand_pat) {
            alpha = stand_pat;
        }

        for (final short move : board.getAllCapturingMoves()) {
            if (board.play(move)) {
                final double score = -quiesce(-beta, -alpha);
                board.unplay();

                if (score >= beta) {
                    return beta;
                } else if (score > alpha) {
                    alpha = score;
                }
            }
        }
        return alpha;
    }

    private double iterativeSearch() {
        double firstguess = 0;
        for (short i = 1; i < depth; i++) {
            firstguess = mtdf(firstguess, i);
        }
        return firstguess;
    }

    private double mtdf(double first, short depth) {
        double g = first;
        double lowerbound = Double.NEGATIVE_INFINITY;
        double upperbound = Double.POSITIVE_INFINITY;
        do {
            double beta = (g == lowerbound) ? g + 1 : g;
            g = -negascout(depth, beta - 1, beta);
        } while (lowerbound >= upperbound);
        return g;
    }

    private double negascout(short depth, double alpha, double beta) {
        double score = Double.NEGATIVE_INFINITY;
        short bestMove = Move.NO_MOVE;
        //Flag flag = Flag.ALPHA;
        if (TranspositionTable.getInstance().contains(board)) {
            double lower = TranspositionTable.getInstance().getLowerbound(board);
            double upper = TranspositionTable.getInstance().getUpperbound(board);
            if (lower >= beta) {
                return lower;
            } else if (upper <= alpha) {
                return upper;
            }
            alpha = Math.max(alpha, lower);
            beta = Math.min(beta, upper);
        } else {
            double b = beta;
            if (depth > 0 && !stop) {
                boolean first = true;
                for (final short move : board.getAllMoves()) {
                    if (board.play(move)) {
                        score = -negascout((short) (depth - 1), -b, -alpha);
                        if ((score > alpha) && (score < b) && !first) {
                            score = -negascout((short) (depth - 1), -beta, -alpha);
                        }
                        board.unplay();
                        first = false;
                        alpha = Math.max(alpha, score);
                        if (alpha >= beta) {
                            //flag = Flag.EXACT;
                            bestMove = move;
                            break;
                        }
                        b = alpha + 1;
                    }
                }
            } else {
                alpha = quiesce(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
            TranspositionTable.getInstance().put(board, alpha, beta);
        }
        return alpha;
    }

    @Override
    public Short call() {
        stop = false;
        return search();
    }
}
