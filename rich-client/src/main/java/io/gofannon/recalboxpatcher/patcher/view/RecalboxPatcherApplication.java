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


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RecalboxPatcherApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new VBox(), 400, 350);
        scene.setFill(Color.OLDLACE);

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu();
        fileMenu.setText("_File");

        MenuItem newMenuItem = new MenuItem(("_New..."));


        MenuItem exitMenuItem = new MenuItem("E_xit");
        exitMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+x"));
        exitMenuItem.setOnAction(this::exitApplication);


        fileMenu.getItems().addAll(newMenuItem, new SeparatorMenuItem(), exitMenuItem);

        menuBar.getMenus().add(fileMenu);



        ((VBox) scene.getRoot()).getChildren().addAll(menuBar);


        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setTitle("A fabulous application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void defineScrapperPanel(Stage primaryStage) {
        VBox vbox = new VBox();
        Label label = new Label();
        label.setText("Chemin des noms à scrapper");
        vbox.getChildren().add(label);


        HBox hboxScrapper = new HBox();
        TextField fieldScapper = new TextField();
        Button setScrapperFileButton = new Button();


    }

    public void exitApplication(ActionEvent e) {
        System.out.println("By bye");
        System.exit(0);
    }
}
