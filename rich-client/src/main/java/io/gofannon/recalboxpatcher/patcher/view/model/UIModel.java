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

package io.gofannon.recalboxpatcher.patcher.view.model;

import io.gofannon.recalboxpatcher.patcher.view.processing.ProcessingState;
import io.gofannon.recalboxpatcher.patcher.view.processing.PatchTaskResult;
import javafx.beans.property.*;

import java.io.File;
import java.util.List;

public interface UIModel {


    StringProperty inputRecalboxFileProperty();

    StringProperty inputHyperspinFileProperty();

    StringProperty outputRecalboxFileProperty();

    StringProperty inputImageDirectoryProperty();

    StringProperty outputImageDirectoryProperty();

    StringProperty outputImageRelativeDirectoryProperty() ;

    IntegerProperty widthImageProperty();

    IntegerProperty heightImageProperty();

    File getInputRecalboxFile();

    File getInputHyperspinFile();

    File getOutputRecalboxFile();

    File getInputImageDirectory();

    File getOutputImageDirectory();

    StringProperty imageExtensionProperty();

    String[] getImageFileExtensionList();

    BooleanProperty notFoundOptionProperty();

    BooleanProperty uppercaseOptionProperty();

    BooleanProperty newFileOptionProperty();

    BooleanProperty addNameOptionProperty();

    StringProperty operationLogProperty();

    String getOperationLog();

    ObjectProperty<ProcessingState> processingStateProperty();

    ProcessingState getProcessingState();

    void launchPatchProcess();

    //FIXME is it really usefull, can't we use #getOperationLogs & co ?
    PatchTaskResult geLastPatchResult();
}