package com.ls.rpc.v1;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-30 10:01
 */
public class RpcConsumer1 {
    public static void main(String[] args) throws Exception {
        HelloService service = RpcFramework.refer(HelloService.class, "127.0.0.1", 1234);
        for (int i = 0; i < Integer.MAX_VALUE; i ++) {
            String hello = service.hello("World" + i + "RpcConsumer1");
            System.out.println(hello);
            Thread.sleep(1000);
        }
    }
}
