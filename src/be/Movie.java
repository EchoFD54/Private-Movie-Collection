package be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Movie {
    private StringProperty title;
    private StringProperty imdbRating;
    private StringProperty personalRating;
    private StringProperty lastWatched;
    private StringProperty filePath;
    private IntegerProperty movieId = new SimpleIntegerProperty();
    private Category category;

    public Movie(int movieId, String title, String imdbRating, String personalRating, String filePath, String lastWatched){
        this.movieId = new SimpleIntegerProperty(movieId);
        this.title = new SimpleStringProperty(title);
        this.imdbRating = new SimpleStringProperty(imdbRating);
        this.personalRating = new SimpleStringProperty(personalRating);
        this.filePath = new SimpleStringProperty(filePath);
        this.lastWatched = new SimpleStringProperty(lastWatched);
    }

    public Movie(String title, String imdbRating, String personalRating, String filePath){
        this.title =new SimpleStringProperty(title);
        this.imdbRating = new SimpleStringProperty(imdbRating);
        this.personalRating = new SimpleStringProperty(personalRating);
        this.filePath = new SimpleStringProperty(filePath);
    }

    public StringProperty getTitle() {
        return title;
    }

    public StringProperty getImdbRating(){
        return imdbRating;
    }

    public StringProperty getPersonalRating(){
        return personalRating;
    }

    public StringProperty getLastWatched(){
        return lastWatched;
    }

    public StringProperty getFilePath(){
        return filePath;
    }

    public IntegerProperty getMovieId(){
        return movieId;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating.set(imdbRating);
    }

    public void setPersonalRating(String personalRating) {
        this.personalRating.set(personalRating);
    }

    public void setLastWatched(String lastWatched) {
        this.lastWatched.set(lastWatched);
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public void setMovieId(int movieId) {
        this.movieId.set(movieId);
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
