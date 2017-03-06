package com.xolo.gzqc.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.adapter.ExpandableListViewAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.bean.child.CoAccessories;
import com.xolo.gzqc.bean.child.CoProject;
import com.xolo.gzqc.bean.child.ProjectChild;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.CustomExpandableListView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 维修清单
 * Created by Administrator on 2016/9/27.
 */
public class CoProjectListFragment extends LazyFragment {
    CustomExpandableListView exp;
    CarOwner_add_MaintainAdapter adapter;
    List<CoProject> coProjects = new ArrayList<>();
    List<ProjectChild> projectChildren;
    Button submit;
    EditText disagree_et;
    ImageView project_disagree_img, project_agree_img;
    LinearLayout co_project_disagree, co_project_agree, edt_layout, check_layout;
    View view;
    TextView material_price_text, hour_price_text, totle_price_text;
    List<CarOwner_CarInfo> carlist;
    //String carno;
    Dialog dialog;
    ExpandableListViewAdapter expandableListViewAdapter;
    String phone;
    String bf_receive_id;
    public void setCarno(String bf_receive_id,String phone, User user) {
        this.user = user;
        this.bf_receive_id = bf_receive_id;
        this.phone=phone;
        getData();
        getTtalWorkamt();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_co_project_list, null);
            initView(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //获取工时信息
    void getTtalWorkamt() {

        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getrepairsum");
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("bf_receive_id", bf_receive_id);
        requestParams.addBodyParameter("is_deleted", "0");
        HttpUtil.getInstance().post(requestParams, ORMBean.CoCoTotalworkamtBean.class, new HttpUtil.HttpCallBack<ORMBean.CoCoTotalworkamtBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoCoTotalworkamtBean result) {
                if (result.getRes().equals("1")) {
                    material_price_text.setText(result.getData().get(0).getTotalsaleprice());
                    hour_price_text.setText(result.getData().get(0).getTotalworkamt());
                    totle_price_text.setText(result.getData().get(0).getTotalprice());
                    if (result.getData().get(0).getIs_agree().equals("1")) {
                        submit.setVisibility(View.VISIBLE);
                        submit.setBackgroundResource(R.drawable.btn_grey_bg);
                        submit.setText("已同意报价");
                        submit.setClickable(false);
                        check_layout.setVisibility(View.GONE);
                        edt_layout.setVisibility(View.GONE);
                    } else {
                        submit.setVisibility(View.VISIBLE);
                        submit.setBackgroundResource(R.drawable.shape_button_blue);
                        submit.setText("提交");
                        submit.setClickable(true);
                        check_layout.setVisibility(View.VISIBLE);
                        edt_layout.setVisibility(View.VISIBLE);
                    }
                } else {
                    submit.setVisibility(View.GONE);
                    check_layout.setVisibility(View.GONE);
                    edt_layout.setVisibility(View.GONE);
                    ToastUtil.showShort(getActivity(), result.getMsg());
                    material_price_text.setText("");
                    hour_price_text.setText("");
                    totle_price_text.setText("");
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }
    FinishIface finishIface;

    public FinishIface getFinishIface() {
        return finishIface;
    }

    public void setFinishIface(FinishIface finishIface) {
        this.finishIface = finishIface;
    }

    public  interface  FinishIface{
        void onFinish();
    }


    void sendMessage(boolean bl) {
        TextMessage myTextMessage;
        if (bl) {
            myTextMessage = TextMessage.obtain("同意报价");
        } else {
            myTextMessage = TextMessage.obtain("不同意报价,原因:" + disagree_et.getText().toString());
        }

        //"7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
        //Conversation.ConversationType.PRIVATE 为会话类型。
        Message myMessage = Message.obtain(phone, Conversation.ConversationType.PRIVATE, myTextMessage);
        /**
         * <p>发送消息。
         * 通过 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}
         * 中的方法回调发送的消息状态及消息体。</p>
         *
         * @param message     将要发送的消息体。
         * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
         *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
         *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
         * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
         * @param callback    发送消息的回调，参考 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}。
         */
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                ToastUtil.showLong(getActivity(), "已通知接车员");
                if(finishIface!=null){
                    finishIface.onFinish();
                }
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
                ToastUtil.showLong(getActivity(), "通知接车员失败");
            }
        });
    }

