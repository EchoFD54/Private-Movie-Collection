package be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category {

    private StringProperty name;
    private IntegerProperty id;
    private StringProperty movies;

    public Category(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public Category(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public StringProperty getName() {
        return name;
    }

    public IntegerProperty getId() {
        return id;
    }

    public StringProperty getMovies() {
        return movies;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setMovies(String movies) {
        this.movies.set(movies);
    }
}
