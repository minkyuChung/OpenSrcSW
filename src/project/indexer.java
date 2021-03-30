package project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class indexer {
	File file = null;

	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	Document doc = null;
	
	public indexer(String filepath) {
		file = new File(filepath);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void calculateTfIdf() throws Exception {
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.parse(file); 
		
		NodeList list = doc.getElementsByTagName("body");
		Element oBody = null;
		String content = "";
		
		//Hashmap ¿˙¿Â
		FileOutputStream fileStream = new FileOutputStream("../index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		LinkedHashMap KeywordMap = new LinkedHashMap();

		for (int i = 0; i < list.getLength(); i++) {
			oBody = (Element)list.item(i);	
			content = oBody.getFirstChild().getNodeValue();
			String[] pair = content.split("#");
		
			for (int j = 0; j < pair.length; j++) {
				String[] temp = pair[j].split(":");
				ArrayList<Double> tfidfList;
				if(KeywordMap.containsKey(temp[0])) {
					tfidfList =  (ArrayList<Double>) KeywordMap.get(temp[0]);
				}else {
					tfidfList = new ArrayList<Double>();
				}
				
				if(!temp[1].equals("0")) {
					tfidfList.add(i+1.0);
					tfidfList.add(Double.parseDouble(temp[1]));
				}
				KeywordMap.put(temp[0], tfidfList);
			}
		}
		Iterator<String> it = KeywordMap.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			ArrayList<Double> value = (ArrayList<Double>) KeywordMap.get(key);
			for (int k = 0; k < value.size()/2; k++) {
				double wgt = value.get(2*k+1)*Math.log(list.getLength()/(value.size()/2.0));
				value.set(2*k+1, wgt);
			}
			KeywordMap.replace(key, value);
			System.out.print(key+"->"+value+" ");
		}
		System.out.println();
		objectOutputStream.writeObject(KeywordMap);
		objectOutputStream.close();
	}
}



















