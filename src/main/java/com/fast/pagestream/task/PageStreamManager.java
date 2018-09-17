package com.fast.pagestream.task;

import com.fast.pagestream.task.conf.PageStreamTask;
import org.springframework.stereotype.Component;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 作者：练书锋
 * 时间：2018/7/3
 */

@Component
public class PageStreamManager {


    //任务配置
    private Vector<PageStreamTask> tasks = new Vector<PageStreamTask>();

    //记录执行的步长
    private int stepCount = 0;


    //用于多线程请求任务排队等待
    Vector<CountDownLatch> waitUpdateThread = new Vector<>();


    //是否正在更新
    private Boolean isUpdate = false;

    /**
     * 取一个任务
     *
     * @return
     */
    public PageStreamTask getTask() {
        //等待任务更新
        waitUpdateTask();
        //取任务
        PageStreamTask task = null;
        synchronized (this.tasks) {
            task = this.tasks.get(this.stepCount % this.tasks.size());
            stepCount++;
            if (stepCount > Integer.MAX_VALUE) {
                stepCount = 0;
            }
        }
        return task;
    }

    /**
     * 等待线程任务
     */
    public void waitUpdateTask() {
        if (this.isUpdate) {
            try {
                CountDownLatch downLatch = new CountDownLatch(1);
                downLatch.await(5, TimeUnit.SECONDS);
                synchronized (this.waitUpdateThread) {
                    this.waitUpdateThread.add(downLatch);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 更新任务
     *
     * @param pageStreamTask
     */
    public synchronized void updateAllTask(PageStreamTask... pageStreamTask) {
        this.isUpdate = true;
        this.tasks.clear();
        for (PageStreamTask streamTask : pageStreamTask) {
            this.tasks.add(streamTask);
        }
        this.isUpdate = false;
        cancelWaitThread();
    }


    /**
     * 取消线程的等待
     */
    private void cancelWaitThread() {
        synchronized (this.waitUpdateThread) {
            for (CountDownLatch downLatch : this.waitUpdateThread) {
                downLatch.countDown();
            }
            this.waitUpdateThread.clear();
        }
    }


    /**
     * 获取所有任务总数
     *
     * @return
     */
    public int getTaskCount() {
        return this.tasks.size();
    }


}
