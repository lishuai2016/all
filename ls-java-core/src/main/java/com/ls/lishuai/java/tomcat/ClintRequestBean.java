package com.ls.lishuai.java.tomcat;


import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/6 18:07
 */





public class ClintRequestBean {
    //以一个请求举例:http://127.0.0.1/www/qqq/eee
    private String protocol;//协议类型(eg:http)
    private String protocolVersion;//协议版本(eg:1.1)
    private String data;//请求数据(eg:/www/qqq/eee)
    private String method;//请求方法:(eg:GET)

    /**
     * 客户端请求实体构造方法
     * @param protocol 协议类型 (eg:http)
     * @param protocolVersion 协议版本 (eg:1.1)
     * @param data 请求数据 (eg:/www/qqq/eee)【必须以‘/’分隔】
     * @param method 请求方法 (eg:GET)
     */
    public ClintRequestBean(String protocol, String protocolVersion, String data, String method) {
        super();
        this.protocol = protocol;
        this.protocolVersion = protocolVersion;
        this.data = data;
        this.method = method;
    }
    /**
     * 客户端请求实体构造方法
     * @param request 请求链接，一般针对一条完整的http链接
     */
    public ClintRequestBean(String request){//    GET /qqq/www/eee/?a=1&b=2 HTTP/1.1
        super();
        String [] requestString = request.split(" ");
        this.method = requestString[0];
        this.data = requestString[1];
        String [] proAndVer = requestString[2].split("/");
        this.protocol = proAndVer[0];
        this.protocolVersion = proAndVer[1];
    }

    /**
     * 转化为可读String用于分析请求
     * @return
     */
    public String toReadString(){
        return "ClintRequestBean [protocol=" + protocol + ", protocolVersion=" + protocolVersion + ", data=" + data
                + ", method=" + method + "]";
    }

    /**
     * 得到请求的参数
     * @return map[请求路径|参数map]
     */
    public Map<String, Object> getRequestParm(){
        Map<String,Object> map = new HashMap<>();
        String [] parms = data.split("\\?");
        map.put("path", parms[0]);
        Map<String, String> attrs = new HashMap<>();
        String[] kvs = parms[1].split("&");
        for (String string : kvs) {
            String [] kv = string.split("=");
            attrs.put(kv[0], kv[1]);
        }
        map.put("attrs", attrs);
        return map;
    }

    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public String getProtocolVersion() {
        return protocolVersion;
    }
    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    @Override
    public String toString() {
        return this.method+" "+this.data+" "+this.protocol+"/"+this.protocolVersion;
    }

}
