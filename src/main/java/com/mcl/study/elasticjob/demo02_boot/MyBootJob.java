package com.mcl.study.elasticjob.demo02_boot;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

/**
 * @auth caiguowei
 * @date 2020/5/20
 */
@Component
public class MyBootJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("分片："+shardingContext.getShardingItem());
        // shardingContext.toString() ==> ShardingContext(jobName=myJob, taskId=myJob@-@0@-@READY@-@192.168.2.27@-@40288, shardingTotalCount=1, jobParameter=, shardingItem=0, shardingParameter=null)
        System.out.println("任务执行："+shardingContext.toString());
    }
}
