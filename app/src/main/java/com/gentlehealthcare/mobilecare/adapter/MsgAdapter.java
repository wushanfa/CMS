package com.gentlehealthcare.mobilecare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.msg.MsgAct;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.net.bean.TipBean;
import com.gentlehealthcare.mobilecare.swipe.SimpleSwipeListener;
import com.gentlehealthcare.mobilecare.swipe.SwipeLayout;
import com.gentlehealthcare.mobilecare.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

/**
 * MsgAct适配器
 * 
 * @author zyy
 * @date 2016/08/31
 */
public class MsgAdapter extends BaseSwipeAdapter {

	private MsgAct context;
	private List<TipBean> list;

	public MsgAdapter(MsgAct context, List<TipBean> list) {
		this.context = context;
		this.list = list;
	}

	public void notifyDataSetChanged(List<TipBean> list) {
		this.list = list;
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.swipe;
	}

	@Override
	public View generateView(int position, ViewGroup parent) {
		View convertView = LayoutInflater.from(context).inflate(
				R.layout.item_orders_msg, parent, false);
		final SwipeLayout swipeLayout = (SwipeLayout) convertView
				.findViewById(getSwipeLayoutResourceId(position));

		swipeLayout.addSwipeListener(new SimpleSwipeListener() {
			@Override
			public void onOpen(SwipeLayout layout) {
				GlobalConstant.isSwipeOpenMsg = true;
			}
		});
		return convertView;
	}

	@Override
	public void fillValues(final int position, View convertView) {
		TextView tv_order_context = (TextView) convertView
				.findViewById(R.id.tv_order_context);
		String msg=list.get(position).getMessageContent();
		if(msg.contains("||")){
			msg=msg.replace("||","\n");
		}
	    tv_order_context.setText(msg);
	}

}
