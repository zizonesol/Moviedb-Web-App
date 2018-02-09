

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
 * Servlet implementation class searchpage
 */
@WebServlet("/searchpage")
public class searchpage extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        
        
        HttpSession session = request.getSession(true);
        if(session.isNew())
        {
        	session.setAttribute("loginsuss", "no");
        	response.sendRedirect("/project2/servlet/welcome");
        	
        }
        else
        {
        	if(session.getAttribute("loginsuss").equals("no"))
        	{
        		response.sendRedirect("/project2/servlet/welcome");
        	}
        }
        

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>MovieDB: Found Records</TITLE></HEAD>");
        out.println("<BODY><H1>MovieDB: Found Records</H1>");
		
        try
        {
           //Class.forName("org.gjt.mm.mysql.Driver");
           Class.forName("com.mysql.jdbc.Driver").newInstance();

           Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           // Declare our statement
           Statement statement = dbcon.createStatement();
           String query = "";
           
           String yearsort = request.getParameter("sorty");
           String moviesort = request.getParameter("sortm");
           String qsort = "";
           String htmlicon = "&#9650";
           if(yearsort != null)
           {
        	   if(yearsort.equals("up"))
        	   {
        		   qsort = "ORDER BY masterp.year ASC \r\n";
        	   }
        	   else
        	   {
        		   qsort = "ORDER BY masterp.year DESC \r\n";
        	   }
           }
           else if(moviesort != null)
           {
        	   if(moviesort.equals("up"))
        	   {
        		   qsort = "ORDER BY masterp.title ASC \r\n";
        	   }
        	   else
        	   {
        		   qsort = "ORDER BY masterp.title DESC \r\n";
        	   }
           }
        	   
           if(request.getParameter("title") != null)
           {
        	   String title = request.getParameter("title");
        	   query = "select * , r.rating from\r\n" + 
	            		"	(select s.title,s.year,s.director,GROUP_CONCAT(DISTINCT ss.name) as Stars_Appear,group_concat(DISTINCT gs.name) as geners_list\r\n" + 
	            		"	from movies s, stars_in_movies sm, stars ss,genres_in_movies gm, genres gs\r\n" + 
	            		"	where gs.id = gm.genreId\r\n" + 
	            		"		AND gm.movieId = s.id\r\n" + 
	            		"		AND ss.id = sm.starId\r\n" + 
	            		"        AND sm.movieId = s.id\r\n AND s.title LIKE '" + title + "%'\r\n" + 
	            		"	Group by s.id) as masterp , ratings r , movies m\r\n" + 
	            		"    where m.title = masterp.title\r\n" + 
	            		"    AND m.id = r.movieId \r\n" + qsort +
	            		"limit 20;";
           }
           else {
           
	          String name = request.getParameter("movie_title");
	          String year = request.getParameter("year");
	          String director = request.getParameter("director");
	          String star_name = request.getParameter("star_name");
	          
	          query = "select * , r.rating from\r\n" + 
	            		"	(select s.title,s.year,s.director,GROUP_CONCAT(DISTINCT ss.name) as Stars_Appear,group_concat(DISTINCT gs.name) as geners_list\r\n" + 
	            		"	from movies s, stars_in_movies sm, stars ss,genres_in_movies gm, genres gs\r\n" + 
	            		"	where gs.id = gm.genreId\r\n" + 
	            		"		AND gm.movieId = s.id\r\n" + 
	            		"		AND ss.id = sm.starId\r\n" + 
	            		"        AND sm.movieId = s.id\r\n AND s.title LIKE '%" + name + "%'AND s.director LIKE '%" + director + "%' AND s.year LIKE '%" + year+"%'\r\n" + 
	            		"	Group by s.id) as masterp , ratings r , movies m\r\n" + 
	            		"    where m.title = masterp.title\r\n" + 
	            		"    AND m.id = r.movieId AND masterp.Stars_Appear LIKE '%" + star_name + "%'\r\n" + qsort +
	            		"limit 20;";
           }
           // Perform the query
           ResultSet rs = statement.executeQuery(query);

           out.println("<TABLE border>");

           // Iterate through each row of rs
           out.println("<tr>" + "<td>" + "Movie Title" + "<a href = \"/project2/"     +"&#9650" + "</td>" + "<td>" + "Released Year&#9650" + "</td>"+"<td>" + "Movie Director" + "</td>"
           			+"<td>" + "List of Stars" + "</td>" + "<td>" + "List of Genres" + "</td>" + "<td>" + "Rating" + "</td>" + "</tr>");
           while (rs.next()) {
               String m_title = rs.getString("title");
               Integer m_year = rs.getInt("year");
               Float m_rating = rs.getFloat("rating");
               String m_director = rs.getString("director");
               String m_stars = rs.getString("Stars_Appear");
               
               
               String m_hyperstars = "";
               String[] splitstar = m_stars.split(",");
               
               for (String n : splitstar)
               {
            	   m_hyperstars = m_hyperstars + "<a href= \"/project2/servlet/starinfo?star_name=" + n.replace(" ","+") + "\">" + n + "</a>, ";
               }
               m_hyperstars = m_hyperstars.substring(0, m_hyperstars.length()-2);
               
               String m_genres = (rs.getString("geners_list")).replace(",", ", ");
               out.println("<tr>" + "<td>" + "<a href= \"/project2/servlet/movieinfo?movie_title=" + m_title.replace(" ","+") + "\">"+ m_title + "</a>" + "</td>" + "<td>" + m_year + "</td>" + "<td>" + m_director + "</td>"
                          + "<td>" + m_hyperstars + "</td>"+ "<td>" + m_genres + "</td>" + "<td>" + m_rating + "</td>" + "</tr>");
           }

           out.println("</TABLE></BODY></CENTER>");

           rs.close();
           statement.close();
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
