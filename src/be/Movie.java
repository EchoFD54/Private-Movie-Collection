package be;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Movie {
    private StringProperty title;
    private StringProperty imdbRating;
    private StringProperty personalRating;
    private StringProperty lastWatched;
    private StringProperty filePath;
    private Category category;

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
}
