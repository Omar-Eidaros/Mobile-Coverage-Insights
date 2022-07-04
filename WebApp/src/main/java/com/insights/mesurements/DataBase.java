package com.insights.mesurements;

import java.sql.*;
import java.util.Properties;

public class DataBase {

    // init database constants
//    private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
   private static final String DATABASE_URL = "jdbc:postgresql://batyr.db.elephantsql.com:5432/osaucpzr";
    private static final String USERNAME = "osaucpzr";
    private static final String PASSWORD = "cUEsju_L6bGAdmEuTjryKwPZbuN4qYh7";

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

    /**
     * Connect to the database
     *
     * @return Connection
     */
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

    /**
     * Disconnect database
     */
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

    /**
     * Return the result set when a correct SQL statement is provided
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public ResultSet select(String query) throws SQLException {
        statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    /**
     * Return the status when a SQL query is provided for INSERT, UPDATE or
     * DELETE
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public int DML(String query) throws SQLException {
        prepStatement = connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        int result = prepStatement.executeUpdate();
        return result;
    }
    
    
  
  
}