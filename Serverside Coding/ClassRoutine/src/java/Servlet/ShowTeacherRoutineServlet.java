/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DAO.Routine;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nowshad
 */
public class ShowTeacherRoutineServlet extends HttpServlet {

private int teacherID;
private int dayID;
  private PrintWriter printWriter;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        printWriter = response.getWriter();
Routine myRoutine=new Routine();

teacherID=Integer.parseInt(request.getParameter("teacherID"));
dayID=Integer.parseInt(request.getParameter("dayID"));

printWriter.print(myRoutine.getTeachertRoutineWeekly(teacherID, dayID));
       
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }



}
