package com.xolo.gzqc.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.adapter.CoSheduliAdapter;
import com.xolo.gzqc.alipay.Pay;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.bean.child.CarnoInfo;
import com.xolo.gzqc.bean.child.CoHistroyMaintainState;
import com.xolo.gzqc.bean.child.CoSheduli;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 购件进度
 * Created by Administrator on 2016/9/27.
 */
public class CoSheduliFragment extends LazyFragment {
    List<CarOwner_CarInfo> carlist;
    List<String> liststring = new ArrayList<String>();
    CarnoInfo carnoInfo;
    ScrollListView lv, lv_inventory;
    CarOwner_add_MaintainAdapter adapter;
    List<CoHistroyMaintainState> listco = new ArrayList<>();
    CoSheduliAdapter coSheduliAdapter;
    Dialog dialog;
    Dialog startdialog, enddialog;
    Button pay_bt;
    WebView webviwe;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_co_schedule, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
        super.onViewCreated(view, savedInstanceState);
    }

    List<CarOwner_Add_Maintain> list;

    private void initView(View view) {
        webviwe=(WebView)view.findViewById(R.id.webviwe);
        pay_bt=(Button)view.findViewById(R.id.pay_bt);
        pay_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayData();
            }
        });
        user = SPManager.getUser(mContext);
        lv_inventory = (ScrollListView) view.findViewById(R.id.lv_inventory);
        lv = (ScrollListView) view.findViewById(R.id.lv);
        list = new ArrayList<>();
        list.add(new CarOwner_Add_Maintain("车 牌 号:", true));
        list.add(new CarOwner_Add_Maintain("姓名:", false));
        list.add(new CarOwner_Add_Maintain("手机:", false));
        list.add(new CarOwner_Add_Maintain("预约时间:", false));
        list.add(new CarOwner_Add_Maintain("计划来车:", false));
        list.add(new CarOwner_Add_Maintain("故障描述:", false));
        list.add(new CarOwner_Add_Maintain("总金额:", false));
        adapter = new CarOwner_add_MaintainAdapter(getActivity(), R.layout.fragment_add_maintain_tem, list);
        lv.setAdapter(adapter);
        adapter.setImaOnlickiface(new CarOwner_add_MaintainAdapter.ImaOnlickIface() {
            @Override
            public void rightImgOnlic(final int position) {
                if (position == 0) {
                    if (dialog == null) {
                        dialog = creatListDialog("选择车牌", liststring, new listDialogFace() {
                            @Override
                            public void retrunTest(String text) {
                                if (TextUtils.isEmpty(list.get(position).getEttext())) {
                                    list.get(position).setEttext(text);
                                    getDataInfo(text);
                                    getDataTotle(text);
                                } else {
                                    if ((!list.get(position).getEttext().equals(text))) {
                                        list.get(position).setEttext(text);
                                        getDataInfo(text);
                                        getDataTotle(text);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                        });
                    }
                    dialog.show();
                }
            }

            @Override
            public void changeEdit(int position, String st) {

            }
        });
        listco = new ArrayList<>();
        coSheduliAdapter = new CoSheduliAdapter(getActivity(), R.layout.item_co_inventory, listco);
        lv_inventory.setAdapter(coSheduliAdapter);
    }

    @Override
    protected void loadData() {
        if (needLoad()) {
            getData();
            mHasLoadedOnce = true;
        }
    }

    @Override
    protected void init() {

    }
        boolean is_deal;

    void getDataInfo(final String carno) {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getsubscribeprogressinfo");
        requestParams.addBodyParameter("carno", carno);
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("is_buy_fitting", "1");
        HttpUtil.getInstance().post(requestParams, ORMBean.CoComponentProgressBean.class, new HttpUtil.HttpCallBack<ORMBean.CoComponentProgressBean>() {
            @Override
            public void onSuccess(ORMBean.CoComponentProgressBean result) {
                if (result.getRes().equals("1")) {
                    if(result.getData().get(0).getIs_deal().equals("0")){
                        is_deal=true;
                    }else {
                        is_deal=false;
                    }
                    list.get(1).setEttext(result.getData().get(0).getName());
                    list.get(2).setEttext(result.getData().get(0).getPhone());
                    list.get(3).setEttext(result.getData().get(0).getOperatdate());
                    list.get(4).setEttext(result.getData().get(0).getPlan_com_time());
                    list.get(5).setEttext(result.getData().get(0).getFault_description());
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (i != 0)
                            list.get(i).setEttext("");
                    }
                }
                getDataList(carno);
                adapter.notifyDataSetChanged();
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }


        });
    }

    void getDataTotle(String carno) {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getlistpartstotalamount");
        requestParams.addBodyParameter("carno", carno);
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoSheduliTotlePriceBean.class, new HttpUtil.HttpCallBack<ORMBean.CoSheduliTotlePriceBean>() {
            @Override
            public void onSuccess(ORMBean.CoSheduliTotlePriceBean result) {
                if (result.getRes().equals("1")) {
                    list.get(6).setEttext(result.getData().get(0).getTotalamount());
                    ispay=true;
                } else {
                    ispay=false;
                    list.get(6).setEttext("");
                    ToastUtil.showLong(getActivity(), result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }


        });
    }
    void getDataList(String carno) {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getlistpartsprogress");
        requestParams.addBodyParameter("carno", carno);
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoHistroyMaintainStateBean.class, new HttpUtil.HttpCallBack<ORMBean.CoHistroyMaintainStateBean>() {
            @Override
            public void onSuccess(ORMBean.CoHistroyMaintainStateBean result) {
                if (result.getRes().equals("1")) {
                    listco.clear();
                    listco.add( new CoHistroyMaintainState("状态","总价","单价","配件名","数量"));
                    listco.addAll(result.getData());
                    if(is_deal){
                        for(CoHistroyMaintainState coHistroyMaintainState:  listco){
                            coHistroyMaintainState.setStatus("0");
                        }
                    }else{
                        for(CoHistroyMaintainState coHistroyMaintainState:  listco){
                            coHistroyMaintainState.setStatus("1");
                        }
                    }
                    ispay=true;
                } else {
                    ispay=false;
                    listco.clear();
                    ToastUtil.showLong(getActivity(), result.getMsg());
                }
                coSheduliAdapter.notifyDataSetChanged();
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }


        });
    }
    boolean ispay;
    void getPayData(){
        if(!ispay){
            return;
        }
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getalipayorderinfo");
        requestParams.addBodyParameter("amount", list.get(6).getEttext());
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("subject", "1");
        requestParams.addBodyParameter("body", "购买活塞");
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
            @Override
            public void onSuccess(BaseBean result) {
                Pay pay = new Pay(getActivity());
                pay.setAliPayIface(new Pay.AliPayIface() {
                    @Override
                    public void paySuccee() {
                        ToastUtil.show(getActivity(), "付款成功", 1);
                    }
                    @Override
                    public void failure(String resultStatus) {
                       // ToastUtil.show(getActivity(), "付款失败" + resultStatus, 1);
                    }
                });
                pay.PayOrder(result.getMsg());
                LoadDialog.dismiss(mContext);
            }
        });
    }
    void getData() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getownercarinfo");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());

        HttpUtil.getInstance().post(requestParams, ORMBean.CoCarInfo.class, new HttpUtil.HttpCallBack<ORMBean.CoCarInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }

            @Override
            public void onSuccess(ORMBean.CoCarInfo result) {
                if (result.getRes().equals("1")) {
                    carlist = result.getData();
                    for (CarOwner_CarInfo code : carlist) {
                        liststring.add(code.getCarno());
                    }
                    if(result.getData().size()==1){
                        list.get(0).setEttext(result.getData().get(0).getCarno());
                        getDataInfo(result.getData().get(0).getCarno());
                        getDataTotle(result.getData().get(0).getCarno());
                    }

                } else {
                    ToastUtil.showLong(getContext(), result.getMsg());
                }

                LoadDialog.dismiss(mContext);
            }
        });

    }
}
