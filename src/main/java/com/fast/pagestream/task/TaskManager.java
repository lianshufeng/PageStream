package com.fast.pagestream.task;

import com.fast.pagestream.task.conf.TaskManagerConfig;
import com.fast.pagestream.task.intents.TaskIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：练书锋
 * 时间：2018/7/3
 */
@Component
public class TaskManager {


    @Autowired
    private TaskManagerConfig taskManagerConfig;

    @Autowired
    private ApplicationContext applicationContext;

    //线程池
    ExecutorService taskThreadPool;


    @PostConstruct
    private void init() {
        //初始化线程池
        this.taskThreadPool = Executors.newScheduledThreadPool(taskManagerConfig.getMaxTaskCount());

        //执行任务
        executeTasks();
    }


    /**
     * 执行任务
     */
    private void executeTasks() {
        //执行任务
        for (int i = 0; i < taskManagerConfig.getMaxTaskCount(); i++) {
            nextTask();
        }
    }

    /**
     * 执行单个任务
     */
    public void nextTask() {
        //任务意图
        TaskIntent taskIntent = this.applicationContext.getBean(TaskIntent.class);
        this.taskThreadPool.execute(taskIntent);
    }


    @PreDestroy
    private void shutdown() {
        this.taskThreadPool.shutdownNow();
    }
}
