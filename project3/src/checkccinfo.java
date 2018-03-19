

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class checkccinfo
 */
@WebServlet("/checkccinfo")
public class checkccinfo extends HttpServlet {

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
        
        
        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Order Informations</TITLE></HEAD>");
        out.println("<BODY><H1>Order Information</H1>");
        out.println("<TABLE border>");
        
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
        	
  

           ArrayList<String> mlist = (ArrayList<String>) session.getAttribute("mlist");
           
           String fn = request.getParameter("fname");
           String ln = request.getParameter("lname");
           String ccnum = request.getParameter("ccnum");
           String expdate = request.getParameter("expdate");
           
           String search = "select * from creditcards\r\n" + 
           		"where id = ? and firstName = ? and lastName = ? and DATE(expiration) = ?;";
           
           PreparedStatement xd = dbcon.prepareStatement(search);
	       xd.setString(1,fn);
	       xd.setString(2,ln);
	       xd.setString(3,ccnum);
	       xd.setString(4,expdate);
	       ResultSet zs = xd.executeQuery();
           
           if(zs.next() == false)
           {
        	   zs.close();
        	   session.setAttribute("ccauth", "fail");
        	   response.sendRedirect("/project3/servlet/finalcheckout");
        	   out.close();
           }
           zs.close();
           
           out.println("<TABLE border>");
           
           String cid = (String) session.getAttribute("customerid");
           
           out.println("<tr>" + "<td>" + "Title" + "</td>" + "<td>" + "Amount" + "</td>" + "</tr>");
           if(mlist != null)
           {
	           for(String n : mlist)
	           {
		           String query = "select * from movies\r\n" + 
		           		"where id = ?\r\n" + 
		           		"limit 1;";
			     
		           xd = dbcon.prepareStatement(query);
		           xd.setString(1,n);
		           ResultSet rs = xd.executeQuery();
		           String am = (String) session.getAttribute(n);
		           String mid = "";
		           while (rs.next()) {
		               String m_title = rs.getString("title");
		               mid = rs.getString("id");
		               out.println("<tr>" + "<td>" + m_title + "</td>" + "<td>" + am  + "</td>" + "</tr>");
		           }
		           
		           DataSource ms  = (DataSource) envCtx.lookup("jdbc/master");
		    		if (ms == null)
		    			out.println("ds is NULL");
		    		
		    		Connection mm = ms.getConnection();
		    		if (mm == null)
		    			out.println("dbcon is NULL");
		    		 Statement statement = mm.createStatement();
		           statement.executeUpdate("INSERT INTO sales (customerId,movieId,saleDate) \r\n"
	        			   + "VALUES ('" + cid + "', '" + mid + "',CURDATE());");
		           mm.close();
		           rs.close();
		           
		           
	           }
           }
           out.println("</TABLE></div>");
           
           
           out.println("</BODY></CENTER>");
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
