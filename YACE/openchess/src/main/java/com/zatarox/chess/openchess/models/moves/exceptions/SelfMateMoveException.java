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
package com.zatarox.chess.openchess.models.moves.exceptions;

/**
 * Exception for illegal move when auto-mate is played
 */
public final class SelfMateMoveException extends Exception {

    public SelfMateMoveException(Throwable thrwbl) {
        super(thrwbl);
    }

    public SelfMateMoveException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public SelfMateMoveException(String string) {
        super(string);
    }

}