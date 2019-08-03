package com.ls.lishuai.serialization.java;



import com.ls.lishuai.serialization.model.UserVo;

import java.io.*;

public class T {
	public static void main(String[] args) {
		input();
		//out();
    }
	public static void out() {
		System.out.println("Hello World!");
        UserVo dto = new UserVo();
        dto.setAge(11);
        //在当前文件所在的盘符下创建文件夹和文件
        File file = new File("/users/lishuai/p3.txt");
        try {
            ObjectOutputStream oout =
                    new ObjectOutputStream(new FileOutputStream(file));
            oout.writeObject(dto);
           
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void input() {
		 File file = new File("/users/lishuai/p3.txt");
		 try {
	            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
	            UserVo readObject = (UserVo)input.readObject();
	            System.out.println(readObject.toString());
	           
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
