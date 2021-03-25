package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
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
	public void saveKeyword() throws Exception {
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.parse(file); 
		
		NodeList list = doc.getElementsByTagName("body");
		Element oBody = null;
		String content = "";
		
		
		//Hashmap ¿˙¿Â
		FileOutputStream fileStream = new FileOutputStream("index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		LinkedHashMap KeywordMap = new LinkedHashMap();

		for (int i = 0; i < list.getLength(); i++) {
			oBody = (Element)list.item(i);	
			content = oBody.getFirstChild().getNodeValue();
			String[] pair = content.split("#");
			
			
			
			for (int j = 0; j < pair.length; j++) {
				String[] temp = pair[j].split(":");
				
				String[] tfidfList = new String[2*list.getLength()];
				for (int idx = 0; idx < list.getLength(); idx++) {
					tfidfList[2*idx] = String.valueOf(idx+1);
					tfidfList[2*idx+1] = "0";
				}
				
				if(KeywordMap.containsKey(temp[0])) {
					tfidfList = (String[]) KeywordMap.get(temp[0]);
				}
				tfidfList[2*i+1] = temp[1];
				KeywordMap.put(temp[0], tfidfList);
			}
			
		}
		objectOutputStream.writeObject(KeywordMap);
		objectOutputStream.close();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void calculateTfIdf() throws IOException, ClassNotFoundException {
		FileInputStream fileStream = new FileInputStream("index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		LinkedHashMap hashMap = (LinkedHashMap)object;
		Iterator<String> it = hashMap.keySet().iterator();
		int count;
		
		while(it.hasNext()) {
			String key = it.next();
			String[] value =  (String[]) hashMap.get(key);
			count = 0;
			for (int i = 0; i < value.length/2; i++) {
				if(!value[2*i+1].equals("0")) {
					count++;
				}
			}
			System.out.print(key+"->"+Arrays.toString(convertTfIdf(value, count))+" ");
		}
	}
	public String[] convertTfIdf(String[] value, int cnt) {
		double wgt = 0;
		double num = value.length/2;
		for (int i = 0; i < value.length/2; i++) {
			wgt = Integer.parseInt(value[2*i+1])*Math.log(num/cnt);
			if(wgt == 0) {
				continue;
			}
			value[2*i+1] = String.valueOf(Math.round(wgt*100)/100.0);
		}
		return value;
	}
}



















