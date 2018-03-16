

import java.io.*;
import java.net.*;
import java.text.*;
import java.sql.*;
import java.util.*;

import javax.servlet.ServletContext;
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
 * Servlet implementation class addstar
 */
@WebServlet("/addstar")
public class addstar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addstar() {
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
					"  <TITLE>Add Star Success</TITLE>\r\n" + 
					"</HEAD>\r\n" + 
					"\r\n" +
					"<BODY BGCOLOR=\"#FDF5E6\">\r\n" + 
					"<H1 ALIGN=\"CENTER\">Star Added</H1>\r\n" + 
					"\r\n");
			try
			{
				Context initCtx = new InitialContext();
				if (initCtx == null)
					out.println("initCtx is NULL");
				
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				if (envCtx == null)
					out.println("envCtx is NULL");
					
				DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
				
				if (ds == null)
					out.println("ds is null");
				
				Connection dbcon = ds.getConnection();
				if (dbcon == null)
					out.println("dbcon is NULL");
				
				//Class.forName("com.mysql.jdbc.Driver").newInstance();
				//Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
				
				// Declare our statement
				Statement statement = dbcon.createStatement();
				
				String star = request.getParameter("star_name");
				ResultSet rs = statement.executeQuery("select * from stars where name like \""+ star + "\";");
				
				if(!rs.next())
				{
					// Get the unique id number;
					rs = statement.executeQuery("select max(id) from stars\r\n" + 
							"where id like \"aa%\";");
					
					
					String idf = "aa";
					Integer idb = 0;
					if(rs.next())
					{
						String ts = rs.getString(1);
						if(ts != null)
						{	
							if(ts.substring(0, 2).equals("aa"))
							{
								idb = Integer.parseInt(ts.substring(2));
								idb++;
							}
						}
					}
					
					String sid = idf + String.format("%07d", idb);
					
					String queryS = "INSERT INTO stars (id, name) VALUES(\"" + sid + "\", \"" + star + "\");";
					
					statement.executeUpdate(queryS);
					
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
					}
					else
					{
						out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n" + 
								"<HTML>\r\n" + 
								"<HEAD>\r\n" + 
								"  <TITLE>Add Star Unsuccess</TITLE>\r\n" + 
								"</HEAD>\r\n" + 
								"\r\n" + 
								"<BODY BGCOLOR=\"#FDF5E6\">\r\n" + 
								"<H1 ALIGN=\"CENTER\">Star already exist in database.</H1>\r\n" + 
								"\r\n" + 
								"<FORM ACTION=\"/project3/servlet/dashboard\"\r\n" + 
								"      METHOD=\"POST\">\r\n" + 
								"  <CENTER>\r\n" + 
								"    <INPUT TYPE=\"SUBMIT\" VALUE=\"Go Back\">\r\n" + 
								"  </CENTER>\r\n" + 
								"</FORM>\r\n" + 
								"\r\n" + 
								"</BODY>\r\n" + 
								"</HTML>\r\n" + 
								"");
					}
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
			} catch(java.lang.Exception ex)
	        {
	            out.println("<HTML>" +
	                        "<HEAD><TITLE>" +
	                        "MovieDB: Error" +
	                        "</TITLE></HEAD>\n<BODY>" +
	                        "<P>SQL error in doGet: " +
	                        ex.getMessage() + "</P></BODY></HTML>");
	            return;
	        }
			
			
			// Terminate the STDOUT
			out.close();
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