/**
 * 
 */
package com.ls.li.Leetcode.math;

/**
 * @author lishuai
 * @data 2017-1-10 上午10:21:53
 */

public class Powxn {

	/**
	 * @author lishuai
	 * @data 2017-1-10 上午10:21:53
Implement pow(x, n).
0.00001
2147483647


2.00000
-2147483648


0

	 */

	public static void main(String[] args) {
		System.out.println(pow4(2,-2147483648));

	}
	//4
	public static double pow4(double x, int n) { 
		if (x == 0 && n < 0) {
	    	return -1;
	    } 
		double res = unsignedPower(x,Math.abs(n));
	    if (n < 0) res = 1.0 / res;
	    return res;
	}
	
	
	//定义全局标志位,参数输入错误
    public static boolean gInvalidInput = false;
    public static double power(double base,int e) {
        if (equals(base,0.0) && e < 0) {
            gInvalidInput = true;
            return 0.0;
        }
        double res = unsignedPower(base,Math.abs(e));
        if (e < 0) res = 1.0 / res;
        return res;
    }
    //优化  原理：要是求一个数的32次方，可以求16次方然后相乘，类似分下去，可以减少相乘的次数，递归实现
    public static double unsignedPower(double base,int e) {
        if (e == 0) return 1.0;
        if (e == 1) return base;
        //右移一位代替除2提高效率
        double result = unsignedPower(base,e >> 1);
        result *= result;
        //按位与操作判断是否为奇数
        if ((e & 1) == 1) result = result * base;
        return result;
    }

//  public static double unsignedPower(double base,int e) {
//      double res = 1.0;
//      for (int i = 1;i <= e;i++) res *= base;
//      return res;
//  }
    //判断两个浮点类型的数是否相等
    public static boolean equals(double b1,double b2) {
        if (Math.abs(b1 - b2) < 0.0000001) return true;
        else return false;
    }

	
	
	//3九章
	public static double pow(double x, int n) {
        if (n == 0) {
            return 1;
        }

        if (n == 1) {
            return x;
        }

        boolean isNegative = false;
        if (n < 0) {
            isNegative = true;
            n *= -1;
        }

        int k = n / 2;
        int l = n - k * 2;
        double t1 = pow(x, k);
        double t2 = pow(x, l);
        if (isNegative) {
            return 1/(t1*t1*t2);
        } else {
            return t1*t1*t2;
        }
    }
	//2	
	public double pow2(double x, int n) {
		if (x == 0 && n < 0) {
	    	return -1;
	    } 
        if(n == 0) {
        	return 1;
        }          
        if(n < 0) {
            n = -n;
            x = 1 / x;
        }
        return (n % 2 == 0) ? pow2(x * x, n / 2) : x * pow2(x * x, n / 2);
    }
	//1 Time Limit Exceeded 
	public static double myPow(double x, int n) {
	    if (x == 0 && n < 0) {
	    	return -1;
	    } 
	    if (n == 0) {
	    	return 1;
	    }
		double result = x;
		int steps = 1;
		while (steps < Math.abs(n)) {
			steps *= 2;
			if (steps > Math.abs(n)) {
				break;
			}
			result *= result;			
		}
		if (steps != Math.abs(n)) {
			steps = steps / 2;
			while (steps < Math.abs(n)) {
				result *= x;
				steps++;
			}
		}		
		return n < 0 ? 1 / result : result;
	}
}
