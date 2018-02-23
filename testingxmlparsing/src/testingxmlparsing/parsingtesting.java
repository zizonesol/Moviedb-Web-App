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

public class parsingtesting {

	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        
        List<String> mid_list = new ArrayList<String>();
        List<String> gid_list = new ArrayList<String>();
        List<String> new_list = new ArrayList<String>();

        
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
			 
			 PreparedStatement add_movie = null;
			 String sqladd_movie = null;
			 sqladd_movie = "insert into movies (id, title, year, director) value(?,?,?,?)";
			 add_movie = dbcon.prepareStatement(sqladd_movie);
			 
			 PreparedStatement add_gm = null;
			 String sqladd_gm = null;
			 sqladd_gm = "insert into genres_in_movies (genreId, movieId) "
			 		+ "value((select id from genres where id like ?),(select id from movies where id like ?))";
			 add_gm = dbcon.prepareStatement(sqladd_gm);
			 
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("mains243.xml");
			
			Element docEle = dom.getDocumentElement();
			NodeList nl = docEle.getElementsByTagName("directorfilms");
			if(nl != null && nl.getLength() > 0) {
				for(int i = 0 ; i < 10;i++) {
					
					NodeList cn = nl.item(i).getChildNodes();
					
					for (int z = 0; z < cn.getLength(); z++)
					{
						String dir_name = "";
						if(cn.item(0).getNodeType() == Node.ELEMENT_NODE)
						{
							Element zn = (Element) cn.item(0);
							dir_name = (zn.getElementsByTagName("dirname").item(0)).getTextContent();
						}
						
						NodeList fl = cn.item(1).getChildNodes();
						for(int x = 0; x < fl.getLength();x++)
						{
							if(fl.item(x).getNodeType() == Node.ELEMENT_NODE)
							{
								Element el = (Element) fl.item(x);
								String til = el.getElementsByTagName("t").item(0).getTextContent();
								String fid = el.getElementsByTagName("fid").item(0).getTextContent();
								String year = el.getElementsByTagName("year").item(0).getTextContent();
								
								String cats = el.getElementsByTagName("cats").item(0).getTextContent();
								String g_id = "";
								
								String query = "";
								
								
								query = "SELECT check_movie(\"" + til +"\",\""+ fid + "\");";
						    	// Perform the query
					        	ResultSet rs = statement.executeQuery(query);
					        	rs.next();
					        	
				        		if(rs.getString(1).equals("0") && new_list.contains(fid) == false)
				        		{
				        		
				        			add_movie.setString(1,fid);
				        			add_movie.setString(2, til);
				        			add_movie.setInt(3,  Integer.parseInt(year));
				        			add_movie.setString(4, dir_name);
				        			add_movie.addBatch();
				        			new_list.add(fid);
				        		}
				        		else
				        		{
				        			query = "SELECT * from movies where title LIKE \"" + til + "\";";
				        			rs = statement.executeQuery(query);
				        			if(rs.next())
				        			{
				        				fid = rs.getString("id");
				        			}
				        		}
				        		
				        		rs.close();
				        		
				        		if(!cats.equals(""))
			        			{
			        				query = "SELECT check_genre(\"" + cats +"\");";
			        				ResultSet gs = statement.executeQuery(query);
			        				gs.next();
			        				if(gs.getString(1).equals("0"))
			        				{
			        					statement.execute("INSERT INTO genres (name) \r\n"
				             			   + "VALUES ('" + cats + "');");
			        				}
			        				gs = statement.executeQuery("SELECT * from genres where name LIKE \""+ cats + "\";");
			    					gs.next();
			    					g_id = gs.getString("id");
			    					
			        				mid_list.add(fid);
			        				gid_list.add(g_id);
			        				
			        				gs.close();
			        			
			        			}
					        	
					        	
							}
							
						}
					}
					
				}
				
				for(int g = 0; g < mid_list.size();g++)
				{
					String qq = "";
					ResultSet ss = null;
					
					
					qq = "SELECT check_g_in_m(" + gid_list.get(g) +",\"" + mid_list.get(g)  +"\");";
					ss = statement.executeQuery(qq);
					ss.next();
					if(ss.getString(1).equals("0"))
					{
						add_gm.setInt(1, Integer.parseInt(gid_list.get(g)));
						add_gm.setString(2,mid_list.get(g));
						add_gm.addBatch();
					}
				}
				
			
				add_movie.executeBatch();
				dbcon.commit();
				add_gm.executeBatch();
				dbcon.commit();
			}
			
			
			
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
