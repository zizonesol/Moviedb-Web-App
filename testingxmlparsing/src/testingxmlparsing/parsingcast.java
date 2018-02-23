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

public class parsingcast {

	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        

        
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
				if(ts.substring(0, 2).equals("aa"))
				{
					idb = Integer.parseInt(ts.substring(2));
				}
				idb++;
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
					System.out.println(el.getElementsByTagName("is").item(0).getTextContent());
					
					NodeList ml = el.getElementsByTagName("filmc");
					for(int x = 0; x< ml.getLength(); x++)
					{
						if(ml.item(x).getNodeType() == Node.ELEMENT_NODE)
						{
							Element ee = (Element) ml.item(x);
							System.out.println(ee.getElementsByTagName("a").item(0).getTextContent());
						}
					}
				}
			}	
			
				
			
			//add_star.executeBatch();
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