    void sendIdea() {
        String iead = disagree_et.getText().toString();
        RequestParams requestParams = creatParams("ownerverifyidea");
        requestParams.addBodyParameter("bf_receive_id", bf_receive_id);
        if (isAgree) {
            requestParams.addBodyParameter("status", "1");
            requestParams.addBodyParameter("is_agree", "1");
        } else {
            requestParams.addBodyParameter("status", "0");
            requestParams.addBodyParameter("is_agree", "0");
        }
        Date dt = new Date();
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        requestParams.addBodyParameter("ideadate", matter1.format(dt));
        requestParams.addBodyParameter("idearemark", iead);
        requestParams.addBodyParameter("userid", user.getUser_id());

        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }

            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    sendMessage(isAgree);
                }
                ToastUtil.showLong(mContext, result.getMsg());
                LoadDialog.dismiss(mContext);
            }
        });
    }

    boolean isAgree = true;

    private void initView(View view) {
        edt_layout = (LinearLayout) view.findViewById(R.id.edt_layout);
        check_layout = (LinearLayout) view.findViewById(R.id.check_layout);
        totle_price_text = (TextView) view.findViewById(R.id.totle_price_text);
        material_price_text = (TextView) view.findViewById(R.id.material_price_text);
        hour_price_text = (TextView) view.findViewById(R.id.hour_price_text);
        project_disagree_img = (ImageView) view.findViewById(R.id.project_disagree_img);
        project_agree_img = (ImageView) view.findViewById(R.id.project_agree_img);
        co_project_disagree = (LinearLayout) view.findViewById(R.id.co_project_disagree);
        co_project_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAgree = false;
                project_agree_img.setBackgroundResource(R.mipmap.nil);
                project_disagree_img.setBackgroundResource(R.mipmap.sure);
            }
        });
        co_project_agree = (LinearLayout) view.findViewById(R.id.co_project_agree);
        co_project_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAgree = true;
                project_agree_img.setBackgroundResource(R.mipmap.sure);
                project_disagree_img.setBackgroundResource(R.mipmap.nil);
            }
        });
        user = SPManager.getUser(mContext);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(bf_receive_id)) {
                    ToastUtil.showLong(getActivity(), "请选择车牌");
                    return;
                } else if (exp.getVisibility() == View.GONE) {
                    return;
                }
                sendIdea();
            }
        });

        disagree_et = (EditText) view.findViewById(R.id.disagree_et);
        exp = (CustomExpandableListView) view.findViewById(R.id.exp);
        exp = (CustomExpandableListView) view.findViewById(R.id.exp);
        expandableListViewAdapter = new ExpandableListViewAdapter(getContext(), coProjects);
        exp.setAdapter(expandableListViewAdapter);
        exp.setGroupIndicator(null);

    }

    //获取维修项目父类数据
    void getData() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("ownerquoteverify");
        requestParams.addBodyParameter("bf_receive_id", bf_receive_id);
        requestParams.addBodyParameter("status", "0");
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoProjectBean.class, new HttpUtil.HttpCallBack<ORMBean.CoProjectBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }

            @Override
            public void onSuccess(ORMBean.CoProjectBean result) {
                if (result.getRes().equals("1")) {
                    coProjects.clear();
                    coProjects.add(new CoProject("维修项目", "工时金额"));
                    coProjects.addAll(result.getData());
                    expandableListViewAdapter = new ExpandableListViewAdapter(getContext(), coProjects);
                    exp.setAdapter(expandableListViewAdapter);
                    exp.setVisibility(View.VISIBLE);
                    //获取子类数据
                    for (int i = 0; i < coProjects.size(); i++) {
                        getDataChild(i, coProjects.get(i).getBf_quoted_priced_id());
                    }
                } else {
                    // ToastUtil.showLong(getContext(), result.getMsg());
                    exp.setVisibility(View.GONE);
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }


    //获取子类数据
    void getDataChild(final int position, String bf_quoted_priced_id) {
        if (TextUtils.isEmpty(bf_quoted_priced_id)) {
            return;
        }
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("quotereparibymaintenanceid");
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("bf_quoted_priced_id", bf_quoted_priced_id);
        requestParams.addBodyParameter("is_deleted", "0");
        requestParams.addBodyParameter("is_owner", "1");
        requestParams.addBodyParameter("owen_id", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoAccessoriesBean.class, new HttpUtil.HttpCallBack<ORMBean.CoAccessoriesBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoAccessoriesBean result) {
                if (result.getRes().equals("1")) {
                    result.getData().add(0, new CoAccessories("配件名称", "单价", "数量", "金额"));
                    coProjects.get(position).setCoAccessories(result.getData());
                    exp.expandGroup(position);
                    exp.collapseGroup(position);
                } else {
                    //ToastUtil.showLong(getContext(), result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }

    @Override
    protected void loadData() {
        if (needLoad()) {
            mHasLoadedOnce = true;

        }
    }

    @Override
    protected void init() {

    }

}
