package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RemoveCategoryController {
    @FXML
    public Button cancelRemoveCategoryBtn;

    public Button confirmRemoveCategoryBtn;

    private MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController controller){
        this.mainWindowController = controller;
    }

    public void confirmRemoveCategoryButton() {
        mainWindowController.removeCategory();
        Stage stage = (Stage) confirmRemoveCategoryBtn.getScene().getWindow();
        stage.close();
    }
    private void closeRemovedWindow(){
        Stage stage = (Stage) cancelRemoveCategoryBtn.getScene().getWindow();
        stage.close();
    }
    public void cancelRemoveCategoryButton(){
        closeRemovedWindow();
    }
}
