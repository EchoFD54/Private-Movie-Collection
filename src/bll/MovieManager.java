package bll;

import be.Movie;
import dal.IMovieDAO;
import dal.MovieDAO;

import java.util.List;

public class MovieManager {
    IMovieDAO movieDAO = new MovieDAO();

    /**
     * Creates a Movie on the Database
     */
    public int createMovie(Movie m) {
        movieDAO.createMovie(m);
        return m;
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
}
