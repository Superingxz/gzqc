package com.xolo.gzqc.utils.adapterUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.App;
import com.xolo.gzqc.R;

import io.rong.imageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ViewHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;
    private Context mtx;

    private ViewHolder(Context context, ViewGroup parent, int layoutId)
    {
        mtx = context;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        //setTag
        mConvertView.setTag(this);

    }

    /**
     * 拿到一个ViewHolder对象
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @return
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId)
    {

        if (convertView == null)
        {
            return new ViewHolder(context, parent, layoutId);
        }
        return (ViewHolder) convertView.getTag();
    }


    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId)
    {

        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView()
    {
        return mConvertView;
    }

    public ViewHolder setText(int viewId, String text)
    {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setPadding(int viewId, int padding)
    {
        View tv = getView(viewId);
        tv.setPadding(padding,padding,padding,padding);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int color)
    {
        TextView tv = getView(viewId);
        tv.setTextColor(ContextCompat.getColor(mtx,color));
        return this;
    }

    public ViewHolder setImage(int viewId, int res)
    {
        ImageView tv = getView(viewId);
        tv.setImageResource(res);
        return this;
    }


    public ViewHolder setImage(int viewId, Bitmap  bitmap)
    {
        ImageView tv = getView(viewId);
        tv.setImageBitmap(bitmap);
        return this;
    }


    public ViewHolder setVisibility(int viewId, int visibility ){
        getView(viewId).setVisibility(visibility);
        return  this;
    }

    public ViewHolder setOnClicklistener(int viewId, View.OnClickListener  listener){
        getView(viewId).setOnClickListener(listener);
        return  this;
    }

    public ViewHolder loadUrl(int viewId,String url){
        ImageView iv = getView(viewId);
        Glide.with(mtx).load(url).error(R.mipmap.img_error).into(iv);
//        ImageLoader.getInstance().displayImage(url, iv, App.getOptions());
        return  this;
    }

    public ViewHolder setCheck(int viewId, boolean  isCheck){
        CheckBox checkBox = getView(viewId);
        checkBox.setChecked(isCheck);
        return  this;
    }

    public ViewHolder setClickable(int viewId, boolean  isclick){
        View view = getView(viewId);
        view.setClickable(isclick);
        return  this;
    }

    public ViewHolder setOnCheckChangListener(int viewId, CompoundButton.OnCheckedChangeListener  listener){
        CheckBox cb = getView(viewId);
        cb.setOnCheckedChangeListener(listener);
        return  this;
    }

    public ViewHolder addTextChange(int viewId,TextWatcher listener){
        EditText et = getView(viewId);
        et.addTextChangedListener(listener);
        return  this;
    }

}
