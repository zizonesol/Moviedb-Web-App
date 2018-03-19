

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import java.io.*;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class search_page_app
 */
@WebServlet("/search_page_app")
public class search_page_app extends HttpServlet {
	
	String[] stopw = {"a","about","an","are"
	                  ,"as","at","be","by","com"
	                  ,"de","en","for","from"
	                  ,"how","i","in","is","it"
	                  ,"la","of","on",
	                  "or","that","the","this","to"
	                  ,"was","what","when","where"
	                  ,"who","will","with","und","the","www"};

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		try {
		

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
			String pg = request.getParameter("pgnum");
			
			Integer of = Integer.parseInt(pg)*10;
			
			pg = Integer.toString(of);
			
			String[] sear = name.split(" ");
			String srq = "title LIKE \"%" + name + "%\"";
			Integer stopc = 0;
			String zz = "match(title) against(\"";
			if(sear.length > 1)
			{
				for (int z = 0; z < sear.length ; z++)
				{
					
					if(Arrays.asList(stopw).contains(sear[z]))
					{
						stopc++;
					}
					else
					{
						zz = zz + "+" +sear[z] + "*,";
					}
				}
				zz = zz.substring(0,zz.length()-1);
				zz = zz + "\" in boolean mode)";
				if(sear.length != stopc)
				{
					srq = zz;
				}
			}
			
			
			
			String query = "select title, year, director,GROUP_CONCAT(DISTINCT sname) as Stars_Appear,group_concat(DISTINCT gname) as geners_list\r\n" + 
					"	from\r\n" + 
					"(select sel.id, sel.title, sel.year, sel.director, s.name as sname , g.name as gname from \r\n" + 
					"		((select * from movies where ?) as sel\r\n" + 
					"		left join stars_in_movies sm on sm.movieId=sel.id\r\n" + 
					"		left join stars s on sm.starId=s.id\r\n" + 
					"		left join genres_in_movies gm on gm.movieId=sel.id\r\n" + 
					"		left join genres g on g.id=gm.genreId)) as re\r\n" + 
					"group by re.id\r\n"
					+ "limit 10\r\n"
					+ "offset "+pg+";";

			PreparedStatement xd = dbcon.prepareStatement(query);
		    
		    ResultSet rs = xd.executeQuery();
	           

			
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
					jj.put("allstar", sstar);
				}
				else
				{
					jj.put("allstar", "None");
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
			
			jout.put("mlist",ja);
			out.print(jout.toString());
			
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

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
