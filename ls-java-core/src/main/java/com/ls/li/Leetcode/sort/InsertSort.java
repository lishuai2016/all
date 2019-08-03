/**
 * 
 */
package com.ls.li.Leetcode.sort;

/**
 * @author lishuai
 * @data 2016-9-12 下午4:16:21
 */

public class InsertSort {

	/**
	 * @author lishuai
	 * @data 2016-9-12 下午4:16:21
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] a={6,2,4,8,9,7,1,3,0,5};
		insert_sort1(a);
		for(int i:a){
			System.out.println(i);
		}
	}
	
	public static void insert_sort(int[] s){
		//查找和数据分离
//		int i = 0;
//        int j = 0;
//        int m = 0;
//        for (i =1;i<a.length ;i++){
//        	//要插入的元素
//            int mark=a[i];
//            //要插入的初始位置
//            m=i;
//            //查找要插入的位置
//            for (j=i-1;j>=0;j--){
//            	//比要插入的元素大 后移
//                 if (a[j]>mark){
//                     a[j+1]=a[j];
//                     m=j;
//                }else {                	
//                	break;
//                }
//           }
//            //执行插入
//           a[m]=mark;
//           System. out .println(i+"****************" );
//            for (int n =0;n<a. length;n++){
//                System. out .print(a[n] +"  " );
//           }
//           System. out .println();
//         }
//
//
//       System. out .println();
//        for (int n =0;n<a. length;n++){
//           System. out .print(a[n] +"  " );
//       }
		//查找和数据后移第一种
		for(int i=1;i<s.length;i++){
			int k=i;
			int mark=s[i];
			//查找插入的位置
			for(int j=i-1;j>=0;j--){
				if(mark<s[j]){
					s[j+1]=s[j];
					k=j;
				}else{
					break;
				}
			}
			s[k]=mark;
		}
		 System. out .println();
	}
	
	//查找和数据后移第二种
	public static void insert_sort1(int[] s){
		for(int i=1;i<s.length;i++){
			if(s[i]<s[i-1]){
				int temp=s[i];
				int j;
				for(j=i-1;j>=0&&s[j]>temp;j--){
					s[j+1]=s[j];
				}
				s[j+1]=temp;
			}
		}
	}
	
	
	
	
}
