package com.gentlehealthcare.mobilecare.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.orders.DocOrdersActivity;
import com.gentlehealthcare.mobilecare.bean.orders.OrderListBean;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.swipe.SimpleSwipeListener;
import com.gentlehealthcare.mobilecare.swipe.SwipeLayout;
import com.gentlehealthcare.mobilecare.swipe.adapters.BaseSwipeAdapter;
import com.gentlehealthcare.mobilecare.tool.DateTool;
import com.gentlehealthcare.mobilecare.tool.StringTool;

import java.util.List;

/**
 * DocOrdersActivity适配器
 * 
 * @author zyy
 * @date 2016/02/29
 */
public class OrdersListAdapter extends BaseSwipeAdapter {

	private DocOrdersActivity context;
	private List<OrderListBean> list;
	private int lineCount;

	public OrdersListAdapter(DocOrdersActivity context, List<OrderListBean> list) {
		this.context = context;
		this.list = list;
	}

	public void notifyDataSetChanged(List<OrderListBean> list) {
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
				R.layout.item_orders_record, parent, false);
		final SwipeLayout swipeLayout = (SwipeLayout) convertView
				.findViewById(getSwipeLayoutResourceId(position));

		swipeLayout.addSwipeListener(new SimpleSwipeListener() {
			@Override
			public void onOpen(SwipeLayout layout) {
				GlobalConstant.isSwipeOpen = true;
			}
		});
		swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				GlobalConstant.ISBATCH = true;
				context.changeLayoutBatch();
				return false;
			}
		});
		return convertView;
	}

	@Override
	public void fillValues(final int position, View convertView) {
		View v_flag_isSelect = convertView.findViewById(R.id.flag_select);
		TextView tv_order_title = (TextView) convertView
				.findViewById(R.id.tv_order_title);
		TextView tv_order_context = (TextView) convertView
				.findViewById(R.id.tv_order_context);
		TextView tv_dosage = (TextView) convertView
				.findViewById(R.id.tv_dosage);
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		TextView btn_check = (TextView) convertView
				.findViewById(R.id.btn_check);
		if (list.get(position).isSelect()) {
			v_flag_isSelect.setVisibility(View.VISIBLE);
		}
		if ("1".equals(list.get(position).getStopAttr2())) {
			tv_order_title.setTextColor(Color.RED);
		} else {
			if ("1".equals(list.get(position).getStopAttr())) {
				tv_order_title.setTextColor(Color.RED);
				tv_order_context.setTextColor(Color.RED);
			} else {
				tv_order_title.setTextColor(context.getResources().getColor(R.color.org_blue_background));
				tv_order_context.setTextColor(context.getResources().getColor(R.color.black_text_content));
			}
		}
		tv_date.setText(DateTool.todayTomorryYesterday(list.get(position)
				.getStartTime()));
		tv_order_title.setText(StringTool.pieceSection(list.get(position)));
		String OrderContext = StringTool.toUnify(
				list.get(position).getOrderText()).replace("||", "\n");
		tv_order_context.setText(OrderContext);

		String orderDosage = list.get(position).getDosage()
				.replace("||", StringTool.cloneOrg("\n", lineCount));

		if (orderDosage == null || orderDosage == ""
				|| "null".equals(orderDosage)) {
			tv_dosage.setText("");
		} else {
			String nullToSpace = orderDosage.replace("null", "");
			tv_dosage.setText(nullToSpace);
		}
		if ("1".equals(list.get(position).getStopAttr())) {
			btn_check.setBackgroundResource(R.drawable.btn_check_cancel);
		} else {
			if (TextUtils.equals(list.get(position).getPerformStatus(), "0")) {
				btn_check
						.setBackgroundResource(R.drawable.btn_check_off_holo_light);
			} else if (TextUtils.equals(list.get(position).getPerformStatus(),
					"1")) {
				btn_check.setBackgroundResource(R.drawable.btn_exceting);
			} else if (TextUtils.equals(list.get(position).getPerformStatus(),
					"9")) {
				btn_check.setBackgroundResource(R.drawable.btn_finish);
			} else if (TextUtils.equals(list.get(position).getPerformStatus(),
					"-1")) {
				btn_check.setBackgroundResource(R.drawable.btn_exception);
			}
		}

		if (GlobalConstant.ISBATCH) {
			if (list.get(position).isBatch()) {
				if (TextUtils
						.equals(list.get(position).getPerformStatus(), "0")) {
					btn_check
							.setBackgroundResource(R.color.org_blue_background);
				}
			} else {
				if (TextUtils
						.equals(list.get(position).getPerformStatus(), "0")) {
					btn_check
							.setBackgroundResource(R.drawable.btn_check_off_holo_light);
				} else if (TextUtils.equals(list.get(position)
						.getPerformStatus(), "1")) {
					btn_check.setBackgroundResource(R.drawable.btn_exceting);
				} else if (TextUtils.equals(list.get(position)
						.getPerformStatus(), "9")) {
					btn_check.setBackgroundResource(R.drawable.btn_finish);
				} else if (TextUtils.equals(list.get(position)
						.getPerformStatus(), "-1")) {
					btn_check.setBackgroundResource(R.drawable.btn_exception);
				}
			}
		}

		LinearLayout ll_btn = (LinearLayout) convertView
				.findViewById(R.id.ll_btn);
		ll_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!GlobalConstant.ISBATCH) {
					context.executeOrder(list.get(position));
				} else {
					if (list.get(position).isBatch()) {
						GlobalConstant.countOfBatchOrders -= 1;
						list.get(position).setBatch(false);
					} else {
						GlobalConstant.countOfBatchOrders += 1;
						list.get(position).setBatch(true);
					}
					notifyDataSetChanged();
				}
			}
		});
		LinearLayout ll_tar = (LinearLayout) convertView
				.findViewById(R.id.ll_tar);
		ll_tar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				context.toImplementationRecordAct(list.get(position));
			}
		});

	}

}
