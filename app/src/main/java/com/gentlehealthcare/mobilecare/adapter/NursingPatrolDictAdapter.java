package com.gentlehealthcare.mobilecare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.bean.orders.DictCommonBean;

import java.util.List;

import static com.gentlehealthcare.mobilecare.R.id.tv_patrol_item;

/**
 * @author zyy
 * @Description: 护理巡视字典适配器
 * @date 2015年3月11日 上午9:21:38
 */
public class NursingPatrolDictAdapter extends BaseAdapter {

    private Context context;
    private List<DictCommonBean> dictCommonBeen;


    public NursingPatrolDictAdapter(Context context, List<DictCommonBean> dictCommonBeen) {

        super();
        this.context = context;
        this.dictCommonBeen = dictCommonBeen;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return (dictCommonBeen.size() == 0) ? 0 : dictCommonBeen.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dictCommonBeen.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_patrol_text, null);
            viewHolder.tv_text = (TextView) convertView.findViewById(tv_patrol_item);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_text.setText(dictCommonBeen.get(position).getItemName());
        if (dictCommonBeen.get(position).isSelect()) {
            viewHolder.tv_text.setBackgroundResource(R.drawable.nursing_patrol_yes);
        } else {
            viewHolder.tv_text.setBackgroundResource(R.drawable.nursing_patrol_no);

        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_text;
    }
}
