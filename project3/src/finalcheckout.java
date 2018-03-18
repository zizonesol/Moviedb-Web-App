

import java.io.*;
import java.net.*;
import java.text.*;
import java.sql.*;
import java.util.*;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

/**
 * Servlet implementation class finalcheckout
 */
@WebServlet("/finalcheckout")
public class finalcheckout extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
        if(session.isNew())
        {
	        	session.setAttribute("loginsuss", "no");
	        	response.sendRedirect("/project3/servlet/welcome");
	        	
        }
        else
        {
        		if(session.getAttribute("loginsuss").equals("no"))
        		{
        			response.sendRedirect("/project3/servlet/welcome");
        		}
        }
        
        String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        
        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Confirm Order</TITLE></HEAD>");
        out.println("<BODY><H1>Confirm Order</H1>");
        out.println("<TABLE border>");
        
        try
        {
        		Context initCtx = new InitialContext();
        		if (initCtx == null)
        			out.println("initCtx is NULL");
        		
        		Context envCtx = (Context) initCtx.lookup("java:comp/env");
        		if (envCtx == null)
        			out.println("envCtx is NULL");
        		
        		DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
        		if (ds == null)
        			out.println("ds is NULL");
        		
        		Connection dbcon = ds.getConnection();
        		if (dbcon == null)
        			out.println("dbcon is NULL");
        	
           //Class.forName("org.gjt.mm.mysql.Driver");
           //Class.forName("com.mysql.jdbc.Driver").newInstance();

           //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           //Statement statement = dbcon.createStatement();
        	   PreparedStatement statement = null;
        		
           ArrayList<String> mlist = (ArrayList<String>) session.getAttribute("mlist");
           
           out.println("<TABLE border>");

           
           out.println("<tr>" + "<td>" + "Title" + "</td>" + "<td>" + "Amount" + "</td>" + "</tr>");
           if(mlist != null)
           {
	           for(String n : mlist)
	           {
		           String query = "select * from movies\r\n" + 
		           		"where id = ?\r\n" + 
		           		"limit 1;";
		           statement = dbcon.prepareStatement(query);
		           
		           statement.setString(1, n);
		       
		           statement.execute();
		           ResultSet rs = statement.getResultSet();
		           String am = (String) session.getAttribute(n);
		           
		           while (rs.next()) {
		               String m_title = rs.getString("title");
	
		               out.println("<tr>" + "<td>" + m_title + "</td>" + "<td>" + am  + "</td>" + "</tr>");
		           }
		           rs.close();
	           }
           }
           out.println("</TABLE></div>");
           
           if(session.getAttribute("ccauth") != null)
           {
        	   if(session.getAttribute("ccauth").equals("fail"))
        	   {
        		   out.println("<p>Incorrect Information Inputed.</p>");
        	   }
           }
           
           
           out.println("<FORM ACTION=\"/project3/servlet/checkccinfo\"\r\n" + 
           		"      METHOD=\"POST\">\r\n" + 
           		"  \r\n" + 
           		"  First Name:<br> <input type = \"text\" name = \"fname\"><br>\r\n" + 
           		"  Last Name:<br> <input type = \"text\" name = \"lname\"><br>\r\n" + 
           		"  Credit Card Number:<br> <input type = \"number\" name = \"ccnum\"><br>\r\n" + 
           		"  Expiration Date(Year/Month/Day):<br> <input type = \"text\" name = \"expdate\"><br>\r\n" + 
           		"  \r\n" + 
           		"  <INPUT TYPE=\"SUBMIT\" VALUE=\"Submit Order\"><br>\r\n" + 
           		"  \r\n" + 
           		"</FORM>");
           
           
           out.println("</BODY></CENTER>");
           
     
           statement.close();
           dbcon.close();
         }
	     catch (SQLException ex) {
	           while (ex != null) {
	                 System.out.println ("SQL Exception:  " + ex.getMessage ());
	                 ex = ex.getNextException ();
	             }  // end while
	         }  // end catch SQLException
	
	     catch(java.lang.Exception ex)
	         {
	             out.println("<HTML>" +
	                         "<HEAD><TITLE>" +
	                         "MovieDB: Error" +
	                         "</TITLE></HEAD>\n<BODY>" +
	                         "<P>SQL error in doGet: " +
	                         ex.getMessage() + "</P></BODY></HTML>");
	             return;
	         }
        out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
