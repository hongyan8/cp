package com.demo.lottery.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 日期工具类
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
 * @date 2012-5-9
 */

public class DateUtil
{

	/**
	 * main方法
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		
	}

	/**
	 * 根据时间段，得出当前时间是 白天还是晚上
	 * 
	 * @param String
	 *            begTime (08:00)
	 * @param String
	 *            endTime (20:00)
	 * @return 白天/黑夜
	 */
	public static String getIsNight(String begTime, String endTime)
	{
		String isNight = "";
		String nowTime = getCurrentDateTime();
		String begdate = getCurrentDate() + " " + begTime + ":00";
		long i = DateUtil.getDiff(nowTime, begdate);

		String dateEnd = getCurrentDate() + " " + endTime + ":00";
		long j = DateUtil.getDiff(nowTime, dateEnd);

		if (i <= 0 && j >= 0)
		{
			isNight = "day";
		}
		else
		{
			isNight = "night";
		}
		return isNight;
	}

	/**
	 * 返回当前时间字符串。 格式：yyyy-MM-dd
	 * 
	 * @return String 指定格式的日期字符串.
	 */
	public static String getCurrentDate()
	{
		return getFormatDateTime(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 返回当前指定的时间戳。格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 格式为yyyy-MM-dd HH:mm:ss，总共19位。
	 */
	public static String getCurrentDateTime()
	{
		return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据给定的格式与时间(Date类型的)，返回时间字符串。最为通用。
	 * 
	 * @param date
	 *            指定的日期
	 * @param format
	 *            日期格式字符串
	 * @return String 指定格式的日期字符串.
	 */
	public static String getFormatDateTime(Date date, String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 给指定的时间字符串格式化，返回时间格式为 yyyy-MM-dd HH:mm:ss 的时间串
	 * @param String 原时间串
	 * @return String 格式化后的字符串
	 */
	public static String getFormatDateString(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(time); // 将给定的字符串中的日期提取出来
		} catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
			e.printStackTrace(); // 打印异常信息
		}
		return sdf.format(d);
	}

	/**
	 * 获取两个时间串时间的差值，单位为秒
	 * 
	 * @param startTime
	 *            开始时间 yyyy-MM-dd HH:mm:ss
	 * @param endTime
	 *            结束时间 yyyy-MM-dd HH:mm:ss
	 * @return 两个时间的差值(秒)
	 */
	public static long getDiff(String startTime, String endTime)
	{
		long diff = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			Date startDate = ft.parse(startTime);
			Date endDate = ft.parse(endTime);
			diff = endDate.getTime() - startDate.getTime();
			diff = diff / 1000;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return diff;
	}

	/**
	 * 获取当前日期的后一天
	 * 
	 * @return 当前日期+1
	 */

	public static String nextDate()
	{

		Calendar cal = Calendar.getInstance();// 使用默认时区和语言环境获得一个日历。
		cal.add(Calendar.DAY_OF_MONTH, +1);// 取当前日期的后一天.
		// 通过格式化输出日期
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");

		return format.format(cal.getTime());
	}

	/**
	 * 获取当前日期的前一天
	 * 
	 * @return 当前日期+1
	 */
	public static String preDay(int dayNums)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, dayNums); // 得到前一天
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	/**
	 * 获取中文格式时间 2012年12月12日 上午3:30
	 * 
	 * @param datetimeString
	 * @return
	 */
	public static String getChinaDateStringByDateString(String datetimeString)
	{
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			Date datetime = ft.parse(datetimeString);
			dtstr += (datetime.getYear() + 1900) + "年";
			dtstr += (datetime.getMonth() + 1) + "月";
			dtstr += datetime.getDate() + "日 ";
			int hour = datetime.getHours();
			String timeString = "";
			if (hour < 12)
			{// 上午
				timeString = "上午" + hour ;
			}
			else if (hour == 12)
			{ // 中午
				timeString = "中午" + hour;
			}
			else
			{// 下午
				timeString = "下午" + (hour - 12);
			}
			int minute = datetime.getMinutes();
			String minuteStr = "";
			if(minute < 10){
				minuteStr = "0"+minute;
			}else{
				minuteStr = minute+"";
			}
			dtstr += timeString + ":" + minuteStr;
		}
		catch (Exception e)
		{
			Log.e("getChinaDateStringByDateString", "获取中文格式时间异常" + e.getMessage());
		}
		return dtstr;
	}

