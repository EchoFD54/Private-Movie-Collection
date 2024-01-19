package gui.controllers;

import be.Category;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewCategoryController {

    public TextField nameField;
    public MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController controller){
        this.mainWindowController = controller;
    }

    public void clickAddCategory(){
        String categoryName = nameField.getText().trim();
        Category category = new Category(categoryName);
        if (categoryName != null){
            mainWindowController.createCategory(category);
            closeWindow();
        }
    }

    public void cancelButton(){
        closeWindow();
    }

    public void closeWindow(){
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

}