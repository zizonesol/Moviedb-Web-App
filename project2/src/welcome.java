
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class welcome
 */
@WebServlet("/welcome")
public class welcome extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		
		if (session.isNew())
		{
			session.setAttribute("loginsuss", "no");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n" + 
					"<HTML>\r\n" + 
					"<HEAD>\r\n" + 
					"  <TITLE>Form Example</TITLE>\r\n" + 
					"</HEAD>\r\n" + 
					"\r\n" + 
					"<BODY BGCOLOR=\"#FDF5E6\">\r\n" + 
					"<H1 ALIGN=\"CENTER\">Form Example</H1>\r\n" + 
					"\r\n" + 
					"<FORM ACTION=\"/project2/servlet/login\"\r\n" + 
					"      METHOD=\"POST\">\r\n" + 
					"      \r\n" + 
					"      Email: <input type = \"text\" name = \"email\"><br>\r\n" + 
					"      Password: <input type = \"text\" name = \"password\"><br>\r\n" + 
					"      \r\n" + 
					"  <CENTER>\r\n" + 
					"    <INPUT TYPE=\"SUBMIT\" VALUE=\"Submit Order\">\r\n" + 
					"  </CENTER>\r\n" + 
					"</FORM>\r\n" + 
					"\r\n" + 
					"</BODY>\r\n" + 
					"</HTML>\r\n" + 
					"");
			out.close();
		}
		else
		{
			if(session.getAttribute("loginsuss").equals("yes"))
			{
				response.sendRedirect("/project2/mainpage.html");
			}
			else
			{
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n" + 
						"<HTML>\r\n" + 
						"<HEAD>\r\n" + 
						"  <TITLE>Form Example</TITLE>\r\n" + 
						"</HEAD>\r\n" + 
						"\r\n" + 
						"<BODY BGCOLOR=\"#FDF5E6\">\r\n" + 
						"<H1 ALIGN=\"CENTER\">Form Example</H1>\r\n" + 
						"\r\n" + 
						"<FORM ACTION=\"/project2/servlet/login\"\r\n" + 
						"      METHOD=\"POST\">\r\n" + 
						"      \r\n" + 
						"      Email: <input type = \"text\" name = \"email\"><br>\r\n" + 
						"      Password: <input type = \"text\" name = \"password\"><br>\r\n" + 
						"      \r\n" + 
						"  <CENTER>\r\n" + 
						"    <INPUT TYPE=\"SUBMIT\" VALUE=\"Submit Order\">\r\n" + 
						"  </CENTER>\r\n" + 
						"</FORM>\r\n" + 
						"\r\n" + 
						"</BODY>\r\n" + 
						"</HTML>\r\n" + 
						"");
				out.close();
			}
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