package org.apache.dubbo.demo.provider;

/**
 * @program: all
 * @author: lishuai
 * @create: 2019-08-03 14:38
 */
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.demo.DemoService;


import java.io.IOException;

public class ProviderApplication {

    public static void main(String[] args) throws IOException {
        ServiceConfig<DemoServiceImpl> serviceConfig = new ServiceConfig<DemoServiceImpl>();
        serviceConfig.setApplication(new ApplicationConfig("first-dubbo-provider"));
        serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        serviceConfig.setInterface(DemoService.class);
        serviceConfig.setRef(new DemoServiceImpl());
        serviceConfig.export();
        System.in.read();
    }
}
