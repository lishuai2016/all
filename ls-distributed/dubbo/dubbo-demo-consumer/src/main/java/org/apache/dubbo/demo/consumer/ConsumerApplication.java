package org.apache.dubbo.demo.consumer;

/**
 * @program: all
 * @author: lishuai
 * @create: 2019-08-03 14:44
 */
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.demo.DemoService;


public class ConsumerApplication {
    public static void main(String[] args) {
        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<DemoService>();
        referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
        referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        referenceConfig.setInterface(DemoService.class);
        DemoService greetingService = referenceConfig.get();
        System.out.println(greetingService.sayHello("world"));
    }
}
