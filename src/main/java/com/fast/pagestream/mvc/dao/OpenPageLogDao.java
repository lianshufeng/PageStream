package com.fast.pagestream.mvc.dao;

import com.fast.pagestream.mvc.dao.extend.OpenPageLogDaoExtend;
import com.fast.pagestream.mvc.domain.OpenPageLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 作者：练书锋
 * 时间：2018/7/6
 */
public interface OpenPageLogDao extends MongoRepository<OpenPageLog,String>,OpenPageLogDaoExtend {




}
