package com.xolo.gzqc.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_CarInfoAdapter;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.adapter.CoSenderCarInfoAdapter;
import com.xolo.gzqc.adapter.CoSenderInfoAdapter;
import com.xolo.gzqc.adapter.SendCarInfoAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.bean.child.CarOwner_SendCar_info;
import com.xolo.gzqc.bean.child.CarnoInfoConfirm;
import com.xolo.gzqc.bean.child.CoSenderCarInfo;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.activity.AddSenderInfoActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.PhoneUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.utils.L;

/**
 * 送修人信息
 * Created by Administrator on 2016/9/26.
 */
public class CarOwners_SendinfoFragment extends LazyFragment {
    List<CarOwner_SendCar_info> carOwner_sendCar_infos = new ArrayList<>();
    CoSenderInfoAdapter coSenderInfoAdapter;
    CoSenderCarInfoAdapter coSenderCarInfoAdapter;
    ScrollListView co_project_list, co_project_car_info;
    EditText send_info_name_text, send_info_phone1_text, send_info_phone2_text, send_info_d_num_text;
    LinearLayout man_img_layout, female_img_layout;
    ImageView female_img, man_img;
    RelativeLayout et_img_layout;
    TextView maintain_et;
    Button button;
    List<CoSenderCarInfo> coSenderCarInfolist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sender_info, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    void initView(View view) {
        button = (Button) view.findViewById(R.id.add_sender_info);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(maintain_et.getText().toString())) {
                    ToastUtil.showShort(getContext(), "请选择车牌号");
                    return;
                }
                initDialog("添加", -1);
            }
        });
        coSenderCarInfoAdapter = new CoSenderCarInfoAdapter(getContext(), R.layout.item_co_sendercar_info, coSenderCarInfolist);
        coSenderInfoAdapter = new CoSenderInfoAdapter(getActivity(),
                R.layout.item_co_sender_info, carOwner_sendCar_infos);
        co_project_list = (ScrollListView) view.findViewById(R.id.co_project_list);
        co_project_list.setAdapter(coSenderInfoAdapter);
        co_project_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    return;

                getSenderCarInfo(carOwner_sendCar_infos.get(position - 1).getPhone());
            }
        });
        co_project_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                bc_car_sendman_id = carOwner_sendCar_infos.get(position).getBc_car_sendman_id();
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("编辑送修人").setNeutralButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initDialog("修改", position);
                    }
                }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                }).create();
                dialog.show();
                return false;
            }
        });
        co_project_car_info = (ScrollListView) view.findViewById(R.id.co_project_car_info);
        co_project_car_info.setAdapter(coSenderCarInfoAdapter);


        maintain_et = (TextView) view.findViewById(R.id.maintain_et);
        et_img_layout = (RelativeLayout) view.findViewById(R.id.et_img_layout);
        et_img_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = creatListDialog("选择车牌", liststring, new BaseFragment.listDialogFace() {
                        @Override
                        public void retrunTest(String text) {
                            maintain_et.setText(text);
                            for (CarOwner_CarInfo carnoInfoConfirm : carlist) {
                                if (carnoInfoConfirm.getCarno().equals(text)) {
                                    bc_car_info_id = carnoInfoConfirm.getBc_car_info_id();
                                    break;
                                }
                            }
                        }
                    });

                }
                dialog.show();
            }
        });
    }

    void delete() {
        RequestParams requestParams = creatParams("deletesendmanbyid");
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("bc_car_sendman_id", bc_car_sendman_id);
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    getSenderData();
                }
                ToastUtil.showLong(getContext(), result.getMsg());
                LoadDialog.dismiss(mContext);

            }
        });
    }

    @Override
    protected void loadData() {
        if (needLoad()) {
            // getData();
            getSenderData();
            getDatacarInfo();
            mHasLoadedOnce = true;
        }

    }

    @Override
    protected void init() {

    }

    void getSenderCarInfo(String phone) {
        LoadDialog.show(getContext());
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getcarlistbysendman");
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("phone", phone);
        HttpUtil.getInstance().post(requestParams, ORMBean.CoSenderCarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CoSenderCarInfoBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoSenderCarInfoBean result) {
                if (result.getRes().equals("1")) {
                    coSenderCarInfolist.clear();
                    CoSenderCarInfo coSenderCarInfo = new CoSenderCarInfo("品牌", "型号", "车牌");
                    coSenderCarInfolist.add(coSenderCarInfo);
                    coSenderCarInfolist.addAll(result.getData());
                } else {
                    coSenderCarInfolist.clear();
                    ToastUtil.showLong(getContext(), result.getMsg());
                }
                coSenderCarInfoAdapter.notifyDataSetChanged();
                LoadDialog.dismiss(mContext);
            }
        });
    }

    void getSenderData() {
        LoadDialog.show(getContext());
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getsendmanlist");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());

        HttpUtil.getInstance().post(requestParams, ORMBean.CoSenderCar.class, new HttpUtil.HttpCallBack<ORMBean.CoSenderCar>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoSenderCar result) {
                if (result.getRes().equals("1")) {
                    carOwner_sendCar_infos.clear();
                    carOwner_sendCar_infos.add(new CarOwner_SendCar_info("手机号1", "手机号2", "姓名"));
                    carOwner_sendCar_infos.addAll(result.getData());
                    coSenderInfoAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showLong(getContext(), result.getMsg());
                }

                LoadDialog.dismiss(mContext);
            }
        });

    }

    List<String> liststring = new ArrayList<String>();

    String sex;
    AlertDialog adddialog;

    private View initDialogView(int position) {
        View view = View.inflate(mContext, R.layout.dialog_sender_info, null);
        female_img = (ImageView) view.findViewById(R.id.female_img);
        man_img = (ImageView) view.findViewById(R.id.man_img);
        man_img_layout = (LinearLayout) view.findViewById(R.id.man_img_layout);
        female_img_layout = (LinearLayout) view.findViewById(R.id.female_img_layout);
        send_info_name_text = (EditText) view.findViewById(R.id.send_info_name_text);
        send_info_phone1_text = (EditText) view.findViewById(R.id.send_info_phone1_text);
        send_info_phone2_text = (EditText) view.findViewById(R.id.send_info_phone2_text);
        send_info_d_num_text = (EditText) view.findViewById(R.id.send_info_d_num_text);

        man_img_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man_img.setBackgroundResource(R.mipmap.sure);
                female_img.setBackgroundResource(R.mipmap.nil);
                sex = "1";
            }
        });
        female_img_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female_img.setBackgroundResource(R.mipmap.sure);
                man_img.setBackgroundResource(R.mipmap.nil);
                sex = "0";
            }
        });
        if (position >= 0) {
            send_info_name_text.setText(carOwner_sendCar_infos.get(position).getName());
            if (carOwner_sendCar_infos.get(position).getSex().equals("1")) {
                man_img.setBackgroundResource(R.mipmap.sure);
                female_img.setBackgroundResource(R.mipmap.nil);
                sex = "1";
            } else {
                man_img.setBackgroundResource(R.mipmap.nil);
                female_img.setBackgroundResource(R.mipmap.sure);
                sex = "0";
            }
            send_info_phone1_text.setText(carOwner_sendCar_infos.get(position).getPhone());
            send_info_phone2_text.setText(carOwner_sendCar_infos.get(position).getPhone2());
            send_info_d_num_text.setText(carOwner_sendCar_infos.get(position).getDrivingno());
        }
        return view;
    }

    void initDialog(final String text, final int position) {
        if (adddialog == null) {
            adddialog = new AlertDialog.Builder(mContext).setView(initDialogView(position)).setNegativeButton(text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (TextUtils.isEmpty(send_info_name_text.getText().toString())) {
                        ToastUtil.showShort(getActivity(), "请填写姓名");
                        return;
                    }
                    if (TextUtils.isEmpty(sex)) {
                        ToastUtil.showShort(getActivity(), "请选择性别");
                        return;
                    }
                    if (TextUtils.isEmpty(send_info_d_num_text.getText().toString().trim())) {
                        ToastUtil.showShort(getActivity(), "请输入驾驶证号");
                        return;
                    }
                    if (TextUtils.isEmpty(send_info_phone1_text.getText().toString().trim())) {
                        ToastUtil.showShort(getActivity(), "请填写手机号码1");
                        return;
                    }
                    if (!TextUtils.isEmpty(send_info_phone1_text.getText().toString().trim()) && !PhoneUtil.isMobileNO(send_info_phone1_text.getText().toString().trim())) {
                        ToastUtil.showShort(getActivity(), "请填写正确的手机号码");
                        return;

                    }
                    if (!TextUtils.isEmpty(send_info_phone2_text.getText().toString().trim()) && !PhoneUtil.isMobileNO(send_info_phone2_text.getText().toString().trim())) {
                        ToastUtil.showShort(getActivity(), "请填写正确的手机号码");
                        return;
                    }

                    if (text.equals("添加")) {
                        addSendInfo("addcarsendman");
                    } else {
                        addSendInfo("updatesendmanbyid");
                    }
                }
            }).create();
        }
        adddialog.show();
    }

    String bc_car_info_id;
    String bc_car_sendman_id;
    //获取车牌信息
    List<CarOwner_CarInfo> carlist;

    void getDatacarInfo() {
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

    //updatesendmanbyid
    //addcarsendman
    void addSendInfo(String type) {
        LoadDialog.show(getActivity());
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams(type);
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("name", send_info_name_text.getText().toString());
        requestParams.addBodyParameter("sex", sex);
        requestParams.addBodyParameter("drivingno", send_info_d_num_text.getText().toString());
        requestParams.addBodyParameter("phone", send_info_phone1_text.getText().toString());
        requestParams.addBodyParameter("phone2", send_info_phone2_text.getText().toString());
        if (type.equals("addcarsendman")) {
            requestParams.addBodyParameter("bc_car_info_id", bc_car_info_id);
        } else {
            requestParams.addBodyParameter("bc_car_sendman_id", bc_car_sendman_id);

        }


        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
                adddialog.dismiss();
            }

            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    send_info_name_text.setText("");
                    send_info_phone1_text.setText("");
                    send_info_d_num_text.setText("");
                    send_info_phone2_text.setText("");
                    sex = "";
                    female_img.setBackgroundResource(R.mipmap.nil);
                    man_img.setBackgroundResource(R.mipmap.nil);
                    getSenderData();
                    adddialog.dismiss();
                }

                ToastUtil.showLong(getActivity(), result.getMsg());
                LoadDialog.dismiss(mContext);

            }

        });

    }


    Dialog dialog;
}
