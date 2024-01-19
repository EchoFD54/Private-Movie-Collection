package dal;

import be.Category;
import be.Movie;
import java.util.List;


public interface ICategoryDAO {

    /**
     * Creates a Category on the Database
     */
    void createCategory(Category category);

    /**
     * Updates a Category on the Database by the ID
     */
    void updateCategory(String newCategoryName, String categoryName);

    /**
     * Deletes a Category on the Database by the ID
     */
    void deleteCategory(int categoryId);

    /**
     * @return a list of Categories saved on the database
     */
    List<Category> getAllCategories();

    /**
     * @return a list of movies from a specific category saved on the database
     */
    List<Movie> getAllMoviesOfCategory(int categoryId);

    /**
     * Associates a movie with a category in the database
     */
    void placeMovieOnCategory(int categoryId, int movieId);

    /**
     * Removes a movie from a category in the database
     */
    void removeMovieFromCategory(int movieId, int categoryId);

}
