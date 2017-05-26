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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;

public final class WidgetFactory {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("view");

    public static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button();
        button.setText(text);
        button.setOnAction(action);
        return button;
    }

    public static Button createButtonWithBundle(String key, EventHandler<ActionEvent> action) {
        String text = resourceBundle.getString(key);
        return createButton(text, action);
    }

    public static TextField createNotEditableTextField() {
        TextField textField = new TextField();
        textField.setEditable(false);
        return textField;
    }
}
