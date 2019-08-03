package com.ls.lishuai.jvm;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/8 11:07
 */
public class StackErrorMock {
    private static int index = 1;

    public void call(){
        index++;
        call();
    }

    public static void main(String[] args) {
        StackErrorMock mock = new StackErrorMock();
        try {
            mock.call();
        }catch (Throwable e){
            System.out.println("Stack deep : "+index);
            e.printStackTrace();
        }
    }
}
