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

public class parsing_actor {

	
	
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
			 
			
			 ResultSet rs = statement.executeQuery("select max(id) from stars\r\n" + 
						"where id like \"aa%\";");
				
			 String idf = "aa";
			Integer idb = 0;
			if(rs.next())
			{
				String ts = rs.getString(1);
				if(ts != null)
				{	
					if(ts.substring(0, 2).equals("aa"))
					{
						idb = Integer.parseInt(ts.substring(2));
						idb++;
					}
				}
			}

			DocumentBuilder db = dbf.newDocumentBuilder();

			dom = db.parse("actors63.xml");
			
			Element docEle = dom.getDocumentElement();
			NodeList nl = docEle.getElementsByTagName("actor");
			System.out.println(nl.getLength());
			for(int i = 0; i < nl.getLength();i++)
			{
				if(nl.item(i).getNodeType() == Node.ELEMENT_NODE)
				{
					
					Element el = (Element) nl.item(i);
					
					String name = el.getElementsByTagName("stagename").item(0).getTextContent();
					String dob = "";
					System.out.println(name);
					name = name.replaceAll("\"", "");
					String query = "SELECT check_star(\"" + name +"\" );";
					rs = statement.executeQuery(query);
					rs.next();
					
					if(rs.getString(1).equals("0"))
					{
						if(el.getElementsByTagName("dob").getLength() > 0)
						{
							String dd = el.getElementsByTagName("dob").item(0).getTextContent();
							if(!dd.equals(""))
							{
								dob = dd;
							}
							if(dob.equals(""))
							{
								add_star.setString(3, null);
							}
							else
							{
								try
								{
									Integer x = Integer.parseInt(dob);
									add_star.setInt(3, x);
								}
								catch (Exception e)
								{
									add_star.setString(3, null);
								}
							}
						}
						
						String sid = idf + String.format("%07d", idb);
						add_star.setString(1, sid);
						add_star.setString(2,name);
						add_star.addBatch();
						idb++;
					}
				}
			}	
			
				
			
			add_star.executeBatch();
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
