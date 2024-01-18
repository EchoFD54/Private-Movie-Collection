package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RemoveCategoryController {
    @FXML
    public Button btnConfirmDelete, btnCancel;
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
