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
package com.zatarox.chess.openchess.controllers.engines;

import com.zatarox.chess.openchess.models.moves.Move;
import java.io.Serializable;

public final class SolveResult implements Serializable {

    private final Move best;
    private final Move refutation;

    public SolveResult(Move best, Move refutation) {
        this.best = best;
        this.refutation = refutation;
    }

    public Move getBest() {
        return best;
    }

    public Move getRefutation() {
        return refutation;
    }

}
