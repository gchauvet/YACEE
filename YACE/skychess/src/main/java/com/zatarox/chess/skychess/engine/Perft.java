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
package com.zatarox.chess.skychess.engine;

import chesspresso.move.IllegalMoveException;
import chesspresso.move.Move;
import chesspresso.position.Position;
import com.zatarox.chess.skychess.Notification;
import java.util.LinkedList;
import java.util.List;

/**
 * This class runs has utility methods for perft tests
 */
public class Perft {

    /**
     * Start the perft search
     *
     * @param board
     * The board to search
     * @param depth
     * The depth to search to
     * @param divide
     * Should we divide the first moves or just return the total
     * value
     * @return number of nodes
     */
    public static long perft(Position board, int depth, boolean divide) {
        long nNodes;
        long zobrist = board.getHashCode();

        if (divide) {
            nNodes = divide(board, depth);
        } else {
            nNodes = miniMax(board, depth);
        }

        if (zobrist != board.getHashCode()) {
            Notification.getInstance().getLogger().error("Error in zobrist update!");
        }

        return nNodes;

    }

    /**
     * Keeps track of every starting move and its number of child moves, and
     * then prints it on the screen.
     *
     * @param board
     * The position to search
     * @param depth
     * The depth to search to
     */
    private static long divide(Position board, int depth) {
        List<Long> children = new LinkedList<>();
        final short[] moves = board.getAllMoves();
        for (final short move : moves) {
            try {
                board.doMove(move);
                children.add(miniMax(board, depth - 1));
                board.undoMove();
            } catch (IllegalMoveException ex) {
            }
        }

        long nodes = 0;
        for (int i = 0; i < children.size(); i++) {
            System.out.print(Move.getString(moves[i]) + " ");
            System.out.println(children.get(i));
            nodes += children.get(i);
        }

        System.out.println("Moves: " + children.size());
        return nodes;
    }

    /**
     * Generates every move from the position on board and returns the total
     * number of moves found to the depth
     *
     * @param board
     * The board used
     * @param depth
     * The depth currently at
     * @return int The number of moves found
     */
    private static long miniMax(Position board, int depth) {
        long nodes = 0;

        if (depth == 0) {
            return 1;
        }

        for (final short move : board.getAllMoves()) {
            try {
                board.doMove(move);
                nodes += miniMax(board, depth - 1);
                board.undoMove();
            } catch (IllegalMoveException ex) {
            } catch(RuntimeException ex) {
                System.err.println(board);
            }
        }

        return nodes;
    }

    /**
     * Takes number and converts it to minutes, seconds and fraction of a second
     * also includes leading zeros
     *
     * @param millis
     * the Milliseconds to convert
     * @return String the conversion
     */
    public static String convertMillis(long millis) {
        long minutes = millis / 60000;
        long seconds = (millis % 60000) / 1000;
        long fracSec = (millis % 60000) % 1000;

        String timeString = "";

        // Add minutes to the string, if no minutes this part will not add to
        // the string
        if (minutes < 10 && minutes != 0) {
            timeString += "0" + Long.toString(minutes) + ":";
        } else if (minutes >= 10) {
            timeString += Long.toString(minutes) + ":";
        }

        // Add seconds to the string
        if (seconds == 0) {
            timeString += "0";
        } else if (minutes != 0 && seconds < 10) {
            timeString += "0" + Long.toString(seconds);
        } else if (seconds < 10) {
            timeString += Long.toString(seconds);
        } else {
            timeString += Long.toString(seconds);
        }

        timeString += ".";

        // Add fractions of a second to the string
        if (fracSec == 0) {
            timeString += "000";
        } else if (fracSec < 10) {
            timeString += "00" + Long.toString(fracSec);
        } else if (fracSec < 100) {
            timeString += "0" + Long.toString(fracSec);
        } else {
            timeString += Long.toString(fracSec);
        }

        return timeString;
    }
}
