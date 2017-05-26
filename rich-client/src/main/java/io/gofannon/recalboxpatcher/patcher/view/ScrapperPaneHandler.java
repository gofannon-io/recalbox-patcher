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


import io.gofannon.recalboxpatcher.patcher.view.model.UIModel;

import static io.gofannon.recalboxpatcher.patcher.view.WidgetFactory.createNotEditableTextField;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.util.ResourceBundle;

import static io.gofannon.recalboxpatcher.patcher.view.FileExtensionUtils.*;

class ScrapperPaneHandler implements PaneHandler {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    private UIModel model;

    private Window ownerWindow;
    private Pane pane;

    private TextField inputRecalboxFileTextField;
    private TextField inputHyperspinFileTextField;
    private TextField outputRecalboxFileTextField;


    @Override
    public void initialize (Window ownerWindow, UIModel model) {
        this.ownerWindow = ownerWindow;
        this.model = model;

        inputRecalboxFileTextField = createNotEditableTextField();
        inputHyperspinFileTextField = createNotEditableTextField();
        outputRecalboxFileTextField = createNotEditableTextField();

        inputRecalboxFileTextField.textProperty().bind(model.inputRecalboxFileProperty());
        inputHyperspinFileTextField.textProperty().bind(model.inputHyperspinFileProperty());
        outputRecalboxFileTextField.textProperty().bind(model.outputRecalboxFileProperty());


        final String inputRecalboxFileLabel = resourceBundle.getString("scrapper-pane.inputRecalboxFile.label");
        final String inputHyperspinFileLabel = resourceBundle.getString("scrapper-pane.inputHyperspinFile.label");
        final String outputRecalboxFileLabel = resourceBundle.getString("scrapper-pane.outputRecalboxFile.label");

        this.pane = new FileSelectionPaneBuilder()
                .addPathSelector(inputRecalboxFileLabel,
                        inputRecalboxFileTextField,
                        this::onInputRecalboxFileButtonPressed)
                .addPathSelector(inputHyperspinFileLabel,
                        inputHyperspinFileTextField,
                        this::onInputHyperspinFileButtonPressed)
                .addPathSelector(outputRecalboxFileLabel,
                        outputRecalboxFileTextField,
                        this::onOutputRecalboxFileButtonPressed)
                .build();
    }

    @Override
    public Pane getPane() {
        return this.pane;
    }

    private void onInputRecalboxFileButtonPressed(ActionEvent event) {
        FileSelector selector = new FileSelector(ownerWindow);

        final String title = resourceBundle.getString("scrapper-pane.inputRecalboxFile.dialog.title");
        selector.setFileChooserTitle(title);

        selector.addFileChooserExtensions( recalboxFilesExtension() );
        selector.setCurrentFile(model.getInputRecalboxFile());

        selector.showOpenDialog();

        model.inputRecalboxFileProperty().setValue(selector.getSelectedFileAsString());
    }


    private void onInputHyperspinFileButtonPressed(ActionEvent event) {
        FileSelector selector = new FileSelector(ownerWindow);

        final String title = resourceBundle.getString("scrapper-pane.inputHyperspinFile.dialog.title");
        selector.setFileChooserTitle(title);

        selector.addFileChooserExtensions(hyperspinFilesExtension());
        selector.setCurrentFile(model.getInputRecalboxFile());

        selector.showOpenDialog();
        model.inputHyperspinFileProperty().setValue(selector.getSelectedFileAsString());
    }

    private void onOutputRecalboxFileButtonPressed(ActionEvent event) {
        FileSelector selector = new FileSelector(ownerWindow);

        final String title = resourceBundle.getString("scrapper-pane.outputRecalboxFile.dialog.title");
        selector.setFileChooserTitle(title);

        selector.addFileChooserExtensions(recalboxFilesExtension());
        selector.setCurrentFile(model.getInputRecalboxFile());

        selector.showSaveDialog();
        model.outputRecalboxFileProperty().setValue(selector.getSelectedFileAsString());
    }
}