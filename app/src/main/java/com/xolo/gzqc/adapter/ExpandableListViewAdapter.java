package com.xolo.gzqc.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CoProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ExpandableListViewAdapter implements ExpandableListAdapter {

    List<CoProject> coProjects;
    Context context;

    public ExpandableListViewAdapter(Context context, List<CoProject> coProjects) {
        this.coProjects = coProjects;
        this.context = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return coProjects.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(coProjects.get(groupPosition).getCoAccessories()==null){
            return 0;
        }
        return coProjects.get(groupPosition).getCoAccessories().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_co_project_list, null);
        if (groupPosition == 0) {
            ((RelativeLayout) convertView.findViewById(R.id.show_child_layout)).setVisibility(View.INVISIBLE);
            ((TextView) convertView.findViewById(R.id.project_name)).getPaint().setFakeBoldText(true);
            ((TextView) convertView.findViewById(R.id.project_price)).getPaint().setFakeBoldText(true);
            ((TextView) convertView.findViewById(R.id.project_name)).setTextColor(Color.parseColor("#3a3a3a"));
            ((TextView) convertView.findViewById(R.id.project_price)).setTextColor(Color.parseColor("#3a3a3a"));
        }
        ((TextView) convertView.findViewById(R.id.project_name)).setText(coProjects.get(groupPosition).getItemt_name());
        ((TextView) convertView.findViewById(R.id.project_price)).setText(coProjects.get(groupPosition).getWorkamt());
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_co_project_child, null);
        if(childPosition==0){
            ((TextView) convertView.findViewById(R.id.accessories_name)).getPaint().setFakeBoldText(true);
            ((TextView) convertView.findViewById(R.id.accessories_util_price)).getPaint().setFakeBoldText(true);
            ((TextView) convertView.findViewById(R.id.accessories_num)).getPaint().setFakeBoldText(true);
            ((TextView) convertView.findViewById(R.id.accessories_price)).getPaint().setFakeBoldText(true);

            ((TextView) convertView.findViewById(R.id.accessories_name)).setTextColor(Color.BLACK);
            ((TextView) convertView.findViewById(R.id.accessories_util_price)).setTextColor(Color.BLACK);
            ((TextView) convertView.findViewById(R.id.accessories_num)).setTextColor(Color.BLACK);
            ((TextView) convertView.findViewById(R.id.accessories_price)).setTextColor(Color.BLACK);
        }
        ((TextView) convertView.findViewById(R.id.accessories_name))
                .setText(coProjects.get(groupPosition).getCoAccessories().get(childPosition).getParts_name());
        ((TextView) convertView.findViewById(R.id.accessories_util_price))
                .setText(coProjects.get(groupPosition).getCoAccessories().get(childPosition).getSaleprice());

        ((TextView) convertView.findViewById(R.id.accessories_num))
                .setText(coProjects.get(groupPosition).getCoAccessories().get(childPosition).getQty());

        ((TextView) convertView.findViewById(R.id.accessories_price))
                .setText(coProjects.get(groupPosition).getCoAccessories().get(childPosition).getTotalprice());
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
