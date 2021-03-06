/**
 * 
 */
package org.ferrari.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Kevin
 */
public class JsonUtils {
	/**
	 * 将一个实体类对象转换成Json数据格式
	 * @param bean 需要转换的实体类对象
	 * @return 转换后的Json格式字符串
	 */
	public static String beanToJson(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = objectToJson(props[i].getName());
					String value = objectToJson(props[i].getReadMethod().invoke(bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * 将一个List对象转换成Json数据格式返回
	 * @param list需要进行转换的List对象
	 * @return 转换后的Json数据格式字符串
	 */
	public static String listToJson(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}
	
	/**
	 * 将一个对象数组转换成Json数据格式返回
	 * @param array 需要进行转换的数组对象
	 * @return 转换后的Json数据格式字符串
	 */
	public static String arrayToJson(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * 将一个Map对象转换成Json数据格式返回
	 * @param map 需要进行转换的Map对象
	 * @return 转换后的Json数据格式字符串
	 */
	public static String mapToJson(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(objectToJson(key));
				json.append(":");
				json.append(objectToJson(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * 将一个Set对象转换成Json数据格式返回
	 * @param set 需要进行转换的Set对象
	 * @return 转换后的Json数据格式字符串
	 */
	public static String setToJson(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	private static String numberToJson(Number number) {
		return number.toString();
	}

	private static String booleanToJson(Boolean bool) {
		if(bool == null) return null;
		return bool.toString();
	}

	private static String nullToJson() {
		return "";
	}

	private static String stringToJson(String s) {
		if (s == null)return nullToJson();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"': 	sb.append("\\\""); 	break;
			case '\\':	sb.append("\\\\");	break;
			case '\b':	sb.append("\\b");	break;
			case '\f':	sb.append("\\f");	break;
			case '\n':	sb.append("\\n");	break;
			case '\r':	sb.append("\\r");	break;
			case '\t':	sb.append("\\t");	break;
			case '/':	sb.append("\\/");	break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}

	private static String objectToJson(Object obj) {
		StringBuffer json = new StringBuffer();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof Number) {
			json.append(numberToJson((Number) obj));
		} else if (obj instanceof Boolean) {
			json.append(booleanToJson((Boolean) obj));
		} else if (obj instanceof String) {
			json.append("\"").append(stringToJson(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(arrayToJson((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(listToJson((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(mapToJson((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(setToJson((Set<?>) obj));
		} else {
			json.append(beanToJson(obj));
		}
		return json.toString();
	}

	// ============================================================================================

//	/**
//	 * 将Json格式的字符串转换成指定的对象返回
//	 * @param jsonString Json格式的字符串
//	 * @param pojoCalss 转换后的对象类型
//	 * @return 转换后的对象
//	 */
//	public static Object json2Object(String jsonString, Class<?> pojoCalss) {
//		Object pojo;
//		JSONObject jsonObject = JSONObject.fromObject(jsonString);
//		pojo = JSONObject.toBean(jsonObject, pojoCalss);
//		return pojo;
//	}
//
//	/**
//	 * 将Json格式的字符串转换成Map<String,Object>对象返回
//	 * @param jsonString 需要进行转换的Json格式字符串
//	 * @return 转换后的Map<String,Object>对象
//	 */
//	public static Map<String, Object> json2Map(String jsonString) {
//		JSONObject jsonObject = JSONObject.fromObject(jsonString);
//		Iterator<?> keyIter = jsonObject.keys();
//		String key;
//		Object value;
//		Map<String, Object> valueMap = new HashMap<String, Object>();
//		while (keyIter.hasNext()) {
//			key = (String) keyIter.next();
//			value = jsonObject.get(key);
//			valueMap.put(key, value);
//		}
//		return valueMap;
//	}
	
//	/**
//	 * 将Json格式的字符串转换成对象数组返回
//	 * @param jsonString需要进行转换的Json格式字符串
//	 * @return 转换后的对象数组
//	 */
//	public static Object[] json2ObjectArray(String jsonString) {
//		JSONArray jsonArray = JSONArray.fromObject(jsonString);
//		return jsonArray.toArray();
//	}
//
//	/**
//	 * 将Json格式的字符串转换成指定对象组成的List返回
//	 * @param jsonString Json格式的字符串
//	 * @param pojoClass转换后的List中对象类型
//	 * @return 转换后的List对象
//	 */
//	public static List<Object> json2List(String jsonString, Class<?> pojoClass) {
//		JSONArray jsonArray = JSONArray.fromObject(jsonString);
//		JSONObject jsonObject;
//		Object pojoValue = null;
//		List<Object> list = new ArrayList<Object>();
//		for (int i = 0; i < jsonArray.size(); i++) {
//			jsonObject = jsonArray.getJSONObject(i);
//			pojoValue = JSONObject.toBean(jsonObject, pojoClass);
//			list.add(pojoValue);
//		}
//		return list;
//	}
//
//	/**
//	 * 将Json格式的字符串转换成字符串数组返回
//	 * @param jsonString需要进行转换的Json格式字符串
//	 * @return 转换后的字符串数组
//	 */
//	public static String[] json2StringArray(String jsonString) {
//		JSONArray jsonArray = JSONArray.fromObject(jsonString);
//		String[] stringArray = new String[jsonArray.size()];
//		for (int i = 0; i < jsonArray.size(); i++) {
//			stringArray[i] = jsonArray.getString(i);
//		}
//		return stringArray;
//	}
//
//	/**
//	 * 将Json格式的字符串转换成Long数组返回
//	 * @param jsonString需要进行转换的Json格式字符串
//	 * @return 转换后的Long数组
//	 */
//	public static Long[] json2LongArray(String jsonString) {
//		JSONArray jsonArray = JSONArray.fromObject(jsonString);
//		Long[] longArray = new Long[jsonArray.size()];
//		for (int i = 0; i < jsonArray.size(); i++) {
//			longArray[i] = jsonArray.getLong(i);
//		}
//		return longArray;
//	}
//
//	/**
//	 * 将Json格式的字符串转换成Integer数组返回
//	 * @param jsonString需要进行转换的Json格式字符串
//	 * @return 转换后的Integer数组
//	 */
//	public static Integer[] json2IntegerArray(String jsonString) {
//		JSONArray jsonArray = JSONArray.fromObject(jsonString);
//		Integer[] integerArray = new Integer[jsonArray.size()];
//		for (int i = 0; i < jsonArray.size(); i++) {
//			integerArray[i] = jsonArray.getInt(i);
//		}
//		return integerArray;
//	}
//
//	/**
//	 * 将Json格式的字符串转换成日期数组返回
//	 * @param jsonString需要进行转换的Json格式字符串
//	 * @param DataFormat返回的日期格式
//	 * @return 转换后的日期数组
//	 */
//	public static Date[] json2DateArray(String jsonString, String DataFormat) {
//		JSONArray jsonArray = JSONArray.fromObject(jsonString);
//		Date[] dateArray = new Date[jsonArray.size()];
//		String dateString;
//		Date date;
//		for (int i = 0; i < jsonArray.size(); i++) {
//			dateString = jsonArray.getString(i);
//			date = DateUtils.parseDate(dateString, DataFormat);
//			dateArray[i] = date;
//		}
//		return dateArray;
//	}
//
//	/**
//	 * 将Json格式的字符串转换成Double数组返回
//	 * @param jsonString需要进行转换的Json格式字符串
//	 * @return 转换后的Double数组
//	 */
//	public static Double[] json2DoubleArray(String jsonString) {
//		JSONArray jsonArray = JSONArray.fromObject(jsonString);
//		Double[] doubleArray = new Double[jsonArray.size()];
//		for (int i = 0; i < jsonArray.size(); i++) {
//			doubleArray[i] = jsonArray.getDouble(i);
//		}
//		return doubleArray;
//	}
//	
//	public static void main(String[] args) {
//		List list = new ArrayList();
//		list.add("kevin");
//		list.add("madoka");
//		Map map = new HashMap();
//		map.put("1", "kevin");
//		map.put("2", "madoka");
//		map.put("3", "master");
//		map.put("4", "master4");
//		map.put("8", "master8");
//		map.put("9", "master9");
//		map.put("5", "master5");
//		map.put("6", "master6");
//		map.put("7", "master7");
//
//		System.out.println(objectToJson(list));
//		System.out.println(objectToJson(map));
//	}
}
