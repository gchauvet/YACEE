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
package com.zatarox.chess.openchess.models.materials;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public final class BitBoardTest {

    @Test
    public void testGetSize() {
        BitBoard bitboard = new BitBoard();
        bitboard.set(EnumSet.allOf(Square.class));
        assertThat(bitboard.getSize(), is(64));
        bitboard.clear();
        assertThat(bitboard.getSize(), is(0));
    }

    @Test
    public void testOccuped() {
        BitBoard bitboard = new BitBoard();
        bitboard.set(Square.A1);
        assertThat(bitboard.isOccuped(Square.A1), is(true));
        assertThat(bitboard.isOccuped(Square.A2), is(false));
        bitboard.unset(Square.A1);
        assertThat(bitboard.isOccuped(Square.A1), is(false));
    }

    @Test(timeout = 1000)
    public void testIterator() {
        final List<Square> squares = Arrays.asList(Square.A1, Square.C7, Square.H8, Square.H1, Square.A8);
        BitBoard bitboard = new BitBoard();
        bitboard.set(new HashSet<>(squares));
        assertThat(bitboard.getSize(), is(squares.size()));
        for (Square square : bitboard) {
            assertThat(bitboard.isOccuped(square), is(true));
            assertThat(squares.contains(square), is(true));
        }
    }

    @Test
    public void testEquals() {
        final BitBoard b1 = new BitBoard();
        b1.set(Square.B6);
        final BitBoard b2 = new BitBoard();
        b2.set(Square.G2);
        final BitBoard b3 = new BitBoard();
        b3.set(Square.B6);
        assertThat(b1, not(equalTo(b2)));
        assertThat(b1, equalTo(b3));
        assertThat(b2, not(equalTo(b3)));
    }
    
    @Test
    public void testMerge() {
        final BitBoard b1 = new BitBoard();
        b1.set(Square.A1);
        final BitBoard b2 = new BitBoard();
        b1.set(Square.H1);
        final BitBoard a = new BitBoard(b1);
        a.merge(b2);
        assertThat(a.getSize(), is(2));
        assertTrue(a.isOccuped(Square.A1));
        assertTrue(a.isOccuped(Square.H1));
    }
}
