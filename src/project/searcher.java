package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class searcher {

	File post = null;
	File title = null;
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	Document doc = null;
	
	String query = "";
	String[] keyword = null;
	double[] sim = null;
	
	public searcher(String filepath, String query) {
		post = new File(filepath + "index.post");
		title = new File(filepath + "collection.xml");
		this.query = query;
	}
	
	public void GetKeyword() throws Exception {
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		keyword = new String[kl.size()];
		for (int i = 0; i < kl.size(); i++) {
			keyword[i] = kl.get(i).getString();
		}
		
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.parse(title); 
		NodeList list = doc.getElementsByTagName("title");
		sim = new double[list.getLength()];
		
		CalcSim(keyword, post);
		int[] grade = {1,2,3,4,5};
		//높은 숫자 순으로 정렬
		for (int i = 0; i < sim.length-1; i++) {
			for (int j = i; j < sim.length-1; j++) {
				if(sim[j] < sim[j+1]) {
					int tmp = grade[j];
					grade[j] = grade[j+1];
					grade[j+1] = tmp;
				}
			}
		}
		System.out.println("##유사도 순위##");
		int cnt = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < grade.length; j++) {
				if(grade[j] == i+1) {
					if(sim[j]==0)	continue;
					//소수점 2자리까지 나타내는 기능 추가
					System.out.println(i+1+"위 : "+ list.item(j).getFirstChild().getNodeValue().trim()+" / "+String.format("%.2f", sim[j]));
					cnt++;
				}
			}
		}
		if(cnt == 0)
			System.out.println("관련 문서를 찾을 수 없습니다.");
	}
	@SuppressWarnings({ "rawtypes", "unchecked", "resource", "null" })
	public void CalcSim(String[] keyword, File post) throws Exception{
		FileInputStream fileStream = new FileInputStream(post);
		ObjectInputStream objectIntputStream = new ObjectInputStream(fileStream);
		LinkedHashMap KeywordMap = new LinkedHashMap();
		KeywordMap = (LinkedHashMap) objectIntputStream.readObject();
		
		double[] inner = innerProduct(keyword, post);
		double[] pow = new double[sim.length];
		
		for (int i = 0; i < keyword.length; i++) {
			Iterator<String> it = KeywordMap.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				ArrayList<Double> value = (ArrayList<Double>) KeywordMap.get(key);
				if(key.equals(keyword[i])) {
					for (int j = 0; j < value.size()/2; j++) {
						pow[(int) (value.get(2*j)-1)] += Math.pow(value.get(2*j+1),2);
					}
					break;
				}
			}
		}
		
		for (int i = 0; i < sim.length; i++) {
			//0인 값은 순위 출력 제외
			if(pow[i]==0)	continue;
			sim[i] = inner[i]/(Math.sqrt(2*pow[i]));
		}
	}
	
	//유사도 계산
	@SuppressWarnings({ "rawtypes", "unchecked", "resource", "null" })
	public double[] innerProduct(String[] keyword, File post) throws Exception {
		FileInputStream fileStream = new FileInputStream(post);
		ObjectInputStream objectIntputStream = new ObjectInputStream(fileStream);
		LinkedHashMap KeywordMap = new LinkedHashMap();
		KeywordMap = (LinkedHashMap) objectIntputStream.readObject();
		
		//전역변수 대신 내부에서 double 배열 넘겨주는 방식 변경이 있었습니다.
		double[] inner = new double[sim.length];
		
		for (int i = 0; i < keyword.length; i++) {
			Iterator<String> it = KeywordMap.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				ArrayList<Double> value = (ArrayList<Double>) KeywordMap.get(key);
				if(key.equals(keyword[i])) {
					for (int j = 0; j < value.size()/2; j++) {
						inner[(int) (value.get(2*j)-1)] += value.get(2*j+1);
					}
					break;
				}
			}
		}
		//유사도 순서대로 담은 실수형 배열 반환
		return inner;
	}
}









