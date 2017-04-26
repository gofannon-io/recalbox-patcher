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

package io.gofannon.recalboxpatcher.patcher;

import io.gofannon.recalboxpatcher.patcher.processor.PatchProcessingResult;

/**
 * An {@link ApplicationHandler} that does nothing
 */
public class NullApplicationHandler implements ApplicationHandler {

    @Override
    public void argumentFileNotExists(String documentedName) {
    }

    @Override
    public void argumentFileNotAFile(String documentedName) {

    }

    @Override
    public void argumentFileNotReadable(String documentedName) {

    }

    @Override
    public void argumentDirectoryNotExist(String documentedName) {

    }

    @Override
    public void argumentDirectoryNotADirectory(String documentedName) {

    }

    @Override
    public void argumentDirectoryNotWritable(String documentedName) {

    }

    @Override
    public void argumentGenerableFileNotExists(String documentedName) {

    }

    @Override
    public void argumentGenerableFileParentDirectoryNotExist(String documentedName) {

    }

    @Override
    public void argumentGenerableFileParentDirectoryNotWritable(String documentedName) {

    }

    @Override
    public void invalidArgumentCount() {

    }

    @Override
    public void displayResult(PatchProcessingResult result) {

    }

    @Override
    public void exitApplicationOnFailure(ApplicationInterruptedException ex) {

    }

    @Override
    public void exitApplication() {

    }
}
