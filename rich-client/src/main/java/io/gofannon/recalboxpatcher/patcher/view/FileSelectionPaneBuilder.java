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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import static io.gofannon.recalboxpatcher.patcher.view.WidgetFactory.createButton;


public class FileSelectionPaneBuilder {

    private int currentRow;
    private GridPane pane = new GridPane();

    public FileSelectionPaneBuilder() {
        seUpColumnConstraints();
        setUpPaddingAndGap();
    }

    private void setUpPaddingAndGap() {
        pane.setPadding(new Insets(10,-10,10,5));
        pane.setHgap(5);
        pane.setVgap(5);
    }

    private void seUpColumnConstraints() {
        ColumnConstraints fieldColumn = new ColumnConstraints(150,300,Double.MAX_VALUE);
//        fieldColumn.setFillWidth(true);
        fieldColumn.setHgrow(Priority.ALWAYS);
        ColumnConstraints buttonColumn = new ColumnConstraints(50);
        //buttonColumn.setFillWidth(true);
        pane.getColumnConstraints().addAll(fieldColumn, buttonColumn); // first column gets any extra width
    }

    public FileSelectionPaneBuilder addPathSelector(String text, TextField textField, EventHandler<ActionEvent> buttonAction) {
        addLabelLine(text);
        addSelectorLine(textField,buttonAction);
        return this;
    }


    protected void addLabelLine(String text) {
        Label label = new Label(text);
        pane.setConstraints(label, 0, currentRow, 2, 1);
        pane.getChildren().add(label);
        currentRow++;
    }


    protected void addSelectorLine(TextField textField, Button button) {
        pane.setConstraints(textField, 0, currentRow, 1,1);
        pane.setConstraints(button, 1, currentRow, 1,1);
        pane.getChildren().addAll(textField,button);
        currentRow++;
    }


    protected void addSelectorLine(TextField textField, EventHandler<ActionEvent> buttonAction) {
        Button button = createButton("...", buttonAction);
        addSelectorLine(textField, button);
    }

    public Pane build() {
        return pane;
    }
}