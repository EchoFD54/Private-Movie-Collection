package dal;

import be.Category;
import be.Movie;
import java.util.List;

public interface IMovieDAO {

    /**
     * @return the generated key = Movie ID
     * Creates a Movie on the Database
     */
    int createMovie(Movie m);

    /**
     * Updates a Movie on the Database
     */
    void updateMovie(Movie m);

    /**
     * Updates the last view date of a Movie on the Database
     */
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
     * @return a list of all Categories of a specific Movie saved on the Database
     */
    List<Category> getAllCategoriesOfMovie(int movieId);

    /**
     * @return a list of all Movies with personal rating under 6
     * and last view date with more than 2 years saved on the Database
     */
    List<Movie> getAllOldMovies(String date);
}
