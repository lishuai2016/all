/**
 * 
 */
package com.ls.li.interview.renren;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author lishuai
如你所知，中国素来有发红包的习俗。
新年要到了，小明想要知道朋友圈里每个人的收益。
每个人有mi数量的钱用来发红包，并且这笔钱会平均地发给ki个人（收益得到的钱不再发红包）。
而且发给每个人的钱都是整数。如果不能整除，发红包的人保留mi mod ki的钱。


								
输入
第1行，一个整数n，2<=n<=10，小明的n个朋友。
第 2到n+1行每行一个字符串，第i+1行表示第i个人的名字。
接下来n段：
每段第一行是一个字符串，表示发红包的人的名字。
第二行为两个数mi,ki。含义如上。
接下来ki行，每行一个字符串表示被发红包的人的名字。
 */

public class T2 {

		public static void main(String[] args) {
			Scanner scanner =new Scanner(System.in);
			int n=scanner.nextInt();
			scanner.nextLine();
			Map<String, Integer> map=new LinkedHashMap<String, Integer>();
			for(int i=0;i<n;i++){
				String name=scanner.nextLine();
				map.put(name, 0);
			}
			for(int i=0;i<n;i++){
				String name=scanner.next();
				int mi=scanner.nextInt();//每个人的钱数，用来法红包
				int ki=scanner.nextInt();
				int money=0;
				if(ki!=0){
					int t=mi%ki;
					money=(mi-t)/ki;
					map.put(name, mi%ki+map.get(name)-mi);
				}
				for(int j=0;j<ki;j++){
					String tname=scanner.next();
					map.put(tname,(map.get(tname)+money));
				}
			}
			for(Map.Entry<String, Integer> entry:map.entrySet()){
				System.out.println(entry.getKey()+" "+entry.getValue());
			}
			scanner.close();
		}
	}

