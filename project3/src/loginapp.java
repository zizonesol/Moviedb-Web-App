

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Statement;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

/**
 * Servlet implementation class loginapp
 */
@WebServlet("/loginapp")
public class loginapp extends HttpServlet {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
   

		
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
	       xd.setString(1,em);
	       ResultSet rs = xd.executeQuery();

	       
	       if(rs.next())
	       {
		        	 String pw = rs.getString("password");
		        	 if (pw.equals(passw))
		        	 {
		        		 out.print("1");
		        	 }
		        	 else
		        	 {
		        		 out.print("0");
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
	    		 Statement statement = mm.createStatement();
	        	   statement.executeUpdate("INSERT INTO customers (email,password,ccId) \r\n"
	        			   + "VALUES ('" + em + "', '" + passw + "',941);");
	        	   out.print("1");
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

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
