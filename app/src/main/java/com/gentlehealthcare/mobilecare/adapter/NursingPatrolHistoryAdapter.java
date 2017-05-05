package com.gentlehealthcare.mobilecare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.bean.NursingHistoryBean;

import java.util.List;

/**
 * @author zyy
 * @Description: 护理巡视历史记录适配器
 * @date 2015年3月11日 上午9:21:38
 */
public class NursingPatrolHistoryAdapter extends BaseAdapter {

    private Context context;
    private List<NursingHistoryBean> nursingHistoryBeen;


    public NursingPatrolHistoryAdapter(Context context, List<NursingHistoryBean> dictCommonBeen) {

        super();
        this.context = context;
        this.nursingHistoryBeen = dictCommonBeen;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return (nursingHistoryBeen.size() == 0) ? 0 : nursingHistoryBeen.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return nursingHistoryBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nursing_history, null);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_nursing_name = (TextView) convertView.findViewById(R.id.tv_nursing_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_date.setText(nursingHistoryBeen.get(position).getLogTime());
        viewHolder.tv_content.setText(nursingHistoryBeen.get(position).getPerformDesc());
        viewHolder.tv_nursing_name.setText("操作护士:"+nursingHistoryBeen.get(position).getNurseInOperate());

        return convertView;
    }

    class ViewHolder {
        TextView tv_date;
        TextView tv_content;
        TextView tv_nursing_name;
    }
}
