package com.fast.pagestream.proxy;

import com.fast.pagestream.util.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Vector;

/**
 * 作者：练书锋
 * 时间：2018/7/5
 * 代理池
 */
@Component
public class ProxyPool {


    @Value("${proxy.order}")
    private String order;

    @Value("${proxy.minCount}")
    private int minCount;

    @Value("${proxy.num}")
    private int num;


    //缓存代理
    private Vector<String> cacheIps = new Vector<>();


    //获取代理
    public synchronized String getIp() {
        if (cacheIps.size() <= minCount) {
            updateProxy();
        }
        String ip = null;
        if (cacheIps.size() > 0) {
            ip = cacheIps.remove(0);
        }
        return ip;
    }

    //更新缓存
    private void updateProxy() {
        try {
            String url = "http://tvp.daxiangdaili.com/ip/?tid=" + this.order + "&category=2&filter=on&num=" + this.num;
            String content = new String(new HttpClient().get(url), "UTF-8");
            String[] ips = content.split("\r\n");
            cacheIps.addAll(Arrays.asList(ips));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
