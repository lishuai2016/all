package com.ls.lishuai.concurrency;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TestPrintWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileWriter file = null;
		PrintWriter pw = null;
		try {
			file = new FileWriter("d:\\log.txt");
			pw = new PrintWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.printf("11111111111111111");
		pw.println();
		pw.printf("22222222222222222");
		pw.close();
	}

}
