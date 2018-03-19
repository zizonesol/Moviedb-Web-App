
/* A servlet to display the contents of the MySQL movieDB database */
import java.io.*;
import java.net.*;
import java.text.*;
import java.sql.*;
import java.util.*;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;
import java.sql.PreparedStatement;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

public class login extends HttpServlet
{
    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
    		// Output stream to STDOUT
        PrintWriter out = response.getWriter();
    	
    		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
        
        // Verify CAPTCHA
        boolean valid = verifyUtils.verify(gRecaptchaResponse);
        if (!valid) 
        {
    	    		//errorString = "Captcha invalid!";
    	    		out.println("<HTML>" +
    	    				"<HEAD><TITLE>" +
    	    				"MovieDB: Error" +
    	    				"</TITLE></HEAD>\n<BODY>" +
    	    				"<P>Recaptcha WRONG!!!! </P></BODY></HTML>");
    	    		return;
    		}
    	

        HttpSession se = request.getSession(true);
        if(se.isNew())
        {
        		se.setAttribute("loginsuss", "no");
        		response.sendRedirect("/project3/servlet/welcome");
        	
        }
        else
        {
	        	if(se.getAttribute("loginsuss").equals("no"))
	        	{
	        		response.sendRedirect("/project3/servlet/welcome");
	        	}
        }
        
        try
        {

        	
        	Context initCtx = new InitialContext();
    		
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
           


           String em = request.getParameter("email");
           String passw = request.getParameter("password");
           
	       String query = "SELECT * from customers where email = ?;";

	       
           // Perform the query

	       PreparedStatement xd = dbcon.prepareStatement(query);
	       xd.setString(1, em);
	       ResultSet rs = xd.executeQuery();

           
           if(rs.next())
           {
		        	 String pw = rs.getString("password");
		        	 if (pw.equals(passw))
		        	 {
		        		 HttpSession session = request.getSession(true);
		        	      session.setAttribute("loginsuss", "yes");
		        	      session.setAttribute("customerid", rs.getString("id"));
		        	       response.sendRedirect("/project3/mainpage.html");
		        	 
		        	 }
		        	 else
		        	 {
		        		 response.sendRedirect("/project3/servlet/welcome");
		        	 }
	        }
	        else
	        {
		        	DataSource ms  = (DataSource) envCtx.lookup("jdbc/master");
		    		if (ms == null)
		    			out.println("ds is NULL");
		    		
		    		Connection mm = ms.getConnection();
		    		if (mm == null)
		    			out.println("dbcon is NULL");
		    		 Statement st = mm.createStatement();
		    		 st.executeUpdate("INSERT INTO customers (email,password,ccId) \r\n"
	        			   + "VALUES ('" + em + "', '" + passw + "',941);");
	        	   HttpSession session = request.getSession(true);
	        	   session.setAttribute("loginsuss", "yes");
	        	   System.out.println("add");
	        	   mm.close();
	     	   response.sendRedirect("/project3/mainpage.html");
	        }
	
	
	           rs.close();
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
	             return;
	         }
       
       
    }
    
     public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	doGet(request, response);
	} 
}
