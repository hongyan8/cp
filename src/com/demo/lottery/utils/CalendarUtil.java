package com.demo.lottery.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * description 日期工具类 author yin.liu date 2012-2-12
 */
public class CalendarUtil {

	/**
	 * 根据某天在一周中第几天返回中文模式的星期几
	 * 
	 * @param day
	 * endby - 2012 - 4-26
	 */
	public static String getChineseStringByDayOfWeek(int day) {
		String dayString = "";
		switch (day) {
		case 0:
			dayString = "一";
			break;
		case 1:
			dayString = "二";
			break;
		case 2:
			dayString = "三";
			break;
		case 3:
			dayString = "四";
			break;
		case 4:
			dayString = "五";
			break;
		case 5:
			dayString = "六";
			break;
		case 6:
			dayString = "日";
			break;
		default:
			break;
		}
		return dayString;
	}
	public static String getChineseStringByDayOfWeeker(int day)
	{
		String dayString = "";
		switch (day) {
		case 1:
			dayString = "日";
			break;
		case 2:
			dayString = "一";
			break;
		case 3:
			dayString = "二";
			break;
		case 4:
			dayString = "三";
			break;
		case 5:
			dayString = "四";
//			System.out.println(dayString);
			break;
		case 6:
			dayString = "五";
			break;
		case 7:
			dayString = "六";
			break;
		default:
			break;
		}
		return dayString;
	}

