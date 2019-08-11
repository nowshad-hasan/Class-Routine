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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nowshad
 */
@WebServlet(name = "SingleDayStudentRoutineServlet", urlPatterns = {"/SingleDayStudentRoutineServlet"})
public class SingleDayStudentRoutineServlet extends HttpServlet {

private PrintWriter printWriter;
private int sessionID;

private String singleDate;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        printWriter = response.getWriter();
        
        sessionID=Integer.parseInt(request.getParameter("sessionID"));

singleDate=request.getParameter("singleDate");

Routine myRoutine=new Routine();
    try {
        printWriter.print(myRoutine.getSingleDayStudentRoutine(sessionID, singleDate));
    } catch (ParseException ex) {
        ex.printStackTrace();
    }
   
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
    }



}
