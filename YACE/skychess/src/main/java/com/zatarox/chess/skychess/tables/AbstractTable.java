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
package com.zatarox.chess.skychess.tables;

import java.util.Map;

abstract class AbstractTable<K, G> {

    private final Map<K, G> table;

    public AbstractTable(Map<K, G> table) {
        this.table = table;
    }

    final protected Map<K, G> getTable() {
        return table;
    }

    final public void clear() {
        table.clear();
    }

}
