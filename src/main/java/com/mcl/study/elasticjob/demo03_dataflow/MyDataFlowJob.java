package com.mcl.study.elasticjob.demo03_dataflow;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author MCl
 * @Date 2020-05-22 01:48
 */
@Component
public class MyDataFlowJob implements DataflowJob<String> {
    @Override
    public List<String> fetchData(ShardingContext shardingContext) {
        int shardingItem = shardingContext.getShardingItem();
        List<String> result = new ArrayList<>();
        switch (shardingItem){
            case 0:
                result.add("a");
                break;
            case 1:
                result.add("b");
                break;
            case 2:
                result.add("c");
                break;
            default:
                break;


        }
        return result;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<String> list) {
        System.out.println(list);
    }
}
