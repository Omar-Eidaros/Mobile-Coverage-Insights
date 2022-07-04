package com.insights.mesurements;

import javax.ws.rs.DELETE;
import java.sql.*;
import java.util.Properties;

public class DataBase {

    private static final String DATABASE_URL = "jdbc:postgresql://bj7rwhghwwqosiqsms9h-postgresql.services.clever-cloud.com:5432/bj7rwhghwwqosiqsms9h";
    private static final String USERNAME = "unjxtkqihomy5nurhj1c";
    private static final String PASSWORD = "J815kWnvzd3sw9A2ThWy";

    // init connection object
    private Connection connection;

    // init properties object
    private Properties properties;

    // init the statement
    private Statement statement = null;
    private PreparedStatement prepStatement = null;

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
        }
        return properties;
    }

    public Connection connect() {
        if (connection == null) {
            try {
                //  Class.forName(DATABASE_DRIVER);
                connection = (Connection) DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Return the result set when a correct SQL statement is provided
    public ResultSet select(String query) throws SQLException {
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return resultSet;
    }

     //Disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                if (statement != null) {
                    statement.close();
                }
                if (prepStatement != null) {
                    prepStatement.close();
                }
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Return the status when a SQL query is provided for INSERT, UPDATE or DELETE
    public int DML(String query) throws SQLException {
        prepStatement = connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        int result = prepStatement.executeUpdate();
        return result;
    }
}