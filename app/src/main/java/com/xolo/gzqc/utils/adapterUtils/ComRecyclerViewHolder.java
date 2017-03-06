package com.xolo.gzqc.utils.adapterUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.R;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ComRecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View>  views;
    private  View convertView;

    private Context mtx;

    public ComRecyclerViewHolder(View itemView,Context context) {
        super(itemView);
        mtx =context;
        views=new SparseArray<>();
        convertView=itemView;
    }


    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId)
    {

        View view = views.get(viewId);
        if (view == null)
        {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public ComRecyclerViewHolder setText(int viewId, String text)
    {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ComRecyclerViewHolder setPadding(int viewId, int padding)
    {
        View tv = getView(viewId);
        tv.setPadding(padding,padding,padding,padding);
        return this;
    }

    public ComRecyclerViewHolder setTextColor(int viewId, int color)
    {
        TextView tv = getView(viewId);
        tv.setTextColor(ContextCompat.getColor(mtx,color));
        return this;
    }

    public ComRecyclerViewHolder setImage(int viewId, int res)
    {
        ImageView tv = getView(viewId);
        tv.setImageResource(res);
        return this;
    }


    public ComRecyclerViewHolder setImage(int viewId, Bitmap bitmap)
    {
        ImageView tv = getView(viewId);
        tv.setImageBitmap(bitmap);
        return this;
    }


    public ComRecyclerViewHolder setVisibility(int viewId, int visibility ){
        getView(viewId).setVisibility(visibility);
        return  this;
    }

    public ComRecyclerViewHolder setOnClicklistener(int viewId, View.OnClickListener  listener){
        getView(viewId).setOnClickListener(listener);
        return  this;
    }

    public ComRecyclerViewHolder loadUrl(int viewId,String url){
        ImageView iv = getView(viewId);
        Glide.with(mtx).load(url).error(R.mipmap.img_error).into(iv);
//        ImageLoader.getInstance().displayImage(url, iv, App.getOptions());
        return  this;
    }

    public ComRecyclerViewHolder setCheck(int viewId, boolean  isCheck){
        CheckBox checkBox = getView(viewId);
        checkBox.setChecked(isCheck);
        return  this;
    }

    public ComRecyclerViewHolder setClickable(int viewId, boolean  isclick){
        View view = getView(viewId);
        view.setClickable(isclick);
        return  this;
    }

    public ComRecyclerViewHolder setOnCheckChangListener(int viewId, CompoundButton.OnCheckedChangeListener  listener){
        CheckBox cb = getView(viewId);
        cb.setOnCheckedChangeListener(listener);
        return  this;
    }

}
