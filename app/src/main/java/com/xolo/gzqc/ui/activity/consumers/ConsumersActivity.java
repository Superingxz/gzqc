package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.consumers.ConsumerOrderFragment;
import com.xolo.gzqc.ui.fragment.consumers.ConsumersMainFragment;
import com.xolo.gzqc.ui.fragment.consumers.ConsumersPersonFragment;
import com.xolo.gzqc.ui.fragment.consumers.ConsumersShoppingFragment;
import com.xolo.gzqc.ui.view.BottomNavigation;
import com.xolo.gzqc.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消费者主页面
 */
public class ConsumersActivity extends BaseActivity {

    @BindView(R.id.bottomNavigation)
    BottomNavigation navigation;
    @BindView(R.id.fl_contain)
    FrameLayout flContain;

    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumers);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        navigation.setFragments(new Fragment[]{new ConsumersMainFragment(),new ConsumersShoppingFragment(),new ConsumerOrderFragment(),new ConsumersPersonFragment()},getSupportFragmentManager());
        navigation.setImages(new int[]{R.drawable.select_tab_home, R.drawable.select_tab_shopping, R.drawable.select_tab_order,R.drawable.select_tab_person});
        navigation.setTitles(new String[]{"首页","购物车","订单中心","个人中心"});
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        int page = intent.getIntExtra("page", -1);

        if (page!=navigation.getCurren_page()&&page>0){
            navigation.change(page);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
