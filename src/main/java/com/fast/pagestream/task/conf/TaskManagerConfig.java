package com.fast.pagestream.task.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 作者：练书锋
 * 时间：2018/7/3
 */

@Component

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("task")
public class TaskManagerConfig {

    //最大任务数
    private int maxTaskCount;

    //每个任务执行后休眠时间
    private long taskSleepTime;

    //调试模式
    private boolean debug;

    //是否使用代理
    private boolean proxy;

    //是否使用隐身模式启动
    private boolean incognito;

}
