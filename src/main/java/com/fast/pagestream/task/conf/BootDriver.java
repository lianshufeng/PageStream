package com.fast.pagestream.task.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 作者：练书锋
 * 时间：2018/7/5
 */
@Component
public class BootDriver {


    @Autowired
    private ResourceLoader resourceLoader;


    @PostConstruct
    private void init() throws IOException {
        String  driverPath = this.resourceLoader.getResource("classpath:/").getURI().getPath() + "/webdriver/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);
    }
}
