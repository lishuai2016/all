---
title: static、final和static final
categories: 
- java
tags:
---




https://www.cnblogs.com/EasonJim/p/7841990.html



# final

final可以修饰：属性，方法，类，局部变量（方法中的变量）

final修饰的属性的初始化可以在编译期，也可以在运行期，初始化后不能被改变。

final修饰的属性跟具体对象有关，在运行期初始化的final属性，不同对象可以有不同的值。

final修饰的属性表明是一个常数（创建后不能被修改）。

final修饰的方法表示该方法在子类中不能被重写，final修饰的类表示该类不能被继承。

对于基本类型数据，final会将值变为一个常数（创建后不能被修改）；但是对于对象句柄（亦可称作引用或者指针），final会将句柄变为一个常数（进行声明时，必须将句柄初始化到一个具体的对象。而且不能再将句柄指向另一个对象。但是，对象的本身是可以修改的。这一限制也适用于数组，数组也属于对象，数组本身也是可以修改的。方法参数中的final句柄，意味着在该方法内部，我们不能改变参数句柄指向的实际东西，也就是说在方法内部不能给形参句柄再另外赋值）。

# static

static可以修饰：属性，方法，代码段，内部类（静态内部类或嵌套内部类）

static修饰的属性的初始化在编译期（类加载的时候），初始化后能改变。

static修饰的属性所有对象都只有一个值。

static修饰的属性强调它们只有一个。

static修饰的属性、方法、代码段跟该类的具体对象无关，不创建对象也能调用static修饰的属性、方法等

static和“this、super”势不两立，static跟具体对象无关，而this、super正好跟具体对象有关。

static不可以修饰局部变量。

static final和final static：

static final和final static没什么区别，一般static写在前面。

# static final

static修饰的属性强调它们只有一个，final修饰的属性表明是一个常数（创建后不能被修改）。static final修饰的属性表示一旦给值，就不可修改，并且可以通过类名访问。

static final也可以修饰方法，表示该方法不能重写，可以在不new对象的情况下调用