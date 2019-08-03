package com.ls.design_pattern.TemplateMethed;

/**
 * @Author: CipherCui
 * @Description:
 * @Date: Created in 16:42 2018/11/8
 *
 * 输出：
将水煮沸
用沸水冲泡咖啡
将饮料倒入杯中
加入糖和牛奶

将水煮沸
用80度的热水浸泡茶叶5分钟
将饮料倒入杯中
 *
 */
public class Main {

    public static void main(String[] args) {
        RefreshBeverage coffee = new Coffee();
        coffee.prepareBeverageTemplate();
        System.out.println();
        RefreshBeverage tea = new Tea();
        tea.prepareBeverageTemplate();
    }

}
