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
package com.zatarox.chess.skychess.tables;

import chesspresso.move.Move;
import com.zatarox.chess.skychess.engine.Board;
import com.zatarox.chess.skychess.tables.TranspositionTable.Entry;
import java.io.Serializable;
import org.apache.commons.collections.map.LRUMap;

/**
 * This class implements a transposition table.
 */
public class TranspositionTable extends AbstractTable<Board, Entry> {

    public enum Flag {

        EXACT,
        ALPHA,
        BETA
    }

    class Entry implements Serializable {

        public final long hash;
        public double eval;
        public Flag flag;
        public short depth;
        public short move;

        public Entry(Board board, double eval, Flag flag, short depth, short move) {
            this.hash = board.hashCode64();
            this.eval = eval;
            this.flag = flag;
            this.depth = depth;
            this.move = move;
        }

    }

    private static TranspositionTable INSTANCE = null;

    private TranspositionTable() {
        super(new LRUMap());
    }

    /**
     * Records the entry if the spot is empty or new position has deeper depth
     * or old position has wrong ancientNodeSwitch
     *
     * @param position
     * @param depth
     * @param flag
     * @param eval
     * @param move
     */
    public synchronized void put(Board position, short depth, Flag flag, double eval, short move) {
        getTable().put(position, new Entry(position, eval, flag, depth, move));
    }

    /**
     * Returns true if the entry at the right index is 0 which means we have an
     * entry stored
     *
     * @param position
     */
    public synchronized boolean has(Board position) {
        return getTable().containsKey(position) && getTable().get(position).hash == position.hashCode64();

    } // END has

    /**
     * Returns the eval at the right index if the zobrist matches
     *
     * @param position
     */
    public synchronized double getEval(Board position) {
        double result = 0;
        if (getTable().containsKey(position)) {
            result = ((Entry) getTable().get(position)).eval;
        }
        return result;
    } // END getEval

    /**
     * Returns the flag at the right index if the zobrist matches
     *
     * @param position
     */
    public synchronized Flag getFlag(Board position) {
        Flag result = null;
        if (has(position)) {
            result = ((Entry) getTable().get(position)).flag;
        }
        return result;
    }

    /**
     * Returns the move at the right index if the zobrist matches
     *
     * @param position
     * @return
     */
    public synchronized short getMove(Board position) {
        short result = Move.NO_MOVE;
        if (getTable().containsKey(position)) {
            result = ((Entry) getTable().get(position)).move;
        }
        return result;
    }

    /**
     * Returns the depth at the right index if the zobrist matches
     *
     * @param position
     */
    public synchronized short getDepth(Board position) {
        short result = 0;
        if (getTable().containsKey(position)) {
            result = ((Entry) getTable().get(position)).depth;
        }
        return result;
    }

    /**
     * Collects the principal variation starting from the position on the board
     *
     * @param board
     * The position to collect pv from
     * @param current_depth
     * How deep the pv goes (avoids situations where keys point to
     * each other infinitely)
     * @return collectString The moves in a string
     */
    public synchronized short[] collectPV(Board board, short current_depth) {
        short[] arrayPV = new short[128];
        short move = getMove(board);

        int i = 20;
        int index = 0;
        while (i > 0) {
            if (move == 0) {
                break;
            }
            arrayPV[index] = move;
            if (board.play(move)) {
                move = getMove(board);
                i--;
                index++;
            }
        }
        // Unmake the moves
        for (i = index - 1; i >= 0; i--) {
            board.unplay();
        }
        return arrayPV;
    }

    public static TranspositionTable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TranspositionTable();
        }
        return INSTANCE;
    }
}
