/**
 * InterfaceExecutor.java
 */
package com.ofpay.edge;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * <p>
 * 接口执行类
 * </p>
 * 
 * @author Angus
 * @date 2013-3-11
 * @version 1.0
 * @since 1.0
 */
public class InterfaceExecutor {
	private static Logger log = LoggerFactory.getLogger(InterfaceExecutor.class);

	/**
	 * 接口执行方法
	 * 
	 * @param target
	 *            Service Bean对象
	 * @param method
	 *            Service Method
	 * @param inputParamArray
	 *            方法入参信息，采用Json描述
	 * @return 调用结果
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String execute(Object target, Method method, JSONArray inputParamArray) throws Exception {
		Object result;

		try {
			Object[] params = convertParams(inputParamArray, method.getParameterTypes());
			result = method.invoke(target, params);
		} catch (IllegalArgumentException e) {
			result = e.getMessage();
		} catch (Exception e) {
			throw e;
		}

		return JSON.toJSONString(result, SerializerFeature.QuoteFieldNames, SerializerFeature.WriteMapNullValue,
				SerializerFeature.SortField);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object[] convertParams(JSONArray inputParamArray, Class<?>[] paramTypes) throws Exception {
		Object[] paramArr;

		if (inputParamArray == null) {
			paramArr = new Object[] {};
		} else {
			paramArr = inputParamArray.toArray();
		}

		if (paramTypes.length != paramArr.length) {
			log.warn("参数值与参数类型个数不匹配, paramList:{}", new Object[] { inputParamArray });
			throw new IllegalArgumentException("参数错误，传入的参数个数与接口不匹配");
		}

		Object[] params = new Object[paramArr.length];

		for (int i = 0; i < paramArr.length; i++) {
			Object param = paramArr[i];
			Class<?> paramType = paramTypes[i];

			params[i] = TypeUtils.cast(param, paramType, ParserConfig.getGlobalInstance());
		}
		return params;
	}

	/**
	 * 将Throwable转换为字符串描述的堆栈信息
	 * 
	 * @param throwable
	 *            异常信息
	 * @return
	 */
	public static String getStackTrace(Throwable throwable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		throwable.printStackTrace(printWriter);
		return result.toString();
	}

}
