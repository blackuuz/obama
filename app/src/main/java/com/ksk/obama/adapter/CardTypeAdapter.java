package com.ksk.obama.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.model.CardTypeSelect;

import java.util.List;


public class CardTypeAdapter extends BaseAdapter {
	private Context context;
	private List<CardTypeSelect.ResultDataBean> data;
	private LayoutInflater inflater;

	public CardTypeAdapter(Context context, List<CardTypeSelect.ResultDataBean> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Object getItem(int position) {

		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		CardTypeSelect.ResultDataBean cardTypeSelect = data.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_cardtype, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.textView1);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setText(cardTypeSelect.getC_ClassName());
	
		return convertView;

	}

	class ViewHolder {
		TextView text;
	}
}
