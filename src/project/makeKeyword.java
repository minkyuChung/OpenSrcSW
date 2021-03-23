package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.*;


public class makeKeyword {
	File file = null;
	
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	Document doc = null;
	Document xml = null;
	Element docs = null;
	
	public makeKeyword(String filePath) {
		file = new File(filePath);
	}

	
	public void countKeyword() throws Exception {
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.parse(file); 
		
        xml = docBuilder.newDocument();
		
		docs = xml.createElement("docs");
		xml.appendChild(docs);

		NodeList list = doc.getElementsByTagName("body");
		NodeList list2 = doc.getElementsByTagName("title");
		Element oBody = null;
		Element oTitle = null;
		
		String docTitle = "";
		String content = "";
				
		for (int i = 0; i < list.getLength(); i++) {
			oTitle = (Element)list2.item(i);
			oBody = (Element)list.item(i);
			
			docTitle = oTitle.getFirstChild().getNodeValue();
			content = oBody.getFirstChild().getNodeValue();
			
			KeywordExtractor ke = new KeywordExtractor();
			KeywordList kl = ke.extractKeyword(content, true);
			content = "";
			for (int j = 0; j < kl.size(); j++) {
				Keyword kwrd = kl.get(j);
				content += kwrd.getString() + ":" + kwrd.getCnt() + "#";
			}
			Element code = xml.createElement("doc");
			docs.appendChild(code);
			
			String idNum = Integer.toString(i);
			code.setAttribute("id", idNum);
			
			Element title = xml.createElement("title");
			title.appendChild(xml.createTextNode(docTitle));
			code.appendChild(title);
			
			Element body = xml.createElement("body");		
			body.appendChild(xml.createTextNode(content));
			code.appendChild(body);
				
		}
		
		
	}
	
	public void printXML() {
		//XML 파일로 쓰기
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
						
				Transformer transformer;
				DOMSource source = new DOMSource(xml);
				StreamResult result;
				try {
					transformer = transformerFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
					
					result = new StreamResult(new FileOutputStream(new File("..\\index.xml")));
					
					transformer.transform(source, result);
				} catch (TransformerException e) {e.printStackTrace();
				} catch (FileNotFoundException e) {e.printStackTrace();}
	}
}
