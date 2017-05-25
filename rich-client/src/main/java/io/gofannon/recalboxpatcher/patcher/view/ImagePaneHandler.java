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
import io.gofannon.recalboxpatcher.patcher.view.utils.NotImplemented;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class ImagePaneHandler {

    private Window ownerWindow;
    private UIModel model;

    private TextField inputImageDirectoryTextField = new TextField();
    private TextField outputImageDirectoryTextField = new TextField();

    private Spinner<Integer> imageHeightController;
    private Spinner<Integer> imageWidthController;

    private Pane pane;


    public ImagePaneHandler(Window ownerWindow, UIModel model) {
        this.ownerWindow = ownerWindow;
        this.model = model;

        Pane directoryPane = createDirectoryPane();
        Pane imageSizePane = createImageSizePane();
        createImageTypePane();

        VBox rootPane = new VBox(
                directoryPane,
                imageSizePane);
        rootPane.setSpacing(5);

        this.pane = rootPane;

        inputImageDirectoryTextField.textProperty().bind(model.inputImageDirectoryProperty());
        outputImageDirectoryTextField.textProperty().bind(model.outputImageDirectoryProperty());

        imageWidthController.getValueFactory().valueProperty().bindBidirectional(
                model.widthImageProperty().asObject()
        );
        imageHeightController.getValueFactory().valueProperty().bindBidirectional(
                model.heightImageProperty().asObject()
        );
    }

    private Pane createDirectoryPane() {
        return new FileSelectionPaneBuilder()
                .addPathSelector("Chemin des images à télécharger",
                        inputImageDirectoryTextField,
                        this::onInputImageDirectoryButtonPressed)
                .addPathSelector("Chemin des images dans le fichier",
                        outputImageDirectoryTextField,
                        this::onOutputImageDirectoryButtonPressed )
                .build();
    }

    private void onInputImageDirectoryButtonPressed(ActionEvent event) {
        DirectorySelector selector = new DirectorySelector(ownerWindow);
        selector.setFileChooserTitle("Sélectionner le répertoire des images à télécharger");
        selector.setCurrentFile(model.getInputImageDirectory());

        selector.showOpenDialog();
        model.inputImageDirectoryProperty().setValue(selector.getSelectedDirectoryAsString());
    }

    private void onOutputImageDirectoryButtonPressed(ActionEvent event) {
        DirectorySelector selector = new DirectorySelector(ownerWindow);
        selector.setFileChooserTitle("Sélectionner le répertoire des images dans le fichier");
        selector.setCurrentFile(model.getOutputImageDirectory());

        selector.showOpenDialog();
        model.outputImageDirectoryProperty().setValue(selector.getSelectedDirectoryAsString());
    }


    private Pane createImageSizePane() {
        //SpinnerValueFactory factory= new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 500);
        imageHeightController = new Spinner<>(10,500,200);
        imageHeightController.setPrefWidth(100.);
        imageWidthController = new Spinner<>(10,500,200);
        imageWidthController.setPrefWidth(100.);

        VBox heightPane = new VBox(
                new Label("Height"),
                imageHeightController
        );
        heightPane.setSpacing(5);

        VBox widthPane = new VBox(
                new Label("Width"),
                imageWidthController
        );
        widthPane.setSpacing(5);

        HBox imageSizePane = new HBox(heightPane, widthPane);
        imageSizePane.setSpacing(10.);
        return imageSizePane;
    }

    private void createImageTypePane() {

    }

    public Pane getPane() {
        return pane;
    }
}
