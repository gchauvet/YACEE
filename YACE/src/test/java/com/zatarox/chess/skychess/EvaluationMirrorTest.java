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

import static org.junit.Assert.*;

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

public class EvaluationMirrorTest {

    private List<String> positions;

    @Before
    public void setUp() throws Exception {
        positions = new LinkedList<>();

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
        }
        in.close();
    }

    @Test
    public void testEvalMirror() {
        Board board = new Board();
        for (String pos : positions) {
            board.inputFen(pos);
            assertTrue(evalMirror(board));
        }
    }

    private boolean evalMirror(Board board) {
        String originalFen = removeCastling(board.getFen());
        int normal = Evaluation.evaluate(board, false);
        board.inputFen(mirror(originalFen));
        int mirror = Evaluation.evaluate(board, false);
        board.inputFen(reflect(originalFen));
        int reflect = Evaluation.evaluate(board, false);
        board.inputFen(mirror(reflect(originalFen)));
        int reflectMirror = Evaluation.evaluate(board, false);

        boolean positionCorrect = true;
        if (!(normal == mirror && reflect == reflectMirror && normal == reflect)) {
            positionCorrect = false;
            System.err.println("## FAIL ##");
            System.err.println("\nNormal: " + normal);
            System.err.println("Normal fen: " + originalFen);
            board.inputFen(originalFen);
            Evaluation.printEval(board);
            System.err.println("\nMirrored: " + mirror);
            System.err.println("Mirrored fen: " + mirror(originalFen));
            board.inputFen(mirror(originalFen));
            Evaluation.printEval(board);
            System.err.println("\nReflected: " + reflect);
            System.err.println("Reflected fen: " + reflect(originalFen));
            board.inputFen(reflect(originalFen));
            Evaluation.printEval(board);
            System.err.println("\nMirror+reflected: " + reflectMirror);
            System.err.println("Mirror+reflected fen: " + reflect(mirror(originalFen)));
            board.inputFen(mirror(reflect(originalFen)));
            Evaluation.printEval(board);
        }

        board.inputFen(originalFen);

        return positionCorrect;
    }

    private String removeCastling(String s) {
        String[] allParts = s.split(" ");
        StringBuilder newString = new StringBuilder();

        for (int i = 0; i < allParts.length; i++) {
            if (i == 2) {
                newString.append("- ");
            } else {
                newString.append(allParts[i] + " ");
            }
        }

        return newString.toString().trim();
    }

    private String mirror(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        String posPart = s.split(" ")[0];
        StringBuilder restB = new StringBuilder();
        for (int i = 1; i < s.split(" ").length; i++) {
            restB.append(s.split(" ")[i] + " ");
        }

        String[] ranks = posPart.split("/");
        if (ranks.length != 8) {
            return null;
        }

        for (int rank = 0; rank < 8; rank++) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < ranks[rank].length(); i++) {
                buffer.append(ranks[rank].charAt(ranks[rank].length() - i - 1));
            }
            ranks[rank] = buffer.toString();
        }

        StringBuilder newString = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            newString.append(ranks[i]);
            newString.append("/");
        }
        newString.append(ranks[7]);

        return (newString.toString() + " " + restB).trim();
    }

    private String reflect(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }

        String posPart = s.split(" ")[0];
        StringBuilder restB = new StringBuilder();
        for (int i = 1; i < s.split(" ").length; i++) {
            if (i == 1) {
                if (s.split(" ")[i].equals("w")) {
                    restB.append("b ");
                } else {
                    restB.append("w ");
                }
            } else {
                restB.append(s.split(" ")[i] + " ");
            }
        }

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < posPart.length(); i++) {
            char ch = posPart.charAt(i);
            if (Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                ch = Character.toUpperCase(ch);
            }
            buffer.append(ch);
        }
        posPart = buffer.toString();

        String[] ranks = posPart.split("/");
        if (ranks.length != 8) {
            return null;
        }
        String[] newRanks = new String[8];
        for (int i = 0; i < 8; i++) {
            newRanks[i] = ranks[7 - i];
        }

        StringBuilder newString = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            newString.append(newRanks[i]);
            newString.append("/");
        }
        newString.append(newRanks[7]);

        return (newString.toString() + " " + restB).trim();

    }

}
