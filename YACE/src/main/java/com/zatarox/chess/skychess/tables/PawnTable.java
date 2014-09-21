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

import com.zatarox.chess.skychess.board.Evaluation;
import static com.zatarox.chess.skychess.Definitions.*;

/**
 * class Transposition table
 *
 * This class holds a hashtable and entrys
 *
 * @author Jonatan Pettersson (mediocrechess@gmail.com)
 */
public class PawnTable {

    public int[] hashtable; // Used for transposition table
    public int HASHSIZE; // The number of slots either table will have

    public static final int SLOTS = 3; // 3 for one 'table', 6 for two (two
    // tables means one for depth and one
    // for always replace)

    // Ordinary transposition table
    public PawnTable(int sizeInMb) {
        this.HASHSIZE = sizeInMb * 1024 * 1024 * 8 / 32 / SLOTS;
        hashtable = new int[HASHSIZE * SLOTS];
    }

    /**
     * Clears the transposition table
     */
    public void clear() {
        hashtable = new int[HASHSIZE * SLOTS];
    } // END clear()

    public void recordPawnEval(long zobrist, int evalWhite, int evalBlack,
            int passers) {
        int hashkey = (int) (zobrist % HASHSIZE) * 3;

        hashtable[hashkey] = 0 | (evalWhite + 0x3FFF)
                | ((evalBlack + 0x3FFF) << 16);
        hashtable[hashkey + 1] = passers;
        hashtable[hashkey + 2] = (int) (zobrist >> 32);
    }

	// END recordEval()
    public int probePawnEval(long zobrist) {
        int hashkey = (int) (zobrist % HASHSIZE) * 3;
        if (hashtable[hashkey + 2] == ((int) (zobrist >> 32))) {
            Evaluation.passers = hashtable[hashkey + 1];
            return (hashtable[hashkey]);
        }

        return EVALNOTFOUND;
    }
}
