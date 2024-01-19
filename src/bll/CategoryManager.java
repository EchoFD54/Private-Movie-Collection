package bll;

import be.Category;
import be.Movie;
import dal.CategoryDAO;
import dal.ICategoryDAO;
import java.util.List;


public class CategoryManager {
    ICategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Creates a Category in the Database
     */
    public void createCategory(Category category) {
        categoryDAO.createCategory(category);
    }

    /**
     * Updates a Category in the Database
     */
    public void updateCategory(String newCategoryName, String categoryName) {
        categoryDAO.updateCategory(newCategoryName, categoryName);
    }

    /**
     * Deletes a Category in the Database
     */
    public void deleteCategory(int categoryId) {
        categoryDAO.deleteCategory(categoryId);
    }


    /**
     * @return a list of categories
     * Gets all the categories saved in the Database
     */
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    /**
     * Associates a movie to a category in the Database
     */
    public void placeMovieOnCategory(int categoryId, int movieId){
        categoryDAO.placeMovieOnCategory(categoryId, movieId);
    }

    /**
     * @return a list of movies
     * Gets all the movies from a category saved in the Database
     */
    public List<Movie> getAllMoviesOfCategory(int categoryId){
        return categoryDAO.getAllMoviesOfCategory(categoryId);
    }

    /**
     * Deletes a movie from a category in the Database
     */
    public void removeMovieFromCategory(int movieId, int categoryId){
        categoryDAO.removeMovieFromCategory(movieId, categoryId);
    }

}
