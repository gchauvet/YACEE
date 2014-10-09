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
import com.google.common.base.Objects;
import com.zatarox.chess.openchess.models.materials.ChessBoard;
import com.zatarox.chess.openchess.models.materials.Piece;
import com.zatarox.chess.openchess.models.materials.Square;
import com.zatarox.chess.openchess.models.materials.Stone;
import com.zatarox.chess.openchess.models.moves.exceptions.SelfMateMoveException;
import com.zatarox.chess.openchess.controllers.generators.GeneratorFacade;
import com.zatarox.chess.openchess.models.materials.BitBoard;
import java.io.Serializable;

public abstract class AbstractMove implements Serializable, Comparable<AbstractMove>, Move, MoveVisitable {

    private final Square from, to;
    private boolean played = false;
    private float score = Float.NaN;

    protected AbstractMove(Square from, Square to) {
        assert from != null;
        assert to != null;
        assert from != to;
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

    protected void move(ChessBoard board, Square from, Square to) throws IllegalMoveException {
        final Stone stone = board.getStone(from);
        if (stone == null) {
            throw new IllegalMoveException("No piece");
        }
        final BitBoard bitboard = board.getSide(stone.getSide()).get(stone.getPiece());
        bitboard.unset(from);
        bitboard.set(to);
    }

    protected void unmove(ChessBoard board, Square from, Square to) throws IllegalMoveException {
        final Stone stone = board.getStone(to);
        if (stone == null) {
            throw new IllegalMoveException("Piece not found");
        }
        final BitBoard bitboard = board.getSide(stone.getSide()).get(stone.getPiece());
        bitboard.unset(to);
        bitboard.set(from);
    }

    private void checkLegalMove(ChessBoard board) throws SelfMateMoveException {
        final Stone stone = board.getStone(getTo());
        final Square king = board.getSide(stone.getSide()).get(Piece.KING).iterator().next();
        if (GeneratorFacade.getInstance().isEnPrise(board, king)) {
            throw new SelfMateMoveException();
        }
    }

    /**
     * Play a move on chessboard, but not change player turn flag.
     *
     * @param board Chess where move will be played
     * @throws IllegalMoveException
     */
    @Override
    public final void play(ChessBoard board) throws IllegalMoveException, SelfMateMoveException {
        if (played) {
            throw new IllegalMoveException("Move already played");
        }
        doPlay(board);
        played = true;
        checkLegalMove(board);
    }

    protected abstract void doPlay(ChessBoard board) throws IllegalMoveException;

    /**
     * Unplay a move on chessboard, but not change player turn flag.
     *
     * @param board Chessboard unplay move
     * @throws com.zatarox.chess.openchess.models.moves.exceptions.IllegalMoveException
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
        return -((Float) getScore()).compareTo(t.getScore());
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof AbstractMove) {
            AbstractMove other = (AbstractMove) o;
            result = other.from == from && other.to == to;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(from, to);
    }

    @Override
    public String toString() {
        return from + "-" + to;
    }

}
