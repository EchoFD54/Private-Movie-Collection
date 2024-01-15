package gui.controllers;

import be.Category;
import bll.CategoryManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class RemoveMovieFromCategoryController {
    @FXML
    public Button confirmRemoveMovieFromCategoryBtn;
    @FXML
    public Button cancelRemoveMovieFromCategoryBtn;

    public CategoryManager categoryManager;

    public TableView<Category> categoryTableView;

    private MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController controller){
        this.mainWindowController = controller;
    }

    public void confirmRemoveMovieFromCategoryButton(ActionEvent actionEvent) {
        mainWindowController.deleteCategory();

        Stage stage = (Stage) confirmRemoveMovieFromCategoryBtn.getScene().getWindow();
        stage.close();
    }

    public void cancelRemoveMovieFromCategoryButton(ActionEvent actionEvent){
        Stage stage = (Stage) cancelRemoveMovieFromCategoryBtn.getScene().getWindow();
        stage.close();
    }
}
