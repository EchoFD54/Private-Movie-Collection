package bll;

import be.Category;
import be.Movie;
import dal.MovieDAO;

import java.util.List;

public class MovieManager {
    MovieDAO movieDAO = new MovieDAO();

    /**
     * Creates a Movie on the Database
     */
    public int createMovie(Movie m) {
        return movieDAO.createMovie(m);
    }

    /**
     * Updates a Movie on the Database
     */
    public void updateMovie(Movie m){
        movieDAO.updateMovie(m);
    }

    /**
     * Deletes a Movie on the Database
     */
    public void deleteMovie(int movieId){
        movieDAO.deleteMovie(movieId);
    }

    /**
     * @return a list of all Movies saved on the Database
     */
    public List<Movie> getAllMovies() {
        return movieDAO.getAllMovies();
    }

    /**
     * @return a list of all Movies saved on the Database
     */
    public List<Category> getAllCategoriesOfMovie(int movieId) {
        return movieDAO.getAllCategoriesOfMovie(movieId);
    }

    /**
     * Deletes a movie from a category on the Database
     */
    public void confirmRemoveMovieFromCategoryButton(int movieId){
        movieDAO.confirmRemoveMovieFromCategoryButton(movieId);
    }
}


