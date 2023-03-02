package DAO;

import DTO.Test;

import java.util.List;
import java.util.Optional;


public interface ITestDAO {

    Test getTestById(long id);

    Test getTestByName(String name);
    List<Test> getAllTests();
    boolean addTest(Test test);
    boolean updateTest(Test test);
    boolean deleteTest(long id);
}
