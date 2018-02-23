

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
 * Servlet implementation class metadata
 */
@WebServlet("/metadata")
public class metadata extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public metadata() {
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
		
		HttpSession session = request.getSession(true);
		if (session.isNew())
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
		
		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		
		out.println("<HTML><HEAD><TITLE>Metadata</TITLE></HEAD>");
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			// Declare our statement
			Statement statement = dbcon.createStatement();
			
			
			String tName = request.getParameter("table_name");
			
			
			
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n" + 
					"<HTML>\r\n" + 
					"<HEAD>\r\n" + 
					"  <TITLE>Add Movie Success</TITLE>\r\n" + 
					"</HEAD>\r\n" + 
					"\r\n" + 
					"<BODY BGCOLOR=\"#FDF5E6\">\r\n" + 
					"<H1 ALIGN=\"CENTER\">Movie Added</H1>\r\n" + 
					"\r\n" + 
					"<FORM ACTION=\"/project3/servlet/_dashboard\"\r\n" + 
					"      METHOD=\"POST\">\r\n" + 
					"  <CENTER>\r\n" + 
					"    <INPUT TYPE=\"SUBMIT\" VALUE=\"Go Back\">\r\n" + 
					"  </CENTER>\r\n" + 
					"</FORM>\r\n" + 
					"\r\n" + 
					"</BODY>\r\n" + 
					"</HTML>\r\n" + 
					"");
			
			statement.close();
			dbcon.close();
		}
		catch (SQLException ex)
		{
			while (ex != null)
			{
				System.out.println("SQL Exception: " + ex.getMessage());
				ex = ex.getNextException();
			}
		}
		catch(java.lang.Exception ex)
		{
			return;
		}
		
		// Terminate the STDOUT
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
