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

public class ScrapperPaneHandler {

    private UIModel model;

    private Window ownerWindow;
    private Pane pane;

    private TextField inputRecalboxFileTextField;
    private TextField inputHyperspinFileTextField;
    private TextField outputRecalboxFileTextField;


    public ScrapperPaneHandler(Window ownerWindow, UIModel model) {
        this.ownerWindow = ownerWindow;
        this.model = model;

        inputRecalboxFileTextField = createNotEditableTextField();
        inputHyperspinFileTextField = createNotEditableTextField();
        outputRecalboxFileTextField = createNotEditableTextField();

        inputRecalboxFileTextField.textProperty().bind(model.inputRecalboxFileProperty());
        inputHyperspinFileTextField.textProperty().bind(model.inputHyperspinFileProperty());
        outputRecalboxFileTextField.textProperty().bind(model.outputRecalboxFileProperty());

        this.pane = new FileSelectionPaneBuilder()
                .addPathSelector("Chemin des noms à scrapper",
                        inputRecalboxFileTextField,
                        this::onInputRecalboxFileButtonPressed)
                .addPathSelector("Chemin du fichier database Hyperspin",
                        inputHyperspinFileTextField,
                        this::onInputHyperspinFileButtonPressed)
                .addPathSelector("Chemin de sortie fichier game recalbox",
                        outputRecalboxFileTextField,
                        this::onOutputRecalboxFileButtonPressed)
                .build();
    }

    public Pane getPane() {
        return this.pane;
    }

    private void onInputRecalboxFileButtonPressed(ActionEvent event) {
        FileSelector selector = new FileSelector(ownerWindow);
        selector.setFileChooserTitle("Sélectionner le fichier Recalbox source");
        selector.addFileChooserExtensions(
                new FileChooser.ExtensionFilter("Fichiers Recalbox (*.xml)", "*.xml")
        );
        selector.setCurrentFile(model.getInputRecalboxFile());

        selector.showOpenDialog();

        model.inputRecalboxFileProperty().setValue(selector.getSelectedFileAsString());
    }


    private void onInputHyperspinFileButtonPressed(ActionEvent event) {
        FileSelector selector = new FileSelector(ownerWindow);
        selector.setFileChooserTitle("Sélectionner le fichier Hyperspin source");
        selector.addFileChooserExtensions(
                new FileChooser.ExtensionFilter("Fichiers Hyperspin (*.xml)", "*.xml")
        );
        selector.setCurrentFile(model.getInputRecalboxFile());

        selector.showOpenDialog();
        model.inputHyperspinFileProperty().setValue(selector.getSelectedFileAsString());
    }

    private void onOutputRecalboxFileButtonPressed(ActionEvent event) {
        FileSelector selector = new FileSelector(ownerWindow);
        selector.setFileChooserTitle("Sélectionner le fichier Recalbox cible");
        selector.addFileChooserExtensions(
                new FileChooser.ExtensionFilter("Fichiers Recalbox (*.xml)", "*.xml")
        );
        selector.setCurrentFile(model.getInputRecalboxFile());

        selector.showSaveDialog();
        model.outputRecalboxFileProperty().setValue(selector.getSelectedFileAsString());
    }
}