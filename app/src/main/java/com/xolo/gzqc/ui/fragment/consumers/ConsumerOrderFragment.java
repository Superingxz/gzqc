package com.xolo.gzqc.ui.fragment.consumers;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.ui.view.TitleView;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消费者——订单中心
 */
public class ConsumerOrderFragment extends BaseFragment {


    @BindView(R.id.tabview)
    TabView tabview;
    @BindView(R.id.titleview)
    TitleView titleview;
    private BaseFragment[] baseFragments;

    public ConsumerOrderFragment() {
        // Required empty public constructor
    }

    private int currentPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consumer_order, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected void init() {
        titleview.setLeftGone();

        tabview.setTitle(new String[]{"全部", "待付款", "待收货", "已收货", "退款"});

        baseFragments = new BaseFragment[]{new AllOrderFragment(),new WaitPayOrderFragment(),new WaiTakeOrderFragment(),new TakedOrderFragment(),new RefundOrderFragment()};
        tabview.setFragments(baseFragments);

        tabview.setPageChage(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                   baseFragments[position].load();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    baseFragments[currentPage].load();
                }
            },500);
        }
    }
}
