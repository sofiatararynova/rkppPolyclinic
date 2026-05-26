package ru.kafpin.rkpppolyclinic;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL = "jdbc:postgresql://localhost:5432/polyclinica" ;
    private static final String LOGIN = "postgres";
    private static final String PASS = "1234";
    private static Connection connection;
    public static Connection getConnection(){
        if(connection == null ){
            try {
                connection = DriverManager.getConnection(URL,LOGIN, PASS);
            }catch(SQLException ex){
                throw new RuntimeException(ex);
            }

        }
        return connection;
    }
    public static void close(){
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
