package com.ls.lishuai.java.annotation;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/6 20:41
 */


/**
 * 注解使用
 */
public class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor= FruitColor.Color.RED)
    private String appleColor;

    @FruitProvider(id=1,name="陕西红富士集团",address="陕西省西安市延安路89号红富士大厦")
    private String appleProvider;

    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }
    public String getAppleColor() {
        return appleColor;
    }

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }
    public String getAppleName() {
        return appleName;
    }

    public void setAppleProvider(String appleProvider) {
        this.appleProvider = appleProvider;
    }
    public String getAppleProvider() {
        return appleProvider;
    }

    public void displayName(){
        System.out.println("水果的名字是：苹果");
    }

    @FruitAction("lishuai d11111111o")
    public void peel(@FruitIdParameter(11111) int id1,@FruitIdParameter(11111) @FruitNameParameter("2222222222") int id2,int name) {
        System.out.println("6666666666666666");
    }
}
