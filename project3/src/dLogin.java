


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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

public class dLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public dLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{

		String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        
        try
        {

        	Context initCtx = new InitialContext();
    		
    		Context envCtx = (Context) initCtx.lookup("java:comp/env");
    		
    		
    		DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
    		
    		
    		Connection dbcon = ds.getConnection();
    		
        		// Declare our statement
        		

        		

        		String em = request.getParameter("eemail");
        		String passw = request.getParameter("epassword");
        		
        		String query = "SELECT * from employees where email like ?;";

        		
        		PreparedStatement xd = dbcon.prepareStatement(query);
     	       xd.setString(1,em);
     	       ResultSet rs = xd.executeQuery();


        		
        		if (rs.next())
        		{
        			String pw = rs.getString("password");

        			System.out.println(pw);
        			if(pw.equals(passw))
        			{
        				HttpSession session = request.getSession(true);
        				session.setAttribute("employsuss", "yes");

        				//session.setAttribute("fullname", rs.getString(""));
        				response.sendRedirect("/project3/servlet/_dashboard");
        			}
        			else
        			{
        				response.sendRedirect("/project3/servlet/dLoginpage");
        			}
        		}
        		else
        		{
        			DataSource ms  = (DataSource) envCtx.lookup("jdbc/master");
		    		
		    		
		    		Connection mm = ms.getConnection();
		    		
		    		 Statement statement = mm.createStatement();
        			statement.executeUpdate("INSERT INTO employees (email, password, fullname) \r\n"

        					+ "VALUES ('" + em + "', '" + passw + "', \"CS 122B TA\")");
        			HttpSession session = request.getSession(true);
        			session.setAttribute("employsuss", "yes");

        			response.sendRedirect("/project3/servlet/dashboard");
        			mm.close();


        		}
        		
        		rs.close();
        		dbcon.close();
        		
        }
        catch (SQLException ex)
        {
        		while (ex != null)
        		{
        			System.out.println("SQL Exception: " + ex.getMessage());
        			ex = ex.getNextException();
        		}	// end while
        }	// end catch exception
        catch (java.lang.Exception ex)
        {
        		return;
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
