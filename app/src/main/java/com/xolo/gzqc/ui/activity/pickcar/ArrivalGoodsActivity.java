package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.PurchaseOrder;
import com.xolo.gzqc.bean.postJson.ArrivalGoods;
import com.xolo.gzqc.bean.postJson.InquiryPart;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
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
 * 构件到货
 */
public class ArrivalGoodsActivity extends BaseActivity {

    @BindView(R.id.car_nunber)
    TextView carNunber;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.brand)
    TextView brand;
    @BindView(R.id.models)
    TextView models;
    @BindView(R.id.btn_more)
    TextView btnMore;
    @BindView(R.id.displacement)
    TextView displacement;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.drive)
    TextView drive;
    @BindView(R.id.fuel)
    TextView fuel;
    @BindView(R.id.operation)
    TextView operation;
    @BindView(R.id.vin_code)
    TextView vinCode;
    @BindView(R.id.engine)
    TextView engine;
    @BindView(R.id.login_data)
    TextView loginData;
    @BindView(R.id.tl_hidden)
    TableLayout tlHidden;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.titleview)
    TitleView titleview;
    private int type;

    private List<CarInfo> list_carInfo = new ArrayList<>();
    private List<InquiryPart> list_part = new ArrayList<>();
    private List<ArrivalGoods> list_arrival = new ArrayList<>();

    private Dialog dialog_carno;
    private LoadDialog dialog_loding;

    private CommenAdapter<InquiryPart> adapter;

    private PurchaseOrder carInfo;

    private boolean isHidden = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_goods);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        initLv();
        initDialog();
        initIntent();
    }

    private void initTitle() {
        titleview.setRightText("接单");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  acceptbuym();
            }
        });
    }


    // type:
//     0 .接车员
    // 1.采购员
    private void initIntent() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        if (type == 1) {
            select.setVisibility(View.GONE);

            setHint(new TextView[]{displacement,fuel,drive,loginData,operation,carNunber,brand,models,year,vinCode,engine});
            if (!TextUtils.isEmpty(intent.getStringExtra(Key.CARNO))){
                getnewbuym(intent.getStringExtra(Key.CARNO));
            }else
            getbuymbyid(intent.getStringExtra("buy_id"));
        }

    }

    private void initDialog() {
        dialog_carno = creatListDialog("车牌号", list_carInfo, carNunber, new ListDialogCallBack<CarInfo>() {
            @Override
            public String setText(CarInfo carInfo) {
                return carInfo.getCarno();
            }

            @Override
            public void onClick(CarInfo carInfo) {
                clear();
                getbuymbyid(carInfo.getBf_buym_id());
            }
        });
    }


    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_part_arrivalgood, mContext, list_part, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, final int position) {
                InquiryPart part = list_part.get(position);


                holder.setText(R.id.item1,part.getParts_name());
                holder.setText(R.id.item2,part.getCostprice());
                holder.setText(R.id.item3,"x "+part.getQty()+" ( "+part.getQty_unit()+" )");
                holder.setText(R.id.item4,part.getSource());

                double total = Double.parseDouble(part.getCostprice()) * Integer.parseInt(part.getQty());
                holder.setText(R.id.item5,"￥"+String.valueOf(total));

                CheckBox cb = (CheckBox) holder.getView(R.id.item6);
                cb.setVisibility(View.VISIBLE);

                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            list_part.get(position).setIs_all_arrival("1");
                        } else {
                            list_part.get(position).setIs_all_arrival("0");
                        }
                    }

                });

                if (list_part.get(position).getIs_all_arrival().equals("1")) {

                    cb.setChecked(true);
                } else {
                    cb.setChecked(false);
                }

            }
        });
        lv.setAdapter(adapter);
    }


    /**
     * 通过车牌号获取该车的车辆信息接口 getcarownerbycarno(userid,carno) 当前用户ID:userid,车牌号:carno
     */
