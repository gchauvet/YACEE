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

import com.zatarox.chess.openchess.models.materials.Square.File;
import com.zatarox.chess.openchess.models.materials.Square.Rank;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SquareTest {

    @Test
    public void testGetFileIndex() {
        assertThat(Square.A1.getFileIndex(), is(File.A));
        assertThat(Square.A8.getFileIndex(), is(File.A));
        assertThat(Square.H1.getFileIndex(), is(File.H));
        assertThat(Square.H8.getFileIndex(), is(File.H));
    }

    @Test
    public void testGetRankIndex() {
        assertThat(Square.A1.getRankIndex(), is(Rank._1));
        assertThat(Square.A8.getRankIndex(), is(Rank._8));
        assertThat(Square.H1.getRankIndex(), is(Rank._1));
        assertThat(Square.H8.getRankIndex(), is(Rank._8));
    }

    @Test
    public void testFrom() {
        assertThat(Square.A1, is(Square.from(File.A, Rank._1)));
        assertThat(Square.A8, is(Square.from(File.A, Rank._8)));
        assertThat(Square.H1, is(Square.from(File.H, Rank._1)));
        assertThat(Square.H8, is(Square.from(File.H, Rank._8)));
    }
    
    @Test
    public void testFileMirroring() {
       assertThat(File.A.mirror(), is(File.H));
       assertThat(File.H.mirror(), is(File.A));
       assertThat(File.D.mirror(), is(File.E));
       assertThat(File.E.mirror(), is(File.D));
    }

}
