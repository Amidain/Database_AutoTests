package DAO;

import DTO.Session;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import connection.ConnectionInstance;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO implements ISessionDAO{
    private final ISettingsFile sqlQueryReader = new JsonSettingsFile("SQL_queries.json");
    private Connection connection = ConnectionInstance.getConnection();;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private ResultSet rs = null;

    @Override
    public Session getSessionById(int id) {
        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/session_queries/getSessionById").toString());
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Session(
                        rs.getInt("id"),
                        rs.getString("session_key"),
                        rs.getObject("created_time", LocalDateTime.class),
                        rs.getInt("build_number")
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Session> getAllSessions() {
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sqlQueryReader.getValue("/session_queries/getAllSessions").toString());
            List<Session> sessions = new ArrayList<>();

            while (rs.next()) {
                sessions.add(new Session(
                        rs.getInt("id"),
                        rs.getString("session_key"),
                        rs.getObject("created_time", LocalDateTime.class),
                        rs.getInt("build_number")
                ));
            }
            return sessions;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addSession(Session session) {
        boolean addResult;

        try {
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/session_queries/addSession").toString(), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, session.getSession_key());
            preparedStatement.setObject(2, session.getCreated_time());
            preparedStatement.setInt(3, session.getBuild_number());

            addResult = preparedStatement.executeUpdate() == 1;

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    session.setId(generatedKeys.getLong(1));
            }
            return addResult;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateSession(Session session) {
        try{
           preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/session_queries/updateSession").toString());
           preparedStatement.setInt(1, session.getBuild_number());
           preparedStatement.setLong(2, session.getId());
           return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteSession(int id) {
        try{
            preparedStatement = connection.prepareStatement(sqlQueryReader.getValue("/session_queries/deleteSession").toString());
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
