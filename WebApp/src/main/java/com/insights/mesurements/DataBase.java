package com.insights.mesurements;

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
                Class.forName("org.postgresql.Driver");
                connection = (Connection) DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (SQLException e) {
                e.printStackTrace();
            }catch (ClassNotFoundException c){
                c.printStackTrace();
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
        System.out.println("connection in db : "+query);
        prepStatement = connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        int result = prepStatement.executeUpdate();
        System.out.println("connection in db : "+connection);
        System.out.println("result of query " +result);
        return result;
    }
    public int DMLSpecial(String query) throws SQLException {
        System.out.println("connection in db : "+query);
        prepStatement = connection.prepareStatement("Insert into  measurements (id,cell_id,msisdn_id,cell_type,mcc,mnc,country,operator,signal_strength_level,imei,imsi,latitude,longitude,device_model) " +
                "values(default,?,?,?,'602','2','EGYPT','we',60,'dshajgdhsaj','dhsakj',30.284031633867613, 31.20247799547045,'oppo');", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        prepStatement.setString(1,"5000");
        prepStatement.setInt(2,370);
        prepStatement.setString(3,"CDMA");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");
        prepStatement.setString(1,"5000");

        int result = prepStatement.executeUpdate();
        System.out.println("connection in db : "+connection);
        System.out.println("result of query " +result);
        return result;
    }
}