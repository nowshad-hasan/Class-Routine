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
public class ShowEmptyClassServlet extends HttpServlet {
    
    private int dayID,timeDuration;
    private String roomStatus;
    
    private PrintWriter printWriter;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
           printWriter = response.getWriter();

        dayID = Integer.parseInt(request.getParameter("dayID"));
        timeDuration = Integer.parseInt(request.getParameter("timeDuration"));
        roomStatus = request.getParameter("roomStatus");
        
        Routine routine=new Routine();
        printWriter.print(routine.getEmptyClassWeekly(dayID, timeDuration, roomStatus));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
