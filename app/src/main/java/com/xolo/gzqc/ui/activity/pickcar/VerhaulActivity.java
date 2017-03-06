package com.xolo.gzqc.ui.activity.pickcar;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.VerhaulFragment;
import com.xolo.gzqc.ui.fragment.VerhaulHistroyFragment;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.utils.Interface.TabChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 拆检
 */
public class VerhaulActivity extends BaseActivity implements TabChangeListener {


    @BindView(R.id.tabview)
    TabView tabview;
    private VerhaulHistroyFragment verhaulHistroyFragment;

    private  String[] title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verhaul);
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
        initTab();
    }

    private void initTab() {
         title = new String[]{"拆检","拆检历史"};

        verhaulHistroyFragment = new VerhaulHistroyFragment();
        final BaseFragment[] fragments = new BaseFragment[]{new VerhaulFragment(),verhaulHistroyFragment};
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( verhaulHistroyFragment.onKeyDown(keyCode,event)){
            return super.onKeyDown(keyCode,event);
        }else
            return  false;
    }

    @Override
    public void change(int position, int count) {
        tabview.getTab(position).setText(title[position]+"("+count+")");
    }
}
