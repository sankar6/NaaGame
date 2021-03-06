package com.naagame.editor.controllers;

import com.naagame.core.NgmProject;
import com.naagame.core.resources.NgmEntity;
import com.naagame.core.resources.NgmSprite;
import com.shc.easyjson.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityEditorController extends Controller implements Initializable {

    private static JSONObject keyCodes;

    static {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(EntityEditorController.class
                .getClassLoader().getResourceAsStream("keyCodes.json")))) {
            String json = reader.lines().collect(Collectors.joining("\n"));
            keyCodes = JSON.parse(json);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML private ChoiceBox<String> spriteSelector;

    @FXML private Menu addEvtKeyPressedMenu;
    @FXML private Menu addEvtKeyReleasedMenu;
    @FXML private Menu addEvtKeyTappedMenu;

    private NgmEntity entity;
    private NgmSprite sprite;
    private boolean changed;

    @Override
    public void init(String name) {
        entity = NgmProject.find(NgmProject.entities, name);
        sprite = entity.getSprite();
        changed = false;

        if (sprite != null) {
            spriteSelector.getSelectionModel().select(sprite.getName());
        }

        resourcesChanged();
    }

    @FXML
    @Override
    protected void discardChanges() {
        init(entity.getName());
    }

    @Override
    protected void resourcesChanged() {
        spriteSelector.getItems().clear();
        spriteSelector.getItems().addAll(NgmProject.sprites.stream()
                .map(NgmSprite::getName)
                .collect(Collectors.toList()));

        if (sprite == null) {
            return;
        }

        sprite = NgmProject.find(NgmProject.sprites, sprite.getName());

        if (sprite == null) {
            spriteSelector.getSelectionModel().clearSelection();
        } else {
            spriteSelector.getSelectionModel().select(sprite.getName());
        }
    }

    @Override
    protected boolean hasUnsavedEdits() {
        return changed;
    }

    @FXML
    @Override
    protected void commitChanges() {
        entity.setSprite(sprite);
        changed = false;
        notifySave();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spriteSelector.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> {
            if (n != null) {
                sprite = NgmProject.find(NgmProject.sprites, n);
                changed = true;
            }
        });

        createKeyMenu(addEvtKeyPressedMenu, NgmEntity.Event.Type.KEY_DOWN);
        createKeyMenu(addEvtKeyReleasedMenu, NgmEntity.Event.Type.KEY_UP);
        createKeyMenu(addEvtKeyTappedMenu, NgmEntity.Event.Type.KEY_TAP);
    }
    
    private void createKeyMenu(Menu root, NgmEntity.Event.Type type) {
        final Function<JSONObject, MenuItem> createMenuItem = json -> {
            MenuItem menuItem = new MenuItem(json.get("key").getValue());
            
            menuItem.setOnAction(actionEvent ->
                    System.out.println(String.format("Create %s event for key %s with code %d",
                            type,
                            json.get("key").getValue(),
                            json.get("code").<Number> getValue().intValue())));
            
            return menuItem;
        };

        final BiFunction<JSONArray, String, Menu> createMenu = (array, name) -> {
            Menu menu = new Menu(name);

            menu.getItems().addAll(
                    array.stream()
                            .map(JSONValue::<JSONObject> getValue)
                            .map(createMenuItem)
                            .collect(Collectors.toList())
            );

            return menu;
        };
        
        JSONArray arrowKeys = keyCodes.get("arrowKeys").getValue();
        JSONArray functionKeys = keyCodes.get("functionKeys").getValue();
        JSONArray alphabetKeys = keyCodes.get("alphabetKeys").getValue();
        JSONArray numberKeys = keyCodes.get("numberKeys").getValue();
        JSONArray otherKeys = keyCodes.get("otherKeys").getValue();
        JSONArray lockKeys = keyCodes.get("lockKeys").getValue();

        root.getItems().addAll(
                createMenu.apply(arrowKeys, "Arrow Keys"),
                createMenu.apply(functionKeys, "Function Keys"),
                createMenu.apply(alphabetKeys, "Alphabet Keys"),
                createMenu.apply(numberKeys, "Number Keys"),
                createMenu.apply(lockKeys, "Lock Keys"),
                createMenu.apply(otherKeys, "Other Keys")
        );
    }
}
