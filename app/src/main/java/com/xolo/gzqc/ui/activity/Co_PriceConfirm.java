package com.xolo.gzqc.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.bean.child.CarnoInfo;
import com.xolo.gzqc.bean.child.CarnoInfoConfirm;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.fragment.CoAccessoriesFragment;
import com.xolo.gzqc.ui.fragment.CoHistoryFragment;
import com.xolo.gzqc.ui.fragment.CoProjectListFragment;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 价格确认
 * Created by Administrator on 2016/9/27.
 */
public class Co_PriceConfirm extends BaseActivity {
    TabView tabview_info;
    Dialog dialog;
    private Fragment[] fragments;
    private String[] title = {"维修清单", "配件清单", "历史报价"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_price_confirm);
        initView();
        getDatacarInfo();
    }

    List<CarOwner_Add_Maintain> list;
    CarOwner_add_MaintainAdapter adapter;
    ScrollListView co_project_list;
    List<String> liststring = new ArrayList<String>();


    private void initView() {
        carlist = new ArrayList<>();
        co_project_list = (ScrollListView) findViewById(R.id.co_project_list);
        list = new ArrayList<>();
        list.add(new CarOwner_Add_Maintain("车 牌 号:", true));
        list.add(new CarOwner_Add_Maintain("品牌:", false));
        list.add(new CarOwner_Add_Maintain("手机号:", false));
        list.add(new CarOwner_Add_Maintain("姓名:", false));
        adapter = new CarOwner_add_MaintainAdapter(this, R.layout.fragment_add_maintain_tem, list);
        co_project_list.setAdapter(adapter);
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
                                    getCarnoInfo(text);
                                } else {
                                    if ((!list.get(position).getEttext().equals(text))) {
                                        list.get(position).setEttext(text);
                                        getCarnoInfo(text);
                                    }
                                }
                                String phone = null;
                                String bf_receive_id = null;
                                for (CarnoInfoConfirm carnoInfo : carlist) {
                                    if (text.equals(carnoInfo.getCarno())) {
                                        phone = carnoInfo.getPhone();
                                        bf_receive_id= carnoInfo.getBf_receive_id();
                                        break;
                                    }
                                }

                                coProjectListFragment.setCarno(bf_receive_id,phone, SPManager.getUser(mContext));
                                coHistoryFragment.setCarno(text, SPManager.getUser(mContext));
                                for (CarnoInfoConfirm code : carlist) {
                                    if (code.getCarno().equals(text)) {
                                        coAccessoriesFragment.setBf_receive_car_id(code.getBf_receive_id());
                                        break;
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
        tabview_info = (TabView) findViewById(R.id.tabview_info);
        tabview_info.setTitle(title);
        coProjectListFragment = new CoProjectListFragment();
        coAccessoriesFragment = new CoAccessoriesFragment();
        coHistoryFragment = new CoHistoryFragment();
        fragments = new Fragment[]{coProjectListFragment, coAccessoriesFragment, coHistoryFragment};
        coProjectListFragment.setFinishIface(new CoProjectListFragment.FinishIface() {
            @Override
            public void onFinish() {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        tabview_info.setFragments(fragments);


    }

    CoProjectListFragment coProjectListFragment;
    CoAccessoriesFragment coAccessoriesFragment;
    CoHistoryFragment coHistoryFragment;

    //根据车牌获取信息
    void getCarnoInfo(String carno) {
        LoadDialog.show(this);
        RequestParams requestParams = creatParams("getbfcarinfobycarno");
        requestParams.addBodyParameter("carno",carno);
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CarnoInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarnoInfoBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CarnoInfoBean result) {
                if (result.getRes().equals("1")) {
                    //   coAccessoriesFragment.setBf_receive_car_id(result.getData().get(0).getBf_receive_id());
                    list.get(1).setEttext(result.getData().get(0).getBrands());
                    list.get(2).setEttext(result.getData().get(0).getMobile());
                    list.get(3).setEttext(result.getData().get(0).getName());
                } else {
                    list.get(1).setEttext("");
                    list.get(2).setEttext("");
                    list.get(3).setEttext("");
                    //ToastUtil.showLong(getContext(), result.getMsg());
                }
                adapter.notifyDataSetChanged();
                LoadDialog.dismiss(mContext);
            }
        });

    }

    //获取车牌信息
    List<CarnoInfoConfirm> carlist;

    void getDatacarInfo() {
        LoadDialog.show(this);
        RequestParams requestParams = creatParams("getcarlistbydeptidandstatus");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("dept_id", user.getDept_id());

        HttpUtil.getInstance().post(requestParams, ORMBean.CarnoInfoConfirmBean.class, new HttpUtil.HttpCallBack<ORMBean.CarnoInfoConfirmBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }

            @Override
            public void onSuccess(ORMBean.CarnoInfoConfirmBean result) {
                if (result.getRes().equals("1")) {
                    carlist = result.getData();
                    for (CarnoInfoConfirm code : carlist) {
                        liststring.add(code.getCarno());
                    }
                    if(carlist.size()==1) {
                        list.get(0).setEttext(carlist.get(0).getCarno());
                        getCarnoInfo(carlist.get(0).getCarno());
                        coProjectListFragment.setCarno(carlist.get(0).getBf_receive_id(), carlist.get(0).getPhone(), SPManager.getUser(mContext));
                        coHistoryFragment.setCarno(carlist.get(0).getPhone(), SPManager.getUser(mContext));
                        coAccessoriesFragment.setBf_receive_car_id(carlist.get(0).getBf_receive_id());
                    }
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }
}
