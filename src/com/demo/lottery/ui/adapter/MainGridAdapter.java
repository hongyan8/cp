package com.demo.lottery.ui.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.lottery.R;
import com.demo.lottery.utils.StringUtil;

public class MainGridAdapter extends BaseAdapter
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

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            列表类
	 */
	public MainGridAdapter(Context context)
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
		// 先确保view存在
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.fragment_main_grid_item, null);
		}

		// 获得holder
		MainGridHolder holder = null;
		Object tag = convertView.getTag();
		if (tag == null)
		{
			holder = new MainGridHolder(convertView);
		}
		else
		{
			holder = (MainGridHolder) tag;
		}
		holder.setData(map);
		// 设置tag
		convertView.setTag(holder);

		return convertView;
	}

	private class MainGridHolder
	{
		ImageView imageView;

		TextView textTitle;

		TextView textTotal;

		TextView textDate;

		private MainGridHolder(View convertView)
		{
			imageView = (ImageView) convertView.findViewById(R.id.img_main_icon);
			textTitle = (TextView) convertView.findViewById(R.id.text_title);
			textTotal = (TextView) convertView.findViewById(R.id.text_total);
			textDate = (TextView) convertView.findViewById(R.id.text_date);
		}

		private void setData(Map<String, Object> map)
		{
			textTitle.setText(StringUtil.Object2String(map.get("TITLE")));
			textTotal.setText(StringUtil.Object2String(map.get("TOTAL")));
			textDate.setText(StringUtil.Object2String(map.get("DATE")));
			if ("1".equals(map.get("ID")))
				imageView.setBackgroundResource(R.drawable.logo_dlt);
			else
				imageView.setBackgroundResource(R.drawable.logo_qxc);
		}
	}
}
