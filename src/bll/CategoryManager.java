package bll;

import be.Category;
import be.Movie;
import dal.CategoryDAO;
import dal.ICategoryDAO;

import java.util.List;

public class CategoryManager {
    ICategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Creates a Category on the Database
     */
    public void createCategory(Category category) {
        categoryDAO.createCategory(category);
    }

    /**
     * Updates a Category on the Database
     */
    public void updateCategory(String newCategoryName, String categoryName) {
        categoryDAO.updateCategory(newCategoryName, categoryName);
    }

    /**
     * Deletes a Category on the Database
     */
    public void deleteCategory(int categoryId) {
        categoryDAO.deleteCategory(categoryId);
    }


    /**
     * @return a list of categories
     * Gets all the categories saved on the Database
     */
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    /**
     * @return a list of movies
     * Associates a movie to a category on the Database
     */
    public List<Movie> placeMovieOnCategory(int categoryId, int movieId){
        return categoryDAO.placeMovieOnCategory(categoryId, movieId);
    }

    /**
     * @return a list of movies
     * Gets all the movies from a category saved on the Database
     */
    public List<Movie> getAllMoviesOfCategory(int categoryId){
        return categoryDAO.getAllMoviesOfCategory(categoryId);
    }


}
