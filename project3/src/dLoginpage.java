

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class dLogin
 */
@WebServlet("/dLoginpage")
public class dLoginpage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public dLoginpage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		HttpSession session = request.getSession(true);
		
		if (session.isNew())
		{
			session.setAttribute("employsuss", "no");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n"
					+ "<HTML>\r\n"
					+ "<HEAD>\r\n"
					+ "	<TITLE>dLoginpage</TITLE>\r\n"
					+ "</HEAD>\r\n"
					+ "\r\n"
					+ "<BODY BGCOLOR=\"#FDF5E6\">\r\n"
					+ "<H1 ALIGN=\"CENTER\">Dashboard Login</H1>\r\n"
					+ "\r\n"
					+ "<FORM ACTION=\"/project3/servlet/dLogin\"\r\n"
					+ "		METHOD=\"POST\">\r\n"
					+ "		\r\n"
					+ "		Email: <input type = \"text\" name = \"eemail\"><br>\r\n"
					+ "		Password: <input tyep = \"text\" name = \"epassword\"<br>\r\n"
					+ "		\r\n"
					+ "	<CENTER>\r\n"
					+ "		<INPUT TYPE=\"SUBMIT\" VALUE=\"Submit Order\">\r\n"
					+ "	</CENTER>\r\n"
					+ "</FORM>\r\n"
					+ "\r\n"
					+ "</BODY>\r\n"
					+ "</HTML>\r\n"
					+ "");
			out.close();
		}
		else 
		{
			if (!(session.getAttribute("employsuss") == null))
			{
				if(session.getAttribute("employsuss").equals("yes"))
				{
					response.sendRedirect("/project3/servlet/dashboard");
				}
				else
				{
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n"
							+ "<HTML>\r\n"
							+ "<HEAD>\r\n"
							+ "	<TITLE>dLoginpage</TITLE>\r\n"
							+ "</HEAD>\r\n"
							+ "\r\n"
							+ "<BODY BGCOLOR=\"#FDF5E6\">\r\n"
							+ "<H1 ALIGN=\"CENTER\">Dashboard Login</H1>\r\n"
							+ "\r\n"
							+ "<FORM ACTION=\"/project3/servlet/dLogin\"\r\n"
							+ "		METHOD=\"POST\">\r\n"
							+ "		\r\n"
							+ "		Email: <input type = \"text\" name = \"eemail\"><br>\r\n"
							+ "		Password: <input tyep = \"text\" name = \"epassword\"<br>\r\n"
							+ "		\r\n"
							+ "	<CENTER>\r\n"
							+ "		<INPUT TYPE=\"SUBMIT\" VALUE=\"Submit Order\">\r\n"
							+ "	</CENTER>\r\n"
							+ "</FORM>\r\n"
							+ "\r\n"
							+ "</BODY>\r\n"
							+ "</HTML>\r\n"
							+ "");
					out.close();
				}
			}
			else
			{
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n"
						+ "<HTML>\r\n"
						+ "<HEAD>\r\n"
						+ "	<TITLE>dLoginpage</TITLE>\r\n"
						+ "</HEAD>\r\n"
						+ "\r\n"
						+ "<BODY BGCOLOR=\"#FDF5E6\">\r\n"
						+ "<H1 ALIGN=\"CENTER\">Dashboard Login</H1>\r\n"
						+ "\r\n"
						+ "<FORM ACTION=\"/project3/servlet/dLogin\"\r\n"
						+ "		METHOD=\"POST\">\r\n"
						+ "		\r\n"
						+ "		Email: <input type = \"text\" name = \"eemail\"><br>\r\n"
						+ "		Password: <input tyep = \"text\" name = \"epassword\"<br>\r\n"
						+ "		\r\n"
						+ "	<CENTER>\r\n"
						+ "		<INPUT TYPE=\"SUBMIT\" VALUE=\"Submit Order\">\r\n"
						+ "	</CENTER>\r\n"
						+ "</FORM>\r\n"
						+ "\r\n"
						+ "</BODY>\r\n"
						+ "</HTML>\r\n"
						+ "");
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
