package gui.controllers;

import be.Category;
import bll.CategoryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class RemoveCategoryController {
    @FXML
    public Button confirmRemoveCategoryBtn;
    @FXML
    public Button cancelRemoveCategoryBtn;

    public CategoryManager categoryManager;

    public TableView<Category> categoryTableView;

    private MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController controller){
        this.mainWindowController = controller;
    }

    public void confirmRemoveCategoryButton(ActionEvent actionEvent) {
        this.mainWindowController.categoryManager.deleteCategory(this.mainWindowController.categoryTableView.getSelectionModel().getSelectedItem().getId().get());
        this.mainWindowController.refreshCategoryTableView();

        Stage stage = (Stage) confirmRemoveCategoryBtn.getScene().getWindow();
        stage.close();
    }

    public void cancelRemoveCategoryButton(ActionEvent actionEvent){
        Stage stage = (Stage) cancelRemoveCategoryBtn.getScene().getWindow();
        stage.close();
    }
}
