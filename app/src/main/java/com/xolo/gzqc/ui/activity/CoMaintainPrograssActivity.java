package com.xolo.gzqc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.fragment.CoCarOrderFragment;
import com.xolo.gzqc.ui.fragment.CoHistoryPrograssFragment;
import com.xolo.gzqc.ui.fragment.HistoryRecordFragment;
import com.xolo.gzqc.ui.fragment.MaintainDetailsFragemnt;
import com.xolo.gzqc.ui.view.TabView;

/**
 * Created by Administrator on 2016/10/13.
 */
public class CoMaintainPrograssActivity extends BaseActivity {
    TabView tabview_info;
    String[] titles = {"维修进度", "接车单"};
    String bf_receive_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_prograss);
        user = SPManager.getUser(mContext);
        bf_receive_id = getIntent().getStringExtra("bf_receive_id");
        initView();
    }
    private void initView() {
        tabview_info = (TabView) findViewById(R.id.tabview_info);
        tabview_info.setTitle(titles);
        Bundle bundle = new Bundle();
        bundle.putString("bf_receive_id", bf_receive_id);
        CoHistoryPrograssFragment coHistoryPrograssFragment = new CoHistoryPrograssFragment();
        coHistoryPrograssFragment.setArguments(bundle);
        CoCarOrderFragment coCarOrderFragment=new CoCarOrderFragment();
        coCarOrderFragment.setArguments(bundle);
        tabview_info.setFragments(new Fragment[]{coHistoryPrograssFragment,coCarOrderFragment});
    }

}
