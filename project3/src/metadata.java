

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
			
			String table = request.getParameter("table_name");

			String query = "SELECT table_name, column_name, data_type, column_type  \r\n" + 
					"FROM information_schema.columns \r\n" + 
					"WHERE (table_schema='moviedb')\r\n" + 
					"order by table_name;\r\n";

			// Perform the query
			ResultSet rs = statement.executeQuery(query);

			out.println("<TABLE border>");

			// Iterate through each row of rs
			out.println("<tr>" + "</td>" + "Table Name" + "</td>" + "<td>"+ "Column Name" + "</td>" 
						+ "<td>" + "Data Type" + "</td>" + "<td>" + "Column Type" + "</td>" + "</tr>");
			while (rs.next()) {
				String tName = rs.getString("table_name");
				String cName = rs.getString("column_name");
				String dtype = (rs.getString("data_type"));
				String ctype = rs.getString("column_type");
				
				out.println("<tr>" + "<td>" + tName + "</td>" + "<td>" + cName + "</td>" + "<td>" + dtype
						+ "</td>" + "<td>" + ctype + "</td>" + "</tr>");
			}

			out.println("</TABLE></BODY></CENTER>");

			rs.close();
			statement.close();
			dbcon.close();
		} 
		catch (SQLException ex) 
		{
			while (ex != null) 
			{
				System.out.println("SQL Exception:  " + ex.getMessage());
				ex = ex.getNextException();
			} // end while
		} // end catch SQLException
		catch(java.lang.Exception ex)
        {
            out.println("<HTML>" +
                        "<HEAD><TITLE>" +
                        "MovieDB: Error" +
                        "</TITLE></HEAD>\n<BODY>" +
                        "<P>SQL error in doGet: " +
                        ex.getMessage() + "</P></BODY></HTML>");
            return;
        }
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
