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

public enum Square {

    A1, B1, C1, D1, E1, F1, G1, H1,
    //
    A2, B2, C2, D2, E2, F2, G2, H2,
    //
    A3, B3, C3, D3, E3, F3, G3, H3,
    //
    A4, B4, C4, D4, E4, F4, G4, H4,
    //
    A5, B5, C5, D5, E5, F5, G5, H5,
    //
    A6, B6, C6, D6, E6, F6, G6, H6,
    //
    A7, B7, C7, D7, E7, F7, G7, H7,
    //
    A8, B8, C8, D8, E8, F8, G8, H8;

    public enum File {

        A, B, C, D, E, F, G, H;

        public File mirror() {
            return values()[values().length - ordinal() - 1];
        }
    }

    public enum Rank {

        _1, _2, _3, _4, _5, _6, _7, _8;

        public static Rank[] reverse() {
            Rank[] result = new Rank[Rank.values().length];
            for (int i = 0; i < values().length; i++) {
                result[i] = values()[values().length - i - 1];
            }
            return result;
        }
    }

    public File getFileIndex() {
        return File.values()[this.ordinal() % 8];
    }

    public Rank getRankIndex() {
        return Rank.values()[this.ordinal() / 8];
    }

    public static Square from(File file, Rank rank) {
        return values()[8 * rank.ordinal() + file.ordinal()];
    }
    
    public long toLong() {
        return 1L << this.ordinal();
    }

}
