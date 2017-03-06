package com.xolo.gzqc.ui.activity.pickcar;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Logistics;
import com.xolo.gzqc.bean.child.News;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.LoadDialog;
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
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import io.rong.push.notification.PushNotificationMessage;


/**
 * 车主提醒
 */
public class NewsActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.cb)
    CheckBox cb;

    private List<News> list_news = new ArrayList<>();
    private List<String> list_id = new ArrayList<>();

    private CommenAdapter<News> adapter;

    LoadDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        init();
        listcarownerwarn();
    }

    private void init() {
        initTitle();
        initLv();

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (News n : list_news) {
                        n.setSelect(true);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    for (News n : list_news) {
                        n.setSelect(false);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_news, mContext, list_news, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final News news = list_news.get(position);

                holder.setText(R.id.item2, news.getName() + "   " + news.getPhone());
                holder.setText(R.id.item3, news.getContent2());

                CheckBox cb = holder.getView(R.id.item4);

                if (news.isSelect()) {
                    cb.setChecked(true);
                } else {
                    cb.setChecked(false);
                }

                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            list_id.add(news.getBf_warn_id());
                            news.setSelect(true);
                        } else {
                            list_id.remove(news.getBf_warn_id());
                            news.setSelect(false);
                        }
                    }
                });

            }
        });

        lv.setAdapter(adapter);
    }

    private void initTitle() {
        titleview.setRightText("查询");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listcarownerwarn();
            }
        });
    }


    /**
     * 2-69 本维修厂车主提醒列表接口 listcarownerwarn(userid,dept_id)
     * 当前用户ID:userid,维修厂id：dept_id
     */
    private void listcarownerwarn() {
        RequestParams params = creatParams("listcarownerwarn");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.NewsBean.class, new HttpUtil.HttpCallBack<ORMBean.NewsBean>() {
            @Override
            public void onSuccess(ORMBean.NewsBean result) {
                List<News> data = result.getData();
                list_news.clear();
                list_news.addAll(data);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_news.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 2-70 设置该提醒内容不用再提醒接口 setnotremindagain(userid,warn_id)
     * 当前用户ID:userid,提醒表id(多个使用逗号隔开)：warn_id
     */
    private void setnotremindagain() {
        if (list_id.size() == 0) {
            ToastUtil.showShort(mContext, "请勾选要提醒的信息");
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < list_id.size(); i++) {
            String s = list_id.get(i);

            if (i == 0) {
                buffer.append(s);
            } else {
                buffer.append("," + s);
            }
        }

        RequestParams params = creatParams("setnotremindagain");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("warn_id", buffer.toString());

        loadDialog.show(mContext);

        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    rongImSend(0);
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                    loadDialog.dismiss(mContext);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
            }
        });
    }

    private void rongImSend(final int position) {
        if (position < list_news.size()) {

            if (!list_news.get(position).isSelect()){
                LogUtil.i(position+"无选中");
                rongImSend(position+1);
                return;
            }
            LogUtil.i(position+"选中");

            News news = list_news.get(position);

            TextMessage myTextMessage = TextMessage.obtain(news.getContent2());

//"7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
//Conversation.ConversationType.PRIVATE 为会话类型。
            Message myMessage = Message.obtain(news.getPhone(), Conversation.ConversationType.PRIVATE, myTextMessage);

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
                    rongImSend(position + 1);
                    LogUtil.i("成功");
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    //消息发送失败的回调
                    LogUtil.i("失败");
                    loadDialog.dismiss(mContext);
                }
            });

        } else {
            ToastUtil.showShort(mContext, "回复成功");
            loadDialog.dismiss(mContext);
            finish();
        }

    }

    @OnClick(R.id.btn)
    public void onClick() {

        if (list_id.size()>0){
            setnotremindagain();
        }else {
            ToastUtil.showShort(mContext,"请勾选要回复的信息");
        }
    }
}
