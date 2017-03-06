package com.xolo.gzqc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.CarOwners_Add_Maintain;
import com.xolo.gzqc.ui.fragment.CoRecentMaintainFragment;
import com.xolo.gzqc.ui.fragment.HistoryRecordFragment;
import com.xolo.gzqc.ui.fragment.MaintainDetailsFragemnt;
import com.xolo.gzqc.ui.fragment.MaintainRecordFragment;
import com.xolo.gzqc.ui.view.TabView;

/**
 * 维修记录
 * Created by Administrator on 2016/9/28.
 */
public class OcMaintainRecordActivity  extends BaseActivity{

    TabView tabview_info;
    String []titles={"历史维修记录","车辆详细信息"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocmaintainrecord);
        initView();
    }
    private void initView() {
        tabview_info=(TabView)findViewById(R.id.tabview_info);
        tabview_info.setTitle(titles);
        tabview_info.setFragments(new Fragment[]{new HistoryRecordFragment(),new MaintainDetailsFragemnt()});
    }
}
