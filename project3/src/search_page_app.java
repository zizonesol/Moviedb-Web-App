

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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class search_page_app
 */
@WebServlet("/search_page_app")
public class search_page_app extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		try {
		
			String loginUser = "lihengz2";
	        String loginPasswd = "as499069589";
	        String loginUrl = "jdbc:mysql://ec2-52-53-153-231.us-west-1.compute.amazonaws.com:3306/moviedb";
			        
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           // Declare our statement
			Statement statement = dbcon.createStatement();
			
			String name = request.getParameter("movie_title");
			
			String[] sear = name.split(" ");
			String srq = "title LIKE \"%" + name + "%\"";
			if(sear.length > 1)
			{
				String zz = "match(title) against(\"";
				for (int z = 0; z < sear.length ; z++)
				{
					zz = zz + "+" +sear[z] + "*,";
				}
				zz = zz.substring(0,zz.length()-1);
				zz = zz + "\" in boolean mode)";
				srq = zz;
			}
			
			
			String query = "select title, year, director,GROUP_CONCAT(DISTINCT sname) as Stars_Appear,group_concat(DISTINCT gname) as geners_list\r\n" + 
					"	from\r\n" + 
					"(select sel.id, sel.title, sel.year, sel.director, s.name as sname , g.name as gname from \r\n" + 
					"		((select * from movies where title like \"%good%\") as sel\r\n" + 
					"		left join stars_in_movies sm on sm.movieId=sel.id\r\n" + 
					"		left join stars s on sm.starId=s.id\r\n" + 
					"		left join genres_in_movies gm on gm.movieId=sel.id\r\n" + 
					"		left join genres g on g.id=gm.genreId)) as re\r\n" + 
					"group by re.id;";
			ResultSet rs = statement.executeQuery(query);
			
			JSONObject jout = new JSONObject();
			JSONArray ja = new JSONArray();
			
			while(rs.next())
			{
				String m_title = rs.getString("title");
				Integer m_year = rs.getInt("year");
				String m_director = rs.getString("director");
				String m_stars = rs.getString("Stars_Appear");
				String m_genre = rs.getString("geners_list");
				
				JSONObject jj = new JSONObject();
				if(m_stars != null) {
					String sstar = m_stars.replaceAll(",", ", ");
					sstar = sstar.substring(0,sstar.length()-2);
					jj.put("all_star", sstar);
				}
				else
				{
					jj.put("all_star", "None");
				}
				
				if(m_year == 0)
				{
					jj.put("year", "Unknown");
				}
				else
				{
					jj.put("year", String.valueOf(m_year));
				}
				System.out.println("HI");
				if(m_genre == null)
				{
					jj.put("genre", "None");
				}
				else
				{
					m_genre = m_genre.replaceAll(",", ", ");
					m_genre = m_genre.substring(0, m_genre.length()-2);
					jj.put("genre", m_genre);
				}
				
				jj.put("title", m_title);
				jj.put("director", m_director);
				
				ja.put(jj);
			}
			
			jout.put("all_movie",ja);
			out.print(jout.toString());
			
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