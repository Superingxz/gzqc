package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.MaintenanceInfo;
import com.xolo.gzqc.bean.child.Ordermbuyparts;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.ScrollGridView;
import com.xolo.gzqc.ui.view.ScrollListView;
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


/**
 * 维修清单
 */
public class MaintenanceListActivity extends BaseActivity {

    @BindView(R.id.carno)
    TextView carno;
    @BindView(R.id.brand)
    TextView brand;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.btn_1)
    Button btn1;
    @BindView(R.id.btn_2)
    Button btn2;
    @BindView(R.id.gv)
    ScrollGridView gv;

    private Ordermbuyparts order;

    private int type;

    private List<MaintenanceInfo> list = new ArrayList<>();
    private List<Control> list_control = new ArrayList<>();

    private CommenAdapter<MaintenanceInfo> adapter;

    private Dialog dialog;

    private int position_targer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initIntent();
        initLv();
        initDialog();

        getlistrepairproject();
    }

    private void initIntent() {
        Intent intent = getIntent();
        order = (Ordermbuyparts) intent.getSerializableExtra(Key.OBJECT);

        carno.setText(order.getCarno());
        brand.setText(order.getBrands() + "  " + order.getTypecode());

        type = intent.getIntExtra("type", 0);

        if (type == 1) {
         btn1.setVisibility(View.GONE);
         btn2.setVisibility(View.GONE);
        }

    }

    private void initDialog() {
        dialog = creatListDialog("帮组", list_control, null, new ListDialogCallBack<Control>() {
            @Override
            public String setText(Control control) {
                return control.getDisplay_data();
            }

            @Override
            public void onClick(Control control) {
                MaintenanceInfo maintenanceInfo = list.get(position_targer);
                maintenanceInfo.setDefault_team_id(control.getControl_id());
                maintenanceInfo.setDefault_team_name(control.getDisplay_data());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_maintenance_list, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, final int position) {
                final MaintenanceInfo maintenanceInfo1 = list.get(position);

                holder.setText(R.id.item1, "维修项目：" + maintenanceInfo1.getItemt_name());
                holder.setText(R.id.item2, "状态：" + maintenanceInfo1.getStatus());
                holder.setText(R.id.item3, maintenanceInfo1.getDefault_team_name());
                holder.setImage(R.id.iv,R.mipmap.item_iv);

                holder.getView(R.id.item5).setVisibility(View.GONE);
                holder.getView(R.id.item4).setVisibility(View.GONE);

                final TextView view = holder.getView(R.id.item3);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list_control.size() > 0) {
                            position_targer = position;
                            dialog.show();
                        } else {
                            getcontrolbytype("8", position);
                        }
                    }
                });
            }
        });
        lv.setAdapter(adapter);
    }


    /**
     * 2-38 通过报价单ID获取本次报价的维修项目清单列表接口 getlistrepairproject(userid,quoted_pricem_id)
     * 当前用户ID:userid,报价单ID:quoted_pricem_id
     */
    private void getlistrepairproject() {
        RequestParams params = creatParams("getlistrepairproject");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.QUOTED_PRICEM_ID, order.getBf_quoted_pricem_id());
        params.addBodyParameter("source",order.getSource());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.MaintenanceInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.MaintenanceInfoBean>() {
            @Override
            public void onSuccess(ORMBean.MaintenanceInfoBean result) {
                List<MaintenanceInfo> data = result.getData();
                list.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 根据类别获取对应的数据字典接口。1为燃料，2为操控，3为驱动，4为排量 ,5为油量
     *
     * @param type
     * @param position
     */
    private void getcontrolbytype(final String type, final int position) {
        RequestParams params = creatParams("getcontrolbytype");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("type", type);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ControlBean.class, new HttpUtil.HttpCallBack<ORMBean.ControlBean>() {
            @Override
            public void onSuccess(ORMBean.ControlBean result) {
                List<Control> data = result.getData();
                list_control.addAll(data);

                position_targer = position;
                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-39 确认派工接口，即分派 assignwork(userid,receiver_id,quoted_pricem_id,is_rework,json_project)
     * 当前用户ID:userid,接车单ID:receiver_id,报价单ID:quoted_pricem_id,是否属于返修:is_rework,维修项目清单列表:json_project
     */
    private void assignwork() {
        if (!isAllSelect()){
            return;
        }

        RequestParams params = creatParams("assignwork");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.RECRIVE_ID, order.getBf_receive_car_id());
        params.addBodyParameter(Key.IS_REWORK, order.getIs_rework());
        params.addBodyParameter(Key.QUOTED_PRICEM_ID, order.getBf_quoted_pricem_id());
// {"itemt_id":"101","itemt_name":"发动机大修","team_id":"101","team_name":"机械组","operator":"jxstar6852949","assign_time":"2016-09-28"}
        params.addBodyParameter(Key.JSON_PROJECT, "{\"data\":" + list.toString() + "}");

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());

                startActivity(new Intent(mContext,FuntionActivity.class));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-36 获取默认派工维修项目对应的维修班组接口 getdefaultassignwork(userid,itemt_codes)
     * 当前用户ID:userid,修项目编码字符串，用逗号隔开:itemt_codes
     */
    private void getdefaultassignwork() {
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < list.size(); i++) {
            MaintenanceInfo info = list.get(i);
            if (i == 0) {
                buffer.append(info.getItemt_id());
            } else {
                buffer.append("," + info.getItemt_id());
            }
        }

        RequestParams params = creatParams("getdefaultassignwork");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.ITEMT_CODES, buffer.toString());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.MaintenanceInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.MaintenanceInfoBean>() {
            @Override
            public void onSuccess(ORMBean.MaintenanceInfoBean result) {
                List<MaintenanceInfo> data = result.getData();
                for (MaintenanceInfo info : data) {
                    String itemt_code = info.getItemt_code();
                    for (MaintenanceInfo info1 : list) {
                        String itemt_id = info1.getItemt_id();
                        if (itemt_id.equals(itemt_code)) {
                            info1.setDefault_team_name(info.getDefault_team_name());
                            info1.setDefault_team_id(info.getDefault_team_id());
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });

    }


    /**
     * 2-51A 拆检派工接口，即拆检分派 spiltassignwork(userid,receiver_id,json_project)
     当前用户ID:userid,接车表ID:receiver_id,拆检配件清单列表:json_project
     */
     private  void  spiltassignwork(){
         if (!isAllSelect()){
             return;
         }

         RequestParams params = creatParams("spiltassignwork");
         params.addBodyParameter(Key.USER_ID,user.getUser_id());
         params.addBodyParameter("receiver_id",order.getBf_receive_car_id());
         params.addBodyParameter("json_project","{\"data\":" + list.toString() + "}");

         HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
             @Override
             public void onSuccess(BaseBean result) {
                 ToastUtil.showShort(mContext, result.getMsg());

                 startActivity(new Intent(mContext,FuntionActivity.class));
             }

             @Override
             public void onError(Throwable ex, boolean isOnCallback) {

             }
         });
     }

    private  boolean  isAllSelect(){
        for (MaintenanceInfo  info:list) {
            if (TextUtils.isEmpty(info.getDefault_team_name())){
                ToastUtil.showShort(mContext,"请分派完成");
                return false;
            }
        }
        return true;
    }



    @OnClick({R.id.btn_1, R.id.btn_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                getdefaultassignwork();
                break;
            case R.id.btn_2:
                if (list.size()>0){
                    String status = list.get(0).getStatus();
                    if (status.equals("拆检中")){
                        spiltassignwork();
                    }else {
                        assignwork();
                    }
                }
                break;
        }
    }
}