//    private void getcarinfobycarno(final String no) {
//        RequestParams params = creatParams("getcarinfobycarno");
//        params.addBodyParameter(Key.USER_ID, user.getUser_id());
//        params.addBodyParameter(Key.CARNO, no);
//
//        dialog_loding.show(mContext);
//
//        HttpUtil.getInstance().post(params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
//            @Override
//            public void onSuccess(ORMBean.CarInfoBean result) {
//                if (result.getRes().equals("1")) {
//                    CarInfo carInfo = result.getData().get(0);
//
//                    carNunber.setText(carInfo.getCarno());
//                    brand.setText(carInfo.getBrands());
//                    models.setText(carInfo.getTypecode());
//                    year.setText(carInfo.getProductyear());
//                    vinCode.setText(carInfo.getVincode());
//
//                    displacement.setText(carInfo.getOutput());
//                    drive.setText(carInfo.getDrive_type());
//                    fuel.setText(carInfo.getFuel());
//                    operation.setText(carInfo.getOperation_type());
//                    loginData.setText(carInfo.getReg_date());
//                    engine.setText(carInfo.getEnginecode());
//
//                    getnewbuym(carInfo.getCarno());
//                } else {
//                    ToastUtil.showShort(mContext, result.getMsg());
//                    dialog_loding.dismiss(mContext);
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                dialog_loding.dismiss(mContext);
//            }
//        });
//    }

    /**
     * 2-41 根据车牌号获取本维修厂该车最新的购件单接口 getnewbuym(userid,dept_id,carno)
     * 当前用户ID:userid,维修厂ID:dept_id,车牌号:carno
     */
    private void getnewbuym(String carno) {
        RequestParams params = creatParams("getnewbuym");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, carno);
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());


        HttpUtil.getInstance().post(params, ORMBean.PurchaseOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.PurchaseOrderBean>() {
            @Override
            public void onSuccess(ORMBean.PurchaseOrderBean result) {
                if (result.getRes().equals("1")) {
                    PurchaseOrder carInfo1 = result.getData().get(0);

                    carInfo = carInfo1;

                    carNunber.setText(carInfo.getCarno());
                    brand.setText(carInfo.getBrands());
                    models.setText(carInfo.getTypecode());
                    year.setText(carInfo.getProductyear());
                    vinCode.setText(carInfo.getVincode());

                    displacement.setText(carInfo.getOutput());
                    drive.setText(carInfo.getDrive_type());
                    fuel.setText(carInfo.getFuel());
                    operation.setText(carInfo.getOperation_type());
                    loginData.setText(carInfo.getReg_date());
                    engine.setText(carInfo.getEnginecode());

                    getlistrepairparts();
                } else {
                    ToastUtil.showShort(mContext, "配件清单为空");
                    dialog_loding.dismiss(mContext);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });
    }


    /**
     * 2-45 根据购件单ID获取配件清单接口 getlistrepairparts(userid,buym_id)
     * 当前用户ID:userid,购件单ID:buym_id
     */
    private void getlistrepairparts() {
        RequestParams params = creatParams("getlistrepairparts");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.BUYM_ID, carInfo.getBf_buym_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.InquiryPartBean.class, new HttpUtil.HttpCallBack<ORMBean.InquiryPartBean>() {
            @Override
            public void onSuccess(ORMBean.InquiryPartBean result) {
                if (result.getRes().equals("1")) {
                    List<InquiryPart> data = result.getData();
                    list_part.clear();
                    list_part.addAll(data);
                    adapter.notifyDataSetChanged();

//                    is_edit = true;
//                    confirm.setText("修改");
                } else {
//                    is_edit = false;
//                    confirm.setText("配件询价");
                    ToastUtil.showShort(mContext, "配件清单为空");
                }
                dialog_loding.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });
    }


    @OnClick({R.id.select, R.id.confirm, R.id.btn_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select:
                if (list_carInfo.size() > 0) {
                    dialog_carno.show();
                    return;
                }
                listcarinfo();
                break;
            case R.id.confirm:
                if (list_part.size() == 0) {
                    ToastUtil.showShort(mContext, "配件清单不能为空");
                    return;
                }
                noticereceiveman();
                break;
            case R.id.btn_more:
                if (isHidden) {
                    btnMore.setText("隐藏更多");
                    tlHidden.setVisibility(View.VISIBLE);
                    isHidden = false;
                } else {
                    btnMore.setText("显示更多");
                    tlHidden.setVisibility(View.GONE);
                    isHidden = true;
                }
                break;
        }
    }


    /**
     * 4-8 获取本部门的购件到货选择车辆接口 getlistbuymcarno(userid,dept_id)
     * 当前用户ID：userid,维修厂ID：dept_id
     */
    private void listcarinfo() {
        RequestParams params = creatParams("getlistbuymcarno");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                List<CarInfo> data = result.getData();
                list_carInfo.addAll(data);

                dialog_carno.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-47 通知接车员接口 noticereceiveman(userid,json_parts)
     * 当前用户ID:userid,配件请单列表：json_parts
     */
    private void noticereceiveman() {

        if (TextUtils.isEmpty(getText(carNunber))) {
            ToastUtil.showShort(mContext, "请选择车牌号");
            return;
        }

        list_arrival.clear();
        for (InquiryPart part : list_part) {
            ArrivalGoods arrivalGoods = new ArrivalGoods();
            arrivalGoods.setBuyd_id(part.getBf_buyd_id());
            arrivalGoods.setIs_all_arrival(part.getIs_all_arrival());

            list_arrival.add(arrivalGoods);
        }

        RequestParams params = creatParams("noticereceiveman");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, getText(carNunber));
        params.addBodyParameter(Key.BUYM_ID, carInfo.getBf_buym_id());
        params.addBodyParameter("json_parts", "{\"data\":" + list_arrival.toString() + "}");

        dialog_loding.show(mContext);

        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {

                if (result.getRes().equals("1")) {
//                    1  需要提醒接车员。消息（即时通讯）
                    if (type == 1) {
                        rongImSend(carInfo.getMobile());
                    } else {
                        dialog_loding.dismiss(mContext);
                        finish();
                    }
                } else {
                    dialog_loding.dismiss(mContext);
                }

                ToastUtil.showShort(mContext, result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });
    }


    private void clearDate() {
        carNunber.setText("");
        brand.setText("");
        models.setText("");
        year.setText("");
        vinCode.setText("");

        displacement.setText("");
        drive.setText("");
        fuel.setText("");
        operation.setText("");
        loginData.setText("");
        engine.setText("");

        list_part.clear();
        adapter.notifyDataSetChanged();
    }


    private void rongImSend(String phone) {

        TextMessage myTextMessage = TextMessage.obtain("你有新的配件到货");

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
                dialog_loding.dismiss(mContext);
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回
                dialog_loding.dismiss(mContext);
                ToastUtil.showShort(mContext, "提醒接车员失败");
            }

        });
    }

    public void clear() {
        brand.setText("");
        models.setText("");
        year.setText("");
        vinCode.setText("");

        displacement.setText("");
        drive.setText("");
        fuel.setText("");
        operation.setText("");
        loginData.setText("");
        engine.setText("");

        list_part.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     *    4-9 采购员接单接口 acceptbuym(userid,buym_id)
     当前用户ID：userid,采购单ID:buym_id
     */
    private  void  acceptbuym(){
        RequestParams params = creatParams("acceptbuym");
        params.addBodyParameter("buym_id",carInfo.getBf_buym_id());

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                  ToastUtil.showShort(mContext,result.getMsg());
                titleview.setRightVisibility(View.GONE);
                confirm.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 4-4 根据购件单ID获取购件单信息接口 getbuymbyid(userid,buym_id)
     当前用户ID：userid,购件单ID:buym_id
     * @param buy_id
     */
       private void getbuymbyid(final String buy_id){
           RequestParams params = creatParams("getbuymbyid");
           params.addBodyParameter("buym_id",buy_id);

           HttpUtil.getInstance().post( params, ORMBean.PurchaseOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.PurchaseOrderBean>() {
               @Override
               public void onSuccess(ORMBean.PurchaseOrderBean result) {
                   if (result.getRes().equals("1")) {
                       PurchaseOrder carInfo1 = result.getData().get(0);

                       carInfo = carInfo1;

                       carInfo.setBf_buym_id(buy_id);

                       carNunber.setText(carInfo.getCarno());
                       brand.setText(carInfo.getBrands());
                       models.setText(carInfo.getTypecode());
                       year.setText(carInfo.getProductyear());
                       vinCode.setText(carInfo.getVincode());

                       displacement.setText(carInfo.getOutput());
                       drive.setText(carInfo.getDrive_type());
                       fuel.setText(carInfo.getFuel());
                       operation.setText(carInfo.getOperation_type());
                       loginData.setText(carInfo.getReg_date());
                       engine.setText(carInfo.getEnginecode());

                       //采购员接单功能，无人接的单显示头部接单按钮，已接单的只有接单人才能操作
                       if (type == 1){
                           String accept_userid = carInfo.getAccept_userid();
                           if (!TextUtils.isEmpty(accept_userid)){
                               if (!accept_userid.equals(user.getUser_id())){
                                   ToastUtil.showShort(mContext,"已被别人接单");
                                   confirm.setVisibility(View.GONE);
                               }else
                                   confirm.setVisibility(View.VISIBLE);
                           }else{
                               initTitle();
                               confirm.setVisibility(View.GONE);
                           }
                       }


                       getlistrepairparts();
                   } else {
                       ToastUtil.showShort(mContext, "配件清单为空");
                       dialog_loding.dismiss(mContext);
                   }
               }

               @Override
               public void onError(Throwable ex, boolean isOnCallback) {
                   dialog_loding.dismiss(mContext);
               }
           });
       }

}


