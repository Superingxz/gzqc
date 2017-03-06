package com.xolo.gzqc.rong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.view.TitleView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Bob on 15/8/18.
 * 会话页面
 */
public class ConversationActivity extends FragmentActivity {

    @BindView(R.id.titleview)
    TitleView titleview;

    /**
     * 对方id
     */
    private String mTargetId;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    /**
     * title
     */
    private String title;
    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        getIntentDate(intent);
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        title = intent.getData().getQueryParameter("title");
        String type = intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(type.toUpperCase(Locale.getDefault()));

        if (TextUtils.isEmpty(title)) {
            titleview.setTitle(mTargetId);
        } else {
            titleview.setTitle(title);
        }

        if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
                titleview.setRightVisibility(View.VISIBLE);
                titleview.setRightBg(R.mipmap.icon2_menu);
            titleview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else {
            titleview.setRightVisibility(View.GONE);
        }

        enterFragment(mConversationType, mTargetId);
    }


    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }


}
