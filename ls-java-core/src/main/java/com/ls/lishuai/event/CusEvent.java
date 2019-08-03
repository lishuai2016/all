package com.ls.lishuai.event;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/9 13:05
 */

import java.util.EventObject;

/**
 * 事件类,用于封装事件源及一些与事件相关的参数.
 * @author Eric
 */
public class CusEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    private Object source;//事件源

    public CusEvent(Object source){
        super(source);
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
