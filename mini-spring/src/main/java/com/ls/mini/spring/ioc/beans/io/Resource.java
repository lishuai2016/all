package com.ls.mini.spring.ioc.beans.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 14:59
 *
 * Resource是spring内部定位资源的接口。
 */
public interface Resource {
    InputStream getInputStream() throws IOException;
}
