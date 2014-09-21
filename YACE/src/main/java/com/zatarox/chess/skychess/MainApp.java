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

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.fluxchess.jcpi.options.CheckboxOption;
import com.fluxchess.jcpi.options.SpinnerOption;
import com.zatarox.chess.skychess.board.Board;
import com.zatarox.chess.skychess.board.Move;
import com.zatarox.chess.skychess.engine.Engine;
import com.zatarox.chess.skychess.engine.Engine.LineEval;
import java.io.IOException;

/**
 * class MainApp
 *
 * This is the main class of MainApp which is used to connect to Winboard etc.
 *
 * @author Jonatan Pettersson (mediocrechess@gmail.com) Date: 2006-12-27
 */
public class MainApp extends AbstractEngine {

    Board board = new Board(); // Create a board on which we will be

    public static void main(String args[]) throws IOException {
        MainApp main = new MainApp();
        main.run();
    }

    @Override
    protected void quit() {
    }

    @Override
    public void receive(EngineInitializeRequestCommand command) {
        Settings.getInstance();
        ProtocolInitializeAnswerCommand request = new ProtocolInitializeAnswerCommand("SkyChess", "Guillaume Chauvet");
        request.addOption(new SpinnerOption(Settings.OPTION_HASH, Settings.DEFAULT_HASH_SIZE, 1, 512));
        request.addOption(new SpinnerOption(Settings.OPTION_EVAL_HASH, Settings.DEFAULT_EVAL_HASH_SIZE, 1, 32));
        request.addOption(new SpinnerOption(Settings.OPTION_PAWN_HASH, Settings.DEFAULT_EVAL_HASH_SIZE, 1, 32));
        request.addOption(new CheckboxOption(Settings.OPTION_PONDER, Settings.DEFAULT_PONDER));
        request.addOption(new CheckboxOption(Settings.OPTION_OWN_BOOK, Settings.DEFAULT_USE_OWN_BOOK));
        getProtocol().send(request);
    }

    @Override
    public void receive(EngineSetOptionCommand command) {
        switch (command.name) {
            case Settings.OPTION_EVAL_HASH:
                Settings.getInstance().setEvalTableSize(Integer.valueOf(command.value));
                break;
            case Settings.OPTION_HASH:
                Settings.getInstance().setTranspositionTableSize(Integer.valueOf(command.value));
                break;
            case Settings.OPTION_PAWN_HASH:
                Settings.getInstance().setPawnTableSize(Integer.valueOf(command.value));
                break;
            case Settings.OPTION_OWN_BOOK:
                Settings.getInstance().setUseOwnBook(Boolean.valueOf(command.value));
                break;
            case Settings.OPTION_PONDER:
                Settings.getInstance().setPonder(Boolean.valueOf(command.value));
                break;
        }
    }

    @Override
    public void receive(EngineDebugCommand command) {
    }

    @Override
    public void receive(EngineReadyRequestCommand command) {
        getProtocol().send(new ProtocolReadyAnswerCommand("readyok"));
    }

    @Override
    public void receive(EngineNewGameCommand command) {
        Settings.getInstance().getRepTable().clear(); // Reset the history
        Settings.getInstance().getTranspositionTable().clear(); // Reset transposition table
    }

    /**
     * Takes an inputted move-string and matches it with a legal move generated
     * from the board
     *
     * @param move
     * The inputted move
     * @param board
     * The board on which to find moves
     * @return int The matched move
     */
    private int receiveMove(String move, Board board) {
        Move[] legalMoves = new Move[256];
        for (int i = 0; i < 256; i++) {
            legalMoves[i] = new Move();
        }
        int totalMoves = board.gen_allLegalMoves(legalMoves, 0); // All moves

        for (int i = 0; i < totalMoves; i++) {
            if (Move.inputNotation(legalMoves[i].move).equals(move)) {
                return legalMoves[i].move;
            }
        }
        return 0;
    }

    @Override
    public void receive(EngineAnalyzeCommand command) {
        board.inputFen(command.board.toString());
        for (GenericMove move : command.moves) {
            board.makeMove(receiveMove(move.toString(), board));
            Settings.getInstance().getRepTable().recordRep(board.zobristKey);
        }
    }

    @Override
    public void receive(EngineStartCalculatingCommand command) {
        int wtime = command.getClock(GenericColor.WHITE) == null ? 0 : command.getClock(GenericColor.WHITE).intValue();
        int btime = command.getClock(GenericColor.BLACK) == null ? 0 : command.getClock(GenericColor.BLACK).intValue();
        int winc = command.getClockIncrement(GenericColor.WHITE) == null ? 0 : command.getClockIncrement(GenericColor.WHITE).intValue();
        int binc = command.getClockIncrement(GenericColor.BLACK) == null ? 0 : command.getClockIncrement(GenericColor.BLACK).intValue();
        final boolean ponder = command.getPonder();
        final int searchDepth = command.getDepth() == null ? 0 : command.getDepth();
        final int movetime = command.getMoveTime() == null ? 0 : command.getMoveTime().intValue();

        if (command.getInfinite()) {
            wtime = 99990000;
            btime = 99990000;
            winc = 0;
            binc = 0;
        }

        // We now have the times so set the engine's time and increment
        // to whatever side he is playing (the side to move on the
        // board)
        int engineTime;
        int engineInc;
        if (board.toMove == 1) {
            engineTime = wtime;
            engineInc = winc;
        } else {
            engineTime = btime;
            engineInc = binc;
        }

        // The engine's turn to move, so find the best line
        LineEval bestLine = new LineEval();

        boolean useBook = false;
        if (Settings.getInstance().isUseOwnBook()) {
            String bookMove = Settings.getInstance().getBook().getMoveFromBoard(board);

            if (!bookMove.equals("")) {
                bestLine.line[0] = receiveMove(bookMove, board);
                useBook = true;
            }
        }
        if (!useBook) {
            try {
                bestLine = Engine.search(board, searchDepth, engineTime, engineInc, movetime, ponder);
            } catch (Exception e) {
                Notification.getInstance().getLogger().error("Error while searching", e);
            }
        }
        if (bestLine.line[0] != 0) {
            try {
                board.makeMove(bestLine.line[0]); // Make best move on the board

                Settings.getInstance().getRepTable().recordRep(board.zobristKey);
                GenericMove pMove = null;
                if (Settings.getInstance().getPonder() & bestLine.line[1] != 0) {
                    pMove = new GenericMove(Move.inputNotation(bestLine.line[1]));
                }
                getProtocol().send(new ProtocolBestMoveCommand(new GenericMove(Move.inputNotation(bestLine.line[0])), pMove));
            } catch (IllegalNotationException ex) {
            }
        }
    }

    @Override
    public void receive(EngineStopCalculatingCommand command) {
        Engine.setStopSearch(true);
    }

    @Override
    public void receive(EnginePonderHitCommand command) {
        Engine.setPonder(true);
    }
}
