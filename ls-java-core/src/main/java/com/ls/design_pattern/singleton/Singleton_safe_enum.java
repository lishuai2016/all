package com.ls.design_pattern.singleton;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 14:34
 */
public enum Singleton_safe_enum {
    INSTANCE;

    private String objName;


    public String getObjName() {
        return objName;
    }


    public void setObjName(String objName) {
        this.objName = objName;
    }


    public static void main(String[] args) {

        // 单例测试
        Singleton_safe_enum firstSingleton = Singleton_safe_enum.INSTANCE;
        firstSingleton.setObjName("firstName");
        System.out.println(firstSingleton.getObjName());
        Singleton_safe_enum secondSingleton = Singleton_safe_enum.INSTANCE;
        secondSingleton.setObjName("secondName");
        System.out.println(firstSingleton.getObjName());
        System.out.println(secondSingleton.getObjName());

        // 反射获取实例测试
        try {
            Singleton_safe_enum[] enumConstants = Singleton_safe_enum.class.getEnumConstants();
            for (Singleton_safe_enum enumConstant : enumConstants) {
                System.out.println(enumConstant.getObjName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
