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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.*;
import static io.gofannon.recalboxpatcher.patcher.TestResourceHelper.*;


public class PatcherCLIApplicationTest {

    PatcherCLIApplication application;
    IOApplicationHandler applicationHandler;
    ByteArrayOutputStream binaryOutput;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    File hyperspinFile;
    File recalboxFile;
    File fixedFile;

    @Before
    public void startUp() throws Exception {
        hyperspinFile = createHyperspinFile(folder);
        recalboxFile = createRecalboxFile(folder);
        fixedFile = new File( folder.getRoot(), "fixed-recalbox-file.xml");


        application = new PatcherCLIApplication();

        binaryOutput = new ByteArrayOutputStream();

        PrintStream printer = new PrintStream(binaryOutput);
        applicationHandler = new IOApplicationHandler(System.out);
    }

//    sourceRecalboxFile = new File(args[0]);
//    hyperspinFile = new File(args[1]);
//    fixedRecalboxFile = new File(args[2]);
//    targetImageDirectory = new File(args[3]);


    @Test
    public void run() throws Exception {
        PatcherCLIApplication.run( applicationHandler,
                recalboxFile.getAbsolutePath(),
                hyperspinFile.getAbsolutePath(),
                fixedFile.getAbsolutePath(),
                "/sample-img"
                );


        System.out.println("Exit status = "+applicationHandler.getExistStatus());

        assertThat(fixedFile).exists();
        assertThat(fixedFile.length()).isGreaterThan(0);
        assertThat(applicationHandler.getExistStatus()).isEqualTo(0);
    }

    @Test(expected = NullPointerException.class)
    public void run_shallNotAcceptNullApplicationHandler() throws Exception {
        PatcherCLIApplication.run(null,
                recalboxFile.getAbsolutePath(),
                hyperspinFile.getAbsolutePath(),
                fixedFile.getAbsolutePath(),
                "/sample-img"
        );
    }

}