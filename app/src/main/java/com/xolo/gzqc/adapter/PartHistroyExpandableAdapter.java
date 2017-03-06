package com.xolo.gzqc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.postJson.Offer;
import com.xolo.gzqc.bean.postJson.Part;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/13.
 */
public class PartHistroyExpandableAdapter extends BaseExpandableListAdapter {

    private final LayoutInflater inflater;
    private List<Offer> list_offer = new ArrayList<>();
    private Context context;


    public PartHistroyExpandableAdapter(List<Offer> list_offer, Context context) {
        this.list_offer = list_offer;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return list_offer.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list_offer.get(groupPosition).getRepairlist().size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_offer, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Offer offer = list_offer.get(groupPosition);
        holder.item1.setText(offer.getItemt_name());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolderChild  holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_part_2, parent, false);
            holder = new ViewHolderChild(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolderChild) convertView.getTag();
        }

        List<Part> repairlists = list_offer.get(groupPosition).getRepairlist();
        Part repairlist = repairlists.get(childPosition);

        holder.item1.setText(repairlist.getParts_name() + "   "  + repairlist.getQty() + "("+repairlist.getQty_unit()+")");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return false;
    }



    static class ViewHolder {
        @BindView(R.id.item1)
        TextView item1;
        @BindView(R.id.item3)
        TextView item3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static  class ViewHolderChild {
        @BindView(R.id.item1)
        TextView item1;
        @BindView(R.id.item3)
        TextView item3;
        @BindView(R.id.item2)
        CheckBox item2;
        @BindView(R.id.item4)
        TextView item4;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
