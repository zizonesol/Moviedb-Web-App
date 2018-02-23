

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

			String query = "select * , r.rating from\r\n"
					+ "	(select s.title,s.year,s.director,GROUP_CONCAT(DISTINCT ss.name) as Stars_Appear,group_concat(DISTINCT gs.name) as geners_list\r\n"
					+ "	from movies s, stars_in_movies sm, stars ss,genres_in_movies gm, genres gs\r\n"
					+ "	where gs.id = gm.genreId\r\n" + "		AND gm.movieId = s.id\r\n"
					+ "		AND ss.id = sm.starId\r\n" + "        AND sm.movieId = s.id\r\n"
					+ "	Group by s.id) as masterp , ratings r , movies m\r\n" + "    where m.title = masterp.title\r\n"
					+ "    AND m.id = r.movieId\r\n" + "order by r.rating DESC\r\n" + "limit 20;";

			// Perform the query
			ResultSet rs = statement.executeQuery(query);

			out.println("<TABLE border>");

			// Iterate through each row of rs
			out.println("<tr>" + "<td>" + "Movie Title" + "</td>" + "<td>" + "Released Year" + "</td>" + "<td>"
					+ "Movie Director" + "</td>" + "<td>" + "List of Stars" + "</td>" + "<td>" + "List of Genres"
					+ "</td>" + "<td>" + "Rating" + "</td>" + "</tr>");
			while (rs.next()) {
				String m_title = rs.getString("title");
				Integer m_year = rs.getInt("year");
				Float m_rating = rs.getFloat("rating");
				String m_director = rs.getString("director");
				String m_stars = (rs.getString("Stars_Appear")).replace(",", ", ");
				String m_genres = (rs.getString("geners_list")).replace(",", ", ");
				out.println("<tr>" + "<td>" + m_title + "</td>" + "<td>" + m_year + "</td>" + "<td>" + m_director
						+ "</td>" + "<td>" + m_stars + "</td>" + "<td>" + m_genres + "</td>" + "<td>" + m_rating
						+ "</td>" + "</tr>");
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
