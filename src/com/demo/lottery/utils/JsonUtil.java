package com.demo.lottery.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class JsonUtil
{

	/**
	 * getJSON(将用json格式字符串转JSONObject对象)
	 * 
	 * @param jsonString
	 * @return
	 */
	public static JSONObject getJSON(String jsonString)
	{
		JSONObject obj = new JSONObject();
		try
		{
			obj = new JSONObject(jsonString);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * getJsonArray(将用json格式字符串转JSONArray对象)
	 * 
	 * @param jsonString
	 * @return
	 */
	public static JSONArray getJsonArray(String jsonString)
	{
		JSONArray jsonArray = new JSONArray();
		try
		{
			jsonArray = new JSONArray(jsonString);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return jsonArray;
	}

	/**
	 * getMap(将用json格式字符串转换为 map对象)
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String, Object> getMap(String jsonString)
	{
		JSONObject jsonObject = getJSON(jsonString);
		Map<String, Object> valueMap = new HashMap<String, Object>();
		try
		{
			@SuppressWarnings("unchecked") Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			while (keyIter.hasNext())
			{
				key = (String) keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key, value);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return valueMap;
	}

	/**
	 * getMap(将用json格式字符串转换为 map对象)
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String, Object> getMapString(String jsonString)
	{
		JSONObject jsonObject = getJSON(jsonString);
		Map<String, Object> valueMap = new HashMap<String, Object>();
		try
		{
			@SuppressWarnings("unchecked") Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			while (keyIter.hasNext())
			{
				key = (String) keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key, value.toString());
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return valueMap;
	}

	/**
	 * getList(把json 转换为 ArrayList 形式)
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> getList(String jsonString)
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONArray jsonArray = getJsonArray(jsonString);
		try
		{
			JSONObject jsonObject;
			for (int i = 0; i < jsonArray.length(); i++)
			{
				jsonObject = jsonArray.getJSONObject(i);
				list.add(getMap(jsonObject.toString()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * getList(把json 转换为 ArrayList 形式)
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> getListString(String jsonString)
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONArray jsonArray = getJsonArray(jsonString);
		try
		{
			JSONObject jsonObject;
			for (int i = 0; i < jsonArray.length(); i++)
			{
				jsonObject = jsonArray.getJSONObject(i);
				list.add(getMapString(jsonObject.toString()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * getKeyValue(从JSONObject中获取指定key的值)
	 * 
	 * @param jsonobj
	 * @param name
	 * @return
	 */
	public static String getKeyValue(JSONObject jsonobj, String name)
	{
		try
		{
			return jsonobj.getString(name);
		}
		catch (Exception e)
		{
			return "";
		}
	}
	/*
	 * json转map
	 *@param cmd_string
	 *2013-5-13
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> decodeCmd(String cmd_string){
		Gson gson = new Gson();
		return (Map<String,Object>)gson.fromJson(cmd_string, Map.class);
	}
	/*
	 * map转json
	 *@param cmd
	 *@return
	 *2013-5-13
	 */
	public static String encodeCmd(Map<String,Object> cmd){
		Gson gson = new Gson();
		return gson.toJson(cmd);
	}

}
