package com.ls.lishuai.concurrency;

/**
 * �̵߳Ĵ���������
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" ); 
    	for (int i = 1; i <= 10; i++) {
    		Calculator c = new Calculator(i);
    		Thread t = new Thread(c);
    		t.start();
    	}
    }
}