	/**
	 * 根据日期calendar拼接返回中文模式日期格式 2012年2月12日 星期日
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getStringByCalendar(Calendar calendar) {
		String daytitleStr = calendar.get(Calendar.YEAR)
				+ "年"
				+ (calendar.get(Calendar.MONTH) + 1)
				+ "月"
				+ calendar.get(Calendar.DATE)
				+ "日  星期"
				+ getChineseStringByDayOfWeeker(calendar
						.get(Calendar.DAY_OF_WEEK));

		return daytitleStr;
	}

	/**
	 * 根据日期返回格式如2012-2-12周日
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getStringFormat_ByCalendar(Calendar calendar) {
		String returnStr = calendar.get(Calendar.YEAR)
				+ "-"
				+ (calendar.get(Calendar.MONTH) + 1)
				+ "-"
				+ calendar.get(Calendar.DATE)
				+ "周"
				+ getChineseStringByDayOfWeek(calendar
						.get(Calendar.DAY_OF_WEEK));

		return returnStr;
	}

	/**
	 * 根据日期返回格式如2012-2-12
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getStringFormatByCalendar(Calendar calendar) {
		String returnStr = calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DATE);

		return returnStr;
	}

	
//	public static String getStringFormatByCalendarYearofMouth(Calendar calendar) {
//		String returnStr = calendar.get(Calendar.YEAR) + "-"
//				+ (calendar.get(Calendar.MONTH) + 1) + "-"
//				+ calendar.get(Calendar.DATE);
//
//		return returnStr;
//	}
	/**
	 * 根据日期获取时间，格式19:19
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getStringTimeByCalendar(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY)
				+ ":"
				+ (calendar.get(Calendar.MINUTE) < 10 ? "0"
						+ calendar.get(Calendar.MINUTE) : calendar
						.get(Calendar.MINUTE) + "");
	}

	/**
	 * 根据日期类型获取日期和时间的字符串，格式2012-02-01 01:01 
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getStringDateAndTimeByCalendar(Calendar calendar) {
		return calendar.get(Calendar.YEAR)
				+ "-"
				+ ((calendar.get(Calendar.MONTH) + 1)<10?("0"+(calendar.get(Calendar.MONTH) + 1)):(calendar.get(Calendar.MONTH) + 1))
				+ "-"
				+ (calendar.get(Calendar.DATE)<10?("0"+calendar.get(Calendar.DATE)):calendar.get(Calendar.DATE))
				+ " "
				+ (calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0"
						+ calendar.get(Calendar.HOUR_OF_DAY) : calendar
						.get(Calendar.HOUR_OF_DAY) + "")
				+ ":"
				+ (calendar.get(Calendar.MINUTE) < 10 ? "0"
						+ calendar.get(Calendar.MINUTE) : calendar
						.get(Calendar.MINUTE) + "");
	}
	
	/**
	 * 根据日期返回分解后所要的值
	 * @param showCalendarString 要展示的日期
	 * @param calendarString 2012-01-01 01:01
	 * @param type [开始小时beginHour，开始分钟beginMin，结束小时endHour，结束分钟endMin]
	 * @return
	 */
	public static String getStringTimeByCalendarString(String showCalendarString, String calendarString,String type){
		/*showCalendarString = "2012-01-01 00:00";
		calendarString ="2012-01-01 01:01";*/
		String returnValue="";
		//显示的日期
		String[] showcalendarArr = showCalendarString.split(" ");
		String[] showdateArr = showcalendarArr[0].split("-");
		//转换的日期
		String[] calendarArr = calendarString.split(" ");
		String[] dateArr = calendarArr[0].split("-");
		String[] timeArr = calendarArr[1].split(":");
		boolean isShowDateTime = showdateArr[0].equals(dateArr[0]) && showdateArr[1].equals(dateArr[1]) && showdateArr[2].equals(dateArr[2]);//是否该年该月该日
		if("beginHour".equals(type)){
			if(isShowDateTime){//是该年该月该日
				returnValue = timeArr[0].startsWith("0") && timeArr[0].length()>1 ? timeArr[0].substring(1):timeArr[0]; //如果是小于10的就只取一位
			}else{
				returnValue = "0"; 
			}
		}else if("beginMin".equals(type)){
			if(isShowDateTime){//是该年该月该日
				returnValue = timeArr[1].startsWith("0") && timeArr[1].length()>1 ? timeArr[1].substring(1):timeArr[1]; //如果是小于10的就只取一位
			}else{
				returnValue = "0"; 
			}
		}else if("endHour".equals(type)){
			if(isShowDateTime){//是该年该月该日
				returnValue = timeArr[0].startsWith("0") && timeArr[0].length()>1 ? timeArr[0].substring(1):timeArr[0]; //如果是小于10的就只取一位
			}else{
				returnValue = "23"; 
			}
		}else if("endMin".equals(type)){
			if(isShowDateTime){//是该年该月该日
				returnValue = timeArr[1].startsWith("0") && timeArr[1].length()>1 ? timeArr[1].substring(1):timeArr[1]; //如果是小于10的就只取一位
			}else{
				returnValue = "60"; 
			}
		}
		return returnValue;
	}

	/**
	 * 根据日期获取本周的日期集合
	 * 
	 * @param calendar
	 * @return
	 */
	public static List<Calendar> getCurrentWeekCalendarList(Calendar calendar) {
		List<Calendar> list = new ArrayList<Calendar>();
		for (int i = 1; i < 8; i++) {
			Calendar calendartemp = Calendar.getInstance();
			calendartemp.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
			calendartemp.add(Calendar.DATE,
					i - calendartemp.get(Calendar.DAY_OF_WEEK));
			list.add(calendartemp);
		}
		return list;
	}
	
	/**
	 * 根据日期获取本周的日期集合
	 * 
	 * @param calendar
	 * @return
	 * data 1-7
	 */
	public static List<Calendar> getCurrentWeekCalendar(Calendar calendar) {
		List<Calendar> list = new ArrayList<Calendar>();
		for (int i = 2; i < 9; i++) {
			Calendar calendartemp = Calendar.getInstance();
			calendartemp.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
			calendartemp.add(Calendar.DATE,
					i - calendartemp.get(Calendar.DAY_OF_WEEK));
			list.add(calendartemp);
		}
		return list;
	}
	
	/**
	*<b>Summary: </b>
	* getCurrentDate(获取当前日期)
	* @return
	 */
	public static String getCurrentDate(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
	}
	

}
