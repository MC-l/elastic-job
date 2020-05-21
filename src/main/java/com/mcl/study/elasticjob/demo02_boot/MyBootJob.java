package com.mcl.study.elasticjob.demo02_boot;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

/**
 * 设置分片规则：3片， "0=a,1=b,2=c"
 * 启动两个进程（80端口和81端口）
 * 观察日志：
 * 发现 80端口被分配了第0个分片0=a
 * 发现 81端口被分配了第1,2个分片1=b，2=c
 *
 * Job可以根据 分片规则执行不同的业务
 *
 * @auth caiguowei
 * @date 2020/5/20
 */
@Component
public class MyBootJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {

        // 分片规则："0=a,1=b,2=c"

        System.out.println("分片项："+shardingContext.getShardingItem());
        // ShardingContext(jobName=com.mcl.study.elasticjob.demo02_boot.MyBootJob, taskId=com.mcl.study.elasticjob.demo02_boot.MyBootJob@-@0,2@-@READY@-@192.168.2.27@-@63568, shardingTotalCount=3, jobParameter=, shardingItem=2, shardingParameter=c)
        String shardingParameter = shardingContext.getShardingParameter();
        System.out.println("分片参数："+shardingParameter);
        System.out.println("任务执行："+shardingContext.toString());
    }
}
