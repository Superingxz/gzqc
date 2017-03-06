package com.xolo.gzqc.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.adapter.CoHistoryMaintainAdapter;
import com.xolo.gzqc.adapter.MaintainRecordAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.bean.child.CarnoInfo;
import com.xolo.gzqc.bean.child.CoHistoryRecord;
import com.xolo.gzqc.bean.child.Maintain_Record;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.activity.CoMaintainDetails;
import com.xolo.gzqc.ui.activity.CoMaintainPrograssActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史维修记录
 * Created by Administrator on 2016/9/28.
 */
public class HistoryRecordFragment extends LazyFragment {

    ListView maintain_re_lv, add_insurance_lv;
    List<CoHistoryRecord> maintain_records;
    CarOwner_add_MaintainAdapter carOwnersadapter;
    List<CarOwner_CarInfo> carlist;
    List<String> liststring = new ArrayList<String>();
    Dialog dialog;
    CarnoInfo carnoInfo;
    Dialog startdialog, enddialog;
    CoHistoryMaintainAdapter coHistoryMaintainAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_history, null);
    }

    List<CarOwner_Add_Maintain> list;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        add_insurance_lv = (ListView) view.findViewById(R.id.add_insurance_lv);
        user = SPManager.getUser(mContext);
        list = new ArrayList<>();
        list.add(new CarOwner_Add_Maintain("车 牌 号:", true));
        list.add(new CarOwner_Add_Maintain("车型:", false));
        list.add(new CarOwner_Add_Maintain("姓名:", false));
        list.add(new CarOwner_Add_Maintain("手机:", false));
        list.add(new CarOwner_Add_Maintain("维修厂:", false));
        list.add(new CarOwner_Add_Maintain("维修开始日期:", true));
        list.add(new CarOwner_Add_Maintain("维修结束日期:", true));
        carOwnersadapter = new CarOwner_add_MaintainAdapter(getActivity(), R.layout.fragment_add_maintain_tem, list);
        add_insurance_lv.setAdapter(carOwnersadapter);
        carOwnersadapter.setImaOnlickiface(new CarOwner_add_MaintainAdapter.ImaOnlickIface() {
            @Override
            public void rightImgOnlic(final int position) {
                if (position == 0) {
                    if (dialog == null) {
                        dialog = creatListDialog("选择车牌", liststring, new listDialogFace() {
                            @Override
                            public void retrunTest(String text) {
                                if (TextUtils.isEmpty(list.get(position).getEttext())) {
                                    list.get(position).setEttext(text);
                                    getCarnoInfo();
                                    getHistroyData();
                                } else {
                                    if ((!list.get(position).getEttext().equals(text))) {
                                        list.get(position).setEttext(text);
                                        getCarnoInfo();
                                        getHistroyData();
                                    }
                                }
                                carOwnersadapter.notifyDataSetChanged();
                            }

                        });
                    }
                    dialog.show();
                } else if (position == 5) {
                    if (startdialog == null) {
                        startdialog = creatDateDialog(new listDialogFace() {
                            @Override
                            public void retrunTest(String text) {
                                list.get(position).setEttext(text);
                                carOwnersadapter.notifyDataSetChanged();
                                getHistroyData();

                            }
                        });
                    }
                    startdialog.show();
                } else if (position == 6) {
                    if (enddialog == null) {
                        enddialog = creatDateDialog(new listDialogFace() {
                            @Override
                            public void retrunTest(String text) {
                                list.get(position).setEttext(text);
                                carOwnersadapter.notifyDataSetChanged();
                                getHistroyData();

                            }
                        });
                    }
                    enddialog.show();
                }

            }

            @Override
            public void changeEdit(int position, String st) {

            }
        });
        maintain_re_lv = (ListView) view.findViewById(R.id.maintain_re_lv);
        maintain_records = new ArrayList<>();
        coHistoryMaintainAdapter = new CoHistoryMaintainAdapter(getActivity(), R.layout.item_co_history_maintain, maintain_records);
        maintain_re_lv.setAdapter(coHistoryMaintainAdapter);
        maintain_re_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (Integer.valueOf(maintain_records.get(position).getStatus())<10) {
                     intent = new Intent(getActivity(), CoMaintainPrograssActivity.class);
                    intent.putExtra("bf_receive_id", maintain_records.get(position).getBf_receive_id());
                    startActivity(intent);
                }else{
                     intent = new Intent(getActivity(), CoMaintainDetails.class);
                    intent.putExtra("bf_receive_id", maintain_records.get(position).getBf_receive_id());


                }
                startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected void loadData() {
        if (needLoad()) {
            getDatacarInfo();
            mHasLoadedOnce = true;
        }
    }

    @Override
    protected void init() {

    }
    boolean isload;
    void getHistroyData() {
        if (TextUtils.isEmpty(list.get(0).getEttext())||isload) {
            return;
        }
        isload=true;
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("ownerlisthistoryrepair");
        requestParams.addBodyParameter("carno", list.get(0).getEttext());
        requestParams.addBodyParameter("dept_id", user.getDept_id());
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("star_date", list.get(5).getEttext());
        requestParams.addBodyParameter("end_date", list.get(6).getEttext());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoHistoryRecordBean.class, new HttpUtil.HttpCallBack<ORMBean.CoHistoryRecordBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
                isload=false;
            }

            @Override
            public void onSuccess(ORMBean.CoHistoryRecordBean result) {
                if (result.getRes().equals("1")) {
                    maintain_records.clear();
                    maintain_records.add(new CoHistoryRecord());
                    maintain_records.addAll(result.getData());
                } else {
                    maintain_records.clear();
                    ToastUtil.showLong(getActivity(), result.getMsg());
                }
                isload=false;
                coHistoryMaintainAdapter.notifyDataSetChanged();
                LoadDialog.dismiss(mContext);
            }
        });


    }


    //根据车牌获取信息
    void getCarnoInfo() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getbfcarinfobycarno");
        requestParams.addBodyParameter("carno", list.get(0).getEttext());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CarnoInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarnoInfoBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CarnoInfoBean result) {
                if (result.getRes().equals("1")) {
                    carnoInfo = result.getData().get(0);
                    list.get(1).setEttext(result.getData().get(0).getBrands());
                    list.get(3).setEttext(result.getData().get(0).getMobile());
                    list.get(2).setEttext(result.getData().get(0).getName());
                    list.get(4).setEttext(user.getDept_name());
                    //getData();

                } else {

                    list.get(1).setEttext("");
                    list.get(2).setEttext("");
                    list.get(3).setEttext("");
                    list.get(4).setEttext("");
                }
                carOwnersadapter.notifyDataSetChanged();
                LoadDialog.dismiss(mContext);
            }
        });
    }


    //获取车牌信息
    void getDatacarInfo() {
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

                }
                LoadDialog.dismiss(mContext);
            }
        });

    }
}
