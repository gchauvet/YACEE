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
package com.zatarox.chess.skychess;

import com.zatarox.chess.skychess.tables.*;

public class Settings {

    public static final String OPTION_OWN_BOOK = "OwnBook";
    private static final String OWN_BOOK_PATH = "performance.bin";
    public static final boolean DEFAULT_USE_OWN_BOOK = true;
    public static final String OPTION_HASH = "Hash";
    public static final int DEFAULT_HASH_SIZE = 16;
    public static final String OPTION_EVAL_HASH = "EvalHash";
    public static final int DEFAULT_EVAL_HASH_SIZE = 8;
    public static final String OPTION_PAWN_HASH = "PawnHash";
    public static final int DEFAULT_PAWN_HASH_SIZE = 8;
    public static final String OPTION_PONDER = "Ponder";
    public static final boolean DEFAULT_PONDER = false;

    /* Transposition tables */
    private TranspositionTable transpositionTable;
    private RepTable repTable;
    private EvalTable evalHash;
    private PawnTable pawnHash;
    private int tt_size;
    private static final int REP_SIZE = 1;
    private int eval_size;
    private int pawn_size;
    private boolean ponder;

    /* Book */
    private Book book;
    private boolean useOwnBook;

    private static Settings instance = null;

    /**
     * Singleton (with private constructor and the getInstance method,
     * only one instance of the class can ever be created)
     */
    private Settings() {
        Notification.getInstance().getLogger().debug("Initializing settings");

        setTranspositionTableSize(DEFAULT_HASH_SIZE);
        setEvalTableSize(DEFAULT_EVAL_HASH_SIZE);
        setPawnTableSize(DEFAULT_PAWN_HASH_SIZE);
        setUseOwnBook(DEFAULT_USE_OWN_BOOK);
        repTable = new RepTable(REP_SIZE);
    }

    public void setUseOwnBook(boolean setOwnBook) {
        if (setOwnBook) {
            useOwnBook = true;
            book = Book.getInstance(OWN_BOOK_PATH);
            if (book == null) {
                Notification.getInstance().getLogger().warn("Book " + OWN_BOOK_PATH + " not found, turning it off");
                useOwnBook = false;
            }
        } else {
            useOwnBook = false;
            book = null;
        }
    }

    public void setPonder(boolean doPonder) {
        ponder = doPonder;
    }

    public boolean getPonder() {
        return ponder;
    }

    public void setTranspositionTableSize(int size) {
        tt_size = size;
        transpositionTable = new TranspositionTable(tt_size);
    }

    public void setEvalTableSize(int size) {
        eval_size = size;
        evalHash = new EvalTable(eval_size);
    }

    public void setPawnTableSize(int size) {
        pawn_size = size;
        pawnHash = new PawnTable(pawn_size);
    }

    /**
     * Creates the singleton of the Book and returns it
     *
     * @return The instance of the settings
     */
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public TranspositionTable getTranspositionTable() {
        return transpositionTable;
    }

    public RepTable getRepTable() {
        return repTable;
    }

    public EvalTable getEvalHash() {
        return evalHash;
    }

    public PawnTable getPawnHash() {
        return pawnHash;
    }

    public int getTt_size() {
        return tt_size;
    }

    public static int getREP_SIZE() {
        return REP_SIZE;
    }

    public int getEval_size() {
        return eval_size;
    }

    public int getPawn_size() {
        return pawn_size;
    }

    public Book getBook() {
        return book;
    }

    public boolean isUseOwnBook() {
        return useOwnBook;
    }
}
