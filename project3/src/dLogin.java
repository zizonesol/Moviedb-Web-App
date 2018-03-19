
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
        		if (initCtx == null)
        			System.out.println("initCtx is NULL");

        		Context envCtx = (Context) initCtx.lookup("java:comp/env");
        		if (envCtx == null)
        			System.out.println("envCtx is NULL");
        		
        		DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
        		if (ds == null)
        			System.out.println("ds is NULL");
        		
        		Connection dbcon = ds.getConnection();
        		if (dbcon == null)
        			System.out.println("dbcon is NULL");
        		
        		//Class.forName("com.mysql.jdbc.Driver").newInstance();
        		
        		//Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// Declare our statement
        		//Statement statement = dbcon.createStatement();
        		

        		String em = request.getParameter("eemail");
        		String passw = request.getParameter("epassword");
        		
        		String query = "SELECT * from employees where email like ?;";

        		PreparedStatement statement = dbcon.prepareStatement(query);
        		
        		statement.setString(1, em);
        		
        		// Perform the query
        		statement.execute();
        		ResultSet rs = statement.getResultSet();
        		
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
        			statement.executeUpdate("INSERT INTO employees (email, password, fullname) \r\n"

        					+ "VALUES ('" + em + "', '" + passw + "', \"CS 122B TA\")");
        			HttpSession session = request.getSession(true);
        			session.setAttribute("employsuss", "yes");
        			response.sendRedirect("/project3/servlet/_dashboard");

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
