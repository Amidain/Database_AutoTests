package DAO;

import DTO.Project;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import connection.ConnectionInstance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO implements IProjectDAO{

    private final ISettingsFile sqlQueryReader = new JsonSettingsFile("SQL_queries.json");
    private Connection connection = ConnectionInstance.getConnection();
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private ResultSet rs = null;

    @Override
    public Project getProjectById(int id) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/project_queries/getProjectById").toString());
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Project(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Project getProjectByName(String name) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/project_queries/getProjectByName").toString());
            preparedStatement.setString(1, name);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Project(
                        rs.getInt("id"),
                        rs.getString("name")
                );
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Project> getAllProjects() {
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sqlQueryReader.getValue("/project_queries/getAllProjects").toString());
            List<Project> projects = new ArrayList<>();

            while (rs.next()) {
                projects.add(new Project(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            return projects;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addProject(Project project) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/project_queries/addProject").toString(), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, project.getName());

            boolean addResult = preparedStatement.executeUpdate() == 1;

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                project.setId(generatedKeys.getLong(1));
            }
            return addResult;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateProject(Project project) {
        try{
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/project_queries/updateProject").toString());
            preparedStatement.setString(1, project.getName());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteProject(int id) {
        try{
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/project_queries/deleteProject").toString());
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean ifRecordExistsInDb(String name){
        List<Project> projects = getAllProjects();
        for (Project project: projects
        ) {
            if(project.getName().equals(name))
                return true;
        }
        return false;
    }
}
