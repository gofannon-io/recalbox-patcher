/*
 * Copyright 2017  the original author or authors.
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
 *  limitations under the License.
 */
package io.gofannon.recalboxpatcher.patcher.processor;

/**
 * Logger for collecting application operations.
 */
public interface OperationLogger {

    /**
     * A game has been fixed
     * @param name the name of the game, cannot be <code>null</code> nor empty
     */
    void logGamePatched(String name);

    /**
     * A game is not found in the Hypersin database
     * @param name the name of the game, cannot be <code>null</code> nor empty
     */
    void logGameNotFoundInHypersinDatabase(String name);

}
