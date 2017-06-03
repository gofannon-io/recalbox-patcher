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

package io.gofannon.recalboxpatcher.patcher.processor.validator;


import io.gofannon.recalboxpatcher.patcher.processor.PatchProcessingResult;

import java.util.ArrayList;
import java.util.List;

class GameDatabaseValidationReportImpl implements GameDatabaseValidationReport {

    private PatchProcessingResult processingResult;
    private List<GameValidationReport> gameValidationReports = new ArrayList<>();


    @Override
    public boolean isValidationSucceeded() {
        return processingResult!=null && this.processingResult.getNotPatchedGameCount()==0;
    }

    @Override
    public List<GameValidationReport> getGameValidationReports() {
        return gameValidationReports;
    }

    public void setGameValidationReports(List<GameValidationReport> gameValidationReports) {
        this.gameValidationReports = gameValidationReports;
    }

    @Override
    public PatchProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(PatchProcessingResult processingResult) {
        this.processingResult = processingResult;
    }
}
