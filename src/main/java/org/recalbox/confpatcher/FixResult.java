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
package org.recalbox.confpatcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.*;

public class FixResult implements FixOperationLogger {

    private Set<String> fixedRecalGames = new HashSet<>();
    private Set<String> notFixedGames = new HashSet<>();

    @Override
    public void logGameFixed(String name) {
        this.fixedRecalGames.add(name);
    }

    @Override
    public void logGameNotFoundInHypersinDatabase(String name) {
        this.notFixedGames.add(name);
    }

    public List<String> getFixedRecalGame() {
        return fixedRecalGames.stream().sorted().collect(toList());
    }

    public List<String> getNotFixedGames() {
        return notFixedGames.stream().sorted().collect(toList());
    }

    public boolean hasNotFixedGame() {
        return notFixedGames.isEmpty() == false;
    }

    public int getFixedGameCount() {
        return fixedRecalGames.size();
    }

    public int getNotFixedGameCount() {
        return notFixedGames.size();
    }

    public int getTotalGameCount() {
        return fixedRecalGames.size() + notFixedGames.size();
    }
}