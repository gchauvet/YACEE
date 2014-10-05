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

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class StoneTest {

    @Test
    public void testEquals() {
        final Stone instance1 = new Stone(Piece.PAWN, BoardSide.WHITE);
        final Stone instance2 = new Stone(Piece.PAWN, BoardSide.BLACK);
        final Stone instance3 = new Stone(Piece.ROOK, BoardSide.WHITE);
        final Stone instance4 = new Stone(Piece.PAWN, BoardSide.WHITE);

        assertThat(instance1, not(equalTo(instance2)));
        assertThat(instance1, not(equalTo(instance3)));
        assertThat(instance1, equalTo(instance4));
        assertThat(instance2, not(equalTo(instance3)));
        assertThat(instance3, not(equalTo(instance1)));
    }

    @Test
    public void testHashCode() {
        final Stone instance1 = new Stone(Piece.PAWN, BoardSide.WHITE);
        final Stone instance2 = new Stone(Piece.PAWN, BoardSide.BLACK);
        final Stone instance3 = new Stone(Piece.ROOK, BoardSide.WHITE);
        final Stone instance4 = new Stone(Piece.PAWN, BoardSide.WHITE);

        assertThat(instance1.hashCode(), not(is(instance2.hashCode())));
        assertThat(instance1.hashCode(), not(is(instance3.hashCode())));
        assertThat(instance1.hashCode(), is(instance4.hashCode()));
        assertThat(instance2.hashCode(), is(not(instance3.hashCode())));
        assertThat(instance3.hashCode(), is(not(instance1.hashCode())));
    }

}
