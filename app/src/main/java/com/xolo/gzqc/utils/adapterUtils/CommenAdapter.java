package com.xolo.gzqc.utils.adapterUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
public class CommenAdapter<T> extends BaseAdapter {
    private  int layoutID;
    private Context context;
    private List<T> list;
    private AdapterCallback callback;

    public CommenAdapter(int layoutID, Context context, List<T> list, AdapterCallback callback) {
        this.layoutID = layoutID;
        this.context = context;
        this.list = list;
        this.callback = callback;
    }

    public CommenAdapter(int layoutID, Context context, AdapterCallback callback) {
        this.layoutID = layoutID;
        this.context = context;
        this.callback = callback;
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(context, convertView, parent, layoutID);
        callback.setView(viewHolder,position);
        return viewHolder.getConvertView();
    }

    public  interface  AdapterCallback{
        void  setView(ViewHolder holder, int position);
    }

    public void  upDt(List<T> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void  clearAll(){
        list.clear();
        notifyDataSetChanged();
    }

    public void  addAll(List<T> l){
        list.addAll(l);
        notifyDataSetChanged();
    }
}
