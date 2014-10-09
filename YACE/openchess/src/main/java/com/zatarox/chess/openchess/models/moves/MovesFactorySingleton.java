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
package com.zatarox.chess.openchess.models.moves;

import com.zatarox.chess.openchess.models.materials.*;

public final class MovesFactorySingleton {

    private static final MovesFactorySingleton INSTANCE = new MovesFactorySingleton();

    private MovesFactorySingleton() {
    }

    public Move createNormal(Square from, Square to) {
        return new BasicMove(from, to);
    }
    
    public Move createCharge(Square from, Square to) {
        return new ChargeMove(from, to);
    }

    public Move createCapture(Square from, Square to, Stone captured) {
        return new CaptureMove(from, to, captured);
    }

    public Move createEnpassant(Square from, Square to) {
        return new EnPassantMove(from, to);
    }

    public Move createPromotion(Square from, Square to, Piece promotion) {
        return new PromotionMove(from, to, promotion);
    }

    public Move createCastle(Castle castle, BoardSide trait) {
        return new CastleMove(castle, trait);
    }

    public static MovesFactorySingleton getInstance() {
        return INSTANCE;
    }

}
