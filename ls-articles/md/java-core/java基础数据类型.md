# java基础数据类型

[参考](https://www.cnblogs.com/singlesoar/p/5688915.html)
在JAVA中一共有八种基本数据类型，他们分别是 byte、short、int、long、float、double、char、boolean 

## 整型 
其中byte、short、int、long都是表示整数的，只不过他们的取值范围不一样 
byte的取值范围为-128~127，占用1个字节（-2的7次方到2的7次方-1） 
short的取值范围为-32768~32767，占用2个字节（-2的15次方到2的15次方-1） 
int的取值范围为（-2147483648~2147483647），占用4个字节（-2的31次方到2的31次方-1） 
long的取值范围为（-9223372036854774808~9223372036854774807），占用8个字节（-2的63次方到2的63次方-1）

可以看到byte和short的取值范围比较小，而long的取值范围太大，占用的空间多，基本上int可以满足我们的日常的计算了，
而且int也是使用的最多的整型类型了。 
在通常情况下，如果JAVA中出现了一个整数数字比如35，那么这个数字就是int型的，如果我们希望它是byte型的，
可以在数据后加上大写的 B：35B，表示它是byte型的，同样的35S表示short型，35L表示long型的，
表示int我们可以什么都不用加，但是如果要表示long型的，就一定要在数据后面加“L”。 

## 浮点型 
float和double是表示浮点型的数据类型，他们之间的区别在于他们的精确度不同 
float 3.402823e+38 ~ 1.401298e-45（e+38表示是乘以10的38次方，同样，e-45表示乘以10的负45次方）  占用4个字节 
double 1.797693e+308~ 4.9000000e-324 占用8个字节 
double型比float型存储范围更大，精度更高，所以通常的浮点型的数据在不声明的情况下都是double型的，
如果要表示一个数据是float型的，可以在数据后面加上“F”。 
浮点型的数据是不能完全精确的，所以有的时候在计算的时候可能会在小数点最后几位出现浮动，这是正常的。 

## boolean型（布尔型） 
这个类型只有两个值，true和false（真和非真） 
boolean t = true； 
boolean f = false； 

## char型（文本型） 
用于存放字符的数据类型，占用2个字节，采用unicode编码，它的前128字节编码与ASCII兼容 
字符的存储范围在\u0000~\uFFFF，在定义字符型的数据时候要注意加' '，比如 '1'表示字符'1'而不是数值1， 
char c = ' 1 '; 
我们试着输出c看看，System.out.println(c);结果就是1，而如果我们这样输出呢System.out.println(c+0); 
结果却变成了49。 
如果我们这样定义c看看 
char c = ' \u0031 ';输出的结果仍然是1，这是因为字符'1'对应着unicode编码就是\u0031 
char c1 = 'h',c2 = 'e',c3='l',c4='l',c5 = 'o'; 
System.out.print(c1);System.out.print(c2);System.out.print(c3);System.out.print(c4);Sytem.out.print(c5);


## 常见的数据范围问题

### 1、两个int数字相加结果大于int的最大值的时候，赋值给一个long型的数，结果是什么？
```java
class Untitled {
	public static void main(String[] args) {
		long temp = 2147483647 + 1;
		System.out.println( temp);
	}
}
```

结果：-2147483648
按道理加入转化为long性的话结果应该是2147483648，说明它没有进行类型的转化

**备注：int最大值加1变成最小int负数**

```java
class Untitled {
	public static void main(String[] args) {
		long temp = (long)(2147483647 + 1);
		System.out.println( temp);
	}
}
```
结果：-2147483648   
分析：进行类型转化了依旧不对，说明是进行强制计算后才进行的类型转换

```java
class Untitled {
	public static void main(String[] args) {
		long temp = (long)2147483647 + 1;
		System.out.println( temp);
	}
}
```
结果：2147483648
分析：等号右边按照最大范围进行数字类型转换