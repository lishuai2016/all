package com.ls.design_pattern.Builder;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 14:47

可以看出，构建器后面隐藏了对象构造的复杂性，内部静态类接受链接方法的调用
 */
public class BuilderTest {

    public static void main (String[] args) {
        Programmer programmer = new Programmer.ProgrammerBuilder().setFirstName("F").setLastName("L")
                .setCity("City").setZipCode("0000A").setAddress("Street 39")
                .setLanguages(new String[] {"bash", "Perl"}).setProjects(new String[] {"Linux kernel"}).build();

        System.out.println(programmer);
    }
}
