

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
 * Servlet implementation class genresearch
 */
@WebServlet("/genresearch")
public class genresearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public genresearch() {
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

        out.println("<HTML><HEAD><TITLE>Movie List by Genre</TITLE></HEAD>");
        out.println("<div align=\"center\"><form action=\"/project3/servlet/shoppingcart\">\r\n" + 
        		"<input type=\"submit\" value=\"Checkout\" />\r\n" + 
        		"</form>\r\n" + 
        		"</div>");
        out.println("<div align=\"right\"><form action=\"/project3/servlet/dLoginpage\">\r\n" + 
        		"<input type=\"submit\" value=\"Dashboard\" />\r\n" + 
        		"</form>\r\n" + 
        		"</div>");
        out.println("<BODY><H1>Movie List by Genre</H1>");
		
        
        
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

           String genre = request.getParameter("genre");
	          
	          
	          String query = "select title\r\n" + 
	          		"from movies m, genres_in_movies gm, genres g\r\n" + 
	          		"where m.id = gm.movieId\r\n" + 
	          		"	AND gm.genreId = g.id\r\n" + 
	          		"    AND g.name = ?\r\n" + 
	          		"LIMIT 20;";

          PreparedStatement xd = dbcon.prepareStatement(query);
	       xd.setString(1,genre);
	       ResultSet rs = xd.executeQuery();
	           

           out.println("<TABLE border>");

           // Iterate through each row of rs
           out.println("<tr>" + "<td>" + "Movie Title" + "</td>" + "</tr>");
           
          
           while (rs.next()) {
               String m_title = rs.getString("title");
               
               String m_hypertitle = "";
               
               for (String n : m_title.split(","))
               {
            	   m_hypertitle = m_hypertitle + "<a href= \"/project3/servlet/movieinfo?movie_title=" + n.replace(" ","+") + "\">" + n + "</a>, ";
               }
               m_hypertitle = m_hypertitle.substring(0, m_hypertitle.length()-2);
               
               out.println("<tr>" + "<td>" + m_title + "</td>" + "</tr>");
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