/*
 * Copyright 2016 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gofannon.recalboxpatcher.patcher.recalbox;

import java.util.List;

public interface RecalboxDatabase {


    List<String> getGameNamesFromPath();

    /**
     * Get all the games in the database
     * @return a non <code>null</code> list of games
     */
    List<RecalboxGame> getAllGames();

    /**
     * Add a game to the database
     *
     * @param game a non <code>null</code> game
     * @throws NullPointerException if the game argument is <code>null</code>
     */
    void addGame(RecalboxGame game) throws NullPointerException;
}
