package dal;

import be.Category;
import be.Movie;

import java.util.List;

public interface IMovieDAO {

    /**
     * Creates a Movie on the Database
     */
    int createMovie(Movie m);

    /**
     * Updates a Movie on the Database
     */
    void updateMovie(Movie m);

    void updateLastViewDate(Movie m, String date);

    /**
     * Deletes a Movie on the Database by the ID
     */
    void deleteMovie(int movieId);

    /**
     * @return a list of all Movies saved on the Database
     */
    List<Movie> getAllMovies();

    /**
     * @return a list of all Movies saved on the Database
     */
    List<Category> getAllCategoriesOfMovie(int movieId);

    public List<Movie> getAllOldMovies(String date);
}
