package gui.controllers;

import be.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindowController {
    @FXML
    private TableView<Movie> movieTableView;
    @FXML
    public Button newMovieBtn;
    private int movieIndex;
@FXML
    private void initialize(){
     setMovieTableView();
     setButtons();
    }

    public void clickNewMovieBtn(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/AddMovieWindow.fxml"));
        Parent root;
        try {
            root = loader.load();
            AddMovieWindowController addMovieController = loader.getController();
            addMovieController.setMainWindowController(this);
            Stage stage = new Stage();
            stage.setTitle("Add Movie");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMovieTableView(){
        // Set up the columns in the TableView
        TableColumn<Movie, String> titleColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(0);
        TableColumn<Movie, String> imdbRatingColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(1);
        TableColumn<Movie, String> personalRatingColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(2);
        TableColumn<Movie, String> lastWatchedColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(3);

        // Define cell value factories for each column
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitle());
        imdbRatingColumn.setCellValueFactory(cellData -> cellData.getValue().getImdbRating());
        personalRatingColumn.setCellValueFactory(cellData -> cellData.getValue().getPersonalRating());
        lastWatchedColumn.setCellValueFactory(cellData -> cellData.getValue().getLastWatched());
    }

    public void setButtons(){
        // Set a listener for handling movie selection
        movieTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selectedMovieIndex = movieTableView.getSelectionModel().getSelectedIndex();
            if (selectedMovieIndex >= 0) {
                movieIndex = selectedMovieIndex;
                System.out.println("Play Movie Now");
                playSelectedMovie(newValue);
            }
        });

        // Set the event handler for the addMovies button
        newMovieBtn.setOnAction(this::clickNewMovieBtn);
    }

    public void updateMovieProperties(String title, String imdbRating, String personalRating, String filePath){
        Movie newMovie = new Movie(title, imdbRating, personalRating, filePath);
        movieTableView.getItems().add(newMovie);
    }

    private void playSelectedMovie(Movie selectedMovie){
    String filePath = String.valueOf(selectedMovie.getFilePath());
        File movieFile = new File(filePath);

        if (Desktop.isDesktopSupported() && movieFile.exists()) {
            try {
                Desktop.getDesktop().open(movieFile);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (will apply this after)
            }
        } else {
            System.out.println(filePath);
            System.out.println("Cannot play the selected movie. File does not exist or Desktop is not supported.");
            // Handle the case where the file doesn't exist or Desktop is not supported
        }
    }
}
