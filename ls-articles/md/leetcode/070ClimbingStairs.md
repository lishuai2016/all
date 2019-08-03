# 70. Climbing Stairs


```java
优解：时间复杂度N，空间复杂度O（2）      模二或者斐波那契数列（两者的性质一样）
/**
 *
 */
package array;

/**
 * @author lishuai
 * @data 2016-12-1 下午5:35:29
 */

public class ClimbingStairs {

    /**
     * @author lishuai
     * @data 2016-12-1 下午5:35:29
You are climbing a stair case. It takes n steps to reach to the top.

Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
     */

    public static void main(String[] args) {
        // TODO Auto-generated method stub 1134903170
        System.out.println(climbStairs5(44));
    }

    //1、斐波那契数列
    public static int climbStairs1(int n) {
        int a = 1, b = 2;
        int c = 0;       
        if (n == 1) return a;
        if (n == 2) return b;       
        for (int i = 2; i < n; ++i)
        {
            c = a + b;
            a = b;
            b = c;
        }       
        return c;
    }
    //2、动态规划  Time Limit Exceeded
    public static int climbStairs2(int n) {   
        return dp(n);
    }
    public static int dp(int n) {
        if (n == 1) return 1;
         if (n == 2) return 2;
         return dp(n-1)+dp(n-2);
    }
    //3、动态规划  迭代实现
    public static int climbStairs4(int n) {   
        if(n<=2) return n;
        int[] dp=new int[n+1];
        dp[0]=1;
        dp[1]=1;
        for(int i=2;i<=n;i++){
            dp[i]=dp[i-1]+dp[i-2];
        }
        return dp[n];

    }


    /**
 variable a tells you the number of ways to reach the current step,
 and b tells you the number of ways to reach the next step.
  So for the situation one step further up, the old b becomes the new a, and the new b is the old a+b,
  since that new step can be reached by climbing 1 step from what b represented or 2 steps from what a represented.
     */
    //3
    public static int climbStairs3(int n) {
        int a = 1, b = 1;
        while (n-- > 0)
            a = (b += a) - a;
        return a;
    }
  //2、动态规划  迭代实现    模二
    public static int climbStairs5(int n) {   
        if(n<=2) return n;
        int[] dp=new int[2];
        dp[0]=1;
        dp[1]=1;
        for(int i=2;i<=n;i++){
            dp[i%2]=dp[(i-1)%2]+dp[(i-2)%2];
        }
        return dp[0]>dp[1]?dp[0]:dp[1];

    }
}





```