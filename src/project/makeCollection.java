package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {
	File file = null;
	File[] files = null;
	
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder docBuilder = null;
	Document doc = null;
	Element docs = null;
	
	public makeCollection(String filePath) {
		file = new File(filePath);
	}

	public void callDocument() {
		
		files = file.listFiles();
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		doc = docBuilder.newDocument();
		
		docs = doc.createElement("docs");
		doc.appendChild(docs);
	}
	
	public void convertHTMLtoXML() throws IOException {
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;
		int count = 0;
		for(int i = 0; i < files.length; i++) {
			String temp = "";
			String content = "";
			String docTitle = "";
			
			try {
				fis = new FileInputStream(files[i]);
				isr = new InputStreamReader(fis,"UTF-8");
				br = new BufferedReader(isr);
				
				while((temp=br.readLine()) != null) {
					if(temp.contains("<title>")) {
						docTitle = temp.replaceAll("<title>|</title>", "");
					}else if(temp.contains("<p>")) {
						String noTag = temp.replaceAll("<p>|</p>", "");
						content += noTag + "\n";
					}	
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				fis.close();
				isr.close();
				br.close();
			}
			Element code = doc.createElement("doc");
			docs.appendChild(code);
			
			String idNum = Integer.toString(count);
			code.setAttribute("id", idNum);
			
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(docTitle));
			code.appendChild(title);
			
			Element body = doc.createElement("body");		
			body.appendChild(doc.createTextNode(content));
			code.appendChild(body);

			count++;
		}
	}
	
	public void printXML() {
		//XML 파일로 쓰기
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer;
		DOMSource source = new DOMSource(doc);
		StreamResult result;

		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			result = new StreamResult(new FileOutputStream(new File("..\\collection.xml")));
			
			transformer.transform(source, result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
	}
}
