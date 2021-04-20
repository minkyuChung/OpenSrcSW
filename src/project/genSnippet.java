package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;



public class genSnippet {
	File file = null;
	String query = "";
	
	
	public genSnippet(String filePath, String query) {
		file = new File(filePath);
		this.query = query;
	}
	
	public String[] split(String query) {
		String[] temp = query.split(query);
		return temp;
	}
	
	public void checkKeyword() throws IOException {
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fis = null;
		String[] keyword = split(query);
		String[] temp = new String[5];
		int[] num = new int[5];
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis,"UTF-8");
			br = new BufferedReader(isr);
			int count=0;		//가장 count 높은 temp가 당첨
			int i =0;
			while((temp[i]=br.readLine()) != null) {
				for (int idx = 0; idx < keyword.length; idx++) {
					if(temp[i].contains(keyword[idx])) {
						count++;	
					}
				}
				num[i] = count;
				i++;
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
		int max = 0;
		for (int i = 0; i < num.length; i++) {
			max = num[i] >num[i+1] ? i:i+1;
		}
		System.out.print(temp[max+1]);
	}
	
}
