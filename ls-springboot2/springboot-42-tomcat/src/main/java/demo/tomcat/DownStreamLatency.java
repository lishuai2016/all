package demo.tomcat;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: all
 * @author: lishuai
 * @create: 2019-08-13 11:17
 */
@RestController
public class DownStreamLatency {

    @RequestMapping("/greeting/latency/{seconds}")
    public String greeting(@PathVariable long seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Greeting greeting = new Greeting("Hello World!");

        return "ok";
    }
}

