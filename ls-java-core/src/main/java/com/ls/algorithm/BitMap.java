package com.ls.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-10 09:56
 *
-2147483648－－2147483647       数字10亿是1G

假设需要排序或则查找的数的总数N=100000000，BitMap中1bit代表一个数字，1个int = 4Bytes = 4*8bit = 32 bit,
那么N个数需要N/32 int空间。所以我们需要申请内存空间的大小为int a[1 + N/32]，其中：a[0]在内存中占32为可以对应十进制数0-31，依次类推：

那么十进制数如何转换为对应的bit位，下面介绍用位移将十进制数转换为对应的bit位:
　　1.求十进制数在对应数组a中的下标
　　十进制数0-31，对应在数组a[0]中，32-63对应在数组a[1]中，64-95对应在数组a[2]中………，使用数学归纳分析得出结论：
对于一个十进制数n，其在数组a中的下标为：a[n/32]
　　2.求出十进制数在对应数a[i]中的下标
　　例如十进制数1在a[0]的下标为1，十进制数31在a[0]中下标为31，十进制数32在a[1]中下标为0。 在十进制0-31就对应0-31，
而32-63则对应也是0-31，即给定一个数n可以通过模32求得在对应数组a[i]中的下标。
　　3.位移
　　对于一个十进制数n,对应在数组a[n/32][n%32]中，但数组a毕竟不是一个二维数组，我们通过移位操作实现置1
　　a[n/32] |= 1 << n % 32
 *
 *
 *
 * 1、n / 32 求十进制数在数组a中的下标
 * 2、相当于 n % 32 求十进制数在数组a[i]中的下标
 *
 */
public class BitMap {
    private  int[] arr = new int[Integer.MAX_VALUE / 32 + 1];  //要多一个 比如32，需要两个int   大概500M的空间
//    private Long[] arr_long = new Long[Long.MAX_VALUE / 256 + 1];

    public void addValue(int n) {
        arr[n/32] |= (1 << n % 32); //设置位  或操作
    }

    public boolean existValue(int n) {
        return (arr[n/32] & (1 << n % 32)) == 1; //与操作检测位
    }

    public void printMap(int row) {
        for (int i = 0;i < row; i++) {
            List list = new ArrayList(32);
            int temp = arr[i];
            for (int j = 0;j < 32; j++) {
                list.add(temp & 1 );
                temp >>= 1;
            }
            System.out.println("arr["+i+"]" + list);
        }
    }

    public static void main(String[] args){
        int num[] = {1,5,30,32,64,56,159,120,21,17,35,45};
        BitMap map = new BitMap();
        for(int i=0;i<num.length;i++){
            map.addValue(num[i]);
        }

        int temp = 120;
        if(map.existValue(temp)){
            System.out.println("temp:" + temp + "has already exists");
        }
        map.printMap(5);
    }


}
