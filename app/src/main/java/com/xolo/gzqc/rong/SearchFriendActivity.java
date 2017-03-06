package com.xolo.gzqc.rong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.push.notification.PushNotificationMessage;


public class SearchFriendActivity extends BaseActivity {

    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.titleview)
    TitleView titleview;

    private List<FriendInfo> list_friend = new ArrayList<>();
    private CommenAdapter<FriendInfo> friendInfoCommenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTitle();
        initLv();
    }

    private void initTitle() {
        titleview.setRightText("查询");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getText(searchEdit))){
                    ToastUtil.showShort(mContext,"搜索内容不能为空");
                    return;
                }
                searchfriend();
            }
        });
    }

    private void initLv() {
        friendInfoCommenAdapter = new CommenAdapter<>(R.layout.friend_item, mContext, list_friend, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                FriendInfo friendInfo = list_friend.get(position);

                holder.setVisibility(R.id.catalog, View.GONE);
                holder.loadUrl(R.id.frienduri, friendInfo.getHead_portrait());
                holder.setText(R.id.friendname, friendInfo.getName());
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final FriendInfo friendInfo = list_friend.get(position);

//                final EditText editText = new EditText(mContext);
//                editText.setHint("附带语句");
//                editText.setTextColor(ContextCompat.getColor(mContext,R.color.text1));
                new AlertDialog.Builder(mContext)
                        .setMessage("申请添加"+friendInfo.getName()+"为好友")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                applyaddfriend(friendInfo);
                            }
                        })
                        .setPositiveButton("取消", null)
                        .show();
            }
        });


        lv.setAdapter(friendInfoCommenAdapter);
    }


    /**
     * 7-16 通讯录搜索接口
     * 应用的界面：应用于通讯录添加好友时搜索需要添加的人，也可以查询已经添加过的好友
     * searchfriend(userid,name,is_friend)
     * 当前用户ID:userid,姓名:name,是否好友(0为好友，1为非好友):is_friend
     */
    private void searchfriend() {
        RequestParams params = creatParams("searchfriend");
        params.addBodyParameter("name", searchEdit.getText().toString().trim());
        params.addBodyParameter("is_friend", "1");

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.FriendInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.FriendInfoBean>() {
            @Override
            public void onSuccess(ORMBean.FriendInfoBean result) {
                list_friend.clear();
                list_friend.addAll(result.getData());
                friendInfoCommenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_friend.clear();
                friendInfoCommenAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 7-14 申请添加好友接口
     应用的界面：应用于通讯录添加好友
     applyaddfriend(userid,be_app_userid)
     当前用户ID:userid,被申请人ID:be_app_userid
     */
    private void applyaddfriend(final FriendInfo info){
        RequestParams params = creatParams("applyaddfriend");
        params.addBodyParameter("be_app_userid",info.getFriends_id());

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                   if (result.getRes().equals("1")){
                           rongImSend(info.getPhone());
                   }else {
                       ToastUtil.showShort(mContext,result.getMsg());
                       mLoad.dismiss(mContext);
                   }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                    mLoad.dismiss(mContext);
            }
        });
    }

    /**
     * 发送添加好友的请求
     * @param phone
     */
    private void rongImSend(String phone) {
//        官方针对 operation 属性定义了 "Request", "AcceptResponse", "RejectResponse" 几个常量
        ContactNotificationMessage contactNotificationMessage = ContactNotificationMessage.obtain("Request", user.getPhone(), phone, "");

//"7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
//Conversation.ConversationType.PRIVATE 为会话类型。
        Message myMessage = Message.obtain(phone, Conversation.ConversationType.NONE, contactNotificationMessage);
/**
 * <p>发送消息。
 * 通过 {@link IRongCallback.ISendMessageCallback}
 * 中的方法回调发送的消息状态及消息体。</p>
 *
 * @param message     将要发送的消息体。
 * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
 *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
 *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
 * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link PushNotificationMessage#getPushData()} 方法获取。
 * @param callback    发送消息的回调，参考 {@link IRongCallback.ISendMessageCallback}。
 */
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                LogUtil.i("成功发送");
                finish();
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回
                finish();
            }

        });
    }



}
