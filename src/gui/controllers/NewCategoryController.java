package gui.controllers;

import be.Category;
import bll.CategoryManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewCategoryController {
    @FXML
    private TextField nameField;
    private MainWindowController mainWindowController;
    private CategoryManager categoryManager = new CategoryManager();
    public void setMainWindowController(MainWindowController controller){
        this.mainWindowController = controller;
    }

    @FXML
    public void clickAddCategory(){
        String categoryName = nameField.getText().trim();
        Category category = new Category(categoryName);
        if (categoryName != null){
            mainWindowController.createCategory(category);
            categoryManager.createCategory(category);
            mainWindowController.refreshCategoryTableView();
            closeWindow();
        }
    }

    @FXML
    public void cancelButton(){
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

}