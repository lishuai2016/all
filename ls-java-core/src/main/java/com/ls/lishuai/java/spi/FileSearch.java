package com.ls.lishuai.java.spi;

import java.util.List;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/22 9:32
 */
public class FileSearch implements Search {

    @Override
    public List<String> searchDoc(String keyword) {
        System.out.println("文件搜索 "+keyword);
        return null;
    }
}
