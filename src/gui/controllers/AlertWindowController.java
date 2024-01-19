package gui.controllers;

import be.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.util.List;

public class AlertWindowController {

    public TableView<Movie> movieTableView;
    public Button btn;

    public void setMainWindowController(MainWindowController mainWindowController) {
    }

    public void initialize(List<Movie> movies) {
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

        ObservableList<Movie> movieData = FXCollections.observableArrayList(movies);
        movieTableView.setItems(movieData);
    }

    public void okBtn(ActionEvent actionEvent) {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }
}
