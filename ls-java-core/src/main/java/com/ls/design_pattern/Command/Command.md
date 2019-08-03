# Command 命令模式

将命令封装成对象中，具有以下作用：

- 使用命令来参数化其它对象
- 将命令放入队列中进行排队
- 将命令的操作记录到日志中
- 支持可撤销的操作

### Class Diagram

- Command：命令
- Receiver：命令接收者，也就是命令真正的执行者
- Invoker：通过它来调用命令
- Client：可以设置命令与命令的接收者


命令模式
定义：将一个请求封装成一个对象，从而让你使用不同的请求把客户端参数化，对请求排队或者记录请求日志，
可以提供命令的撤销和恢复功能。

### 实例JDK

- [java.lang.Runnable](http://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)
- [Netflix Hystrix](https://github.com/Netflix/Hystrix/wiki)
- [javax.swing.Action](http://docs.oracle.com/javase/8/docs/api/javax/swing/Action.html)


命令模式的结构

Command类：是一个抽象类，类中对需要执行的命令进行声明，一般来说要对外公布一个execute方法用来执行命令。
ConcreteCommand类：Command类的实现类，对抽象类中声明的方法进行实现。
Client类：最终的客户端调用类。
以上三个类的作用应该是比较好理解的，下面我们重点说一下Invoker类和Recevier类。
Invoker类：调用者，负责调用命令。
Receiver类：接收者，负责接收命令并且执行命令。
所谓对命令的封装，说白了，无非就是把一系列的操作写到一个方法中，然后供客户端调用就行了，反映到类图上，
只需要一个ConcreteCommand类和Client类就可以完成对命令的封装，即使再进一步，为了增加灵活性，
可以再增加一个Command类进行适当地抽象，这个调用者和接收者到底是什么作用呢？
其实大家可以换一个角度去想：假如仅仅是简单地把一些操作封装起来作为一条命令供别人调用，怎么能称为一种模式呢？
命令模式作为一种行为类模式，首先要做到低耦合，耦合度低了才能提高灵活性，而加入调用者和接收者两个角色的目的也正是为此。


 通过代码我们可以看到，当我们调用时，执行的时序首先是调用者类，
 然后是命令类，最后是接收者类。也就是说一条命令的执行被分成了三步，它的耦合度要比把所有的操作都封装到一个类中要低的多，
 而这也正是命令模式的精髓所在：把命令的调用者与执行者分开，使双方不必关心对方是如何操作的。
 
 
 
 [参考](https://blog.csdn.net/zhengzhb/article/details/7550895 )