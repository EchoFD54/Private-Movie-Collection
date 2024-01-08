package be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Movie {
    private StringProperty title;
    private StringProperty imdbRating;
    private StringProperty personalRating;
    private StringProperty lastWatched;
    private Category category;

    public Movie(String title, String imdbRating, String personalRating){
        this.title =new SimpleStringProperty(title);
        this.imdbRating = new SimpleStringProperty(imdbRating);
        this.personalRating = new SimpleStringProperty(personalRating);
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
}
