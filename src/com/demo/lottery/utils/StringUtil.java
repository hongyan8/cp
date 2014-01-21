package com.demo.lottery.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:字符串工具类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright:Copyright (c) 2012
 * </p>
 * <p>
 * Company:湖南科创
 * </p>
 * 
 * @author xianlu.lu
 * @version 1.0
 * @date 2012-5-2
 */
public class StringUtil
{

	/**
	 * 字符串解析
	 * 
	 * @param str
	 * @return
	 */
	public static String[] split(String str)
	{
		return str.split("/");
	}

	/**
	 * null 转换 为 ""
	 * 
	 * @param str
	 * @return
	 */
	public static String deNull(String str)
	{
		if (str == null)
		{
			return "";
		}
		return str;
	}

	/**
	 * 对象转换为字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String Object2String(Object obj)
	{
		if (obj == null)
		{
			return "";
		}
		return obj.toString();
	}

	/**
	 * 功能描述：是否为空白,包括null和""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str)
	{
		return str == null || str.trim().length() == 0 || "null".equals(str);
	}

	public static String ToDBC(String input)
	{
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == 12288)
			{
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static List<Integer> StringToList(String listText, String splitStr)
	{
		List<Integer> list = new ArrayList<Integer>();
		String[] sArray = listText.split(splitStr);
		for (int i = 0; i < sArray.length; i++)
		{
			list.add(Integer.parseInt(sArray[i].toString()));
		}

		return list;
	}

	public static List<Map<String, Object>> StringToMap(String listText, String splitStr)
	{
		List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
		String[] sArray = listText.split(splitStr);
		List<Integer> list = null;
		for (int i = 0; i < sArray.length; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			if (sArray[i].contains("、"))
			{
				String[] sArrayTemp = sArray[i].split("、");
				list = new ArrayList<Integer>();
				for (int j = 0; j < sArrayTemp.length; j++)
				{
					list.add(Integer.parseInt(sArrayTemp[j].toString().trim()));
				}
			}
			else
			{
				list = new ArrayList<Integer>();
				list.add(Integer.parseInt(sArray[i].toString().trim()));
			}
			map.put("position", i);
			map.put("num", list);
			lists.add(map);
		}

		return lists;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String listToStringArray(List list)
	{
		String a = "";
		for (int i = 0; i < list.size(); i++)
		{
			a += list.get(i) + ",";
		}
		return a.substring(0, a.length() - 1);
	}
}
