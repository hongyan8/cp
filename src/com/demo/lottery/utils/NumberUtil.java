package com.demo.lottery.utils;

/**
 * @Description 小于10自动补零
 * @author wanru.chen
 * @date 2014-1-13
 */
public class NumberUtil
{
	public static String plusZero(int num)
	{
		String numTemp = String.valueOf(num);
		if (num < 10)
		{
			numTemp = "0" + num;
		}
		return numTemp;
	}

	/**
	 * 获得注数
	 * 
	 * @param redSelectNum
	 * @param redNeedNum
	 * @param blueSelectNum
	 * @param blueNeedNum
	 * @return
	 */
	public static int getZhuNumber(int redSelectNum, int redNeedNum, int blueSelectNum, int blueNeedNum)
	{
		int red = NumberUtil.combination(redSelectNum, redNeedNum);
		int blue = NumberUtil.combination(blueSelectNum, blueNeedNum);
		return red * blue;
	}

	public static int combination(int selectNum, int needNum)
	{
		int a = 1;
		int b = 1;
		if (selectNum >= needNum)// 8 : 5
		{
			for (int i = 1; i < needNum; i++)
			{
				if (i == 1)
				{
					a = a * (selectNum - i + 1) * (selectNum - i);
					b = b * (needNum - i + 1) * (needNum - i);
				}
				else
				{
					a = a * (selectNum - i);
					b = b * (needNum - i);
				}
			}
		}
		else
		{
			return 0;
		}

		return a / b;
	}
}
