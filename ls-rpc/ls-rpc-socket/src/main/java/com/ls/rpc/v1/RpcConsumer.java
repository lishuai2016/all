package com.ls.rpc.v1;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-30 09:34
 */
public class RpcConsumer {

    public static void main(String[] args) throws Exception {
        HelloService service = RpcFramework.refer(HelloService.class, "127.0.0.1", 1234);
        for (int i = 0; i < Integer.MAX_VALUE; i ++) {
            String hello = service.hello("World" + i + "RpcConsumer0");
            System.out.println(hello);
            Thread.sleep(1000);
        }
    }

}
