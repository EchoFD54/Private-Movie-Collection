package gui.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddMovieWindowController {

    public TextField fileField;
    public TextField titleField;
    public TextField personalRatingField;
    public Button browseFileBtn;
    public Button addMovieBtn;
    public TextField imdbRatingField;
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
        // Retrieve the song properties from the text fields
        String title = titleField.getText();
        String imdbRating = imdbRatingField.getText();
        String personalRating = personalRatingField.getText();
        String filePath = fileField.getText();

        // Update the song properties in the MainWindowController
        mainWindowController.updateMovieProperties(title, imdbRating, personalRating, filePath);

        // Close the AddSongWindow
        ((Stage) titleField.getScene().getWindow()).close();
    }
}
