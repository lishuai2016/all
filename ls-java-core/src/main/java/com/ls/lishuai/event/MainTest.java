package com.ls.lishuai.event;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/9 13:07
 *
 * https://blog.csdn.net/yiziweiyang/article/details/52413422
 */
public class MainTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EventSourceObject object = new EventSourceObject();
        //注册监听器
        object.addCusListener(new CusEventListener(){
            @Override
            public void fireCusEvent(CusEvent e) {
                super.fireCusEvent(e);
            }
        });
        //触发事件
        object.setName("eric");
    }
}
