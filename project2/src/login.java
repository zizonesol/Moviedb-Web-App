
/* A servlet to display the contents of the MySQL movieDB database */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class login extends HttpServlet
{
    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
    	
    	String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        
        HttpSession se = request.getSession(true);
        if(se.isNew())
        {
        	se.setAttribute("loginsuss", "no");
        	response.sendRedirect("/project2/servlet/welcome");
        	
        }
        else
        {
        	if(se.getAttribute("loginsuss").equals("no"))
        	{
        		response.sendRedirect("/project2/servlet/welcome");
        	}
        }
        
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
        		 HttpSession session = request.getSession(true);
        	      session.setAttribute("loginsuss", "yes");
        	      session.setAttribute("customerid", rs.getString("id"));
        	       response.sendRedirect("/project2/mainpage.html");
        	 
        	 }
        	 else
        	 {
        		 response.sendRedirect("/project2/servlet/welcome");
        	 }
           }
           else
           {
        	   statement.executeUpdate("INSERT INTO customers (email,password,ccId) \r\n"
        			   + "VALUES ('" + em + "', '" + passw + "',941);");
        	   HttpSession session = request.getSession(true);
        	   session.setAttribute("loginsuss", "yes");
     	       response.sendRedirect("/project2/mainpage.html");
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
    
     public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	doGet(request, response);
	} 
}
