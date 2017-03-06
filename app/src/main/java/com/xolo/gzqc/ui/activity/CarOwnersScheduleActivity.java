package com.xolo.gzqc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.CoSheduliFragment;
import com.xolo.gzqc.ui.view.TabView;

/**
 * 购件进度
 * Created by Administrator on 2016/9/27.
 */
public class CarOwnersScheduleActivity extends BaseActivity{
    String[] titles={"购件付款确定"};
    TabView tabview_info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_schedule);
        initView();
    }

    private void initView() {
        tabview_info=(TabView)findViewById(R.id.tabview_info);
        tabview_info.setTitle(titles);
        tabview_info.setFragments(new Fragment[]{new CoSheduliFragment()});
    }
}
