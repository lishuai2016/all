package com.ls.lishuai.java.spi;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/22 9:35
 */
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 这就是因为ServiceLoader.load(Search.class)在加载某接口时，
 * 会去META-INF/services下找接口的全限定名文件，再根据里面的内容加载相应的实现类。
 *
 * 这就是spi的思想，接口的实现由provider实现，
 * provider只用在提交的jar包里的META-INF/services下根据平台定义的接口新建文件，
 * 并添加进相应的实现类内容就好。
 *
 * 那为什么配置文件为什么要放在META-INF/services下面？
 可以打开ServiceLoader的代码，里面定义了文件的PREFIX如下：

 private static final String PREFIX = "META-INF/services/"
 */

public class TestCase {
    public static void main(String[] args) {
        ServiceLoader<Search> s = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = s.iterator();
        while (iterator.hasNext()) {
            Search search =  iterator.next();
            search.searchDoc("hello world");
        }
    }
}
