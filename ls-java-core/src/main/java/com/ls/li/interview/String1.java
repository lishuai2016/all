package com.ls.li.interview;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/25 10:51
 */
public class String1 {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //compact("aaaaabbcdde");
//		System.out.println(Integer.MAX_VALUE);
//		System.out.println(Integer.MAX_VALUE / 1000000000);
        reverse(123);
    }
    //1压缩字符串aaaaabbcdde   5a2b1c2d
    public static void compact(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            int count = 1;
            int j = i + 1;
            while (j < s.length() && s.charAt(i) == s.charAt(j)) {
                j++;
                count++;
            }
            sb.append(count).append(s.charAt(i));
            i = j;
        }
        System.out.println(sb.toString());
    }

    //2翻转一个整数，不许转化成字符串 123   321   2147483647
    public static void reverse(Integer a) {
        //找到最高位
        Integer base = 1000000000;
        while (a / base == 0) {
            base /= 10;
        }
        int k = 1;
        int res = 0;
        while (base != 0) {
            int num = a / base % 10;
            res += num * k;
            k *= 10;
            base /= 10;
        }
        System.out.println(res);
    }

}
