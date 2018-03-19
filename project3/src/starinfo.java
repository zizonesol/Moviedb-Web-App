

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class starinfo
 */
@WebServlet("/starinfo")
public class starinfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public starinfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

        
        HttpSession session = request.getSession(true);
        if(session.isNew())
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
        
        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Star Information</TITLE></HEAD>");
        out.println("<BODY><H1>Star Information</H1>");
        out.println("<div align=\"right\"><form action=\"/project3/mainpage.html\">\r\n" + 
           		"<input type=\"submit\" value=\"Back\" />\r\n" + 
           		"</form>\r\n" + 
           		"</div>");
		
        out.println("<div align=\"center\"><form action=\"/project3/servlet/shoppingcart\">\r\n" + 
        		"<input type=\"submit\" value=\"Checkout\" />\r\n" + 
        		"</form>\r\n" + 
        		"</div>");
        out.println("<div align=\"right\"><form action=\"/project3/servlet/dLoginpage\">\r\n" + 
        		"<input type=\"submit\" value=\"Dashboard\" />\r\n" + 
        		"</form>\r\n" + 
        		"</div>");
        
        try
        {
        	Context initCtx = new InitialContext();
    		
    		Context envCtx = (Context) initCtx.lookup("java:comp/env");
    		if (envCtx == null)
    			out.println("envCtx is NULL");
    		
    		DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
    		if (ds == null)
    			out.println("ds is NULL");
    		
    		Connection dbcon = ds.getConnection();
    		if (dbcon == null)
    			out.println("dbcon is NULL");
           

           String name = request.getParameter("star_name");
	          
	          
	          String query = "select ss.name, ss.birthYear, GROUP_CONCAT(distinct m.title) as Movie_appear\r\n" + 
	          		"from stars ss, stars_in_movies sm, movies m\r\n" + 
	          		"where m.id = sm.movieId\r\n" + 
	          		"AND	ss.id = sm.starId AND ss.name = \""+ name +"\"\r\n" + 
	          		"GROUP BY ss.id;";

           // Perform the query
	          PreparedStatement xd = dbcon.prepareStatement(query);
		      
		      ResultSet rs = xd.executeQuery();
	           

           out.println("<TABLE border>");

           // Iterate through each row of rs
           out.println("<tr>" + "<td>" + "Star Name" + "</td>" + "<td>" + "Released Year" + "</td>"+"<td>" + "Movies Appear In" + "</td>"
           			+ "</tr>");
           
          
           while (rs.next()) {
               String m_title = rs.getString("ss.name");
               Integer m_year = rs.getInt("ss.birthYear");
               String m_movies = rs.getString("Movie_appear");
               
               String m_hypermovie = "";
               
               for (String n : m_movies.split(","))
               {
            	   m_hypermovie = m_hypermovie + "<a href= \"/project3/servlet/movieinfo?movie_title=" + n.replace(" ","+") + "\">" + n + "</a>, ";
               }
               m_hypermovie = m_hypermovie.substring(0, m_hypermovie.length()-2);
               
               out.println("<tr>" + "<td>" + m_title + "</td>" + "<td>" + m_year + "</td>" + "<td>" + m_hypermovie + "</td>"
                           + "</tr>");
           }

           out.println("</TABLE></BODY></CENTER>");

           rs.close();
           dbcon.close();
         }
	     catch (SQLException ex) {
	           while (ex != null) {
	                 System.out.println ("SQL Exception:  " + ex.getMessage ());
	                 ex = ex.getNextException ();
	             }  // end while
	         }  // end catch SQLException
	
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