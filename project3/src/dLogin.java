

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
		String loginUser = "lihengz2";
        String loginPasswd = "as499069589";
        String loginUrl = "jdbc:mysql://ec2-52-53-153-231.us-west-1.compute.amazonaws.com:3306/moviedb";
        
        try
        {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		
        		Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// Declare our statement
        		Statement statement = dbcon.createStatement();
        		
        		String em = request.getParameter("eemail");
        		String passw = request.getParameter("epassword");
        		
        		String query = "SELECT * from employees where email like \"" + em + "\";";
        		
        		// Perform the query
        		ResultSet rs = statement.executeQuery(query);
        		
        		if (rs.next())
        		{
        			String pw = rs.getString("password");
        			System.out.println(pw);
        			if(pw.equals(passw))
        			{
        				HttpSession session = request.getSession(true);
        				session.setAttribute("employsuss", "yes");
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
        					+ "VALUES ('" + em + "', '" + passw + "', \"CS 122B TA\")");
        			HttpSession session = request.getSession(true);
        			session.setAttribute("employsuss", "yes");
        			response.sendRedirect("/project3/servlet/dashboard");
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
