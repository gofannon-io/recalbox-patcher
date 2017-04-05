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

package org.recalbox.confpatcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.rules.TemporaryFolder;

import java.io.*;

/**
 * Helper for managing test resources
 */
public class TestResourceHelper {

    public static void createRecalboxFile(File targetFile) throws IOException {
        try(InputStream in = openResource("/Recalbox gamelist.xml")) {
            FileUtils.copyInputStreamToFile(in, targetFile);
        }
    }

    private static InputStream openResource(String name) {
        return TestResourceHelper.class.getResourceAsStream(name);
    }

    public static void createHyperspinFile(File targetFile) throws IOException {
        try(InputStream in = openResource("/Hyperspin Nintendo Entertainment System.xml")) {
            FileUtils.copyInputStreamToFile(in, targetFile);
        }
    }

    public static File createRecalboxFile(TemporaryFolder folder, String filename) throws IOException {
        return createRecalboxFile(folder.getRoot(), filename);
    }

    public static File createRecalboxFile(TemporaryFolder folder) throws IOException {
        return createRecalboxFile(folder, "recalbox.xml");
    }

    public static InputStream openRecalboxStream() throws IOException {
        return openResourceStream("/Recalbox gamelist.xml");
    }

    private static InputStream openResourceStream(String path) throws IOException {
        try(InputStream in = openResource(path)) {

            ByteArrayOutputStream memoryOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(in, memoryOutputStream);
            return new ByteArrayInputStream(memoryOutputStream.toByteArray());

        }
    }



    public static File createRecalboxFile(File parentDir, String filename) throws IOException {
        File tempFile = new File(parentDir, filename);
        createRecalboxFile(tempFile);
        return tempFile;
    }

    public static File createHyperspinFile(TemporaryFolder folder, String filename) throws IOException {
        return createHyperspinFile(folder.getRoot(), filename);
    }

    public static File createHyperspinFile(TemporaryFolder folder) throws IOException {
        return createHyperspinFile(folder, "hyperspin.xml");
    }


    public static File createHyperspinFile(File parentDir, String filename) throws IOException {
        File tempFile = new File(parentDir, filename);
        createHyperspinFile(tempFile);
        return tempFile;
    }

}