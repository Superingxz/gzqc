package com.xolo.gzqc.ui.activity.pickcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.AddAppointmentFragment;
import com.xolo.gzqc.ui.fragment.MaintenanceRecordFragment;
import com.xolo.gzqc.ui.fragment.SearchAppointmentFragment;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.utils.Interface.TabChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预约构件
 */
public class PurchaseReservationActivity extends BaseActivity implements TabChangeListener {
    @BindView(R.id.tabview)
    TabView tabview;

    private BaseFragment[] fragments ;
    private String[] title ;
    private MaintenanceRecordFragment maintenanceRecordFragment;
    private AddAppointmentFragment addAppointmentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_reservation);
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
        initTab();
    }

    private void initTab() {
        title = getResources().getStringArray(R.array.purchase_reservation);

        maintenanceRecordFragment = new MaintenanceRecordFragment();
        addAppointmentFragment = new AddAppointmentFragment();

        fragments = new BaseFragment[]{addAppointmentFragment,new SearchAppointmentFragment(),maintenanceRecordFragment};

        tabview.setTitle(title);
        tabview.setFragments(fragments);
        tabview.setPage(3);

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
       if ( maintenanceRecordFragment.onKeyDown(keyCode,event) || addAppointmentFragment.onKeyDown(keyCode,event)){
           return false;
       }else
           return super.onKeyDown(keyCode,event);
    }


    @Override
    public void change(int position, int count) {
        tabview.getTab(position).setText(title[position]+"("+count+")");
    }
}
