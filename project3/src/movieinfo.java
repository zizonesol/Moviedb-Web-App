
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
 * Servlet implementation class movieinfo
 */
@WebServlet("/movieinfo")
public class movieinfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public movieinfo() {
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

        out.println("<HTML><HEAD><TITLE>Movie Information</TITLE></HEAD>");
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
        out.println("<BODY><H1>Movie Information</H1>");
		
        
        

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

           String name = request.getParameter("movie_title");
	          System.out.println(name);
	         
	          
	          
  			String query = "select * from (select id, title, year, director,GROUP_CONCAT(DISTINCT sname) as Stars_Appear,group_concat(DISTINCT gname) as geners_list\r\n" + 
  					"from\r\n" + 
  					"	(select sel.id, sel.title, sel.year, sel.director, s.name as sname , g.name as gname from \r\n" + 
  					"							((select * from movies where title LIKE \""+ name +"\") as sel\r\n" + 
  					"							left join stars_in_movies sm on sm.movieId=sel.id\r\n" + 
  					"							left join stars s on sm.starId=s.id\r\n" + 
  					"							left join genres_in_movies gm on gm.movieId=sel.id\r\n" + 
  					"							left join genres g on g.id=gm.genreId)) as re\r\n" + 
  					"                            \r\n" + 
  					"							group by re.id ) as final\r\n" + 
  					"                            left join ratings r on r.movieId=final.id\r\n" + 
  					"                            limit 1\r\n" + 
  					"							;";

           // Perform the query
  			PreparedStatement xd = dbcon.prepareStatement(query);
 	       
 	       ResultSet rs = xd.executeQuery();
           String m_id = "";
           out.println("<TABLE border>");

           // Iterate through each row of rs
           out.println("<tr>" + "<td>" + "Movie Title" + "</td>" + "<td>" + "Released Year" + "</td>"+"<td>" + "Movie Director" + "</td>"
           			+"<td>" + "List of Stars" + "</td>" + "<td>" + "List of Genres" + "</td>" + "<td>" + "Rating" + "</td>" + "</tr>");
           while (rs.next()) {
        	   m_id = rs.getString("id");
               String m_title = rs.getString("title");
               Integer m_year = rs.getInt("year");
               String m_rating = rs.getString("rating");
               String m_director = rs.getString("director");
               String m_stars = rs.getString("Stars_Appear");
               String m_genres = rs.getString("geners_list");
               
               if(m_rating == null)
               {
            	   m_rating = "Unrated";
               }
               
               String m_hyperstars = "";
               String[] splitstar = m_stars.split(",");
               
               for (String n : splitstar)
               {
            	   m_hyperstars = m_hyperstars + "<a href= \"/project3/servlet/starinfo?star_name=" + n.replace(" ","+") + "\">" + n + "</a>, ";
               }
               m_hyperstars = m_hyperstars.substring(0, m_hyperstars.length()-2);
               
               String m_hypergenre = "";
               if(m_genres == null)
               {
            	   m_hypergenre = "Unknown";
               }
               else
               {
	               
	               for (String n : m_genres.split(","))
	               {
	            	   m_hypergenre = m_hypergenre + "<a href= \"/project3/servlet/genresearch?genre=" + n + "\">" + n + "</a>, ";
	               }
	               m_hypergenre = m_hypergenre.substring(0, m_hypergenre.length()-2);
               }
               
               
               out.println("<tr>" + "<td>" + m_title + "</td>" + "<td>" + m_year + "</td>" + "<td>" + m_director + "</td>"
                          + "<td>" + m_hyperstars + "</td>"+ "<td>" + m_hypergenre + "</td>" + "<td>" + m_rating + "</td>" + "</tr>");
           }
           


           out.println("</TABLE>");

           out.println("<div align=\"center\"> <a href= \"/project3/servlet/shoppingcart?movieid="+ m_id + "&amount=1\"> Add To Shopping Cart </a>");
           
           out.print("</BODY></CENTER>");

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