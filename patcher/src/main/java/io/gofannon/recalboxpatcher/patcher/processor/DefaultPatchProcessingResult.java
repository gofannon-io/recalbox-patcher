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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class DefaultPatchProcessingResult implements PatchProcessingResult, OperationLogger {

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

    @Override
    public List<String> getPatchedGames() {
        return patchedRecalGames.stream().sorted().collect(toList());
    }

    @Override
    public List<String> getNotPatchedGames() {
        return notPatchedGames.stream().sorted().collect(toList());
    }

    @Override
    public boolean hasNotPatchedGame() {
        return notPatchedGames.isEmpty() == false;
    }

    @Override
    public int getPatchedGameCount() {
        return patchedRecalGames.size();
    }

    @Override
    public int getNotPatchedGameCount() {
        return notPatchedGames.size();
    }

    @Override
    public int getTotalGameCount() {
        return patchedRecalGames.size() + notPatchedGames.size();
    }
}