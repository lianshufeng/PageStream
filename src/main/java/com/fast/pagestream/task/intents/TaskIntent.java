package com.fast.pagestream.task.intents;

import com.fast.pagestream.log.PageTaskLogHelper;
import com.fast.pagestream.mvc.dao.OpenPageLogDao;
import com.fast.pagestream.proxy.ProxyPool;
import com.fast.pagestream.task.PageStreamManager;
import com.fast.pagestream.task.TaskManager;
import com.fast.pagestream.task.conf.PageStreamTask;
import com.fast.pagestream.task.conf.TaskManagerConfig;
import com.fast.pagestream.util.HttpClient;
import com.fast.pagestream.util.JsonUtil;
import com.fast.pagestream.util.RandomUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：练书锋
 * 时间：2018/7/3
 */
@Component
@Scope("prototype")
public class TaskIntent implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(TaskIntent.class);


    @Autowired
    private TaskManager taskManager;

    @Autowired
    private TaskManagerConfig taskManagerConfig;

    @Autowired
    private PageStreamManager pageStreamManager;

    @Autowired
    private ProxyPool proxyPool;

    @Autowired
    private PageTaskLogHelper pageTaskLogHelper;


    @Autowired
    private OpenPageLogDao openPageLogDao;

    @Override
    public void run() {
        //任务
        work();
        //下一个任务
        nextTask();
    }


    /**
     * 工作代码
     */
    private void work() {
        pageTaskLogHelper.init();

        ChromeOptions options = new ChromeOptions();
        if (!this.taskManagerConfig.isDebug()) {
            options.addArguments("--headless");
        }

        //隐身模式
        if (this.taskManagerConfig.isIncognito()){
            options.addArguments("--incognito");
        }

        String proxyIp = "localhost";
        //代理
        if (this.taskManagerConfig.isProxy()) {
            if (proxyIp != null) {
                proxyIp = proxyPool.getIp();
                options.addArguments("--proxy-server=" + proxyIp);
            }
        }

        ChromeDriver webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);


        try {
            script(webDriver, proxyIp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 退出释放内存
        webDriver.quit();

        pageTaskLogHelper.remove();

    }

    //执行脚本
    private void script(ChromeDriver driver, String proxy) throws Exception {
        PageStreamTask task = this.pageStreamManager.getTask();
        log.info("ip:" + proxy + " , task : " + JsonUtil.toJson(task));

        pageTaskLogHelper.get().setIp(proxy);
        pageTaskLogHelper.get().setTask(task);


        //输入关键词
        inputKeyWord(driver, task);
        Thread.sleep(2000);
        //搜索并翻页
        searchAndNextPage(driver, task, 1);
    }

    //输入关键词
    private void inputKeyWord(ChromeDriver driver, PageStreamTask task) {
        driver.get("https://www.baidu.com");
        WebElement kw = driver.findElementById("kw");
        //输入关键词
        kw.sendKeys(task.getKeyWord());
        //鼠标点击搜索按钮
        Actions builder = new Actions(driver);
        builder.click(driver.findElementById("su")).perform();


    }


    //搜索关键词或翻页
    private void searchAndNextPage(final ChromeDriver driver, PageStreamTask task, int depth) throws Exception {
        WebElement webElement = null;
        for (int i = 0; i < 10; i++) {
            try {
                webElement = driver.findElementById("content_left");
                break;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }

        //找不到则返回
        if (webElement == null) {
            return;
        }

        List<WebElement> as = webElement.findElements(By.className("t"));
        for (WebElement web : as) {
            WebElement aTag = web.findElement(By.tagName("a"));
            String webTitle = aTag.getText();
            //是否匹配
            boolean isMatch = matchTitle(webTitle, task.getTitle(), task.getExclude());
            if (isMatch) {
                openPage(driver, aTag);
            }
        }


        //是否递归翻页
        if (depth < task.getDepth()) {
            List<WebElement> webElements = driver.findElementById("page").findElements(By.tagName("a"));
            if (webElements != null && webElements.size() > 0) {
                WebElement nextPage = webElements.get(webElements.size() - 1);
                new Actions(driver).moveToElement(nextPage).perform();
                Thread.sleep(300);
                new Actions(driver).moveByOffset(RandomUtil.get(0, 6), RandomUtil.get(0, 4)).perform();
                Thread.sleep(RandomUtil.get(300, 1000));
                new Actions(driver).click().perform();


                //等待响应
                Thread.sleep(3000l);
                searchAndNextPage(driver, task, depth + 1);
            }
        }
    }


    /**
     * 打开页面
     *
     * @param aTag
     */
    private synchronized void openPage(ChromeDriver driver, WebElement aTag) throws Exception {
        String pageTitile = aTag.getText();
        //打开过的页面
        Map<String, String> pages = this.pageTaskLogHelper.get().getPages();


        //过滤重复
        if (pages.containsKey(pageTitile)) {
            return;
        }


        //移动到这个标签
        new Actions(driver).moveToElement(aTag).perform();
        Thread.sleep(300);
        new Actions(driver).moveByOffset(RandomUtil.get(0, 15), RandomUtil.get(0, 8)).perform();
        Thread.sleep(1000);
        new Actions(driver).click().perform();


        log.info("匹配：" + pageTitile);

//        / 获取真实的URL
        String href = aTag.getAttribute("href");
        String location = getRealUrl(href);

        //增加日志到数据库里
        openPageLogDao.addLog(this.pageTaskLogHelper.get().getIp(), pageTitile, location);


    }


    /**
     * @param href
     */
    private String getRealUrl(String href) throws IOException {


        HttpClient httpClient = new HttpClient();
        // 取消自动转换
        httpClient.setFollowRedirects(false);
        HttpClient.ResultBean resultBean = httpClient.ReadDocuments(href);


        String url = null;

        //获取URL
        List<String> urls = resultBean.getResponseHead().get("Location");
        if (urls != null && urls.size() > 0) {
            url = urls.get(0);
        }
        //寻找内容
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        String content = new String(resultBean.getData()).trim();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            url = matcher.group();
            break;
        }
        return url;
    }


    /**
     * 判断是否匹配这个标题
     *
     * @param title
     * @param sources
     * @param excludes
     * @return
     */
    private boolean matchTitle(final String title, String[] sources, String[] excludes) {
        boolean match = false;
        //匹配条件
        if (sources != null) {
            for (String source : sources) {
                if (title.indexOf(source) > -1) {
                    match = true;
                    break;
                }
            }
        }

        //判断过滤条件
        if (excludes != null) {
            for (String exclude : excludes) {
                if (title.indexOf(exclude) > -1) {
                    match = false;
                    break;
                }
            }
        }
        return match;
    }


    /**
     * 下一个任务
     */
    private void nextTask() {
        try {
            Thread.sleep(this.taskManagerConfig.getTaskSleepTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.taskManager.nextTask();
    }


}
