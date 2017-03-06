package com.xolo.gzqc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.CarOwners_Add_Maintain;
import com.xolo.gzqc.ui.fragment.CoRecentMaintainFragment;
import com.xolo.gzqc.ui.fragment.MaintainRecordFragment;
import com.xolo.gzqc.ui.view.TabView;

/**
 * 维修预约
 * Created by Administrator on 2016/9/26.
 */
public class MaintainSubscribeActivity extends BaseActivity {
   TabView tabview_info;
    String []titles={"新增预约","预约维修进度","最近预约记录"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwoners_maintain);
        initView();
    }

    private void initView() {
        tabview_info=(TabView)findViewById(R.id.tabview_info);
        tabview_info.setTitle(titles);
        tabview_info.setFragments(new Fragment[]{new CarOwners_Add_Maintain(),new MaintainRecordFragment(),new CoRecentMaintainFragment()});
    }
}
