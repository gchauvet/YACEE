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
package com.zatarox.chess.skychess.tables;

import com.zatarox.chess.skychess.engine.Board;
import com.zatarox.chess.skychess.tables.TranspositionTable.Entry;
import java.io.Serializable;
import org.apache.commons.collections.map.LRUMap;

/**
 * This class implements a transposition table.
 */
public class TranspositionTable extends AbstractTable<Board, Entry> {

    class Entry implements Serializable {

        public final long hash;
        public double lowerbound;
        public double upperbound;

        public Entry(Board board, double lowerbound, double upperbound) {
            this.hash = board.hashCode64();
            this.lowerbound = lowerbound;
            this.upperbound = upperbound;
        }

    }

    private static TranspositionTable INSTANCE = null;

    private TranspositionTable() {
        super(new LRUMap());
    }

    /**
     * Records the entry if the spot is empty or new position contains deeper
     * or old position hcontainswrong ancientNodeSwitch
     *
     * @param position
     * @param upperbound
     * @param lowerbound
     */
    public synchronized void put(Board position, double upperbound, double lowerbound) {
        getTable().put(position, new Entry(position, lowerbound, upperbound));
    }

    /**
     * Returns true if the entry at the right index is 0 which means we have an
     * entry stored
     *
     * @param position
     * @return 
     */
    public synchronized boolean contains(Board position) {
        return getTable().containsKey(position) && getTable().get(position).hash == position.hashCode64();
    }

    /**
     * Returns the upperbound for the current board.
     *
     * @param position
     * @return
     */
    public synchronized double getUpperbound(Board position) {
        double result = Double.NaN;
        if (getTable().containsKey(position)) {
            result = ((Entry) getTable().get(position)).upperbound;
        }
        return result;
    }
    
        /**
     * Returns the upperbound for the current board.
     *
     * @param position
     * @return
     */
    public synchronized double getLowerbound(Board position) {
        double result = Double.NaN;
        if (getTable().containsKey(position)) {
            result = ((Entry) getTable().get(position)).lowerbound;
        }
        return result;
    }

    public static TranspositionTable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TranspositionTable();
        }
        return INSTANCE;
    }
}
