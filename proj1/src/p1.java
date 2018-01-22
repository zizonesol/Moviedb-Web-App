import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class p1
 */
@WebServlet("/p1")
public class p1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public String getServletInfo() {
        return "Servlet connects to MySQL database and displays result of a SELECT";
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>MovieDB</TITLE></HEAD>");
        out.println("<BODY><H1>MovieDB</H1>");

        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();

            String query = "select s.title,s.year,s.director,GROUP_CONCAT(DISTINCT ss.name) as Stars_Appear,group_concat(DISTINCT gs.name) as geners_list,rating \r\n" + 
            		"from ratings r, movies s, stars_in_movies sm, stars ss,genres_in_movies gm, genres gs\r\n" + 
            		"where gs.id = gm.genreId\r\n" + 
            		"    AND gm.movieId = s.id\r\n" + 
            		"    AND ss.id = sm.starId\r\n" + 
            		"    AND sm.movieId = r.movieId\r\n" + 
            		"    AND s.id = r.movieId\r\n" + 
            		"\r\n" + 
            		"Group by s.id\r\n" + 
            		"order by r.rating DESC\r\n" + 
            		"limit 20;";
            

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            out.println("<TABLE border>");

            // Iterate through each row of rs
            while (rs.next()) {
                String m_id = rs.getString("title");
                Integer m_year = rs.getInt("year");
                Float m_name = rs.getFloat("rating");
                String m_dob = rs.getString("director");
                String m_stars = rs.getString("Stars_Appear");
                out.println("<tr>" + "<td>" + m_id + "</td>" + "<td>" + m_name + "</td>" + "<td>" + m_dob + "</td>"
                        + "</tr>" + "<tr>" + "<td>" + m_year + "</td>" + "<tr>" + "<td>" + m_stars + "</td>");
            }

            out.println("</TABLE>");

            rs.close();
            statement.close();
            dbcon.close();
        } catch (SQLException ex) {
            while (ex != null) {
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            } // end while
        } // end catch SQLException

        catch (java.lang.Exception ex) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doGet: " + ex.getMessage() + "</P></BODY></HTML>");
            return;
        }
        out.close();
    }


}
