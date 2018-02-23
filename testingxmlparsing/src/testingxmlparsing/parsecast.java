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

public class parsecast {

	
	
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
			 sqladd_movie = "insert into stars (id, name, birthYear) value(?,?,?)";
			 add_movie = dbcon.prepareStatement(sqladd_movie);
			 
			ResultSet rs = statement.executeQuery("select max(id) from stars;");
			rs.next();
			String idf = rs.getString(1);
			Integer idb = Integer.parseInt(idf.substring(2, idf.length()));
			idf = idf.substring(0,2);
			System.out.println(idf);
			System.out.println(idb);
			DocumentBuilder db = dbf.newDocumentBuilder();
			rs.close();
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
					
					String dab = el.getElementsByTagName("dob").item(0).getTextContent();
					
					if(name != null)
					{
						System.out.println(dab);
						System.out.println(name);
					}
				}
			}
			
				
			
			
			//dbcon.commit();
			
			
			
			
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
