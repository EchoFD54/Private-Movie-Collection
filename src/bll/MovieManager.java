package bll;

import be.Category;
import be.Movie;
import dal.MovieDAO;
import java.util.List;


public class MovieManager {
    MovieDAO movieDAO = new MovieDAO();

    /**
     * @return the generated key = Movie ID
     * Creates a Movie in the Database
     */
    public int createMovie(Movie m) {
        return movieDAO.createMovie(m);
    }

    /**
     * Updates a Movie in the Database
     */
    public void updateMovie(Movie m){
        movieDAO.updateMovie(m);
    }

    /**
     * Updates a movie's last view date in the Database
     */
    public void updateLastViewDate(Movie m, String date){
        movieDAO.updateLastViewDate(m, date);
    }

    /**
     * Deletes a Movie in the Database
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
     * @return a list of all Movies with personal rating under 6
     * and last view date with more than 2 years saved on the Database
     */
    public List<Movie> getAllOldMovies(String date){
        return movieDAO.getAllOldMovies(date);
    }
}


