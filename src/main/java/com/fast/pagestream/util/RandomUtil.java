package com.fast.pagestream.util;

import java.util.Random;

/**
 * 作者：练书锋
 * 时间：2018/7/5
 */
public class RandomUtil {

    /**
     * 取自定的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int get(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

}
