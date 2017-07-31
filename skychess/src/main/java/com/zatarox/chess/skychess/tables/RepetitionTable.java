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
import java.util.HashSet;
import java.util.Set;
import chesspresso.position.Position;

/**
 * This class implements a repetition table as a singleton.
 */
public class RepetitionTable {

    private final Set<Integer> table;
    private static RepetitionTable INSTANCE = null;

    // Repetition table
    private RepetitionTable() {
        table = new HashSet<>(32 * 1024 * 1024 / 8, 1);
    }

    public synchronized void clear() {
        table.clear();
    }

    /**
     * Records a position the repetition table, will search through the table
     * until it finds an empty slot
     *
     * @param position
     * The key to match
     */
    public synchronized void recordRep(Board position) {
        table.add(position.hashCode());
    }

    /**
     * Removes a repetition entry
     *
     * @param position
     */
    public synchronized void removeRep(Board position) {
        table.remove(position.hashCode());

    }

    /**
     * Checks if the zobrist key exists in the repetition table will search
     * through the whole array to see if any spot matches
     *
     * TODO: Make this smoother
     *
     * @param position
     * @return
     */
    public synchronized boolean repExists(Position position) {
        return table.contains(position);
    }

    public static RepetitionTable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepetitionTable();
        }
        return INSTANCE;
    }
}
