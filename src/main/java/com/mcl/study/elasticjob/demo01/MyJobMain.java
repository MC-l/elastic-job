package com.mcl.study.elasticjob.demo01;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

/**
 * @auth caiguowei
 * @date 2020/5/20
 */
public class MyJobMain {
    // zookeeper 地址
    private static final String zk_url = "localhost:2181";
    // 定时任务命名空间
    private static final String job_namespace = "ej-example-java";


    /**
     * elastic-job 在 zookeeper 中的目录结构：
     *
     * /
     *  ej-example-java
     *      myJob
     *          config
     *          instances
     *          leader
     *              election
     *                  latch
     *              sharding
     *          servers
     *              192.168.2.27
     *          sharding
     *              0
     *                  instance
     * @param args
     */
    public static void main(String[] args) {
        CoordinatorRegistryCenter center = configRegistryCenter();

        startJob(center);
    }

    /**
     * 配置注册中心
     */
    private static CoordinatorRegistryCenter configRegistryCenter(){
        // 配置注册中心
        ZookeeperConfiguration config = new ZookeeperConfiguration(zk_url,job_namespace);
        // 减少zk的超时时间
        config.setSessionTimeoutMilliseconds(100);
        // 创建注册中心
        ZookeeperRegistryCenter center = new ZookeeperRegistryCenter(config);
        center.init();
        return center;
    }

    /**
     * 配置并启动任务
     * @param center
     */
    private static void startJob(CoordinatorRegistryCenter center){
        // 创建JobCoreConfiguration(作业名称，cron表达式，分片数)
        JobCoreConfiguration jobCoreConfig = JobCoreConfiguration.newBuilder("myJob", "0/3 * * * * ?",2).build();
        // 创建SimpleJobConfiguration
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfig, MyJob.class.getCanonicalName());
        // 启动任务
        new JobScheduler(center, LiteJobConfiguration.newBuilder(simpleJobConfiguration).build()).init();
    }
}
