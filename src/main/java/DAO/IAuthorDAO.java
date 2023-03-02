package DAO;

import DTO.Author;

import java.util.List;
import java.util.Optional;

public interface IAuthorDAO {
    Author getAuthorById(int id);
    Author getAuthorByName(String name);
    List<Author> getAllAuthors();
    boolean addAuthor(Author author);
    boolean updateAuthor(Author author);
    boolean deleteAuthor(int id);
}
