package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yrh on 2016/6/30.
 * 底部导航栏
 * 主要用法：
 *

 bottomNavigation.setBitmaps(new int[]{R.drawable.select_news, R.drawable.select_vedio, R.drawable.select_topic, R.drawable.select_mine});
 bottomNavigation.setTitles("新闻，视频，话题，我");

 */
public class BottomNavigation extends LinearLayout{
    /**
     * 标题集合
     */
    private String[]  titles ;
    /**
     * 图标集合
     */
    private int[]  Images;

    private Fragment[] fragments;

    private List<ImageView>  list_iv = new ArrayList<>();

    private List<TextView>  list_tv = new ArrayList<>();

    private FragmentManager fragmentManager;

    private  Context mct;
    private int color_relece;
    private int color_press;

    private int curren_page;

    public int getCurren_page() {
        return curren_page;
    }

    public void setCurren_page(int curren_page) {
        this.curren_page = curren_page;
    }

    public BottomNavigation(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        mct=context;
        init(context);
    }

    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mct=context;
        init(context);
    }

    private void init(Context context) {
        color_relece = getResources().getColor(R.color.text2);
        color_press = getResources().getColor(R.color.DarkBlue);
    }

    private void addChild(){
        int length = titles.length;

        for (int i =0;i<length;i++){
            View inflate = LayoutInflater.from(mct).inflate(R.layout.item_bottom_navigation,this,false);

            final ImageView iv = (ImageView) inflate.findViewById(R.id.iv);
            iv.setImageResource(Images[i]);
            list_iv.add(iv);

            final TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
            tv_title.setText(titles[i]);
            list_tv.add(tv_title);

            LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            inflate.setLayoutParams(layoutParams);

            final int finalI = i;
            inflate.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            iv.setSelected(true);
                            tv_title.setTextColor(color_press);
                            break;
                        case MotionEvent.ACTION_UP:
                            change(finalI);
                            break;
                    }
                    return true;
                }
            });

            this.addView(inflate);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i =0 ;i<fragments.length;i++){
            fragmentTransaction.add(R.id.fl_contain,fragments[i]);
        }
        fragmentTransaction.commit();

        change(0);
    }

    public void change(int position){
        curren_page = position;

        for (ImageView i:list_iv) {
            i.setSelected(false);
        }
        for (TextView t:list_tv) {
            t.setTextColor(color_relece);
        }
          list_iv.get(position).setSelected(true);
          list_tv.get(position).setTextColor(color_press);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i =0 ;i<fragments.length;i++){
            if (i!=position){
                fragmentTransaction.hide(fragments[i]);
            }
        }
        fragmentTransaction.show(fragments[position]);
        fragmentTransaction.commit();
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
        addChild();
    }

    public void setImages(int[] images) {
        Images = images;
    }

    public void setFragments(Fragment[] fragments, FragmentManager supportFragmentManager) {
        this.fragments = fragments;
        fragmentManager = supportFragmentManager;
    }
}
