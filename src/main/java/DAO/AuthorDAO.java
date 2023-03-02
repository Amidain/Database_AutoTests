package DAO;

import DTO.Author;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import connection.ConnectionInstance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO implements IAuthorDAO{
    private final ISettingsFile sqlQueryReader = new JsonSettingsFile("SQL_queries.json");
    private Connection connection = ConnectionInstance.getConnection();;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private ResultSet rs = null;

    @Override
    public Author getAuthorById(int id) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/author_queries/getAuthorById").toString());
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Author(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("login"),
                        rs.getString("email")
                );
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Author getAuthorByName(String name) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/author_queries/getAuthorByName").toString());
            preparedStatement.setString(1, name);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Author(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("login"),
                        rs.getString("email")
                );
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Author> getAllAuthors() {
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sqlQueryReader.getValue("/author_queries/getAllAuthors").toString());
            List<Author> authors = new ArrayList<>();

            while (rs.next()) {
                authors.add(new Author(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("login"),
                        rs.getString("email"))
                );
            }
            return authors;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addAuthor(Author author) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/author_queries/addAuthor").toString(), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, author.getName());
            preparedStatement.setString(2, author.getLogin());
            preparedStatement.setString(3, author.getEmail());

            boolean addResult = preparedStatement.executeUpdate() == 1;

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setId(generatedKeys.getLong(1));
            }
            return addResult;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAuthor(Author author) {
        try{
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/author_queries/updateAuthor").toString());
            preparedStatement.setString(1, author.getName());
            preparedStatement.setString(2, author.getLogin());
            preparedStatement.setString(3, author.getEmail());
            preparedStatement.setLong(4, author.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAuthor(int id) {
        try{
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/author_queries/deleteAuthor").toString());
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean ifRecordExistsInDb(String name){
        List<Author> authors = getAllAuthors();
        for (Author author: authors
             ) {
            if(author.getName().equals(name))
                return true;
        }
        return false;
    }
}
