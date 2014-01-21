package com.demo.lottery.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.lottery.R;
import com.demo.lottery.ui.views.MyGridView;
import com.demo.lottery.utils.StringUtil;

public class QxcListAdapter extends BaseAdapter
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

	private List<Map<String, Object>> mapList;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            列表类
	 */
	public QxcListAdapter(Context context)
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
			convertView = inflater.inflate(R.layout.layout_qxc_list_item, null);
		}

		// 获得holder
		QxcListHolder holder = null;
		Object tag = convertView.getTag();
		if (tag == null)
		{
			holder = new QxcListHolder(convertView);
		}
		else
		{
			holder = (QxcListHolder) tag;
		}
		holder.setData(map);
		// 设置tag
		convertView.setTag(holder);

		return convertView;
	}

	private class QxcListHolder
	{

		TextView textNo;

		MyGridView gridview;

		NumberAdapter adapter;

		private QxcListHolder(View convertView)
		{
			textNo = (TextView) convertView.findViewById(R.id.text_no);
			gridview = (MyGridView) convertView.findViewById(R.id.grid_qxc);

			adapter = new NumberAdapter(context);
		}

		private void setData(Map<String, Object> map)
		{
			mapList = new ArrayList<Map<String, Object>>();
			textNo.setText(StringUtil.Object2String(map.get("NO")));
			mapList = (List<Map<String, Object>>) map.get("num_listmap");

			gridview.setNumColumns(5);
			adapter.setDataList(mapList);
			gridview.setAdapter(adapter);
		}
	}

	public List<Map<String, Object>> getMapList()
	{
		return mapList;
	}

	public void setMapList(List<Map<String, Object>> mapList)
	{
		this.mapList = mapList;
	}

}
