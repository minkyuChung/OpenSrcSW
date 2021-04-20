package project;

import java.io.IOException;

public class midterm {

	public static void main(String[] args) throws IOException {
		if(args!=null) {
			if(args[0].equals("f")) {
				if(args[2].equals("q")) {								// collection.xml과 index.post가 같이 있는 디렉토리를 경로명으로 입력해야 합니다.
					genSnippet gen = new genSnippet(args[1], args[3]);
					gen.checkKeyword();
				}
			}
		}
		
	}

}
