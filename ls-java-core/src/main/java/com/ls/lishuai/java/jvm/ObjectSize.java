package com.ls.lishuai.java.jvm;//package ls.java.jvm;
//
///**
// * @Author: lishuai
// * @CreateDate: 2018/7/24 10:47
// */
//import java.util.HashMap;
///**
// * 64位开启指针压缩的话，markword变成8字节，压缩了class指针为4字节，故对象头12字节
// * 64位没有开启指针压缩的话，markword8字节，class指针8字节，对象头16字节
// * 32位markword为4字节，class指针为4字节，对象头8字节
// *
// * 另外，静态属性所占用的空间通常不算在对象本身，因为它的引用是在方法区。
// *
// * @author xixicat
// * @created 2014-10-03
// */
//public class ObjectSize {
//    public static void main(String[] args){
//        System.out.println(SizeOfTool.getObjectSize(new A(),SizeEnum.B));
//        System.out.println(SizeOfTool.getObjectSize(new B(),SizeEnum.B));
//        System.out.println(SizeOfTool.getObjectSize(new C(),SizeEnum.B));
//        System.out.println(SizeOfTool.getObjectSize(new D(),SizeEnum.B));
//        System.out.println(SizeOfTool.getObjectSize(new E(),SizeEnum.B));
//        System.out.println(SizeOfTool.getObjectSize(new Q(),SizeEnum.B));
//        /**
//         * 64位压缩指针下，对象头12字节，数组长度描述4字节，数据4*100 =16+400 = 416
//         */
//        System.out.println(SizeOfTool.getObjectSize(new int[100],SizeEnum.B));
//        /**
//         * 属性4位对齐
//         * 64位压缩指针下，对象头12字节，数组长度描述4字节，数据1*100，对齐后104 = 16+104 = 120
//         */
//        System.out.println(SizeOfTool.getObjectSize(new byte[100],SizeEnum.B));
//        /**
//         * 二维数组
//         * 64位指针压缩下
//         * 第1维数组，对象头12字节，数组长度描述4字节，2个数组引用共8字节，共24字节
//         * 第2维数组，对象头12字节，数组长度描述4字节，100个数组引用共400字节，对齐后共416字节
//         *         第1维的2个引用所指对象大小 = 2*416 = 832 字节
//         *         共24+832 = 856字节
//         */
//        System.out.println(SizeOfTool.getObjectSize(new int[2][100],SizeEnum.B));
//        /**
//         * 二维数组
//         * 64位指针压缩下
//         * 第1维数组，对象头12字节，数组长度描述4字节，100个数组引用共400字节，共416字节
//         * 第2维数组，对象头12字节，数组长度描述4字节，2个数组引用共8字节，共24字节
//         *         第1维的100个引用所指对象大小 = 100*24 = 2400 字节
//         *         共416+2400 = 2816字节
//         */
//        System.out.println(SizeOfTool.getObjectSize(new int[100][2],SizeEnum.B));
//        System.out.println(SizeOfTool.getObjectSize(new Object(),SizeEnum.B));
//        /**
//         * 不算static属性
//         * private final char value[];
//         * private int hash; // Default to 0
//         * private transient int hash32 = 0;
//         *
//         * 32位下，String对象头8字节，2个int类型8字节，char数组引用占4字节，共占24字节
//         *        另外，还要算上value[]数组的占用，数组对象头部8字节，数组长度4字节，对齐后共占16字节
//         *    =》String对象对象大小24+16 = 40字节
//         * 64位开启指针压缩下（压缩指针），String对象头12字节，2个int类型8字节，char数组引用占4字节，共占24字节
//         *        另外，还要算上value[]数组的占用，数组对象头部12字节，数组长度4字节，对齐后共占16字节
//         *    =》String对象大小24+16=40字节
//         */
//        System.out.println(SizeOfTool.getObjectSize(new String(),SizeEnum.B));
//        /**
//         *  transient Entry<K,V>[] table = (Entry<K,V>[]) EMPTY_TABLE;
//         *  transient int size;
//         *  int threshold;
//         *  final float loadFactor;
//         *  transient int modCount;
//         *
//         * 64位开启指针压缩下，对象头部12字节，数组引用4字节，3个int12字节，float4字节，共32字节
//         *                 另外，算上Entry<K,V>[] = 对象头12 +属性16字节+数组长度4字节 = 32字节
//         *
//         *                 final K key;
//         *                 V value;
//         *                 Entry<K,V> next;
//         *                 int hash;
//         *
//         *                 对象头12字节，3个引用共12字节，1个int4字节  =》 一个entry至少占用28字节
//         *
//         *             =》32+32=64字节
//         */
//        System.out.println(SizeOfTool.getObjectSize(new HashMap(),SizeEnum.B));
//    }
//}
////32位下对象头8字节，byte占1字节，对其填充后，总占16字节
////64位开启指针压缩下对象头12字节，byte1字节，对齐后占16字节
//class A{
//    byte b1;
//}
////32位下对象头8字节，8个byte8字节，总16字节
////64位开启指针压缩下对象头12字节，8个byte8字节，对齐后占24字节
//class B{
//    byte b1,b2,b3,b4,b5,b6,b7,b8;
//}
////32位下对象头8字节，9个byte9字节，对其填充后，总24字节
////64位开启指针压缩下对象头12字节，9个byte9字节，对齐后占24字节
//class C{
//    byte b1,b2,b3,b4,b5,b6,b7,b8,b9;
//}
////32位下对象头8字节，int占4字节，引用占4字节，共16字节
////64位开启指针压缩下对象头12字节，int占4字节，引用占4字节，对齐后占24字节
//class D{
//    int i;
//    String str;
//}
////32位下对象头8字节，int4字节，byte占1字节，引用占4字节，对其后，共24字节
////64位开启指针压缩下对象头12字节，int占4字节，引用占4字节，byte占1字节，对齐后占24字节
//class E{
//    int i;
//    byte b;
//    String str;
//}
///**
// * 对齐有两种
// * 1、整个对象8字节对齐
// * 2、属性4字节对齐   ****
// *
// * 对象集成属性的排布
// * markword     4          8
// * class指针     4          4
// * 父类的父类属性  1         1
// * 属性对齐       3         3
// * 父类的属性     1          1
// * 属性对齐       3          3
// * 当前类的属性    1         1
// * 属性对齐填充    3          3
// * 整个对象对齐   8+12 =》 24    12+12=》24
// */
//class O{
//    byte b;
//}
//class P extends O{
//    byte b;
//}
//class Q extends P{
//    byte b;
//}
