package com.ls.lishuai.concurrency.condition;

public class FileMock {
	private String[] content;
	private int index;
	public FileMock(int size, int length) {
		content = new String[size];
		for (int i= 0;i <size;i++) {
			StringBuilder sb = new StringBuilder(length);
			for (int j = 0; j < length; j++) {
				int indice=(int)Math.random()*255;
				sb.append((char)indice);
				content[j] = sb.toString();
			}
		}
		index = 0;
	}
	public boolean hasmorelines() {
		return index<content.length;
	}
	public String getline() {
		if (this.hasmorelines()) {
			System.out.println("Mock: " + (content.length-index));
			return content[index++];
		}
		return null;
	}
}
