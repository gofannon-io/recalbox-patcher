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
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.stage.Window;

import java.util.ResourceBundle;

public class OptionPaneHandler implements PaneHandler {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    private GridPane pane = new GridPane();
    private Window ownerWindow;
    private UIModel model;
    private ToggleGroup fileActionGroup = new ToggleGroup();
    private CheckBox notFoundCheckBox = new CheckBox();
    private CheckBox upperCaseCheckBox = new CheckBox();
    private RadioButton newFileRadioButton;
    private RadioButton addNameRadioButton;

    @Override
    public void initialize(Window ownerWindow, UIModel model) {
        this.ownerWindow = ownerWindow;
        this.model = model;

        initColumns();
        initRadioButtons();
        addControlsToPane();
        bindControlsToModel();
    }

    private void initColumns() {
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setPercentWidth(50);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);

        pane.getColumnConstraints().addAll(column0, column1);
    }

    private void initRadioButtons() {
        newFileRadioButton = createRadioButton("options-pane.newFile");
        addNameRadioButton = createRadioButton("options-pane.addName");


        newFileRadioButton.setToggleGroup(fileActionGroup);
        addNameRadioButton.setToggleGroup(fileActionGroup);
        addNameRadioButton.setSelected(true);
    }

    private RadioButton createRadioButton(String key) {
        String text = resourceBundle.getString(key);
        RadioButton button = new RadioButton(text);
        button.setToggleGroup(fileActionGroup);
        return button;
    }


    private void addControlsToPane() {
        pane.setVgap(10);
        addLabeledNode("options-pane.notFound", notFoundCheckBox, 0, 0);
        addLabeledNode("options-pane.uppercase", upperCaseCheckBox, 0, 1);
        addNode( newFileRadioButton, 1, 0 );
        addNode( addNameRadioButton, 1, 1 );
    }

    private void addLabeledNode(String labelKey, Node node, int column, int row) {
        String labelText = resourceBundle.getString(labelKey);
        Label label = new Label(labelText);
        HBox labeledControl = new HBox(node, label);
        pane.add(labeledControl, column, row);
    }

    private void addNode(Node node, int column, int row) {
        pane.add(node, column, row);
    }

    private void bindControlsToModel() {
        notFoundCheckBox.selectedProperty().bindBidirectional(model.notFoundOptionProperty());
        upperCaseCheckBox.selectedProperty().bindBidirectional(model.uppercaseOptionProperty());
        newFileRadioButton.selectedProperty().bindBidirectional(model.newFileOptionProperty());
        addNameRadioButton.selectedProperty().bindBidirectional(model.addNameOptionProperty());
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}