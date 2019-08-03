# 8. 字符串转换整数 (atoi)
[string-to-integer-atoi](https://leetcode-cn.com/problems/string-to-integer-atoi/)

## 题目描述
请你来实现一个 atoi 函数，使其能将字符串转换成整数。

首先，该函数会根据需要丢弃无用的开头空格字符，直到寻找到第一个非空格的字符为止。

当我们寻找到的第一个非空字符为正或者负号时，则将该符号与之后面尽可能多的连续数字组合起来，作为该整数的正负号；
假如第一个非空字符是数字，则直接将其与之后连续的数字字符组合起来，形成整数。

该字符串除了有效的整数部分之后也可能会存在多余的字符，这些字符可以被忽略，它们对于函数不应该造成影响。

注意：假如该字符串中的第一个非空格字符不是一个有效整数字符、字符串为空或字符串仅包含空白字符时，则你的函数不需要进行转换。

在任何情况下，若函数不能进行有效的转换时，请返回 0。

说明：

假设我们的环境只能存储 32 位大小的有符号整数，那么其数值范围为 [−231,  231 − 1]。如果数值超过这个范围，
qing返回  INT_MAX (231 − 1) 或 INT_MIN (−231) 。

示例 1:
输入: "42"
输出: 42

示例 2:
输入: "   -42"
输出: -42
解释: 第一个非空白字符为 '-', 它是一个负号。我们尽可能将负号与后面所有连续出现的数字组合起来，最后得到 -42 。

示例 3:
输入: "4193 with words"
输出: 4193
解释: 转换截止于数字 '3' ，因为它的下一个字符不为数字。

示例 4:
输入: "words and 987"
输出: 0
解释: 第一个非空字符是 'w', 但它不是数字或正、负号。因此无法执行有效的转换。

示例 5:
输入: "-91283472332"
输出: -2147483648
解释: 数字 "-91283472332" 超过 32 位有符号整数范围。 因此返回 INT_MIN (−231) 。

## 思路



## 答案

```java
优解:法2，3
/**
 *
 */
package string;

/**
 * @author lishuai
 * @data 2016-12-14 上午11:27:20
 */

public class StringtoInteger {

    /**
     * @author lishuai
     * @data 2016-12-14 上午11:27:20
Implement atoi to convert a string to an integer.

Hint: Carefully consider all possible input cases.
If you want a challenge, please do not see below and ask yourself what are the possible input cases.

Notes:
It is intended for this problem to be specified vaguely (ie, no given input specs).
You are responsible to gather all the input requirements up front.

Requirements for atoi:
The function first discards as many whitespace characters as necessary
until the first non-whitespace character is found.
Then, starting from this character, takes an optional initial plus
or minus sign followed by as many numerical digits as possible, and interprets them as a numerical value.

The string can contain additional characters after those that form the integral number,
which are ignored and have no effect on the behavior of this function.

If the first sequence of non-whitespace characters in str is not a valid integral number,
or if no such sequence exists because either str is empty or it contains only whitespace characters,
 no conversion is performed.

If no valid conversion could be performed, a zero value is returned.
If the correct value is out of the range of representable values,
INT_MAX (2147483647) or INT_MIN (-2147483648) is returned.
     */

    public static void main(String[] args) {
        System.out.println(myAtoi("   118100977011111111111111111111111111111"));

    }
    //九章   3 和1、2差不多，它采用long型变量保存中间变量
    public static int myAtoi(String str) {
        // Start typing your Java solution below
        // DO NOT write main() function
        if(str == null) {
            return 0;
        }
        str = str.trim();
        if (str.length() == 0) {
            return 0;
        }

        int sign = 1;
        int index = 0;

        if (str.charAt(index) == '+') {
            index++;
        } else if (str.charAt(index) == '-') {
            sign = -1;
            index++;
        }
        long num = 0;
        for (; index < str.length(); index++) {
            if (str.charAt(index) < '0' || str.charAt(index) > '9')
                break;
            num = num * 10 + (str.charAt(index) - '0');
            if (num > Integer.MAX_VALUE ) {
                break;
            }
        }   
        if (num * sign >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (num * sign <= Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int)num * sign;
    }
    //2 原理和1差不多，但是简化代码（从前拼）
    public static int myAtoi2(String str) {
        int index = 0, sign = 1, total = 0;
        //1. Empty string
        if(str.length() == 0) return 0;
        //2. Remove Spaces
        while(str.charAt(index) == ' ' && index < str.length())
            index ++;
        //3. Handle signs
        if(str.charAt(index) == '+' || str.charAt(index) == '-'){
            sign = str.charAt(index) == '+' ? 1 : -1;
            index ++;
        }       
        //4. Convert number and avoid overflow
        while(index < str.length()){
            int digit = str.charAt(index) - '0';
            if(digit < 0 || digit > 9) break;
            //check if total will be overflow after 10 times and add digit
            if(Integer.MAX_VALUE/10 < total || Integer.MAX_VALUE/10 == total && Integer.MAX_VALUE %10 < digit)
                return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            total = 10 * total + digit;
            index ++;
        }
        return total * sign;
    }

    //1 思路：截取前面连续的数字组合，遇到非数字结束；然后判断数的大小是否超出（从截取的字符串后面拼数字）
    public static int myAtoi1(String str) {
        if (str == null || str.length() == 0 || str.trim().length() == 0) return 0;
        int res = 0;
        String trim = str.trim();
        char c = trim.charAt(0);
        //判断第一个字符是否为+-号
        if (c == '+') {           
            int end = 0;
            for (int i = 1;i < trim.length();i++) {
                if(!Character.isDigit(trim.charAt(i))) break;
                else end = i;
            }
            if (end == 0) return 0; 
            if (end > 10) return 2147483647;
            int mul = 1;
            String value = trim.substring(1, end + 1);         
            for (int i = value.length() - 1;i >=0;i--) {               
                int digit = Character.getNumericValue(value.charAt(i));
                if (res > 147483647 && digit >= 2) return 2147483647;
                res += digit * mul;
                mul *=10;
            }                   
        } else if (c == '-') {
            int end = 0;
            for (int i = 1;i < trim.length();i++) {
                if(!Character.isDigit(trim.charAt(i))) break;
                else end = i;
            }
            if (end == 0) return 0;
            if (end > 10) return -2147483648;
            int mul = 1;
            String value = trim.substring(1, end + 1);

            for (int i = value.length() - 1;i >=0;i--) {               
                int digit = Character.getNumericValue(value.charAt(i));
                if (res > 147483648 && digit >= 2) return -2147483648;
                res += digit * mul;
                mul *=10;
            }                   
        } else {
            int end = -1;       
            for (int i = 0;i < trim.length();i++) {
                if(!Character.isDigit(trim.charAt(i))) break;
                else end = i;
            }
            if (end == -1) return 0;
            if (end + 1 > 10) return 2147483647;
            int mul = 1;
            String value = trim.substring(0, end + 1);
            for (int i = value.length() - 1;i >=0;i--) {               
                int digit = Character.getNumericValue(value.charAt(i));
                if (res > 147483647 && digit >= 2) return 2147483647;
                res += digit * mul;
                mul *=10;
            }       
        }
        if (c == '-') return res * -1;
        else return res;
    }

}


```