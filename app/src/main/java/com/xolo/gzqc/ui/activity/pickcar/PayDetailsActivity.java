package com.xolo.gzqc.ui.activity.pickcar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.fragment.InTheCarInfoFragment;
import com.xolo.gzqc.ui.fragment.MaintenanceList_2Fragment;
import com.xolo.gzqc.ui.fragment.RemindFragment;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.ui.view.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 未付结账详情
 * 0-未付结账，1-综合信息，2-交车
 */
public class PayDetailsActivity extends BaseActivity {

    @BindView(R.id.tabview)
    TabView tabview;
    @BindView(R.id.titleview)
    TitleView titleview;

    private String[] title;

    private BaseFragment[] fragments;
    //    private String finished_id;
//    private String receive_id;
    private CarInfo carInfo;
    private RemindFragment remindFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_details);
        ButterKnife.bind(this);
        initTab();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        carInfo = (CarInfo) intent.getSerializableExtra(Key.OBJECT);
    }

    private void initTab() {
        remindFragment = new RemindFragment();

        title = getResources().getStringArray(R.array.in_the_car_2);
        InTheCarInfoFragment inTheCarInfoFragment=new InTheCarInfoFragment();
        //车主隐打印按钮
        if(!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))){
            Bundle bundle=new Bundle();
            bundle.putString("carwoner","carwoner");
            inTheCarInfoFragment.setArguments(bundle);
        }
        fragments = new BaseFragment[]{inTheCarInfoFragment, new MaintenanceList_2Fragment(), remindFragment};
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


    public CarInfo getCarInfo() {
        return carInfo;
    }


    public void  setFinishId(String  id){
        carInfo.setBf_finished_id(id);
        remindFragment.setCarInfo(carInfo);
    }

    //  切换到保养提醒页
    public void  setTabview(){
        tabview.setCurrentPage(2);
    }


    public void setmilleage(String milleage){
        int i = Integer.parseInt(milleage);
        remindFragment.setMilleage(i);
    }

    public void  setPhone(String phone){
        remindFragment.setPhone(phone);
    }
}
