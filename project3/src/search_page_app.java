

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class search_page_app
 */
@WebServlet("/search_page_app")
public class search_page_app extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("application/json");
		ServletOutputStream out = response.getOutputStream();
		
		try {
		
			String loginUser = "mytestuser";
			String loginPasswd = "mypassword";
			String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
			        
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           // Declare our statement
			Statement statement = dbcon.createStatement();
			
			String name = request.getParameter("movie_title");
			String query = "select * , r.rating from\r\n" + 
            		"	(select s.title,s.year,s.director,GROUP_CONCAT(DISTINCT ss.name) as Stars_Appear,group_concat(DISTINCT gs.name) as geners_list\r\n" + 
            		"	from movies s, stars_in_movies sm, stars ss,genres_in_movies gm, genres gs\r\n" + 
            		"	where gs.id = gm.genreId\r\n" + 
            		"		AND gm.movieId = s.id\r\n" + 
            		"		AND ss.id = sm.starId\r\n" + 
            		"        AND sm.movieId = s.id\r\n AND s.title LIKE '%" + name + "%'\r\n" + 
            		"	Group by s.id) as masterp , ratings r , movies m\r\n" + 
            		"    where m.title = masterp.title\r\n" + 
            		"    AND m.id = r.movieId \r\n" +
            		"limit 10\r\n"+
            		"OFFSET 0;";
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next())
			{
				
			}
			
			
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

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
