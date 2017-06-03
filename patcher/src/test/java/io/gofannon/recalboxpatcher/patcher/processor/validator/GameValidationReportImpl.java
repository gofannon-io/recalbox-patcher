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

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

class GameValidationReportImpl implements GameValidationReport {

    private List<FailedField> failedFields = new ArrayList<>();
    private String gameUniqueName;

    public GameValidationReportImpl(String gameUniqueName) {
        this.gameUniqueName = gameUniqueName;
    }

    @Override
    public String getGameUniqueName() {
        return gameUniqueName;
    }

    @Override
    public List<FailedField> getFailedFields() {
        return failedFields;
    }

    @Override
    public void write(PrintStream out) throws IOException {
        if (isValidationCorrect()) {
            out.println("Validation succeeded for game " + gameUniqueName);
        } else {
            out.println("Validation failed for game "+gameUniqueName);
            failedFields.forEach(f -> writeField(out, f));
        }
    }

    private void writeField(PrintStream out, FailedField failedField) {
        String fieldReport = "Field '" + failedField.getFieldName()
                + "' expected value '" + failedField.getExpectedValue()
                + "' but it found value '" + failedField.getActualValue() + "'";
        out.println(fieldReport);
    }

    public void addFailedFieldReport(FailedField failedField) {
        failedFields.add(failedField);
    }
}
