package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.bean.LookBean;

import java.util.List;

/**
 * @author Zyy
 * @date 2015-9-17下午3:34:19
 * @category 查8对观察界面适配器
 */
public class GridViewLookAdapter extends BaseAdapter {
    private Context context;
    private List<LookBean> lookBeanList;

    public GridViewLookAdapter(Context context, List<LookBean> lookBeanList) {
        this.context = context;
        this.lookBeanList = lookBeanList;
    }

    @Override
    public int getCount() {
        return lookBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return lookBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_look, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_item_look_ = (TextView) convertView.findViewById(R.id.tv_item_look_);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LookBean lookBean = lookBeanList.get(position);
        viewHolder.tv_item_look_.setText(lookBean.getItemName());

        if (lookBean.isSelected()) {
            viewHolder.tv_item_look_.setBackgroundResource(R.drawable.skin_white_gay_choosen);
        } else {
            viewHolder.tv_item_look_.setBackgroundResource(R.drawable.skin_white_gay);
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_item_look_;
    }

}
