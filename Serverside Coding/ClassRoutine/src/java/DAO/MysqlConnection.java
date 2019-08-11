package DAO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nowshad
 */
public class MysqlConnection {
    
    String url = "jdbc:mysql://165.227.187.240:3306/ClassRoutine";
	String user = "root";
	String password = "123456";
	Connection con = null;

    public Connection getDbConnection() {
        
		

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver found");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found:" + e);
		}

		try {
			con = DriverManager.getConnection(url, user, password);
			System.out.println("Database Connection Successful");
		} catch (SQLException e) {
			System.out.println("Database Connection Failed");
		}

		return con;

	}

	public void CloseConnection() {
		try {
			if (con != null) {

				con.close();
				con = null;
			}
		} catch (SQLException e) {   
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
