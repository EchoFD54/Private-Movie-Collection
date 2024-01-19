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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class MainWindowController {
    @FXML
    private TextField filterTextField, minImdbRatingTextField;
    @FXML
    private Button newMovieBtn, filterBtn;
    @FXML
    private int movieIndex;
    @FXML
    private MovieManager movieManager = new MovieManager();
    @FXML
    private CategoryManager categoryManager = new CategoryManager();
    @FXML
    private Boolean isFilterActive = false;
    @FXML
    private TableView<Category> categoryTableView;
    @FXML
    private TableView<Movie> movieTableView, movieInCatTableView;
    @FXML
    private Movie selectedMovie;


    @FXML
    private void initialize() {
        setMovieTableView();
        setButtons();
        dataBaseSetUp();
        setUpCategoryTableView();
        setUpMoviesInCategoryTableView();
        showAlert(oldAndUnder6RatingMovies());
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
            List<Category> sortedCategories = new ArrayList<>(categoriesInMovies(movieId));
            sortedCategories.sort(Comparator.comparing(o -> o.getName().get()));

            String totalCategories = sortedCategories.stream()
                    .map(category -> category.getName().get())
                    .collect(Collectors.joining(", "));

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

    @FXML
    private void setButtons() {
        // Set a listener for handling movie selection
        movieTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selectedMovieIndex = movieTableView.getSelectionModel().getSelectedIndex();
            if (selectedMovieIndex >= 0) {
                movieIndex = selectedMovieIndex;
                selectedMovie = newValue;
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


    private int numberMoviesInCategory(int categoryId) {
        if (categoryId > 0) {
            // @return the number of movies in each category
            return categoryManager.getAllMoviesOfCategory(categoryId).size();
        } else return -1;
    }

    @FXML
    private void moviesInCategory(MouseEvent mouseEvent) {
        movieInCatTableView.getItems().clear();
        for (Movie m : categoryManager.getAllMoviesOfCategory(categoryTableView.getSelectionModel().getSelectedItem().getId().get())) {
            // Gets all movies of the selected category and show it on movieInCatTableView
            movieInCatTableView.getItems().add(m);
        }
    }

    private List<Category> categoriesInMovies(int movieId) {
        // @return a list of categories of each movie
        return movieManager.getAllCategoriesOfMovie(movieId);
    }

    protected void updateMovieProperties(String title, String imdbRating, String personalRating, String filePath) {
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
                // Updates the movie properties in the database
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Play error");
            alert.setHeaderText(null);
            alert.setContentText("Movie file does not exist or is not supported");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickPlayBtn(ActionEvent actionEvent) {
        // Check if a movie is selected in the TableView
        if (selectedMovie != null) {
            playSelectedMovie(selectedMovie);
            movieManager.updateLastViewDate(selectedMovie, todayDate());
            movieInCatTableView.getItems().clear();;
            refreshMoviesTableView();
        } else {
            // Show an alert or message indicating that no Movie is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Movie Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Movie to play.");
            alert.showAndWait();
        }
    }

    protected void createCategory(Category category) {
        // Creates a new category on the database
        categoryManager.createCategory(category);
        refreshCategoryTableView();
        movieInCatTableView.getItems().clear();
    }

    protected void editCategory(String newCategoryName, Category selectedCategoryName) {
        categoryManager.updateCategory(newCategoryName, selectedCategoryName.getName().get()); // Updates a category on the database
        selectedCategoryName.setName(newCategoryName);
        ObservableList<Category> categories = categoryTableView.getItems();
        int index = categories.indexOf(selectedCategoryName);
        if (index != -1) {
            categories.set(index, selectedCategoryName);  // Update the name of the category
            categoryTableView.setItems(categories);  // Update the category table view
            refreshMoviesTableView();
        }
    }

    protected void deleteCategory() {
        // Deletes a category on the database
        categoryManager.deleteCategory(categoryTableView.getSelectionModel().getSelectedItem().getId().get());
        // Refreshes all table views
        refreshCategoryTableView();
        refreshMoviesTableView();
        movieInCatTableView.getItems().clear();
    }

    @FXML
    private void addSelectedMovieToCategory() {
        Integer categoryId = categoryTableView.getSelectionModel().getSelectedItem().getId().get();
        Integer movieId = movieTableView.getSelectionModel().getSelectedItem().getMovieId().get();
        if (categoryId != null && movieId != null) {
            // Assigns a movie to a category on the database
            categoryManager.placeMovieOnCategory(categoryId, movieId);
        }
        // Refreshes all table views
        refreshCategoryTableView();
        refreshMoviesTableView();
        movieInCatTableView.getItems().clear();
    }

    protected void confirmRemoveMovieFromCategoryButton() {
        Movie selectedMovie = movieInCatTableView.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            Category selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
            // Removes a movie from a category on the database
            categoryManager.removeMovieFromCategory(selectedMovie.getMovieId().get(), selectedCategory.getId().get());
            movieInCatTableView.getItems().remove(selectedMovie);
            refreshCategoryTableView();
            refreshMoviesTableView();
        }
    }

    private void refreshCategoryTableView() {
        // Clears the table, gets all categories from the database and show them on the table view
        categoryTableView.getItems().clear();
        List<Category> allCategories = categoryManager.getAllCategories();
        categoryTableView.getItems().addAll(allCategories);
    }

    private void refreshMoviesTableView() {
        // Clears the table, gets all movies from the database and show them on the table view
        movieTableView.getItems().clear();
        List<Movie> allMovies = movieManager.getAllMovies();
        movieTableView.getItems().addAll(allMovies);
    }

    @FXML
    private void toggleFilterBtn(ActionEvent actionEvent) {
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
            // Check if the title or categories contain the filter query
            if ((movie.getTitle().get().toLowerCase().contains(filterQuery) ||
                    containsCategories(movie, filterQuery)) &&
                    compareImdbRating(movie.getImdbRating().get(), minImdbRatingStr) >= 0) {
                filteredMovies.add(movie);
            }
        }

        movieTableView.setItems(filteredMovies);
        filterBtn.setText("Clear");
        isFilterActive = true;
        movieInCatTableView.getItems().clear();
    }

    private boolean containsCategories(Movie movie, String filterQuery) {
        List<Category> movieCategories = categoriesInMovies(movie.getMovieId().get());
        Set<String> categoryNames = new HashSet<>();

        // Convert Category objects to strings
        for (Category category : movieCategories) {
            categoryNames.add(category.getName().get().toLowerCase());
        }

        Set<String> searchCategories = new HashSet<>(Arrays.asList(filterQuery.split(", ")));

        return categoryNames.containsAll(searchCategories);
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

    private String todayDate(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(currentDate);
    }

    private List<Movie> oldAndUnder6RatingMovies() {
        LocalDate today = LocalDate.now();
        LocalDate moreThan2Years = today.minusYears(2);
        // @returns a list of movies with personal rating < 6 and last view date with more than 2 years
        return movieManager.getAllOldMovies(String.valueOf(moreThan2Years));
    }

    private void showAlert(List<Movie> movies) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/AlertWindow.fxml"));
        Parent root;
        try {
            root = loader.load();
            AlertWindowController alertWindowController = loader.getController();
            alertWindowController.initialize(movies); // Pass a list of Movies
            alertWindowController.setMainWindowController(this);


            Stage stage = new Stage();
            stage.setTitle("REMINDER");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openNewMovieWindow(ActionEvent actionEvent) {
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
            addMovieController.addMovieBtn.setText("Add Movie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEditMovieWindow(ActionEvent actionEvent) {
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
                stage.setTitle("Rate/Edit Movie");
                stage.setScene(new Scene(root));
                addMovieController.setStage(stage);
                stage.show();
                addMovieController.addMovieBtn.setText("Rate/Edit Movie");
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
    private void openDeleteMovieWindow(ActionEvent actionEvent) {
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
                    movieManager.deleteMovie(selectedMovie.getMovieId().get());
                    movieTableView.getItems().remove(selectedIndex);
                    refreshCategoryTableView();
                    movieInCatTableView.getItems().clear();
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

    @FXML
    private void openNewCategoryWindow(ActionEvent actionEvent) {
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

    @FXML
    private void openRemoveCategoryWindow() {
        try {
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

    @FXML
    private void openRemoveMovieFromCategoryWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/RemoveMovieFromCategory.fxml"));
            Parent root = loader.load();

            RemoveMovieFromCategoryController removeController = loader.getController();
            removeController.setMainWindowController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Remove Movie From this Category");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

