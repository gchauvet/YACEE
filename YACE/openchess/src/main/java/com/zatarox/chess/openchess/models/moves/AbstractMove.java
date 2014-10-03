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

import com.zatarox.chess.openchess.models.materials.ChessBoard;
import com.zatarox.chess.openchess.models.materials.Square;
import java.io.Serializable;

abstract class AbstractMove implements Serializable, Comparable<AbstractMove>, Move {

    private final Square from, to;
    private boolean played = false;
    private float score;

    protected AbstractMove(Square from, Square to) {
        this.from = from;
        this.to = to;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }

    /**
     * @param board Chess where move will be played
     * @throws IllegalMoveException
     */
    @Override
    public final void play(ChessBoard board) throws IllegalMoveException {
        if (played) {
            throw new IllegalMoveException("Move already played");
        }
        doPlay(board);
        played = true;
    }
    
    protected abstract void doPlay(ChessBoard board) throws IllegalMoveException;

    /**
     * @param board Chessboard unplay move
     * @throws UnsupportedOperationException
     */
    @Override
    public final void unplay(ChessBoard board) throws IllegalMoveException {
        if (!played) {
            throw new IllegalMoveException("Move already played");
        }
        doUnplay(board);
        played = false;
    }
    
    protected abstract void doUnplay(ChessBoard board) throws IllegalMoveException;

    @Override
    public final int compareTo(AbstractMove t) {
        // Hight score in first
        return -((Float) score).compareTo(t.score);
    }

}
