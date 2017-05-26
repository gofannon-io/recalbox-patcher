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

package io.gofannon.recalboxpatcher.patcher.view;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwen on 27/05/17.
 */
public class PatcherProcessingService extends Service<Void> {

    private PatchTaskContext context;
    private List<String> logs = new ArrayList<>();

    public void setContext(PatchTaskContext context) {
        this.context = context;
    }

    @Override
    protected Task<Void> createTask() {
        return new RecalboxPatcherTask(context, logs);
    }

    public List<String> getLogs() {
        return logs;
    }
}
