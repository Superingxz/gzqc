package com.xolo.gzqc.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.adapter.ExpandableListViewAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CoAccessories;
import com.xolo.gzqc.bean.child.CoProject;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.CustomExpandableListView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的维修详情
 * Created by Administrator on 2016/10/16.
 */
public class CoMaintainDetails extends BaseActivity {
    List<CarOwner_Add_Maintain> list = new ArrayList<>();
    CustomExpandableListView exp;
    ScrollListView add_insurance_lv;
    CarOwner_add_MaintainAdapter carOwnersadapter;
    String bf_receive_id;
    List<CoProject> coProjects = new ArrayList<>();
    ExpandableListViewAdapter expandableListViewAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_maintain_details);
        initView();
        getData();
    }

    void initView() {
        exp=(CustomExpandableListView)findViewById(R.id.exp);
        bf_receive_id=getIntent().getStringExtra("bf_receive_id");
        add_insurance_lv = (ScrollListView) findViewById(R.id.add_insurance_lv);
        list.add(new CarOwner_Add_Maintain("车牌:", false));
        list.add(new CarOwner_Add_Maintain("品牌:", false));
        list.add(new CarOwner_Add_Maintain("车型:", false));
        list.add(new CarOwner_Add_Maintain("到厂时间:", false));
        list.add(new CarOwner_Add_Maintain("离场时间:", false));
        list.add(new CarOwner_Add_Maintain("接车员:", false));
        list.add(new CarOwner_Add_Maintain("配件费:", false));
        list.add(new CarOwner_Add_Maintain("工时费:", false));
        list.add(new CarOwner_Add_Maintain("总金额:", false));
        list.add(new CarOwner_Add_Maintain("折扣比例:", false));
        list.add(new CarOwner_Add_Maintain("优惠金额:", false));
        list.add(new CarOwner_Add_Maintain("实际金额:", false));
        carOwnersadapter = new CarOwner_add_MaintainAdapter(this,
                R.layout.fragment_add_maintain_tem, list);
        add_insurance_lv.setAdapter(carOwnersadapter);

    }


    void getData() {
        LoadDialog.show(CoMaintainDetails.this);
        RequestParams requestParams = creatParams("getrepairplan");
        requestParams.addBodyParameter("set_finished","1");
        requestParams.addBodyParameter("bf_receive_id",bf_receive_id);
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoMaintainDetailsBean.class, new HttpUtil.HttpCallBack<ORMBean.CoMaintainDetailsBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }
            @Override
            public void onSuccess(ORMBean.CoMaintainDetailsBean result) {
                if (result.getRes().equals("1")) {
                    list.get(0).setEttext(result.getData().get(0).getCarno());
                    list.get(1).setEttext(result.getData().get(0).getBrands());
                    list.get(2).setEttext(result.getData().get(0).getTypecode());
                    list.get(3).setEttext(result.getData().get(0).getIn_time());
                    list.get(4).setEttext(result.getData().get(0).getGive_date());
                    list.get(5).setEttext(result.getData().get(0).getOperatoname());
                } else {
                    ToastUtil.showLong(CoMaintainDetails.this, result.getMsg());
                }
                carOwnersadapter.notifyDataSetChanged();
                LoadDialog.dismiss(CoMaintainDetails.this);
                getTtalWorkamt();
                getDataParent();

            }
        });

    }

    //获取维修项目父类数据
    void getDataParent() {
        LoadDialog.show(CoMaintainDetails.this);
        RequestParams requestParams = creatParams("ownerquoteverify");
        requestParams.addBodyParameter("carno", list.get(0).getEttext());
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
                    expandableListViewAdapter = new ExpandableListViewAdapter(CoMaintainDetails.this, coProjects);
                    exp.setAdapter(expandableListViewAdapter);
                    exp.setVisibility(View.VISIBLE);
                    //获取子类数据
                    for (int i = 0; i < coProjects.size(); i++) {
                        getDataChild(i, coProjects.get(i).getBf_quoted_priced_id());
                    }
                } else {
                    ToastUtil.showLong(CoMaintainDetails.this, result.getMsg());
                    exp.setVisibility(View.GONE);
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }

    //获取子类数据
    void getDataChild(final int position, String bf_quoted_priced_id) {
        LoadDialog.show(CoMaintainDetails.this);
        RequestParams requestParams = creatParams("quotereparibymaintenanceid");
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("bf_quoted_priced_id", bf_quoted_priced_id);
        requestParams.addBodyParameter("is_deleted", "0");
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

                    ToastUtil.showLong(CoMaintainDetails.this, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }
    //获取工时信息
    void getTtalWorkamt() {
        LoadDialog.show(CoMaintainDetails.this);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getrepairsum");
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("carno", list.get(0).getEttext());

        HttpUtil.getInstance().post(requestParams, ORMBean.CoCoTotalworkamtBean.class, new HttpUtil.HttpCallBack<ORMBean.CoCoTotalworkamtBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoCoTotalworkamtBean result) {
                if (result.getRes().equals("1")) {
                    list.get(6).setEttext(result.getData().get(0).getTotalworkamt());
                    list.get(7).setEttext(result.getData().get(0).getTotalsaleprice());
                    list.get(8).setEttext(result.getData().get(0).getTotalprice());
//                    material_price_text.setText("配件费：" + result.getData().get(0).getTotalworkamt());
//                    hour_price_text.setText("工时费：" + result.getData().get(0).getTotalsaleprice());
//                    totle_price_text.setText("总金额：" + result.getData().get(0).getTotalprice());
                } else {
                    ToastUtil.showLong(CoMaintainDetails.this, result.getMsg());
                }
                carOwnersadapter.notifyDataSetChanged();
                LoadDialog.dismiss(mContext);
            }
        });

    }

}

