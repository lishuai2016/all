package com.ls.design_pattern.ChainOfResponsibility;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-04
 */
public class Request {
    private RequestType type;
    private String name;
    public Request(RequestType type,String name) {
        this.type=type;
        this.name=name;
    }


    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
