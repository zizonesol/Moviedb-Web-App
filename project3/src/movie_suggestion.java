

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class movie_suggestion
 */
@WebServlet("/movie_suggestion")
public class movie_suggestion extends HttpServlet {
	
	
	
	private String[] stopw = {"a","about","an","are"
            ,"as","at","be","by","com"
            ,"de","en","for","from"
            ,"how","i","in","is","it"
            ,"la","of","on",
            "or","that","the","this","to"
            ,"was","what","when","where"
            ,"who","will","with","und","the","www"};
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			String loginUser = "lihengz2";
	        String loginPasswd = "as499069589";
	        String loginUrl = "jdbc:mysql://ec2-52-53-153-231.us-west-1.compute.amazonaws.com:3306/moviedb";
			        
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           // Declare our statement
			Statement statement = dbcon.createStatement();
			
			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();
			
			// get the query string from parameter
			String query = request.getParameter("query");
			
			// return the empty json array if query is null or empty
			if (query == null || query.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
			
			String[] sear = query.split(" ");
			String srq = "title LIKE \"%" + query + "%\"";
			String sq = "name LIKE \"%" + query + "%\"";
			Integer stopc = 0;
			String zz = "match(title) against(\"";
			String ss = "match(name) against(\"";
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
						ss = ss + "+" +sear[z] + "*,";
					}
				}
				zz = zz.substring(0,zz.length()-1);
				ss = ss.substring(0,ss.length()-1);
				zz = zz + "\" in boolean mode)";
				ss = ss + "\" in boolean mode)";
				if(sear.length != stopc)
				{
					srq = zz;
					sq = ss;
				}
			}
			
			String qq = "select title from movies where "+srq+"\r\n" + 
					"limit 10;";
			
			ResultSet rs = statement.executeQuery(qq);
			Integer count = 0;
			while(rs.next())
			{
				String m_title = rs.getString("title");
				jsonArray.add(generateJsonObject(m_title,"movie"));
				count++;
			}
			
			if(count < 10)
			{
				qq = "select name from stars where "+sq+"\r\n" + 
						"limit 10;";
				
				rs = statement.executeQuery(qq);
				
				while(rs.next())
				{
					String m_title = rs.getString("name");
					jsonArray.add(generateJsonObject(m_title,"star"));
					count++;
					if(count >= 10)
					{
						break;
					}
				}
			}
			response.getWriter().write(jsonArray.toString());
			return;
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}
	}
		
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static JsonObject generateJsonObject(String name, String categoryName) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", name);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("category", categoryName);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}

}
