
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

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

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

        out.println("<HTML><HEAD><TITLE>Seach Result</TITLE></HEAD>");
        out.println("<div align=\"right\"><form action=\"/project3/searchpage.html\">\r\n" + 
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
        out.println("<BODY><div align=\"center\"><H1>Seach Result</H1>");
        
		
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
	    			out.println("ds is NULL");
	    		
	    		Connection dbcon = ds.getConnection();
	    		if (dbcon == null)
	    			out.println("dbcon is NULL");
           //Class.forName("org.gjt.mm.mysql.Driver");
           //Class.forName("com.mysql.jdbc.Driver").newInstance();

           //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           // Declare our statement
           Statement statement = dbcon.createStatement();
           String query = "";
           
           String yearsort = request.getParameter("sorty");
           String moviesort = request.getParameter("sortm");
           String qsort = "";
           String mhtmlicon = "&sortm=up" +"\">" + "Movie Title &#9650";
           String yhtmlicon = "&sorty=up" +"\">" + "Released Year&#9650";
           String msortbutton = "";
           String ysortbutton = "";
           
           
           String Numpp = "10";
           
           String npp = request.getParameter("npp");
           if(npp != null)
           {
        	   		Numpp = npp;
           }
        	   
           String b10 = "<a href= \"/project3/servlet/searchpage?npp=10";
           String b25 = "<a href= \"/project3/servlet/searchpage?npp=25";
           String b50 = "<a href= \"/project3/servlet/searchpage?npp=50";
           
           String page = "1";
           String os = "0";
           
           String pagen = request.getParameter("pagenum");
           if (pagen != null)
           {
        	   		page = pagen;
           }
           
	       int x = Integer.parseInt(page);
	       if (x > 1)
	       {
	        	   int y = Integer.parseInt(Numpp);
	        	   int z = (x-1)*y;
	        	   os = String.valueOf(z);
           }
           
           String backbutton = "<a href= \"/project3/servlet/searchpage?npp=" + Numpp;
           String nextbutton = "<a href= \"/project3/servlet/searchpage?npp=" + Numpp;
           
           if(yearsort != null)
           {
        	   if(yearsort.equals("up"))
        	   {
        		   qsort = "ORDER BY masterp.year ASC \r\n";
        		   yhtmlicon = "&sorty=down" +"\">" + "Released Year&#9660";
        		   b10 = b10 + "&sorty=up";
        		   b25 = b25 + "&sorty=up";
        		   b50 = b50 + "&sorty=up";
        		   backbutton = backbutton + "&sorty=up";
        		   nextbutton = nextbutton + "&sorty=up";
        	   }
        	   else
        	   {
        		   qsort = "ORDER BY masterp.year DESC \r\n";
        		   yhtmlicon = "&sorty=up" +"\">" + "Released Year&#9650";
        		   b10 = b10 + "&sorty=down";
        		   b25 = b25 + "&sorty=down";
        		   b50 = b50 + "&sorty=down";
        		   backbutton = backbutton + "&sorty=down";
        		   nextbutton = nextbutton + "&sorty=down";
        	   }
           }
           else if(moviesort != null)
           {
        	   if(moviesort.equals("up"))
        	   {
        		   qsort = "ORDER BY masterp.title ASC \r\n";
        		   mhtmlicon = "&sortm=down" +"\">" + "Movie Title &#9660";
        		   b10 = b10 + "&sortm=up";
        		   b25 = b25 + "&sortm=up";
        		   b50 = b50 + "&sortm=up";
        		   backbutton = backbutton + "&sortm=up";
        		   nextbutton = nextbutton + "&sortm=up";
        	   }
        	   else
        	   {
        		   qsort = "ORDER BY masterp.title DESC \r\n";
        		   mhtmlicon = "&sortm=up" +"\">" + "Movie Title &#9650";
        		   b10 = b10 + "&sortm=down";
        		   b25 = b25 + "&sortm=down";
        		   b50 = b50 + "&sortm=down";
        		   backbutton = backbutton + "&sortm=down";
        		   nextbutton = nextbutton + "&sortm=down";
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
	            		"limit " + Numpp + ";";
        	   //+ "\r\n"+ "OFFSET " + os +
        	   
        	   msortbutton = "<a href= \"/project3/servlet/searchpage?title=" + title  + mhtmlicon +"</a>";
        	   ysortbutton= "<a href= \"/project3/servlet/searchpage?title=" + title + yhtmlicon +"</a>";
        	   b10 = b10 + "&title=\"" + title + "\">"+ "10</a>";
    		   b25 = b25 + "&title=\"" + title + "\">"+ "25</a>";
    		   b50 = b50 + "&title=\"" + title + "\">"+ "50</a>";
    		   backbutton = backbutton + "&title=\"" + title;
    		   nextbutton = nextbutton + "&title=\"" + title;
    		   
        	   
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
	            		"limit "+ Numpp + ";";
	            		
	            		//+ "OFFSET " + os +";";
	          
	          msortbutton = "<a href= \"/project3/servlet/searchpage?movie_title=" + name + "&year="+ year + "&director=" + director + "&star_name="+ star_name + mhtmlicon +"</a>";
	          ysortbutton = "<a href= \"/project3/servlet/searchpage?movie_title=" + name + "&year="+ year + "&director=" + director + "&star_name="+ star_name + yhtmlicon +"</a>";
	          b10 = b10 + "&movie_title=" + name + "&year="+ year + "&director=" + director + "&star_name="+ star_name+ "\">"+ "10</a>";
	   		   b25 = b25 + "&movie_title=" + name + "&year="+ year + "&director=" + director + "&star_name="+ star_name+ "\">"+ "25</a>";
	   		   b50 = b50 + "&movie_title=" + name + "&year="+ year + "&director=" + director + "&star_name="+ star_name+ "\">"+ "50</a>";
	   		backbutton = backbutton + "&movie_title=" + name + "&year="+ year + "&director=" + director + "&star_name="+ star_name ;
 		   nextbutton = nextbutton + "&movie_title=" + name + "&year="+ year + "&director=" + director + "&star_name="+ star_name;
           }
           // Perform the query
           ResultSet rs = statement.executeQuery(query);

           out.println("<div align=\"right\">Number of Movie per page:"+ b10 + ", " + b25 + ", " + b50 +"</div>");
           out.println("<TABLE border>");

           // Iterate through each row of rs
           out.println("<tr>" + "<td>" + msortbutton + "</td>" + "<td>" + ysortbutton + "</td>"+"<td>" + "Movie Director" + "</td>"
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
            	   m_hyperstars = m_hyperstars + "<a href= \"/project3/servlet/starinfo?star_name=" + n.replace(" ","+") + "\">" + n + "</a>, ";
               }
               m_hyperstars = m_hyperstars.substring(0, m_hyperstars.length()-2);
               
               String m_genres = (rs.getString("geners_list")).replace(",", ", ");
               out.println("<tr>" + "<td>" + "<a href= \"/project3/servlet/movieinfo?movie_title=" + m_title.replace(" ","+") + "\">"+ m_title + "</a>" + "</td>" + "<td>" + m_year + "</td>" + "<td>" + m_director + "</td>"
                          + "<td>" + m_hyperstars + "</td>"+ "<td>" + m_genres + "</td>" + "<td>" + m_rating + "</td>" + "</tr>");
           }

           out.println("</TABLE></div>");
           
           if(x == 1)
           {
        	   backbutton = "Previous";
        	   nextbutton = nextbutton + "&pagenum=2\">Next</a>";
        	   out.println("<div align=\"center\">" + backbutton + " | " + nextbutton + "</div>");
           }
           else
           {
        	   backbutton = backbutton + "&pagenum="+ String.valueOf(x-1) + "\">Previous</a>";
        	   nextbutton = nextbutton + "&pagenum="+ String.valueOf(x+1) + "\">Next</a>";
        	   out.println("<div align=\"center\">" + backbutton + " | " + nextbutton + "</div>");
           }
           
           out.println("</BODY></CENTER>");
           
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