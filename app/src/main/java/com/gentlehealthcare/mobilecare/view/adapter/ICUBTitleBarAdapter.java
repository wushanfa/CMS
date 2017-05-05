package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzw on 2015/11/6.
 */
public class ICUBTitleBarAdapter extends BaseAdapter {

    private List<String> mytitle = new ArrayList<String>();
    private Context context;
    private int num;

    public ICUBTitleBarAdapter(Context context, List<String> mytitle, int num) {
        this.mytitle = mytitle;
        this.context = context;
        this.num = num;
    }

    @Override
    public int getCount() {
        return mytitle.size();
    }


    @Override
    public Object getItem(int i) {
        return mytitle.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewholder = null;
        if (view == null) {
            viewholder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.icu_b_title_bar_layout, null);
            viewholder.tv_icu_title_bar = (TextView) view.findViewById(R.id.tv_icu_title_bar);
            viewholder.blue_line = (TextView) view.findViewById(R.id.blue_line);
            viewholder.icu_title_bar = (RelativeLayout) view.findViewById(R.id.icu_title_bar);
            view.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) view.getTag();
        }
        viewholder.tv_icu_title_bar.setText(mytitle.get(i));

        if (num==i) {
            viewholder.tv_icu_title_bar.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            viewholder.tv_icu_title_bar.setTextColor(context.getResources().getColor(R.color.black));
            viewholder.blue_line.setVisibility(View.VISIBLE);
        } else {
            viewholder.tv_icu_title_bar.setTextColor(context.getResources().getColor(R.color.grey_700));
            viewholder.blue_line.setVisibility(View.GONE);
        }
        return view;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private class ViewHolder {
        private TextView tv_icu_title_bar, blue_line;
        private RelativeLayout icu_title_bar;
    }


}
