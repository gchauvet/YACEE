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
package com.zatarox.chess.skychess;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.zatarox.chess.skychess.engine.Board;
import com.zatarox.chess.skychess.engine.Board.Side;
import com.zatarox.chess.skychess.engine.ChessEngine;
import com.zatarox.chess.skychess.engine.DefaultChessEngine;
import com.zatarox.chess.skychess.tables.RepetitionTable;
import com.zatarox.chess.skychess.tables.TranspositionTable;
import java.io.IOException;
import java.util.logging.Level;
import chesspresso.move.Move;

/**
 * This is the main class of SkyChess which is used to connect to a client
 * chessboard program througth UCI protocol.
 */
public class MainApp extends AbstractEngine {

    private final Board game = new Board();
    private final ChessEngine engine = new DefaultChessEngine();

    public static void main(String args[]) throws IOException {
        MainApp main = new MainApp();
        main.run();
    }

    @Override
    protected void quit() {

    }

    @Override
    public void receive(EngineInitializeRequestCommand command) {
        engine.setBoard(game);
        ProtocolInitializeAnswerCommand request = new ProtocolInitializeAnswerCommand("SkyChess", "Guillaume Chauvet");
        //request.addOption(new SpinnerOption(Settings.OPTION_HASH, Settings.DEFAULT_HASH_SIZE, 1, 512));
        getProtocol().send(request);
    }

    @Override
    public void receive(EngineSetOptionCommand command) {
        /*switch (command.name) {
         case Settings.OPTION_HASH:
         Settings.getInstance().setTranspositionTableSize(Integer.valueOf(command.value));
         break;
         }*/
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
        game.reset();
        RepetitionTable.getInstance().clear();
        TranspositionTable.getInstance().clear();
    }

    @Override
    public void receive(EngineAnalyzeCommand command) {
        game.reset(command.board.toString());
        game.play(command.moves);
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
        if (game.getPlayer() == Side.WHITE) {
            engineTime = wtime;
            engineInc = winc;
        } else {
            engineTime = btime;
            engineInc = binc;
        }

        try {
            engine.setDepth((short) searchDepth);
            engine.setEngineTime(engineTime);
            engine.setEngineIncrement(engineInc);
            engine.setMoveTime(movetime);
            engine.setPonder(ponder);

            Short best = engine.call();
            game.play(best); // Make best move on the board
            RepetitionTable.getInstance().recordRep(game);
            getProtocol().send(new ProtocolBestMoveCommand(new GenericMove(Move.getString(best)), null));
        } catch (IllegalNotationException e) {
            Notification.getInstance().getLogger().log(Level.SEVERE, "Error while searching", e);
        }
    }

    @Override
    public void receive(EngineStopCalculatingCommand command) {
        engine.stop();
    }

    @Override
    public void receive(EnginePonderHitCommand command) {
        engine.setPonder(true);
    }
}
