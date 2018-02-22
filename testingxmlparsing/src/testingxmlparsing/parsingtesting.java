package testingxmlparsing;

import java.io.IOException;
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

	
	
	public static void main(String[] args){
		//create an instance
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	//	dbf.setValidating(true);
	//	dbf.setNamespaceAware(true);
		Document dom;

		try {
			
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation of the XML file
			dom = db.parse("mains243.xml");
			
			Element docEle = dom.getDocumentElement();
			
			NodeList nl = docEle.getElementsByTagName("directorfilms");
			if(nl != null && nl.getLength() > 0) {
				for(int i = 0 ; i < nl.getLength();i++) {
					
					NodeList cn = nl.item(i).getChildNodes();
					
					for (int z = 0; z < cn.getLength(); z++)
					{
						System.out.println(cn.item(z).getNodeName());
						NodeList zn = cn.item(z).getChildNodes();
						System.out.println(zn.getLength());
					}
					
				}
			}
			

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		
		
		
		
	}
}
