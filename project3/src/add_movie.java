
import java.io.*;
import java.net.*;
import java.text.*;
import java.sql.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

/**
 * Servlet implementation class add_movie
 */
@WebServlet("/add_movie")
public class add_movie extends HttpServlet 
{


	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
		HttpSession session = request.getSession(true);
		if(session.isNew())
		{
			session.setAttribute("employsuss", "no");
			response.sendRedirect("/project3/servlet/dLoginpage");
		}
		else if(session.getAttribute("employsuss") == null)
		{
			response.sendRedirect("/project3/servlet/dLoginpage");
		}
		else if(session.getAttribute("employsuss").equals("no"))
		{
			response.sendRedirect("/project3/servlet/dLoginpage");
		}
		else
		{

		
		response.setContentType("text/html");
		
		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n" + 
				"<HTML>\r\n" + 
				"<HEAD>\r\n" + 
				"  <TITLE>Add Movie Success</TITLE>\r\n" + 
				"</HEAD>\r\n" + 
				"\r\n" + 
				"<BODY BGCOLOR=\"#FDF5E6\">\r\n" + 
				"<H1 ALIGN=\"CENTER\">Movie Added</H1>\r\n" + 
				"\r\n");
		try
		{
			Context initCtx = new InitialContext();
			if (initCtx ==null)
				out.println("initCtx is NULL");
			
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if (envCtx == null)
				out.println("envCtx is NULL");
			
			DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
			if (ds == null)
				out.println("ds is null");
			
			
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			//Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			Connection dbcon = ds.getConnection();
			if (dbcon == null)
				out.println("dbcon is null");
			
			// Declare our statement
			
			Statement statement = dbcon.createStatement();
			
			String movie = request.getParameter("movie_title");
			String star = request.getParameter("star_name");
			String genre = request.getParameter("genre");
			String director = request.getParameter("director");
			String year = request.getParameter("year");
			

			statement.executeQuery("CALL add_movie(\""+ movie +"\","+ year +",\"" + director +"\", \""+ star +"\",\""+ genre +"\");");

			
			
			out.println("<FORM ACTION=\"/project3/servlet/dashboard\"\r\n" + 

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

	}}


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