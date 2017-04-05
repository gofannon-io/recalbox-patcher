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

public class PatchProcessingResult implements OperationLogger {

    private Set<String> patchedRecalGames = new HashSet<>();
    private Set<String> notPatchedGames = new HashSet<>();

    @Override
    public void logGamePatched(String name) {
        this.patchedRecalGames.add(name);
    }

    @Override
    public void logGameNotFoundInHypersinDatabase(String name) {
        this.notPatchedGames.add(name);
    }

    public List<String> getPatchedRecalGame() {
        return patchedRecalGames.stream().sorted().collect(toList());
    }

    public List<String> getNotPatchedGames() {
        return notPatchedGames.stream().sorted().collect(toList());
    }

    public boolean hasNotFixedGame() {
        return notPatchedGames.isEmpty() == false;
    }

    public int getPatchedGameCount() {
        return patchedRecalGames.size();
    }

    public int getNotPatchedGameCount() {
        return notPatchedGames.size();
    }

    public int getTotalGameCount() {
        return patchedRecalGames.size() + notPatchedGames.size();
    }
}