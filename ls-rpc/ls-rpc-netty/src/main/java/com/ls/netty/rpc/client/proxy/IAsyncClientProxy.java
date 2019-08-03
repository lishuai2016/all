package com.ls.netty.rpc.client.proxy;

import com.ls.netty.rpc.client.RPCFuture;

/**
 * Created by luxiaoxun on 2016/3/16.
 */
public interface IAsyncClientProxy {
    public RPCFuture call(String funcName, Object... args);
}