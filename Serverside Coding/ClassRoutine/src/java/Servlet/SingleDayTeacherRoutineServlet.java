/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DAO.Routine;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nowshad
 */
public class SingleDayTeacherRoutineServlet extends HttpServlet {

private PrintWriter printWriter;
private int teacherID;

private String singleDate;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        printWriter = response.getWriter();
        
                teacherID=Integer.parseInt(request.getParameter("teacherID"));

singleDate=request.getParameter("singleDate");

Routine myRoutine=new Routine();
    try {
        printWriter.print(myRoutine.getSingleDayTeacherRoutine(teacherID, singleDate)); ;
    } catch (ParseException ex) {
        ex.printStackTrace();
    }
       
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }


}
