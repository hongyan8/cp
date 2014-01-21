package com.demo.lottery.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.demo.lottery.R;
import com.demo.lottery.utils.StringUtil;

public class NumberAdapter extends BaseAdapter
{
	/**
	 * Layout填充器
	 */
	private LayoutInflater inflater = null;

	/**
	 * 存储列表实体类的list
	 */
	private List<Map<String, Object>> list = null;

	/**
	 * 列表类
	 */
	@SuppressWarnings("unused")
	private Context context;

	// private Map<String, Object> mapForActivity;

	private List<Integer> intLists = new ArrayList<Integer>();

	private List<Integer> intBlueLists = new ArrayList<Integer>();

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            列表类
	 */
	public NumberAdapter(Context context)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	public void setDataList(List<Map<String, Object>> list)
	{
		this.list = list;
	}

	public List<Map<String, Object>> getList()
	{
		return list;
	}

	@Override
	public int getCount()
	{
		return list == null ? 0 : list.size();
		// return 5;
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final Map<String, Object> map = list.get(position);
		// this.mapForActivity = map;
		// 先确保view存在
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.layout_dlt_grid_item, null);
		}

		// 获得holder
		GridHolder holder = null;
		Object tag = convertView.getTag();
		if (tag == null)
		{
			holder = new GridHolder(convertView, StringUtil.Object2String(map.get("type")));

		}
		else
		{
			holder = (GridHolder) tag;
		}
		holder.setData(map, convertView);
		// 设置tag
		convertView.setTag(holder);

		return convertView;
	}

	private class GridHolder
	{
		CheckBox item;

		Map<String, Object> map;

		String type;

		private void setData(Map<String, Object> map, View convertView)
		{
			this.map = map;
			item.setText(map.get("num").toString());

			if ("red".equals(type))
			{
				if ((Boolean) map.get("isChoose"))
				{
					item.setChecked(true);
					convertView.findViewById(R.id.item).setBackgroundResource(R.drawable.betball_red);
				}
				else
				{
					item.setChecked(false);
					convertView.findViewById(R.id.item).setBackgroundResource(R.drawable.betball_red_default);
				}
			}
			else
			{
				if ((Boolean) map.get("isChoose"))
				{
					item.setChecked(true);
					convertView.findViewById(R.id.item).setBackgroundResource(R.drawable.betball_blue);
				}
				else
				{
					item.setChecked(false);
					convertView.findViewById(R.id.item).setBackgroundResource(R.drawable.betball_blue_default);
				}
			}

		}

		public GridHolder(View convertView, String type)
		{
			this.type = type;
			item = (CheckBox) convertView.findViewById(R.id.item);
			if ("red".equals(type))
				item.setTextAppearance(context, R.style.red_num_check_style);
			else
				item.setTextAppearance(context, R.style.blue_num_check_style);

			item.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (isChecked)
					{
						map.put("isChoose", true);
						if ("red".equals(map.get("type")))
						{
							intLists.add((Integer) map.get("no"));
							buttonView.setBackgroundResource(R.drawable.betball_red);
						}
						else
						{
							intBlueLists.add((Integer) map.get("no"));
							buttonView.setBackgroundResource(R.drawable.betball_blue);
						}
					}
					else
					{
						map.put("isChoose", false);
						if ("red".equals(map.get("type")))
						{
							// if(intLists.contains((Integer) map.get("no")))
							for (int n = 0; n < intLists.size(); n++)
							{
								int no = intLists.get(n);
								if (no == (Integer) map.get("no"))
								{
									intLists.remove(n);
								}
							}
							// intLists.remove((Integer) map.get("no"));
							buttonView.setBackgroundResource(R.drawable.betball_red_default);
						}
						else
						{
							for (int n = 0; n < intBlueLists.size(); n++)
							{
								int no = intBlueLists.get(n);
								if (no == (Integer) map.get("no"))
								{
									intBlueLists.remove(n);
								}
							}
							// intBlueLists.remove((Integer) map.get("no"));
							buttonView.setBackgroundResource(R.drawable.betball_blue_default);
						}
					}
				}
			});
		}
	}

	public List<Integer> getIntLists()
	{
		return intLists;
	}

	public void setIntLists(List<Integer> intLists)
	{
		this.intLists = intLists;
	}

	public void setIntBlueLists(List<Integer> intBlueLists)
	{
		this.intBlueLists = intBlueLists;
	}

	public List<Integer> getIntBlueLists()
	{
		return intBlueLists;
	}

}
