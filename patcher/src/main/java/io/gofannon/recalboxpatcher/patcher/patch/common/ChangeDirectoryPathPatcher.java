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

package io.gofannon.recalboxpatcher.patcher.patch.common;

import org.apache.commons.io.FilenameUtils;

import static org.apache.commons.lang3.Validate.*;



/**
 * Description of the context for patching a game
 */
public class ChangeDirectoryPathPatcher implements PathPatcher {

    private String directoryPath ="";

    public ChangeDirectoryPathPatcher() {}

    public ChangeDirectoryPathPatcher(String path) {
        setDirectoryPath(path);
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public final void setDirectoryPath(String directoryPath) {
        notNull(directoryPath, "directoryPath argument shall not be null");
        this.directoryPath = directoryPath;
    }

    @Override
    public String patch(String path) {
        if( path == null || path.isEmpty())
            return path;

        String filename = FilenameUtils.getName(path);
        return FilenameUtils.concat(directoryPath, filename);
    }
}
