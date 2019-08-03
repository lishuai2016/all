[JAVA中的反射机制](https://blog.csdn.net/liujiahan629629/article/details/18013523)



通过传入javabean来执行实例对象的方法，借助反射机制可以很好的解耦系统。
比如常见的RPC框架的服务端就是根据服务消费者传递过来接口、参数的类型和参数，通过反射机制来执行具体接口实现类的方法

```java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassDemo {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        ClassDemo cls = new ClassDemo();
        Class c = cls.getClass();

        try {
            //1、 parameter type is null   无参数
            Method m = c.getDeclaredMethod("show", null);
            System.out.println("method = " + m.toString());
            m.invoke(cls);

            // 2、method Integer   有一个参数
            Method lMethod = c.getDeclaredMethod("showInteger", Integer.class);
            System.out.println("method = " + lMethod.toString());
            lMethod.invoke(cls,12);


            // 3、method Integer  有两个参数
            Method lMethod1 = c.getDeclaredMethod("showMulParam", Integer.class,String.class);
            System.out.println("method = " + lMethod1.toString());
            lMethod1.invoke(cls,Integer.valueOf(12),"lishuai");

            // 4、有两个参数 构造参数类型列表获取method，然后构建参数列表执行方法
            Class<?>[] types = new Class[2];
            types[0] = Integer.class;
            types[1] = String.class;


            Object[] parameters = new Object[2];
            parameters[0] = Integer.valueOf(66666);
            parameters[1] = "77777";
            Method mmmmm = c.getMethod("showMulParam", types);
            mmmmm.invoke(cls,parameters);



        }catch(NoSuchMethodException e){
            System.out.println(e.toString());
        }
    }


    private Integer show() {
        System.out.println("111111111111111");
        return 1;
    }

    public void showInteger(Integer i) {
        System.out.println(String.format("showInteger:%s",i));
    }

    public void showMulParam(Integer i,String name) {

        System.out.println(String.format("showMulParam:%s,%s",name,i));
    }

}


```