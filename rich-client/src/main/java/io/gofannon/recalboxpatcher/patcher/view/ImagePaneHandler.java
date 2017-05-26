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
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.ResourceBundle;

class ImagePaneHandler implements PaneHandler {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    private Window ownerWindow;
    private UIModel model;

    private TextField inputImageDirectoryTextField = new TextField();
    private TextField outputImageDirectoryTextField = new TextField();

    private Spinner<Integer> imageHeightController;
    private Spinner<Integer> imageWidthController;
    private ComboBox<String> imageFileExtensionController;

    private Pane pane;
    private ObjectProperty<Integer> imageWidthBiDir;
    private ObjectProperty<Integer> imageHeightBiDir;


    @Override
    public void initialize (Window ownerWindow, UIModel model) {
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

        imageWidthBiDir = model.widthImageProperty().asObject();
        imageWidthController.getValueFactory().valueProperty().bindBidirectional(imageWidthBiDir);

        imageHeightBiDir = model.heightImageProperty().asObject();
        imageHeightController.getValueFactory().valueProperty().bindBidirectional(imageHeightBiDir);

        imageFileExtensionController.valueProperty().bindBidirectional(model.imageExtensionProperty());
    }


    private Pane createDirectoryPane() {
        final String inputImageDirectoryLabel = resourceBundle.getString("images-pane.inputImageDirectory.label");
        final String outputImageDirectoryLabel = resourceBundle.getString("images-pane.outputImageDirectory.label");


        return new FileSelectionPaneBuilder()
                .addPathSelector(inputImageDirectoryLabel,
                        inputImageDirectoryTextField,
                        this::onInputImageDirectoryButtonPressed)
                .addPathSelector(outputImageDirectoryLabel,
                        outputImageDirectoryTextField,
                        this::onOutputImageDirectoryButtonPressed )
                .build();
    }

    private void onInputImageDirectoryButtonPressed(ActionEvent event) {
        final String title = resourceBundle.getString("images-pane.inputImageDirectory.dialog.title");

        DirectorySelector selector = new DirectorySelector(ownerWindow);
        selector.setFileChooserTitle(title);
        selector.setCurrentFile(model.getInputImageDirectory());

        selector.showOpenDialog();
        model.inputImageDirectoryProperty().setValue(selector.getSelectedDirectoryAsString());
    }

    private void onOutputImageDirectoryButtonPressed(ActionEvent event) {
        final String title = resourceBundle.getString("images-pane.outputImageDirectory.dialog.title");

        DirectorySelector selector = new DirectorySelector(ownerWindow);
        selector.setFileChooserTitle(title);
        selector.setCurrentFile(model.getOutputImageDirectory());

        selector.showOpenDialog();
        model.outputImageDirectoryProperty().setValue(selector.getSelectedDirectoryAsString());
    }


    private Pane createImageSizePane() {
        imageHeightController = createImageSizeSpinner();
        imageWidthController = createImageSizeSpinner();
        imageFileExtensionController = createImageFileExtensionComboBox();

        Pane heightPane = createLabeledControl("images-pane.image.height", imageHeightController);
        Pane widthPane = createLabeledControl("images-pane.image.width", imageWidthController);
        Pane fileExtensionPane = createLabeledControl("images-pane.image.extension", imageFileExtensionController);

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

    private Pane createLabeledControl(String key, Node node) {
        String labelText = resourceBundle.getString(key);

        HBox pane = new HBox(
                new Label(labelText),
                node
        );
        pane.setSpacing(5);
        pane.setAlignment(Pos.BASELINE_LEFT);
        return pane;
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
