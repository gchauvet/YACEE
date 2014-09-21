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

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.zatarox.chess.skychess.board.Board;
import com.zatarox.chess.skychess.engine.Engine;
import com.zatarox.chess.skychess.engine.Perft;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeedTest {

    private static Logger logger = LoggerFactory.getLogger(SpeedTest.class);

    private final static int DEPTH = 8;
    private List<String> positionsMiddle;
    private List<String> positionsEnding;

    @Before
    public void setUp() throws Exception {
        loadMiddle();
        loadEnding();
    }

    private void loadMiddle() throws Exception {
        positionsMiddle = new LinkedList<>();
        URL url = this.getClass().getResource("speedtestmiddle");
        File testSetFile = new File(url.getFile());
        FileInputStream fstream = new FileInputStream(testSetFile);
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            positionsMiddle.add(strLine);
        }
        in.close();
    }

    private void loadEnding() throws Exception {
        positionsEnding = new LinkedList<>();
        URL url = this.getClass().getResource("speedtestending");
        File testSetFile = new File(url.getFile());
        FileInputStream fstream = new FileInputStream(testSetFile);
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            positionsEnding.add(strLine);
        }
        in.close();
    }

    @Test
    public void speedTestMiddle() throws IOException {
        Board board = new Board();

        long totalTime = System.currentTimeMillis();
        int count = 0;
        for (String pos : positionsMiddle) {
            Settings.getInstance().getTranspositionTable().clear();
            Settings.getInstance().getPawnHash().clear();
            Settings.getInstance().getEvalHash().clear();

            board.inputFen(pos);
            long thisTime = System.currentTimeMillis();
            Engine.search(board, DEPTH, 0, 0, 0, false);
            logger.debug("Position " + (++count) + "/" + positionsEnding.size() + " Time: " + Perft.convertMillis((System.currentTimeMillis() - thisTime)) + " Total time: " + Perft.convertMillis((System.currentTimeMillis() - totalTime)));
        }

        logger.debug("Time: " + Perft.convertMillis((System.currentTimeMillis() - totalTime)));
    }

    @Test
    public void speedTestEnding() throws IOException {
        Board board = new Board();

        long totalTime = System.currentTimeMillis();
        int count = 0;
        for (String pos : positionsEnding) {
            Settings.getInstance().getTranspositionTable().clear();
            Settings.getInstance().getPawnHash().clear();
            Settings.getInstance().getEvalHash().clear();

            board.inputFen(pos);
            long thisTime = System.currentTimeMillis();
            Engine.search(board, DEPTH, 0, 0, 0, false);
            logger.debug("Position " + (++count) + "/" + positionsEnding.size() + " Time: " + Perft.convertMillis((System.currentTimeMillis() - thisTime)) + " Total time: " + Perft.convertMillis((System.currentTimeMillis() - totalTime)));
        }

        logger.debug("Time: " + Perft.convertMillis((System.currentTimeMillis() - totalTime)));
    }
}
