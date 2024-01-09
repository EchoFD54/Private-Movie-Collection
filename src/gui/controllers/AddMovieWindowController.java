package gui.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddMovieWindowController {

    public TextField fileField, titleField, personalRatingField, imdbRatingField;
    public Button browseFileBtn, addMovieBtn;
    private MainWindowController mainWindowController;

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void clickBrowse(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Movie File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Set the selected file path to the fileField
            fileField.setText(selectedFile.getAbsolutePath());
        }
    }

    public void clickAddMovie(ActionEvent actionEvent) {
        // Retrieve the movie properties from the text fields
        String title = titleField.getText();
        String imdbRating = imdbRatingField.getText();
        String personalRating = personalRatingField.getText();
        String filePath = fileField.getText();

        // Update the movie properties in the MainWindowController
        mainWindowController.updateMovieProperties(title, imdbRating, personalRating, filePath);

        // Close the AddMovieWindow
        ((Stage) titleField.getScene().getWindow()).close();
    }
}
