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

import io.gofannon.recalboxpatcher.patcher.processor.validator.GameDatabaseProcessorValidator;
import io.gofannon.recalboxpatcher.patcher.processor.validator.GameDatabaseValidationReport;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.assertj.core.api.Assertions.assertThat;


public class GameDatabasePatchProcessorIntegTest {

    @Rule
    public TemporaryFolder rootFolder = new TemporaryFolder();


    @Test
    public void validateTest1() throws Exception {
        GameDatabaseProcessorValidator validator = new GameDatabaseProcessorValidator();

        String uri = "/"+getClass().getPackage().getName().replace(".","/") +"/validator/test1";
        GameDatabaseValidationReport report = validator.validateFromResourceDirectory(rootFolder.newFolder(), uri);

        assertThat(report.isValidationSucceeded());
    }
}