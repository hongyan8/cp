package com.demo.lottery.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RandomNumberUtil
{
	/**
	 * @param part1
	 *            要抽出第一部分的个数，也就是抽的6个红球
	 * @param part2
	 *            要抽出第二部分的个数，也就是抽的1个蓝球
	 * @param arrange1
	 *            红球的抽取范围
	 * @param arrange2
	 *            蓝球的抽取范围
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getRandom(int part1, int part2, int arrange1, int arrange2)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		Random r = new Random();

		List<Integer> no1 = new ArrayList<Integer>();
		while (no1.size() < part1)
		{
			int temp = r.nextInt(arrange1);
			if (temp != 0 && !no1.contains(temp))
			{
				no1.add(temp);
			}
		}

		List<Integer> no2 = new ArrayList<Integer>();
		while (no2.size() < part2)
		{
			int temp = r.nextInt(arrange2);
			if (temp != 0 && !no2.contains(temp))
			{
				no2.add(temp);
			}
		}

		result.put("no1", no1);
		result.put("no2", no2);

		return result;
	}

	@SuppressWarnings("rawtypes")
	public static List<Integer> getRandom(int part1, int arrange1)
	{
		Random r = new Random();

		List<Integer> no1 = new ArrayList<Integer>();
		while (no1.size() <= part1)
		{
			int temp = r.nextInt(arrange1);
			no1.add(temp);
		}
		return no1;
	}

	public static void removeDuplicateWithOrder(List list)
	{
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		list.clear();
		list.addAll(newList);
	}

}
