package com.xolo.gzqc.rong;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.rong.widget.DragPointView;
import com.xolo.gzqc.ui.view.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.seal_me)
    RelativeLayout sealMe;
    @BindView(R.id.titleview)
    TitleView titleview;
    private List<Fragment> mFragment = new ArrayList<>();

    private ImageView mImageChats, mImageContact, mImageMe, mMineRed;
    private TextView mTextChats, mTextContact, mTextMe;

    private DragPointView mUnreadNumView;

    /**
     * 会话列表的fragment
     */
    private Fragment mConversationListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initMainViewPager();
        changeTextViewColor();
        changeSelectedTabState(0);

        sealMe.setVisibility(View.GONE);

        titleview.setRightBg(R.mipmap.main_activtiy_add_normal);
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(mContext,SearchFriendActivity.class));
            }
        });
    }


    private void initMainViewPager() {
        mConversationListFragment = initConversationList();

        mUnreadNumView = (DragPointView) findViewById(R.id.seal_num);

        mFragment.add(mConversationListFragment);
        mFragment.add(new ContactsFragment());

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTextViewColor();
                changeSelectedTabState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initData();
    }

    private void initView() {
        RelativeLayout chatRLayout = (RelativeLayout) findViewById(R.id.seal_chat);
        RelativeLayout contactRLayout = (RelativeLayout) findViewById(R.id.seal_contact_list);
        RelativeLayout mineRLayout = (RelativeLayout) findViewById(R.id.seal_me);
        mImageChats = (ImageView) findViewById(R.id.tab_img_chats);
        mImageContact = (ImageView) findViewById(R.id.tab_img_contact);
        mImageMe = (ImageView) findViewById(R.id.tab_img_me);
        mTextChats = (TextView) findViewById(R.id.tab_text_chats);
        mTextContact = (TextView) findViewById(R.id.tab_text_contact);
        mTextMe = (TextView) findViewById(R.id.tab_text_me);
        mMineRed = (ImageView) findViewById(R.id.mine_red);

        chatRLayout.setOnClickListener(this);
        contactRLayout.setOnClickListener(this);
        mineRLayout.setOnClickListener(this);
    }


    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = ConversationListFragment.getInstance();

            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .build();

            listFragment.setUri(uri);
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    private void changeTextViewColor() {
        mImageChats.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_chat));
        mImageContact.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_contacts));
        mImageMe.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_me));
        mTextChats.setTextColor(Color.parseColor("#abadbb"));
        mTextContact.setTextColor(Color.parseColor("#abadbb"));
        mTextMe.setTextColor(Color.parseColor("#abadbb"));
    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                mTextChats.setTextColor(Color.parseColor("#0099ff"));
                mImageChats.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_chat_hover));
                break;
            case 1:
                mTextContact.setTextColor(Color.parseColor("#0099ff"));
                mImageContact.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_contacts_hover));
                break;
//            case 3:
//                mTextMe.setTextColor(Color.parseColor("#0099ff"));
//                mImageMe.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_me_hover));
//                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seal_chat:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.seal_contact_list:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.seal_me:
                mViewPager.setCurrentItem(3, false);
                mMineRed.setVisibility(View.GONE);
                break;

        }
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

}
