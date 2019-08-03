package com.ls.rpc.v2;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-30 09:34
 */
public class RpcProvider {

    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        HelloService1 service1 = new HelloService1Impl();
        RpcFramework.export(service, 1234);
        RpcFramework.export(service1, 1234);
    }

}
