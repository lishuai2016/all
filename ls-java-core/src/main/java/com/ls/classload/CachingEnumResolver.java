package com.ls.classload;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-03 11:25
 *
 输出：
CODE_MAP_CACHE为空,问题在这里开始暴露.
{0=北京市}

 原因：实例构造器先执行，然后静态代码块进行了覆盖
1.加载类变量只加载一次，读第一句的时候就不会再进行第二次
2.第一句的时候在初始化的时候开始：对象初始化，接着类属性的初始化，接着实现方法
3.所以造成构造函数的方法的执行结果被静态代码块的结果覆盖（重新new HashMap）

 解决：
<client>()方法由编译器自动收集类中所有类变量的赋值动作和静态语句块static{}中的语句块合并产生，
编译器收集的顺序由语句在源文件中出现的顺序决定，所以demo中只要改变将singleton的初始化放在static后面即可。
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CachingEnumResolver {
    //单态实例　一切问题皆由此行引起
    private static final CachingEnumResolver SINGLE_ENUM_RESOLVER = new CachingEnumResolver();
    /*MSGCODE->Category内存索引*/
    private static Map CODE_MAP_CACHE;
    static {
        CODE_MAP_CACHE = new HashMap();
        //为了说明问题,我在这里初始化一条数据
        CODE_MAP_CACHE.put("0","北京市");
    }

    //private, for single instance
    private CachingEnumResolver() {
        //初始化加载数据  引起问题，该方法也要负点责任
        initEnums();
    }

    /**
     * 初始化所有的枚举类型
     */
    public static void initEnums() {
        // ~~~~~~~~~问题从这里开始暴露 ~~~~~~~~~~~//
        if (null == CODE_MAP_CACHE) {
            System.out.println("CODE_MAP_CACHE为空,问题在这里开始暴露.");
            CODE_MAP_CACHE = new HashMap();
        }
        CODE_MAP_CACHE.put("1", "北京市");
        CODE_MAP_CACHE.put("2", "云南省");

        //..... other code...
    }

    public Map getCache() {
        return Collections.unmodifiableMap(CODE_MAP_CACHE);
    }

    /**
     * 获取单态实例
     *
     * @return
     */
    public static CachingEnumResolver getInstance() {
        return SINGLE_ENUM_RESOLVER;
    }

    public static void main(String[] args) {
        System.out.println(CachingEnumResolver.getInstance().getCache());
    }
}
