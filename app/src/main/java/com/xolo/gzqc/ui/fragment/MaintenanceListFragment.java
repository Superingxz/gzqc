package com.xolo.gzqc.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.OfferExpandableAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.ReceiveInfo;
import com.xolo.gzqc.bean.postJson.Offer;
import com.xolo.gzqc.bean.postJson.Part;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.AddOfferItemActivity;
import com.xolo.gzqc.ui.activity.pickcar.DispatchingActivity;
import com.xolo.gzqc.ui.activity.pickcar.OfferActivity;
import com.xolo.gzqc.ui.view.CustomExpandableListView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
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
 * 维修清单-报价
 */
public class MaintenanceListFragment extends BaseFragment {

    @BindView(R.id.lv)
    CustomExpandableListView lv;
    @BindView(R.id.price_part)
    TextView pricePart;
    @BindView(R.id.price_cost)
    TextView priceCost;
    @BindView(R.id.price_total)
    TextView priceTotal;
    @BindView(R.id.sendMan)
    TextView sendMan;
    @BindView(R.id.confirm_save)
    Button confirm;
    @BindView(R.id.confirm_send)
    Button confirmSend;
    @BindView(R.id.print)
    CheckBox print;
    @BindView(R.id.confirmed)
    Button confirmed;

    private LoadDialog loadDialog;

    private boolean isRead;

    private List<Offer> list_offer = new ArrayList<>();
    private OfferExpandableAdapter adapter;

    private ReceiveInfo receiveInfo;

    private String phone;

    private String pricem_id;

    private OfferActivity offerActivity;

    private Dialog dialog_select;
    private Dialog dialog_longClick;
    private Dialog dialog_select_2;

    private int position_click;

    public MaintenanceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        offerActivity = (OfferActivity) context;

        int type = offerActivity.getIntent().getIntExtra("type", 0);

