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
public class UserDetailsServlet extends HttpServlet {
    
     private PrintWriter printWriter;
     private int userID;
     private String userRole;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        printWriter=response.getWriter();
        Routine myRoutine=new Routine();
        
        userID=Integer.parseInt(request.getParameter("userID"));
        userRole=request.getParameter("userRole");
        
        printWriter.print(myRoutine.getUserDetails(userID, userRole));
        
        
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }



}
