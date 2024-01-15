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
    public Button btnConfirmDelete;
    @FXML
    public Button btnCancel;

    public CategoryManager categoryManager;

    public TableView<Category> categoryTableView;

    private MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController controller){
        this.mainWindowController = controller;
    }

    public void confirmDelete(ActionEvent actionEvent) {
        mainWindowController.deleteCategory();

        Stage stage = (Stage) btnConfirmDelete.getScene().getWindow();
        stage.close();
    }

    public void cancelAction(ActionEvent actionEvent){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
