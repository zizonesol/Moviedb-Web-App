

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class loginapp
 */
@WebServlet("/loginapp")
public class loginapp extends HttpServlet {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
   
		String loginUser = "lihengz2";
        String loginPasswd = "as499069589";
        String loginUrl = "jdbc:mysql://ec2-52-53-153-231.us-west-1.compute.amazonaws.com:3306/moviedb";
	   
	    
	    try
	    {
	       //Class.forName("org.gjt.mm.mysql.Driver");
	       Class.forName("com.mysql.jdbc.Driver").newInstance();
	
	       Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	       // Declare our statement
	       Statement statement = dbcon.createStatement();
	
	       String em = request.getParameter("email");
	       String passw = request.getParameter("password");
	       
	       String query = "SELECT * from customers where email = \""+ em   +"\";";
	
	       // Perform the query
	       ResultSet rs = statement.executeQuery(query);
	       
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
	        	   statement.executeUpdate("INSERT INTO customers (email,password,ccId) \r\n"
	        			   + "VALUES ('" + em + "', '" + passw + "',941);");
	        	   out.print("1");
	        }
	
	
           rs.close();
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
	             return;
	         }
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
