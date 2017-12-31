package com.naagame.editor;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EntityEditorController implements IController {
    String currentEntity;

    @FXML TextField nameField;

    public void init() {
        nameField.setText(currentEntity);
    }
}
