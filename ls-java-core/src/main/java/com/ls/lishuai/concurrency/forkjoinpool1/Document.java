package com.ls.lishuai.concurrency.forkjoinpool1;

import java.util.Random;

public class Document {

	private String[] words= {"the","hello","goodbye","packet","java",
			"thread","pool","random","class","main"};
	
	
	public String[][] generateDocument(int numlines,int numwords,String word) {
		int counter = 0;
		String[][] document = new String[numlines][numwords];
		Random random = new Random();
		for (int i = 0;i < numlines;i++) {
			for (int j = 0;j<numwords;j++) {
				int index = random.nextInt(words.length);
				document[i][j] = words[index];
				if (document[i][j].endsWith(word)) {
					counter++;
				}
			}
		}
		System.out.printf("the word appears :%d\n",counter);
		return document;
	}
}
