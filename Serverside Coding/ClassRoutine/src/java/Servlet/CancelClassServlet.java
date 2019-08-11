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
public class CancelClassServlet extends HttpServlet {
    
private PrintWriter printWriter;
private int routineID;
private String cancelDate;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        printWriter=response.getWriter();
        
        routineID= Integer.parseInt(request.getParameter("routineID")) ;
        cancelDate=request.getParameter("cancelDate");
        
        Routine myRoutine=new Routine();
        printWriter.print(myRoutine.cancelClass(routineID, cancelDate));
        
        
        
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }


}
