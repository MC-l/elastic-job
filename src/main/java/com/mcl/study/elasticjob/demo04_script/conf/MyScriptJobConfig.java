package com.mcl.study.elasticjob.demo04_script.conf;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.mcl.study.elasticjob.demo03_dataflow.MyDataFlowJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

/**
 * @auth caiguowei
 * @date 2020/5/21
 */
@Configuration
public class MyScriptJobConfig {

    // zookeeper 地址
    private static final String zk_url = "112.74.169.206:2181";
    // 定时任务命名空间
    private static final String job_namespace = "ej-script-java";

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
     * @param cron
     * @param shadingTotalCount
     * @param shardingItemParameters
     * @return
     */
    public LiteJobConfiguration liteJobConfiguration(String cron, int shadingTotalCount, String shardingItemParameters) throws IOException {
        // 创建JobCoreConfiguration(作业名称，cron表达式，分片数)
        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder("ScriptJob", cron, shadingTotalCount);
        if (StringUtils.isNoneBlank(shardingItemParameters)){
            builder.shardingItemParameters(shardingItemParameters);
        }
        // ScriptJobConfiguration
        ScriptJobConfiguration scriptJobConfiguration = new ScriptJobConfiguration(builder.build(), buildScriptCommandLine());
        // 启动任务
        return LiteJobConfiguration
                .newBuilder(scriptJobConfiguration)
                .overwrite(true).build();
    }

    /**
     * 3，创建SpringJobScheduler
     */
    @Bean(initMethod = "init")
    public JobScheduler springJobScheduler(CoordinatorRegistryCenter center) throws IOException {
        return new JobScheduler(center,liteJobConfiguration("0/3 * * * * ?",3,"0=a,1=b,2=c"));
    }

    private static String buildScriptCommandLine() throws IOException {
//        //判断当前系统
//        if (System.getProperties().getProperty("os.name").contains("Windows")) {
//            return Paths.get(MyScriptJobConfig.class.getResource("script.bat").getPath().substring(1)).toString();
//        }
//        Path result = Paths.get(MyScriptJobConfig.class.getResource("/Users/mcl/IdeaProjects/elastic-job/src/main/java/com/mcl/study/elasticjob/demo04_script/conf/script.sh").getPath());
//        Files.setPosixFilePermissions(result, PosixFilePermissions.fromString("rwxr-xr-x"));
//        return result.toString();

        return "echo Hello";
    }
}
