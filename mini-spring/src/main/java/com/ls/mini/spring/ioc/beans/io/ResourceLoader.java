package com.ls.mini.spring.ioc.beans.io;

import java.net.URL;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 15:02
 *
 * 更加配置文件的位置，构建一个Resource对象
 */
public class ResourceLoader {

    public Resource getResource(String location){
        URL resource = this.getClass().getClassLoader().getResource(location);
        return new UrlResource(resource);
    }
}