        isRead = offerActivity.isRead();
        if (isRead) {
            pricem_id = offerActivity.getCarInfo().getBf_quoted_pricem_id();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maintenance_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
//        车辆综合信息
        if (isRead) {
            confirm.setText("打印");
            confirmed.setVisibility(View.GONE);
            confirmSend.setVisibility(View.GONE);
        }

        initLv();
        initDialog();
        //车主隐打印按钮
        if (getArguments() != null) {
            if (!TextUtils.isEmpty(getArguments().getString("carwoner"))) {
                confirm.setVisibility(View.GONE);
                confirmed.setVisibility(View.GONE);
            }
        }
    }

//    派工、返回首页
    private void initDialog() {
        String[] array = new String[]{"派工","返回首页"};
        List<String> strings1 = Arrays.asList(array);
        dialog_select = creatListDialog("", strings1, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                Intent  intent = null;
                if (s.equals("派工")){
                    intent = new Intent(mContext,DispatchingActivity.class);
                }else if (s.equals("返回首页")){
                    mContent.finish();
                    return;
                }
                intent.putExtra(Key.CARNO,receiveInfo.getCarno());
                startActivity(intent);
                mContent.finish();
            }
        });

        String[] array2 = new String[]{"修改","删除"};
        List<String> strings2 = Arrays.asList(array2);
        dialog_longClick = creatListDialog("", strings2, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                if (s.equals("修改")){
                    Intent intent = new Intent(mContext, AddOfferItemActivity.class);
                    intent.putExtra(Key.OBJECT,list_offer.get(position_click));
                    intent.putExtra(Key.CARNO,receiveInfo.getCarno());
                    startActivityForResult(intent, REQUEST_CODE);
                }else if (s.equals("删除")){
                    list_offer.remove(position_click);
                    updatePart();
                    adapter.notifyDataSetChanged();
                }
            }
        });


        String[] array3 = new String[]{"保存","发送给客户"};
        List<String> strings3 = Arrays.asList(array3);
        dialog_select_2 = creatListDialog("", strings3, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                if (s.equals("保存")){
                    getordermtimecnt("3");
                }else if (s.equals("发送给客户")){
                    getordermtimecnt("0");
                }
            }
        });
    }

    private void initLv() {

        adapter = new OfferExpandableAdapter(list_offer, mContext);

        lv.setAdapter(adapter);


            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (!isRead){
                    position_click = position;
                    dialog_longClick.show();
                    }
                    return true;
                }
            });


    }

    public void setReceiveInfo(ReceiveInfo receiveInfo) {
        this.receiveInfo = receiveInfo;

        getordermstatusbyid(receiveInfo.getBf_receive_id());
    }


    /**
     * 2-52 保存报价主表、工时、配件清单接口 savelistquoteoverhaulorderm (userid,bf_receive_car_id,owen_id,name,mobile,carno,operatdate,
     * status,times_cnt,overhaulist,repairlist)
     * 当前用户ID:userid,接车表ID:bf_receive_car_id,车主ID:owen_id,车主姓名:name,车主手机号码:mobile,车牌号:carno,报价时间:operatdate,
     * 报价状态:status,第几次报价:times_cnt,维修项目清单列表:overhaulist,报价配件清单列表:repairlist
     * 报价人：operator
     *status 0=报价中 / 1=已报价
     is_agree 0=不同意 / 1=已同意


     2-92 保存报价信息接口，仅保存 savequotepricem (userid,bf_receive_car_id,owen_id,name,mobile,carno,operatdate,status,times_cnt,overhaulist,repairlist)
     *
     * @param times_cnt
     */
    private void savelistquoteoverhaulorderm(String times_cnt, final String status) {
        int i = Integer.parseInt(times_cnt);
        RequestParams params;
        if (status.equals("3")){
            params = creatParams("savequotepricem");
        }else
        params = creatParams("savelistquoteoverhaulorderm");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", receiveInfo.getBf_receive_id());
        params.addBodyParameter("owen_id", receiveInfo.getBc_car_owner_id());
        params.addBodyParameter("name", receiveInfo.getName());
        params.addBodyParameter("mobile", phone);
        params.addBodyParameter("carno", receiveInfo.getCarno());
        params.addBodyParameter("status",status);
        params.addBodyParameter("is_agree",status);
        params.addBodyParameter("times_cnt", i + 1 + "");
        params.addBodyParameter("overhaulist", "{\"data\":" + list_offer.toString() + "}");
        params.addBodyParameter("is_print", print.isChecked() ? "1" : "0");
        params.addBodyParameter("operator", user.getUser_name());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.DEPT_NAME, user.getDept_name());

        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")){
                    sendMan.setText(user.getUser_name());

                    if (status == "1"){
                        dialog_select.show();
                    }else if (status == "0"){
                       rongImSend(phone);
                        ToastUtil.showShort(mContent, result.getMsg());
                        return;
                    }else {
                        mContent.finish();
                    }

                }
                loadDialog.dismiss(mContent);
                ToastUtil.showShort(mContent, result.getMsg());

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });
    }

    public void addOffer(Offer offer) {
        list_offer.add(offer);
        adapter.notifyDataSetChanged();

        count();
    }


    /**
     * 2-52-A 报价接口--获取是第几次报价 getordermtimecnt(userid,bf_receive_car_id)
     * 当前用户ID:userid,接车表ID:bf_receive_car_id
     */
    private void getordermtimecnt(final String status) {
        if (receiveInfo == null){
            ToastUtil.showShort(mContent,"请选择报价车辆");
            return;
        }
        if (list_offer.size() == 0){
            ToastUtil.showShort(mContent,"请添加报价维修项目");
            return;
        }

        RequestParams params = creatParams("getordermtimecnt");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", receiveInfo.getBf_receive_id());

        loadDialog.show(mContent);
        HttpUtil.getInstance().post(params, ORMBean.OfferHistroyBean.class, new HttpUtil.HttpCallBack<ORMBean.OfferHistroyBean>() {
            @Override
            public void onSuccess(ORMBean.OfferHistroyBean result) {
                if (result.getRes().equals("1")) {
                    String times_cnt = result.getData().get(0).getTimes_cnt();

                    savelistquoteoverhaulorderm(times_cnt,status);
                } else if (result.getRes().equals("-1")) {
                    savelistquoteoverhaulorderm("0",status);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    /**
     * 2-63 车辆综合信息记录设置打印标记接口 setprintmark(userid,page_id,type)
     * 当前用户ID:userid,页签来源ID：page_id, 类别（1为接车单，2为报价单，3为结算单）:type
     */
    private void setprintmark() {
        RequestParams params = creatParams("setprintmark");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("page_id", pricem_id);
        params.addBodyParameter("type", "3");

        HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContent, result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-55-4 报价接口--根据接车表id查询出所有的报价信息（维修项目清单以及配件清单） actiondo:quoteallorderm,
     * 接车表ID:bf_receive_car_id,userid：978691E5-E3AC-4A26-96F7-EFDF28910712
     */
    private void quoteallorderm() {
        RequestParams params = creatParams("quoteallorderm");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", receiveInfo.getBf_receive_id());


        HttpUtil.getInstance().post(params, ORMBean.OfferBean.class, new HttpUtil.HttpCallBack<ORMBean.OfferBean>() {
            @Override
            public void onSuccess(ORMBean.OfferBean result) {
                if (result.getRes().equals("1")) {
                    List<Offer> data = result.getData();

                    list_offer.clear();
                    list_offer.addAll(data);

                    for (Offer o : list_offer) {
                        offerActivity.addRepairList(o.getRepairlist());
                    }

                    count();

                    adapter.notifyDataSetChanged();
                } else {
                    list_offer.clear();
                    adapter.notifyDataSetChanged();
                }

                loadDialog.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });
    }

    private void count() {
        Double price_part = 0.0;
        Double price_work = 0.0;

        for (Offer o : list_offer) {
            Double v = Double.parseDouble(o.getWorkamt());
            price_work += v;

            List<Part> repairlist = o.getRepairlist();
            for (Part r : repairlist) {
                double v1 = Double.parseDouble(r.getSaleprice()) * Integer.parseInt(r.getQty());
                price_part += v1;
            }
        }

        pricePart.setText(String.valueOf(price_part));
        priceCost.setText(String.valueOf(price_work));
        priceTotal.setText(String.valueOf(price_work + price_part));
    }


    /**
     * 2-55-5 根据接车单id以及是否作废 查询报价状态和报价人actiondo:getordermstatusbyid(bf_receive_car_id,userid)
     */
    private void getordermstatusbyid(String id) {
        RequestParams params = creatParams("getordermstatusbyid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", id);

        loadDialog.show(mContent);
        HttpUtil.getInstance().post(params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                if (result.getRes().equals("1")) {
                    CarInfo carInfo = result.getData().get(0);

                    if (carInfo.getStatus().equals("1")) {
                        if (isRead){
                            confirm.setText("打印");
                        }else {
                            confirm.setText("已确定报价  点击新增报价");
                            confirmSend.setVisibility(View.GONE);
//                        禁掉点击事件
                            isRead =true;
                        }
                        confirmed.setVisibility(View.GONE);
                        offerActivity.setbtnVIsible(View.GONE);

                    }
                    sendMan.setText(carInfo.getOperator());
                }

                quoteallorderm();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });
    }

    @Override
    public void clear() {
        super.clear();
        confirmed.setVisibility(View.VISIBLE);
        confirmSend.setVisibility(View.VISIBLE);
        confirm.setText("保存");
        pricePart.setText("");
        priceCost.setText("");
        priceTotal.setText("");
        sendMan.setText("");
        isRead =false;
    }

    @OnClick({R.id.confirm_save, R.id.confirmed,R.id.confirm_send})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.confirm_save:
                if (confirm.getText().equals("打印")) {
                    setprintmark();
                } else {
                    if (confirm.getText().equals("已确定报价  点击新增报价")) {
                        new AlertDialog.Builder(mContent).setMessage("是否新增报价？").setPositiveButton("取消", null).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                offerActivity.setbtnVIsible(View.VISIBLE);
                                sendMan.setText("");
                                confirmed.setVisibility(View.VISIBLE);
                                confirm.setText("保存");
                                confirmSend.setVisibility(View.VISIBLE);
                                isRead = false;
                            }
                        }).show();
                    } else
                        getordermtimecnt("3");
                }
                break;
            case R.id.confirmed:
                getordermtimecnt("1");
                break;
            case R.id.confirm_send:
                getordermtimecnt("0");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == mContent.RESULT_OK) {
            Offer offer = (Offer) data.getSerializableExtra(Key.OBJECT);
            Offer offer1 = list_offer.get(position_click);

            offer1.setItemt_name(offer.getItemt_name());
            offer1.setWorkamt(offer.getWorkamt());
            offer1.setRepairlist(offer.getRepairlist());
            offer1.setItemt_id(offer.getItemt_id());

            updatePart();

            adapter.notifyDataSetChanged();
            count();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public  void   updatePart(){
        List<Part>  partList = new ArrayList<>();
        for (Offer offer:list_offer){
            partList.addAll(offer.getRepairlist());
        }
        offerActivity.updateRepairList(partList);
    }


    private void rongImSend(String phone) {

        TextMessage myTextMessage = TextMessage.obtain("您好，您在【"+user.getDept_name()+"】维修厂维修的车辆已经报价，请点击【我的维修】进入去确认报价");

//"7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
//Conversation.ConversationType.PRIVATE 为会话类型。
        Message myMessage = Message.obtain(phone, Conversation.ConversationType.PRIVATE, myTextMessage);
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
            loadDialog.dismiss(mContext);
            mContent.finish();
        }

        @Override
        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            //消息发送失败的回
            loadDialog.dismiss(mContext);
            ToastUtil.showShort(mContext, "提醒车主报价失败");
        }

    });
   }




}
