package com.ls.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-16 15:37
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    public static ApplicationContext applicationContext=null;//可写成单利模式，这里为了方便

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext=arg0;
        System.out.println("设置ApplicationContext成功！");
    }

}

