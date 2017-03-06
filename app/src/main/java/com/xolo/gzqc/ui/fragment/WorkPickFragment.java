package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.activity.pickcar.FuntionActivity;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.Interface.TabChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 接车员-首页工作台
 */
public class WorkPickFragment extends BaseFragment implements FuntionActivity.FuntionActivityCallback{

    @BindView(R.id.tabview)
    TabView tabview;
    @BindView(R.id.titleview)
    TitleView titleview;

    private BaseFragment[] fragments;
    private String[] title;
    private WaitDealThingFragment waitDealThingFragment;

    private FuntionActivity  funtionActivity;

    @Override
    public void onAttach(Context context) {
        funtionActivity = (FuntionActivity) context;
        super.onAttach(context);
    }

    public WorkPickFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_pick, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        titleview.setLeftGone();
        initTab();
    }

    @Override
    public void load() {
        super.load();
        waitDealThingFragment.getreceivemytodo();
    }

    private void initTab() {

        title = getResources().getStringArray(R.array.procurementDeal);

        waitDealThingFragment = new WaitDealThingFragment();
        fragments = new BaseFragment[]{waitDealThingFragment, new DealedThingFragment()};
        tabview.setTitle(title);
        tabview.setFragments(fragments);

        tabview.setPageChage(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                fragments[position].load();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void chageTab(int position, int count) {
        tabview.getTab(position).setText(title[position]+"("+count+")");
    }
}
