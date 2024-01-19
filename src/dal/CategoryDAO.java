package dal;

import be.Category;
import be.Movie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryDAO implements ICategoryDAO{

    private final ConnectionManager cm = new ConnectionManager();

    @Override
    public void createCategory(Category category) {
        try(Connection con = cm.getConnection())
        {
            String sql = "INSERT INTO Category(Name) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, category.getName().get());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCategory(String newCategoryName, String categoryName) {
        try(Connection con = cm.getConnection())
        {
            String sql = "UPDATE Category SET Name=? WHERE Name=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newCategoryName);
            pstmt.setString(2, categoryName);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        try(Connection con = cm.getConnection())
        {
            String sql = "DELETE FROM CatMovie WHERE CategoryId=?";
            String sql1 = "DELETE FROM Category WHERE Id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            PreparedStatement pstmt1 = con.prepareStatement(sql1);
            pstmt.setInt(1, categoryId);
            pstmt.execute();
            pstmt1.setInt(1, categoryId);
            pstmt1.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        try(Connection con = cm.getConnection())
        {
            String sql = "SELECT * FROM Category";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int id      = rs.getInt("Id");
                String name = rs.getString("Name");

                Category c = new Category(id, name);
                categories.add(c);
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movie> getAllMoviesOfCategory(int categoryId) {
        List<Movie> moviesInCategory = new ArrayList<>();
        try(Connection con = cm.getConnection())
        {
            String sql = "select *\n" +
                    "from Movies m\n" +
                    "inner join CatMovie cm on m.Id = cm.MoviesId\n" +
                    "where CategoryId = (?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id                = rs.getInt("Id");
                String name           = rs.getString("Name");
                String imdbRating     = rs.getString("IMDBRating");
                String personalRating = rs.getString("PersonalRating");
                String filePath       = rs.getString("FilePath");
                String lastView       = rs.getString("LastView");

                Movie m = new Movie(id, name, imdbRating, personalRating, filePath, lastView);
                moviesInCategory.add(m);
            }
            return moviesInCategory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeMovieOnCategory(int categoryId, int movieId) {
        try(Connection con = cm.getConnection())
        {
            String sql = "INSERT INTO CatMovie(CategoryId, MoviesId) VALUES (?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            pstmt.setInt(2, movieId);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeMovieFromCategory(int movieId, int categoryId) {
        try(Connection con = cm.getConnection())
        {
            String sql = "DELETE FROM CatMovie WHERE MoviesId=? AND CategoryId=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, movieId);
            pstmt.setInt(2, categoryId);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
