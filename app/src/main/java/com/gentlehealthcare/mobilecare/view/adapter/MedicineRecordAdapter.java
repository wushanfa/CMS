package com.gentlehealthcare.mobilecare.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gentlehealthcare.mobilecare.R;

import java.util.List;
import java.util.Map;

/**
 * Created by ouyang on 2015/3/18.
 */
public class MedicineRecordAdapter extends BaseExpandableListAdapter {

    private Context context;

    private String[] group;

    private List<List<Map<String, String>>> children;

    public MedicineRecordAdapter(Context context, String[] group, List<List<Map<String, String>>> children) {
        super();
        this.context = context;
        this.group = group;
        this.children = children;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return children.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return children.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }



    @Override
    public int getChildTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_head, null);

            viewHolder = new ViewHolder();

            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_view_name);

            viewHolder.tvSymbol = (TextView) convertView.findViewById(R.id.tv_symbol);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        String string = group[groupPosition];

        viewHolder.tvName.setText(string);

        if (isExpanded) {

            viewHolder.tvSymbol.setBackgroundResource(R.drawable.skin_down);
        } else {

            viewHolder.tvSymbol.setBackgroundResource(R.drawable.skin_right);
        }

        return convertView;

    }

    class ViewHolder {

        TextView tvName;

        TextView tvSymbol;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {



                TextView textView;

                if (convertView == null) {

                    convertView = LayoutInflater.from(context).inflate(R.layout.item_expand_type_one, null);

                    textView = (TextView) convertView.findViewById(R.id.tv_details);

                    convertView.setTag(textView);
                } else {

                    textView = (TextView) convertView.getTag();
                }

                String string = children.get(groupPosition).get(childPosition).get("message");

                textView.setText(string);



        return convertView;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {


        }
    };

    class ViewHolderChild {

        TextView textView;

        ImageView imageView;

        Button button;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}
