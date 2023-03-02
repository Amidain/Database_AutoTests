package connection;

import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionInstance {

    private final static ISettingsFile configReader = new JsonSettingsFile("config.json");
    private static final String PROTOCOL = configReader.getValue("/db_connection/protocol").toString();
    private static final String HOSTNAME = configReader.getValue("/db_connection/hostname").toString();
    private static final String PORT = configReader.getValue("/db_connection/port").toString();
    private static final String DB_NAME = configReader.getValue("/db_connection/db_name").toString();
    private static final String USER = configReader.getValue("/credentials/user").toString();
    private static final String PASSWORD = configReader.getValue("/credentials/password").toString();
    private static final String DB_URL = String.format("%s%s:%s/%s", PROTOCOL, HOSTNAME, PORT, DB_NAME);
    public static Connection getConnection(){
        try{
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e){
            throw  new RuntimeException("Error connecting to the database",e);
        }
    }
}
