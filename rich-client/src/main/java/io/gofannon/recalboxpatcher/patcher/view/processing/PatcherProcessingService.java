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

package io.gofannon.recalboxpatcher.patcher.view.processing;

import io.gofannon.recalboxpatcher.patcher.processor.PatchProcessingResult;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by gwen on 27/05/17.
 */
public class PatcherProcessingService extends Service<PatchTaskResult> {

    private Class<? extends Task<PatchTaskResult>> taskClass;
    private PatchTaskContext context;

    public PatcherProcessingService(Class<? extends Task<PatchTaskResult>> taskClass) {
        this.taskClass = taskClass;
    }

    public void setContext(PatchTaskContext context) {
        this.context = context;
    }

    @Override
    protected Task<PatchTaskResult> createTask() {
        try {

            Constructor<? extends Task<PatchTaskResult>> constructor = taskClass.getConstructor(PatchTaskContext.class);
            return constructor.newInstance(context);

        } catch( Throwable th) {
            System.err.println("Fail to create Task");
            th.printStackTrace(System.err);
        }


        return new SimulatedRecalboxPatcherTask(context);
    }

}
