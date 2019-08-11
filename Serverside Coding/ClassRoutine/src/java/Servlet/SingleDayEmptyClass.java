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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nowshad
 */
public class SingleDayEmptyClass extends HttpServlet {

    
    private String singleDate;
    private int timeDuration;
    private PrintWriter printWriter;
    private String roomStatus;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        printWriter=response.getWriter();
        
        singleDate=request.getParameter("singleDate");
        timeDuration=Integer.parseInt(request.getParameter("timeDuration"));
        roomStatus=request.getParameter("roomStatus");
        
        Routine myRoutine=new Routine();
        try
        {
            printWriter.print(myRoutine.getSingleDayEmptyClass(singleDate, timeDuration, roomStatus));
            
            
        }catch(ParseException ex)
        {
            ex.printStackTrace();
        }
        
        
      
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }



}
