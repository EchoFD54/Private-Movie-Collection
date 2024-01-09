package gui.controllers;

import be.Category;
import be.Movie;
import bll.CategoryManager;
import bll.MovieManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindowController {
    @FXML
    private TableView<Category> categoryTableView;
    @FXML
    private TableView<Movie> movieTableView, movieInCatTableView;
    @FXML
    private Button newMovieBtn;
    @FXML
    private int movieIndex;
    @FXML
    private MovieManager movieManager = new MovieManager();
    @FXML
    private CategoryManager categoryManager = new CategoryManager();

    @FXML
    private void initialize(){
     setMovieTableView();
     setButtons();
     dataBaseSetUp();
     setUpCategoryTableView();
     setUpMoviesInCategoryTableView();
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

    private void setUpMoviesInCategoryTableView(){
        // Set up the columns in the TableView
        TableColumn<Movie, String> titleColumn = (TableColumn<Movie, String>) movieInCatTableView.getColumns().get(0);
        TableColumn<Movie, String> imdbRatingColumn = (TableColumn<Movie, String>) movieInCatTableView.getColumns().get(1);
        TableColumn<Movie, String> personalRatingColumn = (TableColumn<Movie, String>) movieInCatTableView.getColumns().get(2);
        TableColumn<Movie, String> lastWatchedColumn = (TableColumn<Movie, String>) movieInCatTableView.getColumns().get(3);

        // Define cell value factories for each column
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitle());
        imdbRatingColumn.setCellValueFactory(cellData -> cellData.getValue().getImdbRating());
        personalRatingColumn.setCellValueFactory(cellData -> cellData.getValue().getPersonalRating());
        lastWatchedColumn.setCellValueFactory(cellData -> cellData.getValue().getLastWatched());
    }

    private void setUpCategoryTableView(){
        // Set up the columns in the TableView
        TableColumn<Category, String> categoryName = (TableColumn<Category, String>) categoryTableView.getColumns().get(0);
        TableColumn<Category, Integer> movies = (TableColumn<Category, Integer>) categoryTableView.getColumns().get(1);

        // Define cell value factories for each column
        categoryName.setCellValueFactory(cellData -> cellData.getValue().getName());
        movies.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getId().get();
            int totalMovies = numberMoviesInCategory(categoryId);
            return new SimpleIntegerProperty(totalMovies).asObject();
        });
    }

    private int numberMoviesInCategory(int categoryId){
        if(categoryId > 0){
            return categoryManager.getAllMoviesOfCategory(categoryId).size();
        }
        else return -1;
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

    private void dataBaseSetUp(){
        // Show all movies saved on the Database
        for(Movie m : movieManager.getAllMovies()){
            movieTableView.getItems().add(m);
        }
        // Shows all categories saved on the Database
        for(Category c : categoryManager.getAllCategories()){
            categoryTableView.getItems().add(c);
        }
    }

    public void moviesInCategory(MouseEvent mouseEvent) {
        movieInCatTableView.getItems().clear();
        for(Movie m : categoryManager.getAllMoviesOfCategory(categoryTableView.getSelectionModel().getSelectedItem().getId().get())){
            movieInCatTableView.getItems().add(m);
        }
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

    public void updateMovieProperties(String title, String imdbRating, String personalRating, String filePath){
        // Check if the movie is already in the TableView
        boolean movieExists = false;
        Movie existingMovie = null;

        // Modify existing movie properties
        for (Movie movie : movieTableView.getItems()) {
            if (movie.getFilePath().get().equals(filePath)) {
                existingMovie = movie;
                existingMovie.setTitle(title);
                existingMovie.setImdbRating(imdbRating);
                existingMovie.setPersonalRating(personalRating);
                // Updates the song properties in the database
                movieManager.updateMovie(existingMovie);
                movieExists = true;
                break;
            }
        }

        // If the movie is not in the TableView, add a new one
        if (!movieExists) {
            Movie newMovie = new Movie(title, imdbRating, personalRating, filePath);
            // Adds a new movie in the database
            movieManager.createMovie(newMovie);
            movieTableView.getItems().add(newMovie);
        }
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
