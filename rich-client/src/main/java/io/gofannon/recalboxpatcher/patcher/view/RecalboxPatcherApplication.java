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


import io.gofannon.recalboxpatcher.patcher.view.model.UIModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static io.gofannon.recalboxpatcher.patcher.view.WidgetFactory.createButtonWithBundle;

public class RecalboxPatcherApplication extends Application {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    private Pane scrapperPane;
    private Pane imagePane;
    private Pane optionPane;

    private Stage stage;

    private UIModel model;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        this.model = new UIModel();

        scrapperPane = createPane(ScrapperPaneHandler.class);
        imagePane = createPane(ImagePaneHandler.class);
        optionPane = createPane(OptionPaneHandler.class);

        Scene scene = createScene();
        stage.setScene(scene);

        stage.setTitle(resourceBundle.getString("application.title"));
        stage.setOnCloseRequest(this::onCloseRequest);

        stage.show();
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

    private Scene createScene() {
        TitledPane scrapperPane = createTitledPane("scrapper-pane.title", this.scrapperPane);
        TitledPane imagePane = createTitledPane("images-pane.title", this.imagePane);
        TitledPane optionPane = createTitledPane("options-pane.title", this.optionPane);
        Pane controlPane = createControlPane();

        VBox rootPane = new VBox(
                scrapperPane,
                imagePane,
                optionPane,
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
        Button logButton = createButtonWithBundle("application.log", this::onLog);
        Button saveButton = createButtonWithBundle("application.save", this::onSave);
        Button exitButton = createButtonWithBundle("application.exit", this::onExit);

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
        List<String> log = createDummyLog();
        StringBuilder content = new StringBuilder();
        log.forEach( l -> content.append(l).append("\n"));
        content.deleteCharAt(content.length()-1);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Génération du fichier");
        alert.setHeaderText("Désolé, le générateur de fichier n'est pas encore intégré.");
        alert.setContentText(content.toString());
        alert.showAndWait();

        model.replaceLog(log);
    }

    private List<String> createDummyLog() {
        List<String> logs = new ArrayList<>();
        logs.add("Fichier d'entrée Recalbox: "+model.inputRecalboxFileProperty().getValue());
        logs.add("Fichier d'entrée Hyperspin: "+model.inputHyperspinFileProperty().getValue());
        logs.add("Fichier de sortie Recalbox: "+model.outputRecalboxFileProperty().getValue());
        logs.add("Chemin des fichiers à télécharger: "+model.inputImageDirectoryProperty().getValue());
        logs.add("Chemin des images dans le fichier: "+model.outputImageDirectoryProperty().getValue());
        logs.add("Hauteur des images: "+model.heightImageProperty().getValue());
        logs.add("Largeur des images: "+model.widthImageProperty().getValue());
        logs.add("Extension des images: "+model.imageExtensionProperty().getValue());
        logs.add("Option 'Non trouvée' activée: "+model.notFoundOptionProperty().getValue());
        logs.add("Option 'Majuscule' activée: "+model.upperCaseOptionProperty().getValue());
        logs.add("Option 'Nouveau Fichier' activée: "+model.newFileOptionProperty().getValue());
        logs.add("Option 'Ajout de nom' activée: "+model.addNameOptionProperty().getValue());
        return logs;
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