package com.xolo.gzqc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.NewCarInsuranceFragment;
import com.xolo.gzqc.ui.view.TabView;

/**
 * 车主保险业务
 * Created by Administrator on 2016/9/26.
 */
public class CarOwnersInsuranceActivity  extends BaseActivity{
    TabView tabview_info;
    String[] titles={"新车购险","续保"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carowners_insurance);
        initView();
    }

    private void initView() {
        tabview_info=(TabView)findViewById(R.id.tabview_info);
        tabview_info.setTitle(titles);
        tabview_info.setFragments(new Fragment[]{new NewCarInsuranceFragment(),new Fragment()});
    }
}
