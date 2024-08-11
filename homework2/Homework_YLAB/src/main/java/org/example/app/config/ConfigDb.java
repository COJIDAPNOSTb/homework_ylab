package org.example.app.config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**Конфигурация базы данных */
public class ConfigDb {
   
   
        private Properties properties = new Properties();
    /**Конструктор для получения файла конфигурации */
        public ConfigDb() {
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    System.out.println("ERROR: Unable to find config.properties");
                    return;
                }
                properties.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    
        public String getDbUrl() {
            return properties.getProperty("url");
        }
    
        public String getDbUsername() {
            return properties.getProperty("username");
        }
    
        public String getDbPassword() {
            return properties.getProperty("password");
        }
        public String getLiquibaseChangeLogFile() {
            return properties.getProperty("changeLogFile");
        }
    
        public String getLiquibaseSchemaName() {
            return properties.getProperty("liquibaseSchemaName");
        }
    
        public String getDefaultSchemaName() {
            return properties.getProperty("defaultSchemaName");
        }
    }


