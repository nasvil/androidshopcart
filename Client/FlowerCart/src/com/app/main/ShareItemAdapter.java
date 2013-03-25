package com.app.main;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.main.R;
import com.app.model.ShareItem;

public class ShareItemAdapter extends BaseAdapter {
	private Context context;
	ArrayList<ShareItem> values;

	public ShareItemAdapter(Context context, ArrayList<ShareItem> values) {
		this.context = context;
		this.values = values;
	}

	String simnumber = "";

	public View getView(int position, View convertView, ViewGroup parent) {
		if (position < 0 && values != null && position >= values.size()) {
			return null;
		}

		ShareItem item = values.get(position);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.share_row_item, parent, false);
		TextView tvItem = (TextView) rowView.findViewById(R.id.tvItem);
		ImageView imIcon = (ImageView) rowView.findViewById(R.id.imIcon);

		tvItem.setText(item.getItemName());
		imIcon.setImageResource(item.getImageID());

		return rowView;
	}

	public int getCount() {
		if (values == null) {
			return 0;
		}
		return values.size();
	}

	public Object getItem(int position) {
		if (values == null || position < 0 || position > values.size()) {
			return null;
		}
		return values.get(position);
	}



	public long getItemId(int position) {
		if (values == null || position < 0 || position > values.size()) {
			return -1;
		}
		return position;
	}
	
	public String getItemKey(int position) {
		if (values == null || position < 0 || position > values.size()) {
			return "";
		}
		return values.get(position).getItemKey();
	}
}
