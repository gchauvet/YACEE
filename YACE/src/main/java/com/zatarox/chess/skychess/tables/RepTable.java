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

import com.zatarox.chess.skychess.Notification;

/**
 * class Transposition table
 *
 * This class holds a hashtable and entrys
 *
 * @author Jonatan Pettersson (mediocrechess@gmail.com)
 */
public class RepTable {

    public long[] hashtable; // Used for repetition detection table
    public int HASHSIZE; // Size of the hash table
    public static final int SLOTS = 1; // Number of slots one entry takes

    // Repetition table
    public RepTable(int sizeInMb) {
        this.HASHSIZE = sizeInMb * 1024 * 1024 * 8 / 64 / SLOTS;
        hashtable = new long[HASHSIZE * SLOTS];
    }

    /**
     * Clears the table
     */
    public void clear() {
        hashtable = new long[HASHSIZE * SLOTS];
    } // END clear

    /**
     * Records a position the repetition table, will search through the table
     * until it finds an empty slot
     *
     * @param zobrist
     * The key to match
     */
    public void recordRep(long zobrist) {
        // TODO: Make this smoother with a better looking for empty places
        int hashkey = (int) (zobrist % HASHSIZE);

        if (hashtable[hashkey] == 0 || hashtable[hashkey] == zobrist) {
            hashtable[hashkey] = zobrist;
            return;
        }

        for (int i = 1; i < HASHSIZE; i++) {
            if (hashtable[(hashkey + i) % HASHSIZE] == 0) {
                hashtable[(hashkey + i) % HASHSIZE] = zobrist;
                return;
            }
        }

        Notification.getInstance().getLogger().error("Error: Repetition table is full");
    } // END recordRep

    /**
     * Removes a repetition entry
     *
     * @param zobrist
     * The key to match
     */
    public void removeRep(long zobrist) {
        // TODO: Make this smoother with a better looking for empty places

        int hashkey = (int) (zobrist % HASHSIZE);

        if (hashtable[hashkey] == zobrist) {
            hashtable[hashkey] = 0;
            return;
        }

        for (int i = 1; i < HASHSIZE; i++) {
            if (hashtable[(hashkey + i) % HASHSIZE] == zobrist) {
                hashtable[(hashkey + i) % HASHSIZE] = 0;
                return;
            }
        }
        Notification.getInstance().getLogger().error("Error: Repetition to be removed not found");

    } // END recordRep

    /**
     * Checks if the zobrist key exists in the repetition table will search
     * through the whole array to see if any spot matches
     *
     * TODO: Make this smoother
     *
     * @param zobrist
     * @return
     */
    public boolean repExists(long zobrist) {
        int hashkey = (int) (zobrist % HASHSIZE);

        if (hashtable[hashkey] == 0) {
            return false;
        } else if (hashtable[hashkey] == zobrist) {
            return true;
        }

        for (int i = 1; i < HASHSIZE; i++) {
            if (hashtable[(hashkey + i) % HASHSIZE] == 0) {
                return false;
            } else if (hashtable[(hashkey + i) % HASHSIZE] == zobrist) {
                return true;
            }
        }
        return false;
    } // END repExists
}
