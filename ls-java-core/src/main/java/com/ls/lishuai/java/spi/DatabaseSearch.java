package com.ls.lishuai.java.spi;

import java.util.List;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/22 9:33
 */
public class DatabaseSearch implements Search {
    @Override
    public List<String> searchDoc(String keyword) {
        System.out.println("数据搜索 "+keyword);
        return null;
    }
}
