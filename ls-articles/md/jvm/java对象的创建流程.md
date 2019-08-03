---
title: java对象的创建流程
categories: 
- jvm
tags:
---


[参考](https://www.cnblogs.com/study-everyday/p/6752715.html)
[参考](https://www.jianshu.com/p/ebaa1a03c594)
[参考](https://blog.csdn.net/justloveyou_/article/details/72466416)
[参考](https://juejin.im/post/58c8a504a22b9d006411650a)



前言：
一个Java对象的创建过程往往包括 类初始化 和 类实例化 两个阶段

目录
一、创建java对象的几种方式
1、使用new关键字创建对象
2、反射机制[2-1、使用Class类的newInstance方法(反射机制)，2-2、使用Constructor类的newInstance方法(反射机制)]
3、使用Clone方法创建对象
4、使用(反)序列化机制创建对象[流的writeObject和readObject方法]
二、创建对象的具体过程
三、实例对象的内存分配解析[java源码中的方法信息都是存在方法区中的]
1、内存是否规整 
2、对象的创建十分频繁，如何解决并发带来的不安全问题 


# 一、创建java对象的几种方式
## 1、使用new关键字创建对象
这是我们最常见的也是最简单的创建对象的方式，通过这种方式我们可以调用任意的构造函数（无参的和有参的）去创建对象。
比如：Student student = new Student();

## 2、反射机制
### 2-1、使用Class类的newInstance方法(反射机制)
我们也可以通过Java的反射机制使用Class类的newInstance方法来创建对象，事实上，这个newInstance方法调用无参的构造器创建对象，比如：
Student student2 = (Student)Class.forName("Student类全限定名").newInstance();　或者：Student stu = Student.class.newInstance();

### 2-2、使用Constructor类的newInstance方法(反射机制)
java.lang.relect.Constructor类里也有一个newInstance方法可以创建对象，该方法和Class类中的newInstance方法很像，但是相比之下，
Constructor类的newInstance方法更加强大些，我们可以通过这个newInstance方法调用有参数的和私有的构造函数，比如：

    public class Student {
        private int id;
        public Student(Integer id) {
            this.id = id;
        }
        public static void main(String[] args) throws Exception {
    
            Constructor<Student> constructor = Student.class
                    .getConstructor(Integer.class);
            Student stu3 = constructor.newInstance(123);
        }
    }
使用newInstance方法的这两种方式创建对象使用的就是Java的反射机制，事实上Class的newInstance方法内部调用的也是Constructor的newInstance方法。

## 3、使用Clone方法创建对象
无论何时我们调用一个对象的clone方法，JVM都会帮我们创建一个新的、一样的对象，特别需要说明的是，
用clone方法创建对象的过程中并不会调用任何构造函数。需要注意clone方法以及浅克隆/深克隆机制。
简单而言，要想使用clone方法，我们就必须先实现Cloneable接口并实现其定义的clone方法，这也是原型模式的应用。比如：

    public class Student implements Cloneable{
    
        private int id;
    
        public Student(Integer id) {
            this.id = id;
        }
    
        @Override
        protected Object clone() throws CloneNotSupportedException {
            // TODO Auto-generated method stub
            return super.clone();
        }
    
        public static void main(String[] args) throws Exception {
    
            Constructor<Student> constructor = Student.class
                    .getConstructor(Integer.class);
            Student stu3 = constructor.newInstance(123);
            Student stu4 = (Student) stu3.clone();
        }
    }

## 4、使用(反)序列化机制创建对象
当我们反序列化一个对象时，JVM会给我们创建一个单独的对象，在此过程中，JVM并不会调用任何构造函数。
为了反序列化一个对象，我们需要让我们的类实现Serializable接口，比如：

    public class Student implements Cloneable, Serializable {
    
        private int id;
    
        public Student(Integer id) {
            this.id = id;
        }
    
        @Override
        public String toString() {
            return "Student [id=" + id + "]";
        }
    
        public static void main(String[] args) throws Exception {
    
            Constructor<Student> constructor = Student.class
                    .getConstructor(Integer.class);
            Student stu3 = constructor.newInstance(123);
    
            // 写对象
            ObjectOutputStream output = new ObjectOutputStream(
                    new FileOutputStream("student.bin"));
            output.writeObject(stu3);
            output.close();
    
            // 读对象
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(
                    "student.bin"));
            Student stu5 = (Student) input.readObject();
            System.out.println(stu5);
        }
    }

#  完整实例

public class Student implements Cloneable, Serializable {

    private int id;

    public Student() {

    }

    public Student(Integer id) {
        this.id = id;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    public String toString() {
        return "Student [id=" + id + "]";
    }

    public static void main(String[] args) throws Exception {

        System.out.println("使用new关键字创建对象：");
        Student stu1 = new Student(123);
        System.out.println(stu1);
        System.out.println("\n---------------------------\n");


        System.out.println("使用Class类的newInstance方法创建对象：");
        Student stu2 = Student.class.newInstance();    //对应类必须具有无参构造方法，且只有这一种创建方式
        System.out.println(stu2);
        System.out.println("\n---------------------------\n");

        System.out.println("使用Constructor类的newInstance方法创建对象：");
        Constructor<Student> constructor = Student.class
                .getConstructor(Integer.class);   // 调用有参构造方法
        Student stu3 = constructor.newInstance(123);   
        System.out.println(stu3);
        System.out.println("\n---------------------------\n");

        System.out.println("使用Clone方法创建对象：");
        Student stu4 = (Student) stu3.clone();
        System.out.println(stu4);
        System.out.println("\n---------------------------\n");

        System.out.println("使用(反)序列化机制创建对象：");
        // 写对象
        ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream("student.bin"));
        output.writeObject(stu4);
        output.close();

        // 读取对象
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(
                "student.bin"));
        Student stu5 = (Student) input.readObject();
        System.out.println(stu5);

    }
}

    /* Output: 
        使用new关键字创建对象：
        Student [id=123]

        ---------------------------

        使用Class类的newInstance方法创建对象：
        Student [id=0]

        ---------------------------

        使用Constructor类的newInstance方法创建对象：
        Student [id=123]

        ---------------------------

        使用Clone方法创建对象：
        Student [id=123]

        ---------------------------

        使用(反)序列化机制创建对象：
        Student [id=123]
    */



# 二、创建对象的具体过程
对象实例的结构
对象头
实例变量
填充数据

说明：
[对象头]
这个头包括两个部分，第一部分用于存储自身运行时的数据例如GC标志位、哈希码、锁状态等信息。第二部分存放指向方法区类静态数据的指针。
[实例变量] 
存放类的属性数据信息，包括父类的属性信息。如果是数组的实例部分还包括数组的长度。这部分内存按4字节对齐。
[填充数据] 
这是因为虚拟机要求对象起始地址必须是8字节的整数倍。填充数据不是必须存在的，仅仅是为了字节对齐。
HotSpot VM的自动内存管理要求对象起始地址必须是8字节的整数倍。对象头本身是8的倍数，当对象的实例变量数据不是8的倍数，
便需要填充数据来保证8字节的对齐。另外，堆上对象内存的分配是并发进行的.



Java 对象创建的流程
Object obj = new Object();
1、虚拟机遇到 new 指令
2、检查指令的参数是否能在常量池中定位到一个类的符号引用
3、检查符号引用是否已经被加载、解析和初始化。如果没有则进行[类加载]。
4、虚拟机为新生对象分配内存（对象所需的内存大小在类加载完就可确定）[把对象的所有非静态成员加载到所开辟的空间下]
5、将分配到的内存空间都初始化为零值（不包括对象头）这一步操作保证了对象的实例字段在 Java 代码中可以不赋初始值就直接使用，
程序能访问到这些字段的数据类型所对应的零值[所有的非静态成员加载完成之后，对所有非静态成员变量进行默认初始化]
6、虚拟机对对象进行必要的设置，例如这个对象是哪个类的实例、如何才能知道类的元数据信息、对象的哈希码、对象的 GC 分代年龄信息等等，
这些信息都存放在对象的对象头（Object Header）之中。
7、从虚拟机的视角来看，一个新的对象已经产生了，从 Java 程序角度来看，对象创建才刚刚开始
8、执行 new 指令之后会接着执行方法，把对象按照程序员的意愿进行初始化[调用构造函数初始化]
9、在整个构造函数执行完并弹栈后，把空间分配的地址赋给引用对象。
一个真正可用的对象才算完全产生出来。

此刻，会根据obj这个变量是实例变量、局部变量或静态变量的不同将引用放在不同的地方：
如果obj局部变量，obj变量在栈帧的局部变量表，这个对象的引用就放在栈帧。
如果obj是实例变量，obj变量在堆中，对象的引用就放在堆。
如果obj是静态变量，obj变量在方法区，对象的引用就放在方法区。
[这里有问题吧？？？ 实例变量的对象引用是不是也是在栈中？？？]

# 三、实例对象的内存分配解析
分配内存过程中需要考虑的问题： 
## 1、内存是否规整 
(1)假设Java 堆中内存是连续规整的，也就是说Heap中一侧是已经使用过的内存空间，而另一侧是空闲空间，则此时使用指针指向起始空闲内存，
当需要分配新的空间时，只需要将指针向后移动制定空间大小位置即可完成内存的分配，这种分配方式称为“指针碰撞”（Bump the Pointer） 
(2)假设内存并不是连续规整的，空闲和使用的相互交错，则此时JVM就需要记录哪些内存块石可用的，分配时，分配足够大的空间给对象实例，
同时更新记录列表。这种方式称为“空闲列表”（Free List） 
采用Bump the Pointer的方式还是Free List的方式由JVM Heap是否规整决定，而Heap的规整与否又由所采用的垃圾收集器是否带有亚索整理的功能决定。使用Serial、ParNew等带有Compact过程的收集器时则采用指针碰撞，而使用CMS这种给予Mark Sweep算法收集器时，采用Free List方式。 

## 2、对象的创建十分频繁，如何解决并发带来的不安全问题 
方法一、同步，即对分配内存空间的动作进行同步处理——-采用CAS加上失败重试的方式保证更新操作的原子性。 
方法二、TLAB，把内存分配的动作按照线程划分在不同的空间上进行，即每个线程预先分配一小块内存，
这种方式称为本地线程分配缓冲（Thread Local Allocation Buffer），这种方式只需要在分配新的TLAB时进行同步锁定 
是否采用TLAB方式可以通过使用-XX:+/-UserTLAB决定

对于多线程的情况，如何确保一个线程分配了对象内存但尚未修改内存管理指针时，其他线程又分配该块内存而覆盖的情况？
有一种方法，就是让每一个线程在堆中先预分配一小块内存（TLAB本地线程分配缓冲），每个线程只在自己的内存中分配内存。
但对象本身按其访问属性是可以线程共享访问的。


