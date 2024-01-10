package gui.controllers;

import be.Category;
import be.Movie;
import bll.CategoryManager;
import bll.MovieManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainWindowController {
    @FXML
    public TextField filterTextField;
    @FXML
    private TableView<Category> categoryTableView;
    @FXML
    private TableView<Movie> movieTableView, movieInCatTableView;
    @FXML
    private Button newMovieBtn, deleteBtn, filterBtn;
    @FXML
    private int movieIndex;
    @FXML
    private MovieManager movieManager = new MovieManager();
    @FXML
    private CategoryManager categoryManager = new CategoryManager();
    private Boolean isFilterActive = false;

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
                playSelectedMovie(newValue);
            }
        });

        // Set the event handler for the addMovies button
        newMovieBtn.setOnAction(this::clickNewMovieBtn);

        //event handler for the filter function
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                clearFilter();
            }
        });
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
        String filePath = selectedMovie.getFilePath().get();
        File movieFile = new File(filePath);

        if (Desktop.isDesktopSupported() && movieFile.exists()) {
            try {
                Desktop.getDesktop().open(movieFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(filePath);
            System.out.println("Cannot play the selected movie. File does not exist or Desktop is not supported.");
        }
    }

    public void clickDelBtn(ActionEvent actionEvent) {
        Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete the selected movie?");
            alert.setContentText("This action cannot be undone.");

            // Handle the users response
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User clicked OK, delete the movie
                    int selectedIndex = movieTableView.getSelectionModel().getSelectedIndex();

                    // Remove the movie from the Database, TableView and Categories
                    movieManager.deleteMovie(selectedMovie.getMovieId().getValue());
                    movieTableView.getItems().remove(selectedIndex);
                    refreshCategoryTableView();
                }
            });
        } else {
            // Show a message saying that no movie is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Movie Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a movie to delete.");
            alert.showAndWait();
        }

    }

    public void refreshCategoryTableView() {
        categoryTableView.getItems().clear();
        List<Category> allCategories = categoryManager.getAllCategories();
        categoryTableView.getItems().addAll(allCategories);
    }

    public void editCategory(String selectedCategoryName, Category newCategoryName){
        newCategoryName.setName(selectedCategoryName);
        ObservableList<Category> categories = categoryTableView.getItems();
        int index = categories.indexOf(selectedCategoryName);
        if (index != -1){
            categories.set(index, newCategoryName);  // Update the name of the category
            categoryTableView.setItems(categories);  // Update the category table view
        }
        refreshCategoryTableView();
    }

    public void deleteCategory(){
        categoryManager.deleteCategory(categoryTableView.getSelectionModel().getSelectedItem().getId().get());
        refreshCategoryTableView();
    }

    public void toggleFilterBtn(ActionEvent actionEvent) {
        if (isFilterActive) {
            clearFilter();
        } else {
            applyFilter();
        }
    }

    private void applyFilter() {
        String filterQuery = filterTextField.getText().toLowerCase();
        ObservableList<Movie> filteredMovies = FXCollections.observableArrayList();

        for (Movie song : movieManager.getAllMovies()) {
            if (song.getTitle().get().toLowerCase().contains(filterQuery))  { //|| song.artistProperty().get().toLowerCase().contains(filterQuery))
                filteredMovies.add(song);
            }
        }

        movieTableView.setItems(filteredMovies);
        filterBtn.setText("Clear");
        isFilterActive = true;
    }

    private void clearFilter() {
        movieTableView.setItems(FXCollections.observableArrayList(movieManager.getAllMovies()));
        filterTextField.clear();
        filterBtn.setText("Filter");
        isFilterActive = false;
    }

}
