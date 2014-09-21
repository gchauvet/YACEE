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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.zatarox.chess.skychess.board.Board;
import com.zatarox.chess.skychess.board.Evaluation;

import org.junit.Before;
import org.junit.Test;

public class EvaluationSpeedTest {

    private List<String> positions;
    private List<Board> setupBoards;

    @Before
    public void setUp() throws Exception {
        positions = new LinkedList<>();
        setupBoards = new LinkedList<>();

        URL url = this.getClass().getResource("evaltestpositions");
        File testSetFile = new File(url.getFile());
        FileInputStream fstream = new FileInputStream(testSetFile);
        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            positions.add(strLine);
            Board board = new Board();
            board.inputFen(strLine);
            setupBoards.add(board);
        }
        in.close();
    }

    @Test
    public void testEvalSpeed() {
        for (int i = 0; i < 50000; i++) {
            for (Board b : setupBoards) {
                Evaluation.evaluate(b, false);
            }
        }
    }
}
