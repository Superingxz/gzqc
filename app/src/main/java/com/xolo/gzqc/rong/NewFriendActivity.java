package com.xolo.gzqc.rong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.NewFriend;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.greenrobot.eventbus.EventBus;
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
import io.rong.push.notification.PushNotificationMessage;

public class NewFriendActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.lv)
    ListView lv;

    private List<NewFriend>  list = new ArrayList<>();
    private CommenAdapter<NewFriend> newFriendCommenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        ButterKnife.bind(this);

        titleview.setRightBg(R.mipmap.main_activtiy_add_normal);
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,SearchFriendActivity.class));
            }
        });

        initLv();
        getlistverifyfriend();
    }

    private void initLv() {
        newFriendCommenAdapter = new CommenAdapter<>(R.layout.item_new_friend, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final NewFriend newFriend = list.get(position);
                String accept = newFriend.getAccept();
                String refuse = newFriend.getRefuse();

                holder.loadUrl(R.id.iv,newFriend.getHead_portrait());
                holder.setText(R.id.tv_name,newFriend.getName());

                if (accept.equals("0")&&accept.equals("0")){
                    holder.setVisibility(R.id.tv_status, View.VISIBLE);
                    holder.setVisibility(R.id.btn_1, View.GONE);
                    holder.setVisibility(R.id.btn_2, View.GONE);

                    holder.setOnClicklistener(R.id.btn_1, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            verifyfriend(newFriend.getC_friends_app_id(),"1",newFriend.getPhone());
                        }
                    });
                    holder.setOnClicklistener(R.id.btn_2, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            verifyfriend(newFriend.getC_friends_app_id(),"0",newFriend.getPhone());
                        }
                    });
                }else {
                    holder.setVisibility(R.id.tv_status, View.GONE);
                    holder.setVisibility(R.id.btn_1, View.VISIBLE);
                    holder.setVisibility(R.id.btn_2, View.VISIBLE);
                    holder.setText(R.id.tv_status,accept.equals("1")?"已接收":"已拒接");
                }

            }
        });

        lv.setAdapter(newFriendCommenAdapter);
    }


    /**
     * 7-17 获取待验证好友列表接口
     应用的界面：应用于通讯录添加好友时搜索需要添加的人，也可以查询已经添加过的好友
     getlistverifyfriend(userid)
     当前用户ID:userid
     */
private   void getlistverifyfriend(){
    RequestParams params = creatParams("getlistverifyfriend");

    mLoad.show(mContext);
    HttpUtil.getInstance().post(params, ORMBean.NewFriendBean.class, new HttpUtil.HttpCallBack<ORMBean.NewFriendBean>() {
        @Override
        public void onSuccess(ORMBean.NewFriendBean result) {
            if (result.getRes().equals("1")){
                list.clear();
                List<NewFriend> data = result.getData();
                list.addAll(data);
                newFriendCommenAdapter.notifyDataSetChanged();
            }
            mLoad.dismiss(mContext);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
mLoad.dismiss(mContext);
        }
    });
}


    /**
     * 7-15 验证好友接口
     应用的界面：应用于通讯录验证好友
     verifyfriend(userid,c_friends_app_id,is_accept)
     当前用户ID:userid,申请好友表ID:c_friends_app_id,是否接受(0为拒绝，1为接受):is_accept
     */
       private   void verifyfriend(String id, final String  is_accept, final String phone){
           RequestParams params = creatParams("verifyfriend");
           params.addBodyParameter("c_friends_app_id",id);
           params.addBodyParameter("is_accept",is_accept);

           mLoad.show(mContext);
           HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
               @Override
               public void onSuccess(BaseBean result) {
                         if (result.getRes().equals("1")){
                             rongImSend(phone,is_accept.equals("1"));
                             getlistverifyfriend();
                         }
               }

               @Override
               public void onError(Throwable ex, boolean isOnCallback) {

               }
           });
       }


    /**
     * 发送添加好友的请求
     * @param phone
     */
    private void rongImSend(String phone,boolean isAccept) {
//        官方针对 operation 属性定义了 "Request", "AcceptResponse", "RejectResponse" 几个常量
        ContactNotificationMessage contactNotificationMessage = ContactNotificationMessage.obtain(isAccept?"AcceptResponse":"RejectResponse", user.getPhone(), phone, "");

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
