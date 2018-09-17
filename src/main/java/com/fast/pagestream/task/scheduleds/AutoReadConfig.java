package com.fast.pagestream.task.scheduleds;

import com.fast.pagestream.task.PageStreamManager;
import com.fast.pagestream.task.conf.PageStreamTask;
import com.fast.pagestream.task.conf.TaskConfig;
import com.fast.pagestream.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：练书锋
 * 时间：2018/7/3
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoReadConfig {
    private final static Logger log = LoggerFactory.getLogger(AutoReadConfig.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    private void init() throws Exception {
        // 配置文件路径
        fileConfig = new File(resourceLoader.getResource(".").getURI().getPath() + "/TaskConfig.json");
        readFileConfig();
    }

    @Autowired
    private PageStreamManager pageStreamManager;

    //最后一次更新时间
    private long lastModified = -1;
    File fileConfig = null;


    /**
     * 读取磁盘配置到
     */
    @Scheduled(cron = "*/5 * * * * *")
    private void readFileConfig() throws Exception {
        //读取配置文件
        if (this.lastModified != this.fileConfig.lastModified()) {
            updateConfig();
        }
    }


    /**
     * 更新配置文件
     */
    private void updateConfig() throws Exception {
        String content = FileUtils.readFileToString(this.fileConfig, "UTF-8");
        TaskConfig[] tasks = JsonUtil.toObject(content, TaskConfig[].class);


        //批量配置生成多个任务
        List<PageStreamTask> pageStreamTasks = new ArrayList<>();
        for (TaskConfig task : tasks) {
            for (String kw : task.getKeyWords()) {
                PageStreamTask pageStreamTask = new PageStreamTask();
                BeanUtils.copyProperties(task, pageStreamTask);
                pageStreamTask.setKeyWord(kw);
                pageStreamTasks.add(pageStreamTask);
            }
        }
        this.pageStreamManager.updateAllTask(pageStreamTasks.toArray(new PageStreamTask[0]));
        log.info("update:" + this.pageStreamManager.getTaskCount());
        this.lastModified = this.fileConfig.lastModified();
    }


}
