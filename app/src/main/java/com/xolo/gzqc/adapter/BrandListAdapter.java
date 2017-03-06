package com.xolo.gzqc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.App;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.Brand;
import com.xolo.gzqc.rong.FriendInfo;
import com.xolo.gzqc.rong.RongGenerate;
import com.xolo.gzqc.rong.widget.SelectableRoundedImageView;

import java.util.List;

import io.rong.imageloader.core.ImageLoader;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
public class BrandListAdapter extends BaseAdapter implements SectionIndexer {
    private Context context;

    private List<Brand> list;

    public BrandListAdapter(Context context, List<Brand> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 传入新的数据 刷新UI的方法
     */
    public void updateListView(List<Brand> list) {
        this.list = list;
        notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final Brand mContent = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_brand, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.brandname);
            viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            viewHolder.mImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.branduri);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getPinyin());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
            viewHolder.tvTitle.setText(mContent.getBrands());
//            Glide.with(context).load(mContent.getBrands_path()).into(viewHolder.mImageView);
        ImageLoader.getInstance().displayImage(list.get(position).getBrands_path(), viewHolder.mImageView, App.getOptions());
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getPinyin();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getPinyin().charAt(0);
    }


    final static class ViewHolder {
        /**
         * 首字母
         */
        TextView tvLetter;
        /**
         * 品牌名
         */
        TextView tvTitle;
        /**
         * 头像
         */
        SelectableRoundedImageView mImageView;
    }

}
