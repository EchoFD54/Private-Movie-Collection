package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RemoveMovieFromCategoryController {
    @FXML
    public Button confirmRemoveMovieFromCategoryBtn, cancelRemoveMovieFromCategoryBtn;
    private MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController controller){
        this.mainWindowController = controller;
    }

    public void confirmRemoveMovieFromCategoryButton(ActionEvent actionEvent) {
        mainWindowController.confirmRemoveMovieFromCategoryButton();

        Stage stage = (Stage) confirmRemoveMovieFromCategoryBtn.getScene().getWindow();
        stage.close();
    }

    public void cancelRemoveMovieFromCategoryButton(ActionEvent actionEvent){
        Stage stage = (Stage) cancelRemoveMovieFromCategoryBtn.getScene().getWindow();
        stage.close();
    }
}
