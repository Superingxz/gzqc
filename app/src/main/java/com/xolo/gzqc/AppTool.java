package com.xolo.gzqc;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.rong.AMAPLocationActivity;
import com.xolo.gzqc.ui.activity.LoginActivity;
import com.xolo.gzqc.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.IllegalFormatCodePointException;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.LocationMessage;

/**
 * Created by Administrator on 2016/9/18.
 */
public class AppTool implements RongIM.LocationProvider,RongIM.ConversationBehaviorListener,RongIMClient.OnReceiveMessageListener{

    private static AppTool  instant;


    private static RongIM.LocationProvider.LocationCallback   locationCallback;

    public AppTool(Context context) {
        initListener();
    }

    private void initListener() {
          RongIM.setLocationProvider(this);
          RongIM.setConversationBehaviorListener(this);
        RongIM.setOnReceiveMessageListener(this);
    }


    public static AppTool getInstant(){
        return  instant;
    }

    public static  void  init(Context context){
        if (instant == null) {

            synchronized (AppTool.class) {

                if (instant == null) {
                    instant = new AppTool(context);
                }
            }
        }
    }


    public static RongIM.LocationProvider.LocationCallback getLocationCallback() {
        return locationCallback;
    }

    public static void setLocationCallback(RongIM.LocationProvider.LocationCallback locationCallback) {
        AppTool.locationCallback = locationCallback;
    }

    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {
        setLocationCallback(locationCallback);
        Intent intent = new Intent(context, AMAPLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }


    @Override
    public boolean onReceived(Message message, int i) {
        //        官方针对 operation 属性定义了 "Request", "AcceptResponse", "RejectResponse" 几个常量
        MessageContent messageContent = message.getContent();
        if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;

            String operation = contactNotificationMessage.getOperation();
            if (operation.equals("AcceptResponse")){

            }

            LogUtil.i("好友请求消息");
            Control addFriend = new Control("", "addFriend");
            //新的好友增加红点
            EventBus.getDefault().postSticky(addFriend);
            return true;
        }
        return false;
    }



}
