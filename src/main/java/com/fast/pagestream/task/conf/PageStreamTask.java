package com.fast.pagestream.task.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作者：练书锋
 * 时间：2018/7/3
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageStreamTask extends TaskConfig {

    //关键词
    private String keyWord;


}
