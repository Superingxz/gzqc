package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xolo.gzqc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class TabView extends LinearLayout {
    private TabLayout tablayout;
    private ViewPager viewpager;
    private Fragment[] fragments ;
    private String[] title ;
    private FragmentManager supportFragmentManager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private List<TabLayout.Tab>  list_tab= new ArrayList<>();

    public TabView(Context context) {
        super(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.merge_tabview, this);
        tablayout = ((TabLayout) inflate.findViewById(R.id.tablayout));
        viewpager = ((ViewPager) inflate.findViewById(R.id.tabviewpager));
       supportFragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
    }


    public void setTitle(String[] title) {
        this.title = title;
    }

    public void setFragments(Fragment[] fragment) {
        viewpager.getCurrentItem();
        this.fragments = fragment;
        fragmentPagerAdapter = new FragmentPagerAdapter(supportFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }

        };


        viewpager.setAdapter(fragmentPagerAdapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
        tablayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.DarkBlue));
        tablayout.setTabTextColors(getResources().getColor(R.color.gray),Color.parseColor("#015cab"));
    }


    public void chageTitle(String[] title){
        this.title = title;
        tablayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
    }

    public void setPage(int page){
        viewpager.setOffscreenPageLimit(page);
    }

     public void setPageChage(ViewPager.OnPageChangeListener  listener){
         viewpager.addOnPageChangeListener(listener);
     }


    public void  setCurrentPage(int page){
        viewpager.setCurrentItem(page,false);
    }


    public void  setTabMode(int mode){
        tablayout.setTabMode(mode);
    }

    public int  getcurrentPage(){
        return viewpager.getCurrentItem();
    }

    public TabLayout.Tab getTab(int position) {
       return tablayout.getTabAt(position);
    }
}
