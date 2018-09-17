package com.fast.pagestream.task.conf;

import com.fast.pagestream.task.type.EngineChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作者：练书锋
 * 时间：2018/7/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskConfig {

    //包含连接
    private String[] title;

    //例外
    private String[] exclude;

    //关键词
    private String[] keyWords;

    //备注
    private String remark;

    //深度 ， 最多翻几页
    private int depth = 3;

    //使用引擎
    private EngineChannel channel = EngineChannel.Baidu;
}
