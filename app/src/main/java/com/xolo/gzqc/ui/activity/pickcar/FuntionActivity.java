package com.xolo.gzqc.ui.activity.pickcar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.rong.MainActivity;
import com.xolo.gzqc.rong.widget.DragPointView;
import com.xolo.gzqc.ui.activity.CarOwnersActivity;
import com.xolo.gzqc.ui.activity.LoginActivity;
import com.xolo.gzqc.ui.fragment.HomePickFragment;
import com.xolo.gzqc.ui.fragment.PersonPickFragment;
import com.xolo.gzqc.ui.fragment.WorkPickFragment;
import com.xolo.gzqc.utils.Interface.TabChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 接车员主页面
 */
public class FuntionActivity extends BaseActivity implements TabChangeListener{

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tab_img_1)
    ImageView tabImg1;
    @BindView(R.id.tab_img_2)
    ImageView tabImg2;
    @BindView(R.id.tab_img_3)
    ImageView tabImg3;
    @BindView(R.id.tab_img_4)
    ImageView tabImg4;
    @BindView(R.id.tab_text_1)
    TextView tabText1;
    @BindView(R.id.tab_text_2)
    TextView tabText2;
    @BindView(R.id.tab_text_3)
    TextView tabText3;
    @BindView(R.id.tab_text_4)
    TextView tabText4;
    @BindView(R.id.seal_num)
    DragPointView mUnreadNumView;

    private BaseFragment[] fragments;

    private long mExitTime;
    private WorkPickFragment workPickFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funtion);
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
        initMainViewPager();
        changeTextViewColor();
        changeSelectedTabState(0);
    }

    private void initMainViewPager() {
        workPickFragment = new WorkPickFragment();
        fragments = new BaseFragment[]{new HomePickFragment(),workPickFragment, new PersonPickFragment()};

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        initData();
    }

    protected void initData() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP
        };

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, conversationTypes);
            }
        }, 500);

    }


    public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
        @Override
        public void onMessageIncreased(int count) {
            if (count == 0) {
                mUnreadNumView.setVisibility(View.GONE);
            } else if (count > 0 && count < 100) {
                mUnreadNumView.setVisibility(View.VISIBLE);
                mUnreadNumView.setText(count + "");
            } else {
                mUnreadNumView.setVisibility(View.VISIBLE);
                mUnreadNumView.setText("100+");
            }
        }
    };


    private void changeTextViewColor() {
        tabImg1.setImageResource(R.mipmap.tab_hom1);
        tabImg2.setImageResource(R.mipmap.tab_news1);
        tabImg3.setImageResource(R.mipmap.workbench1);
        tabImg4.setImageResource(R.mipmap.tab_person1);
        tabText1.setTextColor(getResources().getColor(R.color.text2));
        tabText2.setTextColor(getResources().getColor(R.color.text2));
        tabText3.setTextColor(getResources().getColor(R.color.text2));
        tabText4.setTextColor(getResources().getColor(R.color.text2));
    }


    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                tabText1.setTextColor(getResources().getColor(R.color.DarkBlue));
                tabImg1.setImageResource(R.mipmap.tab_home2);
                break;
            case 1:
                tabText3.setTextColor(getResources().getColor(R.color.DarkBlue));
                tabImg3.setImageResource(R.mipmap.tab_workbench2);
                break;
            case 2:
                tabText4.setTextColor(getResources().getColor(R.color.DarkBlue));
                tabImg4.setImageResource(R.mipmap.tab_person_2);
                break;
        }
    }

    @OnClick({R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_1:
                tabChange(0);
                break;
            case R.id.tab_2:
                startActivity(new Intent(mContext, MainActivity.class));
                break;
            case R.id.tab_3:
                tabChange(1);
                break;
            case R.id.tab_4:
                tabChange(2);
                break;
        }
    }

    public  void  tabChange(int position){
        changeTextViewColor();
        changeSelectedTabState(position);
        viewpager.setCurrentItem(position);
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

    @Override
    public void change(int position, int count) {
        ((FuntionActivityCallback) workPickFragment).chageTab(position,count);
    }

    public interface FuntionActivityCallback{
        void  chageTab(int position, int count);
    }

}
