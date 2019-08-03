---
title: Object源码
categories: 
- java
tags:
---


```java
//所有对象的父类

public class Object {

    private static native void registerNatives();   //这个是干嘛的？？？
    static {
        registerNatives();
    }


    public final native Class<?> getClass();   //获取类对象


    //两个对象的equals相同，那么他们的hashCode一定相同；两个对象的equals不相同那么他们的hash也一样不相同（用户自己定义，不是强制的）；hash相同则，equals不一定相同；
 	public native int hashCode();


 	public boolean equals(Object obj) {
        return (this == obj);
    }


    protected native Object clone() throws CloneNotSupportedException;




     public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }



  public final native void notify();


   public final native void notifyAll();


    public final native void wait(long timeout) throws InterruptedException;

    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }


     public final void wait() throws InterruptedException {
        wait(0);
    }



    protected void finalize() throws Throwable { }




}
```