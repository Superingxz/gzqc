package com.xolo.gzqc.ui.activity.pickcar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.TurningCarFragment;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.Interface.TabChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 交车
 */
public class ReturnCarActivity extends BaseActivity implements TabChangeListener {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.tabview)
    TabView tabview;

    private BaseFragment[] fragments ;
    private String[] title ;
    private TurningCarFragment turningCarFragment;
    private TurningCarFragment turningCarFragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_reservation);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTitle();
        initTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initIntent();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tabview.setCurrentPage(1);
            super.handleMessage(msg);
        }
    };

    private void initIntent() {
        int type = getIntent().getIntExtra("type", 0);

        if (type == 1){
            handler.sendEmptyMessageDelayed(1,500);
        }

    }

    private void initTitle() {
        titleview.setTitle("交车");
    }


    private void initTab() {

        title = getResources().getStringArray(R.array.in_the_car_1);

        Bundle bundle = new Bundle();
        bundle.putInt("type",1);
        turningCarFragment = new TurningCarFragment();
        turningCarFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putInt("type",2);
        turningCarFragment1 = new TurningCarFragment();
        turningCarFragment1.setArguments(bundle1);

        fragments = new BaseFragment[]{turningCarFragment, turningCarFragment1};

        tabview.setTitle(title);
        tabview.setFragments(fragments);

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
    protected void onDestroy() {
        startActivity(new Intent(mContext,FuntionActivity.class));
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (turningCarFragment1.onKeyDown(keyCode,event)){
            return false;
        }

        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void change(int position, int count) {
        tabview.getTab(position).setText(title[position]+"("+count+")");
    }
}
