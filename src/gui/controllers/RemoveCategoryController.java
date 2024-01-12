package gui.controllers;

import be.Category;
import bll.CategoryManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class RemoveCategoryController {
    public Button cancelRemoveCategoryBtn;

    public Button confirmRemoveCategoryBtn;

    private MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController controller){
        this.mainWindowController = controller;
    }

    public void confirmRemoveCategory(ActionEvent actionEvent) {
        mainWindowController.removeCategory();
        Stage stage = (Stage) confirmRemoveCategoryBtn.getScene().getWindow();
        stage.close();
    }
    public void cancelRemoveCategory(ActionEvent actionEvent) {
        mainWindowController.removeCategory();
        Stage stage = (Stage) confirmRemoveCategoryBtn.getScene().getWindow();
        stage.close();
    }

    public void refreshCategoryList() {
        ListView<Object> categoryList = null;
        categoryList.getItems().clear();
        CategoryManager categoryManager = new CategoryManager();
        List<Category> allCategories = categoryManager.getAllCategories();
        categoryList.getItems().addAll(allCategories);
    }

}
