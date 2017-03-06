package com.xolo.gzqc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.App;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.WordBeachDealed;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/12/7.
 */
public class WorkbenchExAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater layoutInflater;
    private Context mtx;
    private List<WordBeachDealed> list;

    public WorkbenchExAdapter(BaseActivity mContent, List<WordBeachDealed> list_dealed) {
        mtx = mContent;
        list = list_dealed;
        layoutInflater = mContent.getLayoutInflater();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getDeallist().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getDeallist().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder = null;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_workbench_dealed, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else
           viewHolder = (ViewHolder) convertView.getTag();

        WordBeachDealed wordBeachDealed = list.get(groupPosition);
        viewHolder.item1.setText(wordBeachDealed.getCarno());
        viewHolder.item2.setText(wordBeachDealed.getBrands()+"   "+wordBeachDealed.getTypecode());
        ImageLoader.getInstance().displayImage(wordBeachDealed.getBrands_path(), viewHolder.iv, App.getOptions());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild  viewHolderChild = null;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_workbench_dealed_child, parent, false);
            viewHolderChild = new ViewHolderChild(convertView);
            convertView.setTag(viewHolderChild);
        }else
        viewHolderChild = (ViewHolderChild) convertView.getTag();

        WordBeachDealed.DeallistBean deallistBean = list.get(groupPosition).getDeallist().get(childPosition);
        viewHolderChild.item1.setText(deallistBean.getType());
        viewHolderChild.item2.setText(deallistBean.getDeal_time()+"  处理");
//        viewHolderChild.item3.setText(deallistBean.getIn_time()+"  来车");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }



    static class ViewHolder {
        @BindView(R.id.item1)
        TextView item1;
        @BindView(R.id.item2)
        TextView item2;
        @BindView(R.id.iv)
        ImageView iv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderChild {
        @BindView(R.id.item1)
        TextView item1;
        @BindView(R.id.item2)
        TextView item2;
        @BindView(R.id.item3)
        TextView item3;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
