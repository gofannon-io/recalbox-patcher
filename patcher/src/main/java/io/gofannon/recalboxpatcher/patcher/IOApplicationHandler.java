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

import java.io.PrintStream;
import java.util.ResourceBundle;

/**
 * An {@link ApplicationHandler} that writes message in streams.
 */
public class IOApplicationHandler implements ApplicationHandler {

    private static final ResourceBundle messages = ResourceBundle.getBundle("io.gofannon.recalboxpatcher.patcher.CLIMessages");

    private PrintStream out;
    private PrintStream err;
    private int existStatus;

    public IOApplicationHandler(PrintStream stream) {
        this.out = stream;
        this.err = stream;
    }

    public IOApplicationHandler(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    @Override
    public void argumentFileNotExists(String documentedName) {
        err.println("The " + documentedName + " does not exists");
        throw new ApplicationInterruptedException(-2);
    }

    @Override
    public void argumentFileNotAFile(String documentedName) {
        err.println("The " + documentedName + " is not a file");
        throw new ApplicationInterruptedException(-3);
    }

    @Override
    public void argumentFileNotReadable(String documentedName) {
        err.println("The " + documentedName + " is not readable");
        throw new ApplicationInterruptedException(-4);
    }

    @Override
    public void argumentDirectoryNotExist(String documentedName) {
        err.println("The " + documentedName + " does not exists");
        throw new ApplicationInterruptedException(-10);
    }

    @Override
    public void argumentDirectoryNotADirectory(String documentedName) {
        err.println("The " + documentedName + " is not a directory");
        throw new ApplicationInterruptedException(-5);
    }

    @Override
    public void argumentDirectoryNotWritable(String documentedName) {
        err.println("The " + documentedName + " is not writable");
        throw new ApplicationInterruptedException(-6);
    }

    @Override
    public void argumentGenerableFileNotExists(String documentedName) {
        err.println("The generated file " + documentedName + " already exists");
        throw new ApplicationInterruptedException(-7);
    }

    @Override
    public void argumentGenerableFileParentDirectoryNotExist(String documentedName) {
        err.println("The base directory of " + documentedName + " does not exists");
        throw new ApplicationInterruptedException(-8);
    }

    @Override
    public void argumentGenerableFileParentDirectoryNotWritable(String documentedName) {
        err.println("The base directory of " + documentedName + " is not writable");
        throw new ApplicationInterruptedException(-9);
    }

    @Override
    public void invalidArgumentCount() {
        err.println("Invalid argument count");
        printUsage();
        throw new ApplicationInterruptedException(-1);
    }

    private void printUsage() {
        out.println(messages.getString("usage"));
    }


    @Override
    public void displayResult(PatchProcessingResult result) {
        out.println("Fixed game count: " + result.getPatchedGameCount());
        out.println("Not fixed game count: " + result.getNotPatchedGameCount());
        float percent = (float) (100 * result.getPatchedGameCount()) / (float) result.getTotalGameCount();
        out.println(String.format("%% of success : %.02f", percent));


        if (result.hasNotPatchedGame()) {
            out.println("Cannot found the following games in Hyperspin database:");
            result.getNotPatchedGames().stream().forEach(n -> out.println(n));
            out.println("-------------------------");
        }
    }

    @Override
    public void exitApplicationOnFailure(ApplicationInterruptedException ex) {
        err.println("Exiting application on failure");
        existStatus = ex.getExitStatus();
    }

    @Override
    public void exitApplication() {
        out.println("Exiting application...");
        existStatus = 0;
    }

    public int getExistStatus() {
        return existStatus;
    }
}
