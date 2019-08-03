# 28. 实现strStr()


```java
优解：法3
/**
 *
 */
package string;

/**
 * @author lishuai
 * @data 2016-12-14 下午4:49:32
 */

public class ImplementstrStr {

    /**
     * @author lishuai
     * @data 2016-12-14 下午4:49:32
Implement strStr().
相当于string.indexof(sbustring)返回子串索引的位置
Returns the index of the first occurrence of needle in haystack, or -1 if needle is not part of haystack.
     */

    public static void main(String[] args) {
        System.out.println(strStr("abc","bc"));

    }
    //3九章(觉得2，3类似，3比2好理解)
    public static int strStr(String source, String target) {
        if (source == null || target == null) {
            return -1;
        }       
        for (int i = 0; i < source.length() - target.length() + 1; i++) {
            int j = 0;
            for (j = 0; j < target.length(); j++) {
                if (source.charAt(i + j) != target.charAt(j)) {
                    break;
                }
            }
            // finished loop, target found
            if (j == target.length()) {
                return i;
            }
        }
        return -1;
    }
    //2
    public static int strStr2(String haystack, String needle) {
          for (int i = 0; ; i++) {
            for (int j = 0; ; j++) {
              if (j == needle.length()) return i;
              if (i + j == haystack.length()) return -1;
              if (needle.charAt(j) != haystack.charAt(i + j)) break;
            }
          }
    }


    //1 使用java的内建函数indexOf
    public static int strStr1(String haystack, String needle) {
        if (haystack == null || needle == null) return -1;
        else return haystack.indexOf(needle);
   }
}


```