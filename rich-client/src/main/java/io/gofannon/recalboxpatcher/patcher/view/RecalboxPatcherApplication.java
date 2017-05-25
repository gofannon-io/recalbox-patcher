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
import io.gofannon.recalboxpatcher.patcher.view.utils.NotImplemented;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import static io.gofannon.recalboxpatcher.patcher.view.WidgetFactory.createButton;

public class RecalboxPatcherApplication extends Application {

    private ScrapperPaneHandler scrapperPaneHandler;
    private ImagePaneHandler imagePaneHandler;

    private Stage stage;

    private UIModel model;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        this.model = new UIModel();

        scrapperPaneHandler = new ScrapperPaneHandler(this.stage, model);
        imagePaneHandler = new ImagePaneHandler(this.stage, model);

        Scene scene = createScene();
        stage.setScene(scene);

        stage.setTitle("Recalbox patcher configuration");
        stage.setOnCloseRequest(this::onCloseRequest);

        stage.show();
    }

    private Scene createScene() {
        TitledPane scrapperPane = createTitledPane("Scrapper", scrapperPaneHandler.getPane());


        TitledPane imagePane = createTitledPane("Images", imagePaneHandler.getPane());



        Pane controlPane = createControlPane();

        VBox rootPane = new VBox(
                scrapperPane,
                imagePane,
                controlPane);
        rootPane.setPadding(new Insets(10, 10, 10, 10));
        rootPane.setFillWidth(true);
        rootPane.setSpacing(15);

        return new Scene(rootPane);
    }

    private TitledPane createTitledPane(String text, Node content) {
        TitledPane titledPane = new TitledPane();
        titledPane.setCollapsible(false);
        titledPane.setText(text);
        titledPane.setContent(content);
        return titledPane;
    }

    private Pane createControlPane() {
        Button saveButton = createButton("Enregistrer", this::onSave);
        Button exitButton = createButton("Quitter", this::onExit);

        HBox buttonBox = new HBox(saveButton, exitButton);
        buttonBox.setSpacing(25);
        buttonBox.setAlignment(Pos.BASELINE_CENTER);
        buttonBox.setPadding(new Insets(0, 5, 5, 5));

        return buttonBox;
    }


    private void onSave(ActionEvent event) {
        NotImplemented.displayNotImplementedDialog();
    }

    private void onExit(ActionEvent event) {
        if (userConfirmApplicationExit())
            stage.hide();
    }

    private boolean userConfirmApplicationExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous quitter l'application ?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> response = alert.showAndWait();
        return response.isPresent() && response.get() == ButtonType.YES;
    }

    private void onCloseRequest(WindowEvent windowEvent) {
        if (userConfirmApplicationExit() == false)
            windowEvent.consume();
    }

}