package com.fast.pagestream.log;

import org.springframework.stereotype.Component;

/**
 * 作者：练书锋
 * 时间：2018/7/5
 */
@Component
public class PageTaskLogHelper {

    private ThreadLocal<PageTaskLogs> threadLocal = new ThreadLocal<>();

    /**
     * 初始化
     */
    public void init() {
        this.threadLocal.set(new PageTaskLogs());
    }


    /**
     * 取出
     *
     * @return
     */
    public PageTaskLogs get() {
        return this.threadLocal.get();
    }

    /**
     * 销毁
     */
    public void remove() {
        this.threadLocal.remove();
    }


}
