package chatprogram;

import java.io.IOException;
import java.sql.*;

public class UserRegAuthDB {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASS = "root";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/chatdb";
    private Connection connectionDB;
    public void registration(String loginUserReg, String emailUserReg, int ageUserReg, String phoneUserReg, String passwordUserReg){
        try {
            this.connectionDB = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            String sqlAdd = "INSERT INTO public.users (id, loginuser, emailuser, ageuser, phoneuser, passworduser) VALUES (DEFAULT, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatementAdd = connectionDB.prepareStatement(sqlAdd);
            preparedStatementAdd.setString(1, loginUserReg);
            preparedStatementAdd.setString(2, emailUserReg);
            preparedStatementAdd.setInt(3, ageUserReg);
            preparedStatementAdd.setString(4, phoneUserReg);
            preparedStatementAdd.setString(5, passwordUserReg);
            preparedStatementAdd.executeUpdate();
            System.out.println(loginUserReg + "вы зарегистрированы!");
        } catch (SQLException sqlException) {
            System.out.println("БД SQL Exception: " + sqlException);
        }
    }

    public void authorization(String emailUserAuth, String passwordUserAuth){
        try {
            this.connectionDB = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            String sqlAuth = "SELECT id, loginuser, emailuser, ageuser, phoneuser, passworduser FROM users WHERE emailuser = ? AND passworduser = ?;";
            PreparedStatement preparedStatementAuth = connectionDB.prepareStatement(sqlAuth);
            preparedStatementAuth.setString(1, emailUserAuth);
            preparedStatementAuth.setString(2, passwordUserAuth);
            ResultSet resultSet = preparedStatementAuth.getResultSet();
            if (resultSet!= null){
                System.out.println(resultSet);
            } // else {
                //System.out.println("Вы не зарегистрированы!!!");
            //}
        } catch (SQLException sqlException) {
            System.out.println("БД SQL Exception: " + sqlException);
        }
    }
}
