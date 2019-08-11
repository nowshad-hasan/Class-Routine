package Servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import DAO.MysqlConnection;
import DAO.Routine;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nowshad
 */
@WebServlet(urlPatterns = {"/ShowStudentRoutineServlet"})
public class ShowStudentRoutineServlet extends HttpServlet {

private int sessionID;
private int dayID;
  private PrintWriter printWriter;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
//        MysqlConnection mysqlConnection=new MysqlConnection();
//        Connection dbConnection = mysqlConnection.getDbConnection();
printWriter = response.getWriter();
Routine myRoutine=new Routine();

sessionID=Integer.parseInt(request.getParameter("sessionID"));
dayID=Integer.parseInt(request.getParameter("dayID"));

System.out.println(sessionID+" "+dayID);


    JsonObject jsonObject=myRoutine.getStudentRoutineWeekly(sessionID, dayID);
    printWriter.print(jsonObject);


        

//http://localhost:8080/ClassRoutine/ShowRoutineServlet?sessionID=3&&dayID=2
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }



}
