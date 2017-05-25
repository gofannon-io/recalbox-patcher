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
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

class ImagePaneHandler {

    private Window ownerWindow;
    private UIModel model;

    private TextField inputImageDirectoryTextField = new TextField();
    private TextField outputImageDirectoryTextField = new TextField();

    private Spinner<Integer> imageHeightController;
    private Spinner<Integer> imageWidthController;
    private ComboBox<String> imageFileExtensionController;

    private Pane pane;


    public ImagePaneHandler(Window ownerWindow, UIModel model) {
        this.ownerWindow = ownerWindow;
        this.model = model;

        Pane directoryPane = createDirectoryPane();
        Pane imageSizePane = createImageSizePane();

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
        imageFileExtensionController.valueProperty().bindBidirectional(model.imageExtensionProperty());
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
        imageHeightController = createImageSizeSpinner();
        imageWidthController = createImageSizeSpinner();
        imageFileExtensionController = createImageFileExtensionComboBox();


        Pane heightPane = createLabeledControl("Height", imageHeightController);
        Pane widthPane = createLabeledControl("Width", imageWidthController);
        Pane fileExtensionPane = createLabeledControl("Extension", imageFileExtensionController);

        HBox imageSizePane = new HBox(
                heightPane,
                widthPane,
                fileExtensionPane
                );
        imageSizePane.setSpacing(20.);
        imageSizePane.setAlignment(Pos.CENTER);
        imageSizePane.setPadding(new Insets(-5,0,0,0));

        return imageSizePane;
    }

    private static Spinner<Integer> createImageSizeSpinner() {
        Spinner<Integer> spinner = new Spinner<>(10,500,200);
        spinner.setPrefWidth(70.);
        return spinner;
    }

    private ComboBox<String> createImageFileExtensionComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(model.getImageFileExtensionList());
        return comboBox;
    }

    private static Pane createLabeledControl(String labelText, Node node) {
        HBox pane = new HBox(
                new Label(labelText),
                node
        );
        pane.setSpacing(5);
        pane.setAlignment(Pos.BASELINE_LEFT);
        return pane;
    }

    Pane getPane() {
        return pane;
    }
}
