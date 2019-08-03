/**
 * 
 */
package com.ls.li.interview.baidu;

import java.util.Scanner;

/**
 * @author lishuai
 * @data 2017-4-28 上午11:11:02
 */

public class KongjianSandian {
	public static Point[] temp=new Point[3];
	public static double maxSize=0.0;
	public static Point[] os=null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		while(sc.hasNext()){
			int n=sc.nextInt();
			sc.nextLine();
			Point[] points=new Point[n];
			for(int i=0;i<n;i++){
				String[] ss=sc.nextLine().split(" ");
				points[i]=new Point(ss[0], Integer.parseInt(ss[1]), Integer.parseInt(ss[2]),
						Integer.parseInt(ss[3]));
			}
			os=points;
			combine1(n, 3);			
			System.out.println(String.format("%.5f", maxSize));
			
		}

	}
	public static void combine1(int n, int r) {  
        if(r == 0){  
        	maxSize=Math.max(getSize(temp[0], temp[1], temp[2]),maxSize);  
        }else if(n<r){  
            return;  
        }else{  
            combine1(n-1, r);//不选当前数字，从剩下的数字中选r个  
            temp[r-1] = os[n-1];  
            combine1(n-1, r-1);//选择当前数字，从剩下的当中选r-1个  
        }  
    }  
	public static double getSize(Point a, Point b, Point c){
		double area = -1;  
	    if(!((a.color.equals(b.color)&&a.color.equals(c.color))||
	    		(!a.color.equals(b.color)&&!a.color.equals(c.color)&&!b.color.equals(c.color)))) return area;
	    double[] side=new double[3];//存储三条边的长度;  
	  
	    side[0] = Math.sqrt(Math.pow(a.x - b.x,2)+Math.pow(a.y - b.y,2) + Math.pow(a.z - b.z,2));  
	    side[1] = Math.sqrt(Math.pow(a.x - c.x,2)+Math.pow(a.y - c.y,2) + Math.pow(a.z - c.z,2));  
	    side[2] = Math.sqrt(Math.pow(c.x - b.x,2)+Math.pow(c.y - b.y,2) + Math.pow(c.z - b.z,2));  
	  
	    //不能构成三角形;  
	    if(side[0]+side[1]<=side[2] || side[0]+side[2]<=side[1] || side[1]+side[2]<=side[0]) return area;  
	  
	    //利用海伦公式。s=sqr(p*(p-a)(p-b)(p-c));  
	    double p = (side[0]+side[1]+side[2])/2; //半周长;  
	    area = Math.sqrt(p*(p-side[0])*(p-side[1])*(p-side[2]));  
	    return area;  
		
	}
}


class Point{
	String color;
	int x;
	int y;
	int z;
	public Point(String color,int x,int y,int z){
		this.color=color;
		this.x=x;
		this.y=y;
		this.z=z;
	}
}