[Java中native关键字](https://blog.csdn.net/funneies/article/details/8949660)
[有return的情况下try catch finally的执行顺序（最有说服力的总结）](https://blog.csdn.net/kavensu/article/details/8067850)








# finally
  结论：
  1、不管有木有出现异常，finally块中代码都会执行；
  2、当try和catch中有return时，finally仍然会执行；
  3、finally是在return后面的表达式运算后执行的（此时并没有返回运算后的值，而是先把要返回的值保存起来，管finally中的代码怎么样，返回的值都不会改变，任然是之前保存的值），所以函数返回值是在finally执行前确定的；
  4、finally中最好不要包含return，否则程序会提前退出，返回值不是try或catch中保存的返回值。
  举例：
  情况1：try{} catch(){}finally{} return;
              显然程序按顺序执行。
  情况2:try{ return; }catch(){} finally{} return;
            程序执行try块中return之前（包括return语句中的表达式运算）代码；
           再执行finally块，最后执行try中return;
           finally块之后的语句return，因为程序在try中已经return所以不再执行。
  情况3:try{ } catch(){return;} finally{} return;
           程序先执行try，如果遇到异常执行catch块，
           有异常：则执行catch中return之前（包括return语句中的表达式运算）代码，再执行finally语句中全部代码，
                       最后执行catch块中return. finally之后也就是4处的代码不再执行。
           无异常：执行完try再finally再return.
  情况4:try{ return; }catch(){} finally{return;}
            程序执行try块中return之前（包括return语句中的表达式运算）代码；
            再执行finally块，因为finally块中有return所以提前退出。
  情况5:try{} catch(){return;}finally{return;}
            程序执行catch块中return之前（包括return语句中的表达式运算）代码；
            再执行finally块，因为finally块中有return所以提前退出。
  情况6:try{ return;}catch(){return;} finally{return;}
            程序执行try块中return之前（包括return语句中的表达式运算）代码；
            有异常：执行catch块中return之前（包括return语句中的表达式运算）代码；
                         则再执行finally块，因为finally块中有return所以提前退出。
            无异常：则再执行finally块，因为finally块中有return所以提前退出。
  
  最终结论：任何执行try 或者catch中的return语句之前，都会先执行finally语句，如果finally存在的话。
                    如果finally中有return语句，那么程序就return了，所以finally中的return是一定会被return的，
                    编译器把finally中的return实现为一个warning。
   
  
  下面是个测试程序
  public class FinallyTest  
  {
          public static void main(String[] args) {
                   
                  System.out.println(new FinallyTest().test());;
          }
  
          static int test()
          {
                  int x = 1;
                  try
                  {
                          x++;
                          return x;
                  }
                  finally
                  {
                          ++x;
                  }
          }
  }
  结果是2。
  分析：
     在try语句中，在执行return语句时，要返回的结果已经准备好了，就在此时，程序转到finally执行了。
  在转去之前，try中先把要返回的结果存放到不同于x的局部变量中去，执行完finally之后，在从中取出返回结果，
  因此，即使finally中对变量x进行了改变，但是不会影响返回结果。
  它应该使用栈保存返回值。


# transient

java语言的关键字，变量修饰符，如果用transient声明一个实例变量，当对象存储时，它的值不需要维持。
换句话来说就是，用transient关键字标记的成员变量不参与序列化过程。


# volatile

volatile也是变量修饰符，只能用来修饰变量。volatile修饰的成员变量在每次被线程访问时，都强迫从共享内存中重读该成员变量的值。
而且，当成员变量发生变化时，强迫线程将变化值回写到共享内存。这样在任何时刻，两个不同的线程总是看到某个成员变量的同一个值。

在此解释一下Java的内存机制：

Java使用一个主内存来保存变量当前值，而每个线程则有其独立的工作内存。线程访问变量的时候会将变量的值拷贝到自己的工作内存中，这样，当线程对自己工作内存中的变量进行操作之后，就造成了工作内存中的变量拷贝的值与主内存中的变量值不同。

Java语言规范中指出：为了获得最佳速度，允许线程保存共享成员变量的私有拷贝，而且只当线程进入或者离开同步代码块时才与共享成员变量的原始值对比。

这样当多个线程同时与某个对象交互时，就必须要注意到要让线程及时的得到共享成员变量的变化。

而volatile关键字就是提示VM：对于这个成员变量不能保存它的私有拷贝，而应直接与共享成员变量交互。

使用建议：在两个或者更多的线程访问的成员变量上使用volatile。当要访问的变量已在synchronized代码块中，或者为常量时，不必使用。

由于使用volatile屏蔽掉了VM中必要的代码优化，所以在效率上比较低，因此一定在必要时才使用此关键字。


# static
[java中静态代码块的用法 static用法详解](http://www.cnblogs.com/panjun-Donet/archive/2010/08/10/1796209.html)



# native
[Java中native关键字](https://blog.csdn.net/funneies/article/details/8949660)









