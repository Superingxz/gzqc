package com.xolo.gzqc.ui.activity.administrator;

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
import com.xolo.gzqc.R;
import com.xolo.gzqc.rong.MainActivity;
import com.xolo.gzqc.rong.widget.DragPointView;
import com.xolo.gzqc.ui.activity.pickcar.ProgressActivity;
import com.xolo.gzqc.ui.fragment.AdministratorFragment;
import com.xolo.gzqc.ui.fragment.PersonPickFragment;
import com.xolo.gzqc.ui.fragment.TeamMainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;


public class AdministratorMainActivity extends BaseActivity {
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
    @BindView(R.id.tab_3)
    RelativeLayout tab3;

    private Fragment[] fragments;

    private long mExitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funtion);
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
        tab3.setVisibility(View.GONE);
        tabText3.setText("维修进度");
        initMainViewPager();
        changeTextViewColor();
        changeSelectedTabState(0);
    }

    private void initMainViewPager() {
        fragments = new Fragment[]{new AdministratorFragment(), new PersonPickFragment()};

        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return 2;
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
        tabImg3.setImageResource(R.mipmap.tab_progress1);
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
                tabText4.setTextColor(getResources().getColor(R.color.DarkBlue));
                tabImg4.setImageResource(R.mipmap.tab_person_2);
                break;
        }
    }

    @OnClick({R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_1:
                changeTextViewColor();
                changeSelectedTabState(0);
                viewpager.setCurrentItem(0);
                break;
            case R.id.tab_2:
                startActivity(new Intent(mContext, MainActivity.class));
                break;
            case R.id.tab_3:
                Intent intent = new Intent(mContext, ProgressActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.tab_4:
                changeTextViewColor();
                changeSelectedTabState(1);
                viewpager.setCurrentItem(1);
                break;
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