	/**
	 * 获取中文格式时间 12月12日 上午3:30
	 * 
	 * @param datetimeString
	 * @return
	 */
	public static String getChinaDateStringByDateStringNoYear(String datetimeString)
	{
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			Date datetime = ft.parse(datetimeString);
			dtstr += (datetime.getMonth() + 1) + "月";
			dtstr += datetime.getDate() + "日 ";
			int hour = datetime.getHours();
			String timeString = "";
			if (hour < 12)
			{// 上午
				timeString = "上午" + hour;
			}
			else if (hour == 12)
			{ // 中午
				timeString = "中午" + hour;
			}
			else
			{// 下午
				timeString = "下午" + (hour - 12);
			}
			int minute = datetime.getMinutes();
			String minuteStr = "";
			if(minute < 10){
				minuteStr = "0"+minute;
			}else{
				minuteStr = minute+"";
			}
			dtstr += timeString + ":" + minuteStr;
		}
		catch (Exception e)
		{
			Log.e("getChinaDateStringByDateString", "获取中文格式时间异常" + e.getMessage());
		}
		return dtstr;
	}

	/**
	 * 获取中文格式时间 12日 上午3:30
	 * 
	 * @param datetimeString
	 * @return
	 */
	public static String getChinaDateStringByDateStringNoYearNoMonth(String datetimeString)
	{
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			Date datetime = ft.parse(datetimeString);
			dtstr += datetime.getDate() + "日 ";
			int hour = datetime.getHours();
			String timeString = "";
			if (hour < 12)
			{// 上午
				timeString = "上午" + hour;
			}
			else if (hour == 12)
			{ // 中午
				timeString = "中午" + hour;
			}
			else
			{// 下午
				timeString = "下午" + (hour - 12);
			}
			int minute = datetime.getMinutes();
			String minuteStr = "";
			if(minute < 10){
				minuteStr = "0"+minute;
			}else{
				minuteStr = minute+"";
			}
			dtstr += timeString + ":" + minuteStr;
		}
		catch (Exception e)
		{
			Log.e("getChinaDateStringByDateString", "获取中文格式时间异常" + e.getMessage());
		}
		return dtstr;
	}

	/**
	 * 获取中文格式时间 上午3:30
	 * 
	 * @param datetimeString
	 * @return
	 */
	public static String getChinaDateStringByOnlyTime(String datetimeString)
	{
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			Date datetime = ft.parse(datetimeString);
			int hour = datetime.getHours();
			String timeString = "";
			if (hour < 12)
			{// 上午
				timeString = "上午" + hour;
			}
			else if (hour == 12)
			{ // 中午
				timeString = "中午" + hour;
			}
			else
			{// 下午
				timeString = "下午" + (hour - 12);
			}
			int minute = datetime.getMinutes();
			String minuteStr = "";
			if(minute < 10){
				minuteStr = "0"+minute;
			}else{
				minuteStr = minute+"";
			}
			dtstr += timeString + ":" + minuteStr;
		}
		catch (Exception e)
		{
			Log.e("getChinaDateStringByDateString", "获取中文格式时间异常" + e.getMessage());
		}
		return dtstr;
	}

	/**
	 * 获取中文格式时间 2012年12月12日
	 * 
	 * @param datetimeString
	 * @return
	 */
	public static String getChinaDateStringByDateString1(String dateString)
	{
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date datetime = ft.parse(dateString);
			dtstr += (datetime.getYear() + 1900) + "年";
			dtstr += (datetime.getMonth() + 1) + "月";
			dtstr += datetime.getDate() + "日";
		}
		catch (Exception e)
		{
			Log.e("getChinaDateStringByDateString", "获取中文格式时间异常" + e.getMessage());
		}
		return dtstr;
	}

	/**
	 * 获取中文格式时间 2012年12月12日
	 * 
	 * @param datetimeString
	 * @return
	 */
	public static String getChinaDateStringByDateString2(String dateString)
	{
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date datetime = ft.parse(dateString);
			dtstr += (datetime.getMonth() + 1) + "月";
			dtstr += datetime.getDate() + "日";
		}
		catch (Exception e)
		{
			Log.e("getChinaDateStringByDateString", "获取中文格式时间异常" + e.getMessage());
		}
		return dtstr;
	}

	/**
	 * 获取年
	 * 
	 * @param dateTimeString
	 *            yyyy-MM-dd HH:mm:ss
	 * @return 年
	 */
	public static int getYear(String dateTimeString)
	{
		int year = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy");
		try
		{
			Date dateTime = ft.parse(dateTimeString);
			year = dateTime.getYear() + 1900;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return year;
	}

	/**
	 * 获取月
	 * 
	 * @param dateTimeString
	 *            yyyy-MM-dd HH:mm:ss
	 * @return 月
	 */
	public static int getMonth(String dateTimeString)
	{
		int month = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM");
		try
		{
			Date dateTime = ft.parse(dateTimeString);
			month = dateTime.getMonth() + 1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return month;
	}

	/**
	 * 获取日
	 * 
	 * @param dateTimeString
	 *            yyyy-MM-dd HH:mm:ss
	 * @return 日
	 */
	public static int getDate(String dateTimeString)
	{
		int date = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date dateTime = ft.parse(dateTimeString);
			date = dateTime.getDate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取时间 12-12 09:01
	 * 
	 * @param datetimeString
	 * @return
	 */
	public static String getMonDateTime(String datetimeString)
	{
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try
		{
			Date datetime = ft.parse(datetimeString);
			int month = datetime.getMonth() + 1;
			if(month<10){
				dtstr += "0"+(datetime.getMonth() + 1) + "-";
			}else{
				dtstr += (datetime.getMonth() + 1) + "-";
			}
			int data = datetime.getDate();
			if(data<10){
				dtstr += "0"+datetime.getDate() + " ";
			}else{
				dtstr += datetime.getDate() + " ";
			}
			int hour = datetime.getHours();
			int minute = datetime.getMinutes();
			String hourStr = "";
			String minuteStr = "";
			if(hour < 10){
				hourStr = "0"+hour;
			}else{
				hourStr = hour+"";
			}
			if(minute < 10){
				minuteStr = "0"+minute;
			}else{
				minuteStr = minute+"";
			}
			dtstr += hourStr + ":" + minuteStr;
		}
		catch (Exception e)
		{
			Log.e("getChinaDateStringByDateString", "获取格式时间异常" + e.getMessage());
		}
		return dtstr;
	}
	
	/**
	 * <b>Summary:获取 12:12 格式时间 </b>
	 * getHourAndMinTime
	 * @param datetimeString
	 * @return 
	 * @author huanbing.wang
	 * @date 2013-2-28
	 */
	public static String getHourAndMinTime(String datetimeString){
		String dtstr = "";
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try
		{
			Date datetime = ft.parse(datetimeString);
			int hour = datetime.getHours();
			int minute = datetime.getMinutes();
			String hourStr = "";
			String minuteStr = "";
			if(hour < 10){
				hourStr = "0"+hour;
			}else{
				hourStr = hour+"";
			}
			if(minute < 10){
				minuteStr = "0"+minute;
			}else{
				minuteStr = minute+"";
			}
			dtstr = hourStr + ":" + minuteStr;
		}
		catch (Exception e)
		{
			Log.e("getChinaDateStringByDateString", "获取格式时间异常" + e.getMessage());
		}
		return dtstr;
	}
}
