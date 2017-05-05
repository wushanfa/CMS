package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.net.bean.PlaceStatusBean;

import java.util.List;

/**
 * Created by ouyang on 2015/6/10.
 */
public class InjectionSiteTxtAdapter extends BaseAdapter {
    private List<PlaceStatusBean> sites;
    private Context context;

    public String getSiteNo() {
        return siteNo;
    }

    private String siteNo;


    public InjectionSiteTxtAdapter(List<PlaceStatusBean> sites, Context context) {
        this.sites = sites;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sites.size();
    }

    @Override
    public Object getItem(int position) {
        return sites.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

//    public void notifyBySiteNo(String siteNo){
//        this.siteNo=siteNo;
//        notifyDataSetInvalidated();
//    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.fra_injectionsite_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_text= (TextView) convertView.findViewById(R.id.tv_injectionsite_text);
            viewHolder.tv_text.setTextColor(R.color.blue_400);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        String siteNo=sites.get(position).getSiteNo();
        viewHolder.tv_text.setText(sites.get(position).getItemNo());
        if (sites.get(position).getStatus().equals("-99")){
            viewHolder.tv_text.setBackgroundResource(R.color.temp_jinda);
        }else if (sites.get(position).getStatus().equals("-98")){
            viewHolder.tv_text.setBackgroundResource(R.color.temp_jinda);
        }else if (sites.get(position).getStatus().equals("1")){
            viewHolder.tv_text.setBackgroundResource(R.color.temp_zhusheguo);
        }else if (sites.get(position).getStatus().equals("0")){
            viewHolder.tv_text.setBackgroundResource(R.color.white);
        }else{
            viewHolder.tv_text.setBackgroundResource(R.color.white);
        }


        if (this.siteNo!=null&&this.siteNo.equals(siteNo)){
            viewHolder.tv_text.setBackgroundResource(R.color.temp_bencida);
        }
        viewHolder.tv_text.setTextColor(Color.BLACK);

        return convertView;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    private class ViewHolder{
        private TextView tv_text;
    }
}
