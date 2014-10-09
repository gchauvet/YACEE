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

public class ZobristHashStrategyTest {
    
    @Test
    public void testAdd() {
        final HashStrategy instance = new ZobristHashStrategy();
        assertEquals(0L, instance.hashCode64());
        instance.add(new Stone(Piece.PAWN, BoardSide.WHITE), Square.A6);
        assertThat(instance.hashCode64(), is(not(0L)));
    }

    @Test
    public void testRemove() {
        final HashStrategy instance = new ZobristHashStrategy();
        final Stone stone = new Stone(Piece.PAWN, BoardSide.WHITE);
        final Square square = Square.A6;
        assertEquals(0L, instance.hashCode64());
        instance.add(stone, square);
        instance.remove(stone, square);
        assertEquals(0L, instance.hashCode64());
    }
   
}
