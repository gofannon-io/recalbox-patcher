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

import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.rules.TemporaryFolder;

import java.io.*;

/**
 * Helper for managing test resources
 */
public class TestResourceHelper {

    public static final String RECALBOX_RESOURCE = "/Recalbox gamelist.xml";
    public static final String HYPERSPIN_RESOURCE = "/Hyperspin Nintendo Entertainment System.xml";


    public static File copyResourceToFile(String sourceResource, File targetFile) throws IOException {
        try(InputStream in = openResourceAsStream(sourceResource)) {
            FileUtils.copyInputStreamToFile(in, targetFile);
        }
        return targetFile;
    }


    public static File createRecalboxFile(File targetFile) throws IOException {
        return copyResourceToFile(RECALBOX_RESOURCE, targetFile );
    }

    public static InputStream openResourceAsStream(String name) {
        return TestResourceHelper.class.getResourceAsStream(name);
    }


    public static File createHyperspinFile(File targetFile) throws IOException {
        return copyResourceToFile(HYPERSPIN_RESOURCE, targetFile );
    }


    public static File createRecalboxFile(TemporaryFolder folder, String filename) throws IOException {
        return createRecalboxFile(folder.getRoot(), filename);
    }


    public static File createRecalboxFile(TemporaryFolder folder) throws IOException {
        return createRecalboxFile(folder, "recalbox.xml");
    }


    public static InputStream openRecalboxStream() throws IOException {
        return openResourceStream(RECALBOX_RESOURCE);
    }

    private static InputStream openResourceStream(String path) throws IOException {
        try(InputStream in = openResourceAsStream(path)) {

            ByteArrayOutputStream memoryOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(in, memoryOutputStream);
            return new ByteArrayInputStream(memoryOutputStream.toByteArray());

        }
    }


    public static File createRecalboxFile(File parentDir, String filename) throws IOException {
        return createRecalboxFile(new File(parentDir, filename));
    }


    public static File createHyperspinFile(TemporaryFolder folder, String filename) throws IOException {
        return createHyperspinFile(folder.getRoot(), filename);
    }


    public static File createHyperspinFile(TemporaryFolder folder) throws IOException {
        return createHyperspinFile(folder, "hyperspin.xml");
    }


    public static File createHyperspinFile(File parentDir, String filename) throws IOException {
        return createHyperspinFile(new File(parentDir, filename));
    }

    public static File addResourceToDirectory( String resourcePath, File directory ) throws IOException {
        String filename = extractResourceFilename(resourcePath);
        return copyResourceToFile( resourcePath, new File(directory,filename));
    }

    public static String extractResourceFilename(String resourcePath) {
        if (resourcePath==null)
            return null;

        int lastPathSeparator = resourcePath.lastIndexOf('/');

        if( lastPathSeparator<0)
            return resourcePath;

        if( lastPathSeparator == resourcePath.length()-1)
            return "";

        return resourcePath.substring(lastPathSeparator+1);
    }

    public static Image getImage(String resourcePath) throws Exception {
        try(InputStream in = openResourceAsStream(resourcePath); ) {
            return new Image(in);
        }
    }

}