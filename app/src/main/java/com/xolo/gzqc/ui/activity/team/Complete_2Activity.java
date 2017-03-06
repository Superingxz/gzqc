package com.xolo.gzqc.ui.activity.team;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.FinishedFragment;
import com.xolo.gzqc.ui.fragment.Finished_2Fragment;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.ui.view.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 完工
 */
public class Complete_2Activity extends BaseActivity{


    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.tabview)
    TabView tabview;

    private BaseFragment[] fragments ;
    private String[] title ;
    private FinishedFragment finishedFragment;
    private FinishedFragment finishedFragment1;


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


    private void initTitle() {
        titleview.setTitle("完工");
    }


    private void initTab() {

        title = getResources().getStringArray(R.array.complete);

        Bundle bundle = new Bundle();
        bundle.putInt("type",1);
        finishedFragment = new FinishedFragment();
        finishedFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putInt("type",2);
        finishedFragment1 = new FinishedFragment();
        finishedFragment1.setArguments(bundle1);

        fragments = new BaseFragment[]{finishedFragment,finishedFragment1};

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
}
