package com.fast.pagestream.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StreamUtils;

import java.io.InputStream;

/**
 * json工具
 * 
 * @作者 练书锋
 * @联系 oneday@vip.qq.com
 * @时间 2014年5月17日
 */
public class JsonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 转换到json字符串
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object object) throws Exception {
		return objectMapper.writeValueAsString(object);
	}

	/**
	 * 转换为对象
	 * 
	 * @param json
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static <T> T toObject(String json, Class<T> cls) throws Exception {
		return objectMapper.readValue(json, cls);
	}

	/**
	 * 载入文件到对象
	 * 
	 * @param configName
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static <T> T loadToObject(String configName, Class<T> cls) throws Exception {
		T t = null;
		InputStream inputStream = JsonUtil.class.getClassLoader().getResourceAsStream(configName);
		byte[] bin = StreamUtils.copyToByteArray(inputStream);
		String json = new String(bin, "UTF-8");
		t = toObject(json, cls);
		inputStream.close();
		return t;
	}

}
