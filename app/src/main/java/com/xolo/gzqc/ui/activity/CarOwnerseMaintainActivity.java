package com.xolo.gzqc.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CoRemind;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.activity.pickcar.CarInfoActivity;
import com.xolo.gzqc.ui.activity.supplier.ShopFitmentActivity;
import com.xolo.gzqc.ui.activity.supplier.SupplierActivity;
import com.xolo.gzqc.ui.view.ItemCoRemind;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

/**
 * 我的维修
 * Created by Administrator on 2016/9/26.
 */
public class CarOwnerseMaintainActivity extends BaseActivity {
    LinearLayout home_layout;
    LinearLayout home_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carowners_maintain);
        initView();
        initData();

    }

    void readMsgVoid(final int position, String id, final ItemCoRemind itemCoRemind) {
        LoadDialog.show(mContext);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("updatewarnstatus");
        requestParams.addBodyParameter("bf_warn_owner_id", id);
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoRemindBean.class, new HttpUtil.HttpCallBack<ORMBean.CoRemindBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoRemindBean result) {
                itemCoRemind.setBtColorGrey();
                ToastUtil.showShort(mContext, result.getMsg());
                LoadDialog.dismiss(mContext);

            }
        });
    }

    void initData() {
        LoadDialog.show(mContext);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("remindallkindof");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoRemindBean.class, new HttpUtil.HttpCallBack<ORMBean.CoRemindBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoRemindBean result) {
                if (result.getRes().equals("1")) {
                    for (int i = 0; i < result.getData().size(); i++) {
                        ItemCoRemind itemCoRemind = new ItemCoRemind(CarOwnerseMaintainActivity.this);
                        final String bf_warn_owner_id = result.getData().get(i).getBf_warn_owner_id();
                        itemCoRemind.setData(i + 1, result.getData().get(i), i);
                        itemCoRemind.setItemOnClick(new ItemCoRemind.ItemOnClick() {
                            @Override
                            public void readMsg(int position, ItemCoRemind itemCallBack) {
                                readMsgVoid(position, bf_warn_owner_id, itemCallBack);
                            }

                            @Override
                            public void pay() {

                            }

                            @Override
                            public void priceRemind() {
                                Intent intent2 = new Intent(CarOwnerseMaintainActivity.this, Co_PriceConfirm.class);
                                startActivityForResult(intent2,0x005);
                            }

                            @Override
                            public void remoMsg(final int position, final CoRemind data) {
                                AlertDialog dialog = new AlertDialog.Builder(mContext)
                                        .setMessage("是否删除该消息？").setTitle("提示").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                remoItem(position, data);
                                            }
                                        }).show();

                            }


                        });
                        home_list.addView(itemCoRemind);
                    }
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x005&&requestCode== Activity.RESULT_OK){
            home_list.removeAllViews();
            initData();
        }
    }

    void remoItem(final int posittion, CoRemind data) {
        LoadDialog.show(mContext);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("deleteremindallkindof");
        requestParams.addBodyParameter("bf_warn_owner_id", data.getBf_warn_owner_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    home_list.removeViewAt(posittion);
                } else {
                    ToastUtil.showShort(CarOwnerseMaintainActivity.this, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }


    private void initView() {
        home_list = (LinearLayout) findViewById(R.id.home_list);
        home_layout = (LinearLayout) findViewById(R.id.home_layout);
        for (int i = 0; i < home_layout.getChildCount(); i++) {
            home_layout.getChildAt(i).setTag(i);
            home_layout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (Integer.valueOf(v.getTag().toString())) {
                        case 0:
                           Intent intent0 = new Intent(CarOwnerseMaintainActivity.this, MaintainSubscribeActivity.class);
                           startActivity(intent0);
//                            Intent intent0 = new Intent(CarOwnerseMaintainActivity.this, SupplierActivity.class);
//                            startActivity(intent0);
                            break;
                        case 1:
                            Intent intent1 = new Intent(CarOwnerseMaintainActivity.this, CarOwnersScheduleActivity.class);
                            startActivity(intent1);

                            break;
                        case 2:
                            Intent intent2 = new Intent(CarOwnerseMaintainActivity.this, Co_PriceConfirm.class);
                            startActivityForResult(intent2,0x005);
                            break;
                        case 3:
//                            Intent intent3 = new Intent(CarOwnerseMaintainActivity.this, OcMaintainRecordActivity.class);
//                            startActivity(intent3);
                            Intent intent=new Intent(CarOwnerseMaintainActivity.this, CarInfoActivity.class);
                            intent.putExtra("carwoner","carwoner");
                            startActivity(intent);
                            break;
                    }
                }
            });
        }

    }

}
