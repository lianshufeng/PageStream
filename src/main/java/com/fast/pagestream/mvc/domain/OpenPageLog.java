package com.fast.pagestream.mvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 作者：练书锋
 * 时间：2018/7/6
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenPageLog {

    @Id
    private String id;

    @Indexed
    private String title;

    // URL
    @Indexed
    private String url;

    private String ip;

    //创建时间
    private long CreateTime;

}
