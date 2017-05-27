/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gofannon.recalboxpatcher.patcher.view;


import io.gofannon.recalboxpatcher.patcher.view.model.DefaultUIModel;
import io.gofannon.recalboxpatcher.patcher.view.model.UIModel;
import io.gofannon.recalboxpatcher.patcher.view.processing.PatchTaskContext;
import io.gofannon.recalboxpatcher.patcher.view.processing.PatchTaskResult;
import io.gofannon.recalboxpatcher.patcher.view.processing.PatcherProcessingService;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.util.Optional;
import java.util.ResourceBundle;

import static io.gofannon.recalboxpatcher.patcher.view.WidgetFactory.createButtonWithBundle;

public class RecalboxPatcherApplication extends Application {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    private Pane scrapperPane;
    private Pane imagePane;
    private Pane processingPane;
    private Pane optionPane;

    private Stage stage;

    private UIModel model = new DefaultUIModel();

    private Button saveButton;
    private Button logButton;
    private Button exitButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        buildPanes();
        doBinding();

        Scene scene = createScene();
        stage.setScene(scene);

        stage.setTitle(resourceBundle.getString("application.title"));
        stage.setOnCloseRequest(this::onCloseRequest);

        stage.show();
    }

    private void buildPanes() {
        scrapperPane = createPane(ScrapperPaneHandler.class);
        imagePane = createPane(ImagePaneHandler.class);
        processingPane = createPane(ProcessingPaneHandler.class);
        optionPane = createPane(OptionPaneHandler.class);
    }

    private Pane createPane(Class<? extends PaneHandler> paneHandlerClass) {
        try {

            PaneHandler paneHandler = paneHandlerClass.newInstance();
            paneHandler.initialize(this.stage, this.model);
            return paneHandler.getPane();

        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }


    private void doBinding() {
        model.processingStateProperty().addListener(this::onProcessingStateChanged);
    }

    private void onProcessingStateChanged(ObservableValue<? extends ProcessingState> observable, ProcessingState oldValue, ProcessingState newValue) {
        if( newValue == ProcessingState.SUCESS)
            patchSucceeded();

        saveButton.setDisable( newValue == ProcessingState.RUNNING);
    }

    private void patchSucceeded() {
        PatchTaskResult result = model.geLastPatchResult();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Génération du fichier");
        alert.setHeaderText("Désolé, le générateur de fichier n'est pas encore intégré.");
        String content = String.join("\n", result.getLogs());
        alert.setContentText(content);
        alert.showAndWait();
    }


    private Scene createScene() {
        TitledPane scrapperPane = createTitledPane("scrapper-pane.title", this.scrapperPane);
        TitledPane imagePane = createTitledPane("images-pane.title", this.imagePane);
        TitledPane optionPane = createTitledPane("options-pane.title", this.optionPane);
        Pane controlPane = createControlPane();

        VBox rootPane = new VBox(
                scrapperPane,
                imagePane,
                optionPane,
                processingPane,
                controlPane);
        rootPane.setPadding(new Insets(10, 10, 10, 10));
        rootPane.setFillWidth(true);
        rootPane.setSpacing(15);

        Scene newScene = new Scene(rootPane);
        newScene.getStylesheets().add("/root.css");
        return newScene;
    }

    private TitledPane createTitledPane(String key, Node content) {
        TitledPane titledPane = new TitledPane();
        titledPane.setCollapsible(false);
        String text = resourceBundle.getString(key);
        titledPane.setText(text);
        titledPane.setContent(content);
        return titledPane;
    }

    private Pane createControlPane() {
        logButton = createButtonWithBundle("application.log", this::onLog);
        saveButton = createButtonWithBundle("application.save", this::onSave);
        exitButton = createButtonWithBundle("application.exit", this::onExit);

        HBox buttonBox = new HBox(logButton, saveButton, exitButton);
        buttonBox.setSpacing(25);
        buttonBox.setAlignment(Pos.BASELINE_CENTER);
        buttonBox.setPadding(new Insets(0, 5, 5, 5));

        return buttonBox;
    }

    private void onLog(ActionEvent event) {
        Dialog<Void> dialog = new Dialog();

        LogDialogHandler logDialogPane = new LogDialogHandler();
        logDialogPane.initialize(dialog, model);

        dialog.showAndWait();
    }

    private void onSave(ActionEvent event) {
        model.launchPatchProcess();
    }

    private void onExit(ActionEvent event) {
        if (userConfirmApplicationExit())
            stage.hide();
    }

    private boolean userConfirmApplicationExit() {
        String contentText = resourceBundle.getString("application.exit-confirmation-dialog.content");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                contentText,
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> response = alert.showAndWait();
        return response.isPresent() && response.get() == ButtonType.YES;
    }

    private void onCloseRequest(WindowEvent windowEvent) {
        if (userConfirmApplicationExit() == false)
            windowEvent.consume();
    }

}