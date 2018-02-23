package testingxmlparsing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class parsing_casts {

	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String loginUser = "lihengz2";
        String loginPasswd = "as499069589";
        String loginUrl = "jdbc:mysql://ec2-52-53-153-231.us-west-1.compute.amazonaws.com:3306/moviedb";
        

        
		//create an instance
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//dbf.setValidating(true);
		//dbf.setNamespaceAware(true);
		Document dom;

		try {
			 Class.forName("com.mysql.jdbc.Driver").newInstance();

			 Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        	// Declare our statement
			 Statement statement = dbcon.createStatement();
			 dbcon.setAutoCommit(false);
			 
			 PreparedStatement add_star = null;
			 String sqladd_star = null;
			 sqladd_star = "insert into stars (id, name, birthYear) value(?,?,?)";
			 add_star = dbcon.prepareStatement(sqladd_star);
			 List<String> new_s = new ArrayList<String>();
			 
			 PreparedStatement add_movie = null;
			 String sqladd_movie = null;
			 sqladd_movie = "insert into movies (id, title, year, director) value(?,?,?,?)";
			 add_movie = dbcon.prepareStatement(sqladd_movie);
			 List<String> new_m = new ArrayList<String>();
			 
			 PreparedStatement add_sm = null;
			 String sqladd_sm = null;
			 sqladd_sm = "insert into stars_in_movies (starId, movieId) "
				 		+ "value((select id from stars where id like ?),(select id from movies where id like ?))";
			 add_sm = dbcon.prepareStatement(sqladd_sm);
			 
			 List<String> ss = new ArrayList<String>();
			 List<String> mm = new ArrayList<String>();
			 
			ResultSet rs = statement.executeQuery("select max(id) from stars\r\n" + 
					"where id like \"aa%\";");
			
			String idf = "aa";
			Integer idb = 0;
			if(rs.next())
			{
				String ts = rs.getString(1);
				if(ts.substring(0, 2).equals("aa"))
				{
					idb = Integer.parseInt(ts.substring(2));
				}
			}

			

			DocumentBuilder db = dbf.newDocumentBuilder();

			dom = db.parse("casts124.xml");
			
			Element docEle = dom.getDocumentElement();
			NodeList nl = docEle.getElementsByTagName("dirfilms");
			System.out.println(nl.getLength());
			for(int i = 0; i < nl.getLength();i++)
			{
				if(nl.item(i).getNodeType() == Node.ELEMENT_NODE)
				{
					Element el = (Element) nl.item(i);
					String dir_name = el.getElementsByTagName("is").item(0).getTextContent();
					
					NodeList ml = el.getElementsByTagName("filmc");
					for(int x = 0; x< ml.getLength(); x++)
					{
						if(ml.item(x).getNodeType() == Node.ELEMENT_NODE)
						{
							Element ee = (Element) ml.item(x);
							String movie_name = ee.getElementsByTagName("t").item(0).getTextContent();
							String movie_id = ee.getElementsByTagName("f").item(0).getTextContent();
							String star_name = ee.getElementsByTagName("a").item(0).getTextContent();
							
							rs = statement.executeQuery("SELECT check_movie(\"" + movie_name +"\",\"" + movie_id +"\" );");
							rs.next();
							String ck = rs.getString(1);
							if(ck.equals("0") && !new_m.contains(movie_id))
							{
								add_movie.setString(1, movie_id);
								add_movie.setString(2, movie_name);
								add_movie.setString(4, dir_name);
								add_movie.setInt(3, 0);
								add_movie.addBatch();
								new_m.add(movie_id);
							}
							else if(ck.equals("1"))
							{
								rs = statement.executeQuery("SELECT * from movies where title LIKE \"" + movie_name + "\" or id LIKE \""+ movie_id + "\";");
								rs.next();
								movie_id = rs.getString("id");
							}
							star_name = star_name.replaceAll("\"", "");
							rs = statement.executeQuery("SELECT check_star(\"" + star_name +"\");");
							rs.next();
							String star_id = "";
							star_id = idf + String.format("%07d", idb);
							String check = rs.getString(1);
							if(check.equals("0") && !new_s.contains(star_name))
							{
								idb++;
								star_id = idf + String.format("%07d", idb);
								add_star.setString(1,star_id);
								add_star.setString(2, star_name);
								add_star.setString(3, null);
								add_star.addBatch();
								new_s.add(star_name);
								
							}
							else if(check.equals("1"))
							{
								rs = statement.executeQuery("SELECT * from stars where name LIKE \"" + star_name + "\";");
								rs.next();
								star_id = rs.getString("id");
							}
							ss.add(star_id);
							mm.add(movie_id);
						}
					}
				}
			}	
			
			for(int g = 0; g < ss.size(); g++)
			{
				String qq = "SELECT check_s_in_m(\"" + ss.get(g) +"\",\"" + mm.get(g)  +"\");";
				rs = statement.executeQuery(qq);
				rs.next();
				if(rs.getString(1).equals("0"))
				{
					add_sm.setString(1,ss.get(g));
					add_sm.setString(2,mm.get(g));
					add_sm.addBatch();
				}
			}
			
			
			
			rs.close();
				
			
			add_movie.executeBatch();
			add_star.executeBatch();
			dbcon.commit();
			add_sm.executeBatch();
			dbcon.commit();
			
			
			
		}
		catch (SQLException ex) {
	           while (ex != null) {
	                 System.out.println ("SQL Exception:  " + ex.getMessage ());
	                 ex = ex.getNextException ();
	             }}  // end while
	    catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		
		
		
		
		
	}
}
