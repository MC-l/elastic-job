package com.mcl.study.elasticjob.demo03_dataflow.conf;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.mcl.study.elasticjob.demo02_boot.MyBootJob;
import com.mcl.study.elasticjob.demo03_dataflow.MyDataFlowJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auth caiguowei
 * @date 2020/5/21
 */
//@Configuration
public class MyDataFlowJobConfig {

    // zookeeper 地址
    private static final String zk_url = "112.74.169.206:2181";
    // 定时任务命名空间
    private static final String job_namespace = "ej-dataflow-java";

    /**
     * 1，配置注册中心
     * @return
     */
    @Bean(initMethod="init")
    public CoordinatorRegistryCenter coordinatorRegistryCenter(){
        // 配置注册中心
        ZookeeperConfiguration config = new ZookeeperConfiguration(zk_url,job_namespace);
        // 减少zk的超时时间
        config.setSessionTimeoutMilliseconds(100);
        // 创建注册中心
        ZookeeperRegistryCenter center = new ZookeeperRegistryCenter(config);
        // 初始化方法放到 @Bean(initMethod=方法名)
        center.init();
        return center;
    }

    /**
     * 2，设置 LiteJobConfiguration
     * @param jobClass
     * @param cron
     * @param shadingTotalCount
     * @param shardingItemParameters
     * @return
     */
    public LiteJobConfiguration liteJobConfiguration(Class<? extends DataflowJob> jobClass, String cron, int shadingTotalCount, String shardingItemParameters){
        // 创建JobCoreConfiguration(作业名称，cron表达式，分片数)
        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shadingTotalCount);
        if (StringUtils.isNoneBlank(shardingItemParameters)){
            builder.shardingItemParameters(shardingItemParameters);
        }
        // DataflowJobConfiguration
        DataflowJobConfiguration dataflowJobConfiguration = new DataflowJobConfiguration(builder.build(), jobClass.getCanonicalName(),true);
        // 启动任务
        return LiteJobConfiguration
                .newBuilder(dataflowJobConfiguration)
                .overwrite(true).build();
    }

    /**
     * 3，创建SpringJobScheduler
     */
    @Autowired
    private MyDataFlowJob myDataFlowJob;
    @Bean(initMethod = "init")
    public SpringJobScheduler springJobScheduler(CoordinatorRegistryCenter center){
        return new SpringJobScheduler(myDataFlowJob,center,liteJobConfiguration(myDataFlowJob.getClass(),"0/3 * * * * ?",3,"0=a,1=b,2=c"));
    }
}
