package DAO;

import DTO.Test;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import connection.ConnectionInstance;
import util.NumberGenerator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestDAO implements ITestDAO {
    private final ISettingsFile sqlQueryReader = new JsonSettingsFile("SQL_queries.json");
    private Connection connection = ConnectionInstance.getConnection();
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;

    private Test extractUserFromResultSet(ResultSet resultSet){
        try{
            Test test = new Test(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("status_id"),
                    resultSet.getString("method_name"),
                    resultSet.getInt("project_id"),
                    resultSet.getInt("session_id"),
                    resultSet.getObject("start_time", LocalDateTime.class),
                    resultSet.getObject("end_time", LocalDateTime.class),
                    resultSet.getString("env"),
                    resultSet.getString("browser"),
                    resultSet.getInt("author_id")
            );
            return test;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Test getTestById(long id) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/test_queries/getTestById").toString());
            preparedStatement.setLong(1, id);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        }catch (SQLException e){
                e.printStackTrace();
            }
        return null;
    }

    @Override
    public Test getTestByName(String name) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/test_queries/getTestByName").toString());
            preparedStatement.setString(1, name);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Test> getAllTests() {
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sqlQueryReader.getValue("/test_queries/getAllTests").toString());
            List<Test> tests = new ArrayList<>();

            while (rs.next()) {
                tests.add(extractUserFromResultSet(rs));
            }

            return tests;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addTest(Test test) {
        boolean addResult;

        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/test_queries/addTest").toString(), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, test.getName());
            preparedStatement.setLong(2, test.getStatus_id());
            preparedStatement.setString(3, test.getMethod_name());
            preparedStatement.setLong(4, test.getProject_id());
            preparedStatement.setLong(5, test.getSession_id());
            preparedStatement.setObject(6, test.getStart_time());
            preparedStatement.setObject(7, test.getEnd_time());
            preparedStatement.setString(8, test.getEnv());
            preparedStatement.setString(9, test.getBrowser());
            preparedStatement.setLong(10, test.getAuthor_id());

            addResult = preparedStatement.executeUpdate() == 1;

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                test.setId(generatedKeys.getLong(1));
            }
            return addResult;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateTest(Test test) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/test_queries/updateTest").toString());
            preparedStatement.setString(1, test.getName());
            preparedStatement.setLong(2, test.getStatus_id());
            preparedStatement.setString(3, test.getMethod_name());
            preparedStatement.setLong(4, test.getProject_id());
            preparedStatement.setLong(5, test.getSession_id());
            preparedStatement.setObject(6, test.getStart_time());
            preparedStatement.setObject(7, test.getEnd_time());
            preparedStatement.setString(8, test.getEnv());
            preparedStatement.setString(9, test.getBrowser());
            preparedStatement.setLong(10, test.getAuthor_id());
            return preparedStatement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTest(long id) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/test_queries/deleteTest").toString());
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Test> getTestWithSameDigits(){
        List<Test> testWithSameDigits = new ArrayList<>();
        int digit = NumberGenerator.generateRandomNumberBetweenOneToNine();

        try{
                preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/test_queries/getTestsWithTwoRepeatingDigitsInId").toString());
                preparedStatement.setString(1, String.format("%s%s%s%s","%",digit,digit, "%"));
                rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    testWithSameDigits.add(extractUserFromResultSet(rs));
                }
                return testWithSameDigits;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean ifRecordExistsInDb(String name){
        List<Test> tests = getAllTests();
        for (Test test: tests
        ) {
            if(test.getName().equals(name))
                return true;
        }
        return false;
    }
}
