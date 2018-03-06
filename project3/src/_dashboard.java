

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
 * Servlet implementation class _dashboard
 */
@WebServlet("/dashboard")
public class _dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public _dashboard() 
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n"
				+ "<HTML>\r\n"
				+ "<HEAD>\r\n"
				+ "	<TITLE>Dashboard</TITLE>\r\n"
				+ "</HEAD>\r\n"
				+ "\r\n"
				+ "<BODY BGCOLOR=\"#FDF5E6\">\r\n"
				+ "<H1 ALIGN=\"CENTER\">Dashboard</H1>\r\n"
				+ "\r\n"
				+ "<A HREF=\"/project3/addstarpage.html\">Add Star</A> <BR/>\r\n"
				+ "\r\n"
				+ "<A HREF=\"/project3/addmoviepage.html\">Add Movie</A> <BR/>\r\n"
				+ "\r\n"

				+ "<A HREF=\"/project3/servlet/metadata\">Get Metadata</A> <BR/>\r\n"

				+ "\r\n"
				+ "</BODY>\r\n"
				+ "</HTML>\r\n"
				+ "\r\n");
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
