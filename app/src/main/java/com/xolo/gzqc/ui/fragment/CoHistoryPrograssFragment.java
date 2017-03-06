package com.xolo.gzqc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.adapter.CoPrograssDetilesAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CoMaintainDetile;
import com.xolo.gzqc.bean.child.CoMaintainPrograss;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class CoHistoryPrograssFragment extends  LazyFragment{
    List<CarOwner_Add_Maintain> list = new ArrayList<>();
    CarOwner_add_MaintainAdapter carOwnersadapter;
    ScrollListView add_insurance_lv, lv_inventory;
    //CoCarOrderAdapter coCarOrderAdapter;
    List<CoMaintainPrograss> listprograss = new ArrayList<>();
    TextView selete_bt;
    CoPrograssDetilesAdapter coPrograssDetilesAdapter;
    List<CoMaintainDetile> coMaintainDetiles = new ArrayList<>();
    String bf_receive_id;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return View.inflate(getActivity(),R.layout.fragment_maintain_progress,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        iniView(view);
        super.onViewCreated(view, savedInstanceState);
    }
    

    @Override
    protected void loadData() {
            if(needLoad()){
                getData();
                getDataDetail();
                mHasLoadedOnce=true;
            }
    }

    @Override
    protected void init() {

    }
    private void iniView(View view) {
        user = SPManager.getUser(mContext);
        bf_receive_id=getArguments().getString("bf_receive_id");
        lv_inventory = (ScrollListView) view.findViewById(R.id.lv_inventory);
        add_insurance_lv = (ScrollListView) view.findViewById(R.id.add_insurance_lv);
        list.add(new CarOwner_Add_Maintain("车牌:", false));
        list.add(new CarOwner_Add_Maintain("车型:", false));
        list.add(new CarOwner_Add_Maintain("来车时间:", false));
        list.add(new CarOwner_Add_Maintain("维修状态:", false));
        list.add(new CarOwner_Add_Maintain("是否结算:", false));
        carOwnersadapter = new CarOwner_add_MaintainAdapter(getActivity(),
                R.layout.fragment_add_maintain_tem, list);
        add_insurance_lv.setAdapter(carOwnersadapter);
        coPrograssDetilesAdapter = new CoPrograssDetilesAdapter(getActivity(),
                R.layout.item_co_maintain_prograss_detile, coMaintainDetiles);
        lv_inventory.setAdapter(coPrograssDetilesAdapter);

    }

    void getData() {
        LoadDialog.show(getActivity());
        RequestParams requestParams = creatParams("getrepairplan");
        requestParams.addBodyParameter("bf_receive_id", bf_receive_id);;
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoMaintainPrograssBean.class, new HttpUtil.HttpCallBack<ORMBean.CoMaintainPrograssBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }
            @Override
            public void onSuccess(ORMBean.CoMaintainPrograssBean result) {
                if (result.getRes().equals("1")) {
                    list.get(0).setEttext(result.getData().get(0).getCarno());
                    list.get(1).setEttext(result.getData().get(0).getTypecode());
                    list.get(2).setEttext(result.getData().get(0).getIn_time());
                    list.get(3).setEttext("维修中");
                    if(TextUtils.isEmpty(result.getData().get(0).getSet_finished())){
                        list.get(4).setEttext("未结算");
                    }else  if(result.getData().get(0).getSet_finished().equals("1")){
                        list.get(4).setEttext("已结算");
                    }
                } else {
                    ToastUtil.showLong(getActivity(), result.getMsg());
                }
                carOwnersadapter.notifyDataSetChanged();
                LoadDialog.dismiss(getActivity());

            }
        });

    }

    void getDataDetail() {
        LoadDialog.show(getActivity());
        RequestParams requestParams = creatParams("getrepairplandetail");
        requestParams.addBodyParameter("bf_receive_car_id", bf_receive_id);
        requestParams.addBodyParameter("userid", user.getUser_id());

        HttpUtil.getInstance().post(requestParams, ORMBean.CoMaintainDetileBean.class, new HttpUtil.HttpCallBack<ORMBean.CoMaintainDetileBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }
            @Override
            public void onSuccess(ORMBean.CoMaintainDetileBean result) {
                if (result.getRes().equals("1")) {
                    coMaintainDetiles.clear();
                    coMaintainDetiles.add(new CoMaintainDetile("项目", "班主", "完工"));
                    coMaintainDetiles.addAll(result.getData());
                } else {
                    coMaintainDetiles.clear();
                    ToastUtil.showLong(getActivity(), result.getMsg());
                }
                coPrograssDetilesAdapter.notifyDataSetChanged();
                LoadDialog.dismiss(getActivity());
            }
        });

    }
}
