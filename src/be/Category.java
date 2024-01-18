package be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category {

    private StringProperty name;
    private IntegerProperty id;

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
        return this.id;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return  name.get();
    }
}
