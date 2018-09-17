package com.fast.pagestream.mvc.dao.impl;

import com.fast.pagestream.mvc.dao.extend.OpenPageLogDaoExtend;
import com.fast.pagestream.mvc.domain.OpenPageLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * 作者：练书锋
 * 时间：2018/7/6
 */
public class OpenPageLogDaoImpl implements OpenPageLogDaoExtend {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addLog(String ip, String title, String url) {

        OpenPageLog openPageLog = new OpenPageLog();
        openPageLog.setCreateTime(System.currentTimeMillis());
        openPageLog.setTitle(title);
        openPageLog.setUrl(url);
        openPageLog.setIp(ip);

        this.mongoTemplate.save(openPageLog);

    }
}
