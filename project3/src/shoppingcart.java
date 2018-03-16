

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
 * Servlet implementation class shoppingcart
 */
@WebServlet("/shoppingcart")
public class shoppingcart extends HttpServlet {

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
        
         ArrayList<String> mlist = new ArrayList<String>();
	        
        if(session.getAttribute("mlist") == null)
        {
        	session.setAttribute("mlist", mlist);
        }
        else
        {
        	mlist = (ArrayList<String>) session.getAttribute("mlist");
        }
        
        if(request.getParameter("movieid") != null)
        {
		      
	        String item = request.getParameter("movieid");
	        String nun = request.getParameter("amount");
	        
	        if(mlist.contains(item) && nun.equals("0"))
	        {
	        	mlist.remove(item);
	        }
	        else
	        {
	        	if(mlist.contains(item) == false)
	        	{
	        		mlist.add(item);
	        	}
	        	if(nun != "")
	        	{
	        		session.setAttribute(item, nun);
	        	}
	        }
	        session.setAttribute("mlist", mlist);
        }
        
        
        String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        
        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Shopping Cart</TITLE></HEAD>");
        out.println("<BODY><H1>Shopping Cart</H1>");
        out.println("<div align=\"center\"><form action=\"/project3/mainpage.html\">\r\n" + 
           		"<input type=\"submit\" value=\"Home\" />\r\n" + 
           		"</form>\r\n" + 
           		"</div>");
        
        out.println("<TABLE border>");
        
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
           Statement statement = dbcon.createStatement();

           out.println("<TABLE border>");

           
           out.println("<tr>" + "<td>" + "Title" + "</td>" + "<td>" + "Amount" + "</td>" + "</tr>");
        
           for(String n : mlist)
           {
	           String query = "select * from movies\r\n" + 
	           		"where id = \""+ n +"\"\r\n" + 
	           		"limit 1;";
		     
	       
	           ResultSet rs = statement.executeQuery(query);
	           String am = (String) session.getAttribute(n);
	           
	           while (rs.next()) {
	               String m_title = rs.getString("title");
	               
	               
	               String s = "<FORM ACTION=\"/project3/servlet/shoppingcart\"\r\n" + 
	               		"      METHOD=\"GET\">\r\n" + 
	               		"  New Amount: <input type = \"number\" name = \"amount\"><br>\r\n" + 
	               		"<input type=\"hidden\" name=\"movieid\" value=\"" + n + "\" />"+
	               		"  <INPUT TYPE=\"SUBMIT\" VALUE=\"Submit\">"
	               		+ "<br>\r\n" + 
	               		"  \r\n" + 
	               		"</FORM>";
	               
	               
	
	               out.println("<tr>" + "<td>" + m_title + "</td>" + "<td>" + am + s + "</td>" + "</tr>");
	           }
	           rs.close();
           }
           out.println("</TABLE></div>");
           
           out.println("<div align=\"center\"><form action=\"/project3/servlet/finalcheckout\">\r\n" + 
           		"<input type=\"submit\" value=\"Checkout Now!\" />\r\n" + 
           		"</form>\r\n" + 
           		"</div>");
           
           out.println("</BODY></CENTER>");
           
     
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
	
	
	}

}
