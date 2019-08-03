package com.ls.mini.spring.ioc.beans.io;

import com.ls.mini.spring.ioc.beans.io.Resource;
import com.ls.mini.spring.ioc.beans.io.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 15:29
 */
public class ResourceLoaderTest {
    @Test
    public void test() throws IOException {
        ResourceLoader resourceLoader = new ResourceLoader();
        Resource resource = resourceLoader.getResource("tinyioc.xml");
        InputStream inputStream = resource.getInputStream();
        Assert.assertNotNull(inputStream);
    }
}
