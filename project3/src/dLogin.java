

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
        
        HttpSession se = request.getSession(true);
        if(se.isNew())
        {
        		se.setAttribute("loginsuss", "no");
        		response.sendRedirect("/project3/servlet/dLoginpage");
        	
        }
        else
        {
	        	if(se.getAttribute("loginsuss").equals("no"))
	        	{
	        		response.sendRedirect("/project3/servlet/dLoginpage");
	        	}
        }
        
        try
        {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		
        		Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// Declare our statement
        		Statement statement = dbcon.createStatement();
        		
        		String em = request.getParameter("email");
        		String passw = request.getParameter("password");
        		
        		String query = "SELECT * from employees where email = \"" + em + "\";";
        		
        		// Perform the query
        		ResultSet rs = statement.executeQuery(query);
        		
        		if (rs.next())
        		{
        			String pw = rs.getString("password");
        			if(pw.equals(passw))
        			{
        				HttpSession session = request.getSession(true);
        				session.setAttribute("loginsuss", "yes");
        				//session.setAttribute("fullname", rs.getString(""));
        				response.sendRedirect("/project3/servlet/dashboard");
        			}
        			else
        			{
        				response.sendRedirect("/project3/servlet/dLoginpage");
        			}
        		}
        		else
        		{
        			statement.executeUpdate("INSERT INTO employees (email, password, fullname) \r\n"
        					+ "VALUES ('" + em + "', '" + passw + "', CS 122B TA)");
        			HttpSession session = request.getSession(true);
        			session.setAttribute("loginsuss", "yes");
        			response.sendRedirect("/project3/servlet/dLoginpage");
        		}
        		
        		rs.close();
        		statement.close();
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
