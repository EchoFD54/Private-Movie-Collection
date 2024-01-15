package gui.controllers;

import be.Category;
import be.Movie;
import bll.CategoryManager;
import bll.MovieManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainWindowController {
    @FXML
    public TextField filterTextField, minImdbRatingTextField;
    @FXML
    private TableView<Movie> movieTableView, movieInCatTableView;
    @FXML
    private Button newMovieBtn, deleteBtn, filterBtn, editMovieBtn;
    private int movieIndex;
    private int categoryIndex;
    @FXML
    private MovieManager movieManager = new MovieManager();
    @FXML
    public CategoryManager categoryManager = new CategoryManager();
    private Boolean isFilterActive = false;
    public TableView<Category> categoryTableView;

    private Category selectedCategory;

    @FXML
    private void initialize() {
        setMovieTableView();
        setButtons();
        dataBaseSetUp();
        setUpCategoryTableView();
        setUpMoviesInCategoryTableView();
    }

    private void setMovieTableView() {
        // Set up the columns in the TableView
        TableColumn<Movie, String> titleColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(0);
        TableColumn<Movie, String> imdbRatingColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(1);
        TableColumn<Movie, String> personalRatingColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(2);
        TableColumn<Movie, String> categories = (TableColumn<Movie, String>) movieTableView.getColumns().get(3);
        TableColumn<Movie, String> lastWatchedColumn = (TableColumn<Movie, String>) movieTableView.getColumns().get(4);

        // Define cell value factories for each column
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitle());
        imdbRatingColumn.setCellValueFactory(cellData -> cellData.getValue().getImdbRating());
        personalRatingColumn.setCellValueFactory(cellData -> cellData.getValue().getPersonalRating());
        categories.setCellValueFactory(cellData -> {
                    int movieId = cellData.getValue().getMovieId().get();
                    String totalCategories = categoriesInMovies(movieId).toString();
                    return new SimpleStringProperty(totalCategories);
                });
        lastWatchedColumn.setCellValueFactory(cellData -> cellData.getValue().getLastWatched());
    }

    private void setUpMoviesInCategoryTableView() {
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

    private void setUpCategoryTableView() {
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

    private int numberMoviesInCategory(int categoryId) {
        if (categoryId > 0) {
            return categoryManager.getAllMoviesOfCategory(categoryId).size();
        } else return -1;
    }

    public void setButtons() {
        // Set a listener for handling movie selection
        movieTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selectedMovieIndex = movieTableView.getSelectionModel().getSelectedIndex();
            if (selectedMovieIndex >= 0) {
                movieIndex = selectedMovieIndex;
                playSelectedMovie(newValue);
            }
        });

        // Set the event handler for the addMovies button
        newMovieBtn.setOnAction(this::openNewMovieWindow);

        //event handler for the filter function
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                clearFilter();
            }
        });

        //  listener for the minimum IMDB rating TextField
        minImdbRatingTextField.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());

        // Set listener for selecting a category.
        categoryTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Category selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
            this.selectedCategory = selectedCategory;
        });
    }

    private void dataBaseSetUp() {
        // Show all movies saved on the Database
        for (Movie m : movieManager.getAllMovies()) {
            movieTableView.getItems().add(m);
        }
        // Shows all categories saved on the Database
        for (Category c : categoryManager.getAllCategories()) {
            categoryTableView.getItems().add(c);
        }
    }

    public void moviesInCategory(MouseEvent mouseEvent) {
        movieInCatTableView.getItems().clear();
        for (Movie m : categoryManager.getAllMoviesOfCategory(categoryTableView.getSelectionModel().getSelectedItem().getId().get())) {
            movieInCatTableView.getItems().add(m);
        }
    }

    public List<Category> categoriesInMovies(int movieId) {
        return movieManager.getAllCategoriesOfMovie(movieId);
    }

    public void openNewMovieWindow(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/AddMovieWindow.fxml"));
        Parent root;
        try {
            root = loader.load();
            AddMovieWindowController addMovieController = loader.getController();
            addMovieController.setMainWindowController(this);
            Stage stage = new Stage();
            stage.setTitle("Add/Rate Movie");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateMovieProperties(String title, String imdbRating, String personalRating, String filePath) {
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
            int idMovie = movieManager.createMovie(newMovie);
            newMovie.setMovieId(idMovie);
            movieTableView.getItems().add(newMovie);
        }
    }

    private void playSelectedMovie(Movie selectedMovie) {
        String filePath = selectedMovie.getFilePath().get();
        File movieFile = new File(filePath);

        if (Desktop.isDesktopSupported() && movieFile.exists()) {
            try {
                Desktop.getDesktop().open(movieFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Cannot play the selected movie. File does not exist or Desktop is not supported.");
        }
    }

    public void openDeleteMovieWindow(ActionEvent actionEvent) {
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

    /**
     * Code not needed?
     * Or we can just refresh cat table view and use the category manager here (only)
     * Nelson
     */
    /*
    protected void createCategory(Category categoryName) {
        ObservableList<Category> categories = categoryTableView.getItems();
        categories.add(categoryName);  // Add the new category name to the list
        categoryTableView.setItems(categories);  // Update the category view
    }
     */

    public void deleteCategory(){
        categoryManager.deleteCategory(categoryTableView.getSelectionModel().getSelectedItem().getId().get());
        refreshCategoryTableView();
    }

    public void editCategory(String selectedCategoryName, Category newCategoryName) {
        newCategoryName.setName(selectedCategoryName);
        ObservableList<Category> categories = categoryTableView.getItems();
        int index = categories.indexOf(selectedCategoryName);
        if (index != -1) {
            categories.set(index, newCategoryName);  // Update the name of the category
            categoryTableView.setItems(categories);  // Update the category table view
        }
    }

    @FXML
    private void addSelectedMovieToCategory() {
        Integer categoryId = categoryTableView.getSelectionModel().getSelectedItem().getId().get();
        Integer movieId = movieTableView.getSelectionModel().getSelectedItem().getMovieId().get();
        if (categoryId != null && movieId != null) {
            categoryManager.placeMovieOnCategory(categoryId, movieId);
        }
        refreshCategoryTableView();
    }

    @FXML
    public void deleteMovieOnCategory(ActionEvent actionEvent) {
        Movie selectedMovie = movieInCatTableView.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            Category selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
            categoryManager.deleteMovieOnPlaylist(selectedMovie.getMovieId().get(), selectedCategory.getId().get());
            movieInCatTableView.getItems().remove(selectedMovie);
            refreshCategoryTableView();
        }
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
        String minImdbRatingStr = minImdbRatingTextField.getText();

        ObservableList<Movie> filteredMovies = FXCollections.observableArrayList();

        for (Movie movie : movieManager.getAllMovies()) {
            // Check if the title contains the filter query and IMDB rating is greater than or equal to the minimum
            if (movie.getTitle().get().toLowerCase().contains(filterQuery) && compareImdbRating(movie.getImdbRating().get(), minImdbRatingStr) >= 0) {
                filteredMovies.add(movie);
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

    private int compareImdbRating(String imdbRating1, String imdbRating2) {
        try {
            double rating1 = Double.parseDouble(imdbRating1);
            double rating2 = Double.parseDouble(imdbRating2);
            return Double.compare(rating1, rating2);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void openNewCategoryWindow(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/NewCategoryController.fxml"));
        Parent root;
        try {
            root = loader.load();
            NewCategoryController newCategoryController = loader.getController();
            newCategoryController.setMainWindowController(this);


            Stage stage = new Stage();
            stage.setTitle("Add Category");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEditCategoryWindow() {
        Category selectedCategoryName = categoryTableView.getSelectionModel().getSelectedItems().get(0);  // Retrieve the selected category name

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/EditCategory.fxml"));
            Parent root = loader.load();

            EditCategoryController editCategoryController = loader.getController();
            editCategoryController.setMainWindowController(this);
            editCategoryController.setSelectedCategoryName(selectedCategoryName); // Pass the selected category name

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Category");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openEditMovieWindow(ActionEvent actionEvent) {
        Movie selectedMovie = movieTableView.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/AddMovieWindow.fxml"));
            Parent root;
            try {
                root = loader.load();

                AddMovieWindowController addMovieController = loader.getController();

                addMovieController.setMainWindowController(this);

                // Set the fields in AddMovieWindowController with the selected movie properties
                addMovieController.titleField.setText(selectedMovie.getTitle().get());
                addMovieController.imdbRatingField.setText(selectedMovie.getImdbRating().get());
                addMovieController.personalRatingField.setText(selectedMovie.getPersonalRating().get());
                addMovieController.fileField.setText(selectedMovie.getFilePath().get());

                // Create a new stage for the AddMovieWindow
                Stage stage = new Stage();
                stage.setTitle("Edit Movie");
                stage.setScene(new Scene(root));

                addMovieController.setStage(stage);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert or message indicating that no Movie is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Movie Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Movie to edit.");
            alert.showAndWait();
        }
    }

    @FXML
    public void openRemoveCategoryWindow() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/RemoveCategory.fxml"));
            Parent root = loader.load();

            RemoveCategoryController deleteController = loader.getController();
            deleteController.setMainWindowController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Remove Category");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
