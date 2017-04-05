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

import java.io.File;
import java.util.ResourceBundle;

/**
 * Simple command line application for patching a recalbox file
 */
public class PatcherCLIApplication {

    private static ResourceBundle messages = ResourceBundle.getBundle("CLIMessages");

    public static void main(String... args ) {
        if( args.length != 3) {
            System.err.println("Invalid argument count");
            printUsage();
            System.exit(-1);
        }

        File sourceRecalboxFile = new File(args[0]);
        checkReadableFile( sourceRecalboxFile, "<original recalbox file>");

        File hyperspinFile = new File(args[1]);
        checkReadableFile( hyperspinFile, "<hyperspin file>");

        File fixedRecalboxFile = new File(args[2]);
        checkGenerableFile( fixedRecalboxFile, "<target recalbox file>");

        FileResourceProvider provider = new FileResourceProvider();
        provider.setRecalboxDatabaseSourceFile(sourceRecalboxFile);
        provider.setHyperspinDatabaseFile(hyperspinFile);
        provider.setRecalboxDatabaseTargetFile(fixedRecalboxFile);

        PatchProcessor processor = new PatchProcessor();
        processor.setResourceProvider(provider);
        PatchProcessingResult result =processor.process();

        printResult(result);
    }

    private static void printUsage() {
        System.out.println(messages.getString("usage"));
    }


    private static void checkReadableFile(File file, String documentedName) {
        if( file.exists()==false) {
            System.err.println("The "+documentedName+" does not exists");
            System.exit(-2);
        }
        if( file.isFile()==false) {
            System.err.println("The "+documentedName+" is not a file");
            System.exit(-3);
        }
        if( file.canRead()==false) {
            System.err.println("The "+documentedName+" is not readable");
            System.exit(-4);
        }
    }

    private static void checkGenerableFile(File file, String documentedName) {
        if( file.isFile()==false) {
            System.err.println("The "+documentedName+" is not a file");
            System.exit(-3);
        }
    }

    private static void printResult(PatchProcessingResult result) {
        System.out.println("Fixed game count: "+ result.getPatchedGameCount());
        System.out.println("Not fixed game count: "+ result.getNotPatchedGameCount());
        float percent = (float)(100* result.getPatchedGameCount()) / (float) result.getTotalGameCount();
        System.out.println(String.format("%% of success : %.02f", percent));


        if( result.hasNotFixedGame()) {
            System.out.println("Cannot found the following games in Hyperspin database:");
            result.getNotPatchedGames().stream().forEach(n -> System.out.println(n));
            System.out.println("-------------------------");
        }
    }

}