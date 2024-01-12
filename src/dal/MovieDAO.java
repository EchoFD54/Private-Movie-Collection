package dal;

import be.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements IMovieDAO{
    private final ConnectionManager cm = new ConnectionManager();

    @Override
    public int createMovie(Movie m) {
        try(Connection con = cm.getConnection())
        {
            String sql = "INSERT INTO Movies(Name, IMDBRating, PersonalRating, FilePath) VALUES (?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, m.getTitle().get());
            pstmt.setString(2, m.getImdbRating().get());
            pstmt.setString(3, m.getPersonalRating().get());
            pstmt.setString(4, m.getFilePath().get());
            //pstmt.setString(5, m.getLastWatched().get());
            pstmt.execute();

            //int affectedRows = pstmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                keys.next();
                int id = (int) keys.getLong(1);
                return id;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void updateMovie(Movie m) {
        try(Connection con = cm.getConnection())
        {
            String sql = "UPDATE Movies SET Name=?, PersonalRating=? WHERE Id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, m.getTitle().get());
            pstmt.setString(2, m.getPersonalRating().get());
            pstmt.setInt(3, m.getMovieId().get());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMovie(int movieId) {
        try(Connection con = cm.getConnection())
        {
            String sql = "DELETE FROM CatMovie WHERE MoviesId=?";
            String sql1 = "DELETE FROM Movies WHERE Id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            PreparedStatement pstmt1 = con.prepareStatement(sql1);
            pstmt.setInt(1, movieId);
            pstmt.execute();
            pstmt1.setInt(1, movieId);
            pstmt1.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();

        try(Connection con = cm.getConnection())
        {
            String sql = "SELECT * FROM Movies";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int id                = rs.getInt("Id");
                String name           = rs.getString("Name");
                String imdbRating     = rs.getString("IMDBRating");
                String personalRating = rs.getString("PersonalRating");
                String filePath       = rs.getString("FilePath");
                String lastView       = rs.getString("LastView");

                Movie m = new Movie(id, name, imdbRating, personalRating, filePath, lastView);
                movies.add(m);
            }
            return movies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
