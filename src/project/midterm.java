package project;

import java.io.IOException;

public class midterm {

	public static void main(String[] args) throws IOException {
		if(args!=null) {
			if(args[0].equals("f")) {
				if(args[2].equals("q")) {								// collection.xml�� index.post�� ���� �ִ� ���丮�� ��θ����� �Է��ؾ� �մϴ�.
					genSnippet gen = new genSnippet(args[1], args[3]);
					gen.checkKeyword();
				}
			}
		}
		
	}

}
