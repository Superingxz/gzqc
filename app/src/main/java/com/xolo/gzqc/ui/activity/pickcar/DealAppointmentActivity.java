package com.xolo.gzqc.ui.activity.pickcar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.DealRecordFragment;
import com.xolo.gzqc.ui.fragment.WaitDealCarFragment;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.utils.Interface.OnKeyDownListener;
import com.xolo.gzqc.utils.Interface.TabChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 预约处理
 */
public class DealAppointmentActivity extends BaseActivity implements TabChangeListener {

    @BindView(R.id.tabview)
    TabView tabview;

    private BaseFragment[] fragments ;
    private String[] title ;
    private DealRecordFragment dealRecordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_appointment);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        title = getResources().getStringArray(R.array.appointment_deal);

        dealRecordFragment = new DealRecordFragment();
        fragments = new BaseFragment[]{new WaitDealCarFragment(),dealRecordFragment};

        tabview.setTitle(title);
        tabview.setFragments(fragments);
        tabview.setPage(2);

        tabview.setPageChage(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( dealRecordFragment.onKeyDown(keyCode,event)){
            return super.onKeyDown(keyCode,event);
        }else
            return  false;
    }

    @Override
    public void change(int position, int count) {
        tabview.getTab(position).setText(title[position]+"("+count+")");
    }
}
