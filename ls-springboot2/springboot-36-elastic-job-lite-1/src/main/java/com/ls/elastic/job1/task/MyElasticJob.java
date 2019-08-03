package com.ls.elastic.job1.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-07 13:23
 */
public class MyElasticJob implements SimpleJob {

    @Override
    public void execute(ShardingContext context) {
        System.out.println("11111111111111");
    }
}
