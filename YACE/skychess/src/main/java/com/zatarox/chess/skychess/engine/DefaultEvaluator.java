/*
 * Copyright 2014 Guillaume.
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

public class DefaultEvaluator implements Evaluator {

    @Override
    public double evaluate(Board board) {
        double result;
        if (board.game.isStaleMate()) {
            result = Double.NEGATIVE_INFINITY;
        } else {
            result = board.game.getDomination() + board.game.getMaterial();
        }
        return result;
    }
}
