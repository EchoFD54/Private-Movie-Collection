package dal;

import be.Category;
import be.Movie;

import java.util.List;

public interface ICategoryDAO {

    /**
     * Creates a Category on the Database
     * Values to create: Name
     */
    void createCategory(Category category);

    /**
     * Updates a Category on the Database by the ID
     * Values to update: Name
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
     * Associates and saves a movie with a category in the database
     */
    List<Movie> placeMovieOnCategory(int categoryId, int movieId);

    /**
     * @return a list of movies from a specific category saved on the database
     */
    List<Movie> getAllMoviesOfCategory(int categoryId);

    /**
     * @return a specific category saved on the database
     */
    Category getCategoryById(int categoryId);


}
