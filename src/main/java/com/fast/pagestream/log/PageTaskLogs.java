package com.fast.pagestream.log;

import com.fast.pagestream.task.conf.PageStreamTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：练书锋
 * 时间：2018/7/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageTaskLogs {


    //代理IP
    private String ip;

    //执行参数
    private PageStreamTask task;

    //创建时间
    private long createTime = System.currentTimeMillis();


    //打开的页面
    private Map<String, String> pages = new HashMap();


}
