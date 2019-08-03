package com.ls.rpc.v1;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-30 09:34
 */
public class RpcProvider {

    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        RpcFramework.export(service, 1234);
    }

}
