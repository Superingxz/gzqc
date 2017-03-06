package com.xolo.gzqc.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xolo.gzqc.App;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_CarInfoAdapter;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.ChooseEditView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**新增预约
 * Created by Administrator on 2016/9/26.
 */
public class CarOwners_Add_Maintain extends LazyFragment {
    ListView add_insurance_lv;
    List<CarOwner_CarInfo> carlist;
    CarOwner_add_MaintainAdapter carOwnersadapter;
    Button bt_submit;
    EditText maintain_et;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_maintain, null);
            iniView(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    boolean isOnView;
    List<CarOwner_Add_Maintain> list = new ArrayList<>();

    private void iniView(View view) {
        maintain_et = (EditText) view.findViewById(R.id.maintain_et);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        add_insurance_lv = (ListView) view.findViewById(R.id.add_insurance_lv);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpSubmit();
            }
        });
        list.add(new CarOwner_Add_Maintain("车 牌 号:", true));
        list.add(new CarOwner_Add_Maintain("预约日期:", true));
        list.add(new CarOwner_Add_Maintain("到厂时间:", true));
        list.add(new CarOwner_Add_Maintain("故障描述:", false));

        carOwnersadapter = new CarOwner_add_MaintainAdapter(getActivity(), R.layout.fragment_add_maintain_tem, list);
        carOwnersadapter.setEditeFocusable(true);
        add_insurance_lv.setAdapter(carOwnersadapter);
        carOwnersadapter.setImaOnlickiface(new CarOwner_add_MaintainAdapter.ImaOnlickIface() {
            @Override
            public void rightImgOnlic(final int position) {
                switch (position) {
                    case 0:
                        if (dialog == null) {
                            dialog = creatListDialog("选择车牌", liststring, new listDialogFace() {
                                @Override
                                public void retrunTest(String text) {
                                    list.get(position).setEttext(text);
                                    carOwnersadapter.notifyDataSetChanged();
                                }
                            });

                        }
                        dialog.show();
                        break;
                    case 1:
                        if (datedialog == null) {
                            datedialog = creatDateDialog(new listDialogFace() {
                                @Override
                                public void retrunTest(String text) {
                                    list.get(position).setEttext(text);
                                    carOwnersadapter.notifyDataSetChanged();

                                }
                            });
                        }
                        datedialog.show();
                        break;
                    case 2:
                        if (plandatedialog == null) {
                            plandatedialog = creatDateDialog(new listDialogFace() {
                                @Override
                                public void retrunTest(String text) {
                                    list.get(position).setEttext(text);
                                    carOwnersadapter.notifyDataSetChanged();
                                }
                            });
                        }
                        plandatedialog.show();
                        break;

                }
            }

            @Override
            public void changeEdit(int position, String st) {
                list.get(position).setEttext(st);

            }
        });
        isOnView = true;
    }

    public boolean subumit() {
        boolean is_submit = true;
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.isEmpty(list.get(i).getEttext())) {
                switch (i) {
                    case 0:
                        ToastUtil.showLong(getActivity(), "请选择车牌号码");
                        break;
                    case 1:
                        ToastUtil.showLong(getActivity(), "请选择预约日期");
                        break;
                    case 2:
                        ToastUtil.showLong(getActivity(), "请选择计划来车日期");
                        break;
                    case 3:
                        ToastUtil.showLong(getActivity(), "请输入故障描述");
                        break;
                }
                is_submit = false;
                break;
            }


        }
//        if(TextUtils.isEmpty(maintain_et.getText().toString())){
//            ToastUtil.showLong(getActivity(),"请输入故障描述");
//            is_submit = false;
//        }
        return is_submit;
    }

    void HttpSubmit() {
        if (!subumit()) {
            return;
        }
        LoadDialog.show(getContext());
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("addorder");
        String bc_car_info_id = null;
        for (CarOwner_CarInfo code : carlist) {
            if (code.getCarno().equals(list.get(0).getEttext())) {
                bc_car_info_id = code.getBc_car_info_id();
            }
        }
        requestParams.addBodyParameter("car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("bc_car_info_id", bc_car_info_id);
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("fault_des", list.get(3).getEttext());
        requestParams.addBodyParameter("operatdate", list.get(1).getEttext());
        requestParams.addBodyParameter("plan_com_time", list.get(2).getEttext());
        requestParams.addBodyParameter("dept_id", user.getDept_id());
        requestParams.addBodyParameter("dept_name", user.getDept_name());
        requestParams.addBodyParameter("operator", user.getUser_name());
        requestParams.addBodyParameter("is_buy_fitting", "0");
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showLong(getContext(), result.getMsg());
                if (result.getRes().equals("1")) {
                    for (CarOwner_Add_Maintain carOwner_add_maintain : list) {
                        carOwner_add_maintain.setEttext("");
                    }
                    carOwnersadapter.notifyDataSetChanged();
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }


    Dialog datedialog, plandatedialog;
    Dialog dialog;
    List<String> liststring = new ArrayList<String>();

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

    void getData() {
        LoadDialog.show(getContext());
        user = SPManager.getUser(mContext);
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

                } else {
                    ToastUtil.showLong(getContext(), result.getMsg());
                }

                LoadDialog.dismiss(mContext);
            }
        });

    }
}
