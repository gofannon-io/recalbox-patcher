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

public interface ApplicationHandler {
    void argumentFileNotExists(String documentedName);

    void argumentFileNotAFile(String documentedName);

    void argumentFileNotReadable(String documentedName);

    void argumentDirectoryNotExist(String documentedName);

    void argumentDirectoryNotADirectory(String documentedName);

    void argumentDirectoryNotWritable(String documentedName);

    void argumentGenerableFileNotExists(String documentedName);

    void argumentGenerableFileParentDirectoryNotExist(String documentedName);

    void argumentGenerableFileParentDirectoryNotWritable(String documentedName);

    void invalidArgumentCount();

    void displayResult(PatchProcessingResult result);

    void exitApplicationOnFailure(ApplicationInterruptedException ex);

    void exitApplication();
}
