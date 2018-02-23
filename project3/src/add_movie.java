

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
 * Servlet implementation class add_movie
 */
@WebServlet("/add_movie")
public class add_movie extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public add_movie() 
    {
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
		
		
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			// Declare our statement
			Statement statement = dbcon.createStatement();
			
			
			
			String movie = request.getParameter("movie_title");
			String star = request.getParameter("star_name");
			String genre = request.getParameter("genre");
			
			
			String queryM = "INSERT INTO movies VALUES('" + movie + "','" + ");";
			String queryS = "INSERT INTO stars VALUES('" + star + "', '" + ");";
			String queryG = "INSERT INTO genres VALUES('" + genre + "', '" + ");";
			
			int insertM = statement.executeUpdate(queryM);
			int insertS = statement.executeUpdate(queryS);
			int insertG = statement.executeUpdate(queryG);
			
			
			
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
