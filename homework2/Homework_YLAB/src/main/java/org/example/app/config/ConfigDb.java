package org.example.app.config;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**Конфигурация базы данных */


public class ConfigDb {

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private Properties properties = new Properties();

    // Конструктор для загрузки конфигурации из файла
    public ConfigDb() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("ERROR: Unable to find config.properties");
                return;
            }
            properties.load(input);
            this.dbUrl = properties.getProperty("url");
            this.dbUsername = properties.getProperty("username");
            this.dbPassword = properties.getProperty("password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Конструктор с параметрами для ручной конфигурации
    public ConfigDb(String url, String username, String password) {
        this.dbUrl = url;
        this.dbUsername = username;
        this.dbPassword = password;
    }

    // Геттеры для URL, Username, Password
    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    // Геттеры для Liquibase
    public String getLiquibaseChangeLogFile() {
        return properties.getProperty("changeLogFile", "db/changelog/db.changelog-master.xml");
    }

    public String getLiquibaseSchemaName() {
        return properties.getProperty("liquibaseSchemaName", "public");
    }

    public String getDefaultSchemaName() {
        return properties.getProperty("defaultSchemaName", "public");
    }

    // Метод для получения соединения с базой данных
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
}

    


