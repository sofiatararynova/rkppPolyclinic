package ru.kafpin.rkpppolyclinic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {
    private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);
    private static Connection connection;
    private static String dbUrlBase;
    private static String dbName;

    static {
        try (InputStream input = DBHelper.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            dbUrlBase = prop.getProperty("db.url");
            dbName = prop.getProperty("db.name");
            logger.debug("Загружены настройки подключения из config.properties");
        } catch (Exception e) {
            logger.error("Не удалось загрузить config.properties", e);
            throw new RuntimeException("config.properties not found", e);
        }
    }

    public static void initConnection(String user, String password) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            closeConnection();
        }
        String fullUrl = dbUrlBase + dbName;
        logger.info("Попытка подключения к {} пользователем {}", fullUrl, user);
        connection = DriverManager.getConnection(fullUrl, user, password);
        logger.info("Соединение с БД успешно установлено для {}", user);
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Соединение не инициализировано. Вызовите initConnection()");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Соединение с БД закрыто");
            } catch (SQLException e) {
                logger.error("Ошибка при закрытии соединения", e);
            }
        }
    }
}