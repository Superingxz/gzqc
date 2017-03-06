package com.xolo.gzqc.ui.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.VerhaulExpandableAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.ItemVerhaul;
import com.xolo.gzqc.bean.child.ReceiveInfo;
import com.xolo.gzqc.bean.postJson.PartVerhaul;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.AddPartVerhaulActivity;
import com.xolo.gzqc.ui.activity.pickcar.DispatchingActivity;
import com.xolo.gzqc.ui.view.CustomExpandableListView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 拆检
 */
public class VerhaulFragment extends BaseFragment {

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
    @BindView(R.id.btn_add)
    ImageButton btnAdd;
    @BindView(R.id.lv)
    CustomExpandableListView lv;
    @BindView(R.id.confirm)
    Button confirm;
    private Dialog dialog_carno;
    private LoadDialog dialog_loding;
    private Dialog dialog_longClick;

    private ReceiveInfo receiveInfo;

    private CarInfo carInfo;

    private List<CarInfo> list_carInfo = new ArrayList<>();
    private List<PartVerhaul> list_part = new ArrayList<>();
    private CommenAdapter<PartVerhaul> adapter;

    private boolean isHidden;

    private String recerive_id;

    private int select_position;

    private int type;

    private List<ItemVerhaul> list_itemVerhaul = new ArrayList<>();
    private VerhaulExpandableAdapter verhaulExpandableAdapter;
    private Intent intent;

    public VerhaulFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verhaul, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected void init() {
        intent = mContent.getIntent();
        type = intent.getIntExtra("type", 0);

        initLv();
        initDialog();
        initIntent();
    }

    /**
     * 0 接车员首页
     * 2 班组完工点进
     *
     * 班组首页进来 1其他班组   不可编辑
     * 班组首页进来 2我的班组   可编辑   确定按钮
     */
    private void initIntent() {
        if (type == 1) {
            recerive_id = mContent.getIntent().getStringExtra("id");

                setGone(new View[]{confirm, btnAdd, select});
                setHint(new TextView[]{displacement,fuel,drive,loginData,operation,carNunber,brand,models,year,vinCode,engine});
        }

        if (type == 2) {
            confirm.setText("确定");
        }

        String no = mContent.getIntent().getStringExtra(Key.CARNO);
        if (!TextUtils.isEmpty(no)) {
            getcarinfobycarno(no);
        }
    }

    private void initLv() {

        verhaulExpandableAdapter = new VerhaulExpandableAdapter(list_itemVerhaul, mContent);

        lv.setAdapter(verhaulExpandableAdapter);

        if (type == 0||type==2) {
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    select_position = position;
                    dialog_longClick.show();
                    return true;
                }
            });
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
                clearDate();
                getcarinfobycarno(carInfo.getCarno());
            }
        });

        String[] array2 = new String[]{"修改", "删除"};
        List<String> strings2 = Arrays.asList(array2);
        dialog_longClick = creatListDialog("", strings2, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                if (s.equals("修改")) {
                    Intent intent = new Intent(mContext, AddPartVerhaulActivity.class);
                    intent.putExtra(Key.OBJECT, list_itemVerhaul.get(select_position));
                    startActivityForResult(intent, REQUEST_CODE);
                } else if (s.equals("删除")) {
                    list_itemVerhaul.remove(select_position);
                    verhaulExpandableAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @OnClick({R.id.select, R.id.btn_add, R.id.confirm, R.id.btn_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select:
                if (list_carInfo.size() > 0) {
                    dialog_carno.show();
                    return;
                }
                listcarinfo();
                break;
            case R.id.btn_add:
                Intent intent = new Intent(mContext, AddPartVerhaulActivity.class);
                intent.putExtra(Key.CARNO, carNunber.getText().toString().trim());
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.confirm:
                if (list_itemVerhaul.size() == 0) {
                    ToastUtil.showShort(mContent, "维修清单不能为空");
                    return;
                }

                if (type == 2) {
                    sendsplittoreceive();
                    return;
                }
                savelistoverhaulorderm();
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
     * 2-29 通过车牌号获取本维修厂最新的接车信息接口 getnewreceiveinfo(userid,carno,dept_id) 当前用户ID:userid,车牌号:carno,维修厂ID:dept_id
     * 2-81 通过接车单ID获取拆检单信息接口 getsplitinfobypricemid(userid,receive_id)
     * 当前用户ID:userid,接车单ID：receive_id
     */
    private void getsplitinfobypricemid() {
        RequestParams params = null;
        if (type == 1) {
            params = creatParams("getsplitinfobypricemid");
            params.addBodyParameter("receive_id", recerive_id);
        } else
            params = creatParams("getnewreceiveinfo");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.CARNO, carNunber.getText().toString().trim());

        HttpUtil.getInstance().post(params, ORMBean.ReceiveInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.ReceiveInfoBean>() {
            @Override
            public void onSuccess(ORMBean.ReceiveInfoBean result) {
                if (result.getRes().equals("1")) {
                    receiveInfo = result.getData().get(0);
                    if (type == 1) {
                        receiveInfo.setBf_receive_id(recerive_id);
                    }
//                    overhaulorderm();
                    getlistsplitprojectparts();
                } else {
                    dialog_loding.dismiss(mContent);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContent);
            }
        });
    }


    /**
     * 2-52-B 根据接车当前状态以及维修厂id查询接车车辆列表 getcarlistbydeptidandstatus(userid,dept_id)
     * 当前用户ID:userid,维修厂ID:dept_id
     */
    private void listcarinfo() {
        RequestParams params = creatParams("getcarlistbydeptidandstatus");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
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
     * 2-50 拆检接口--根据接车表id查询出拆检的配件清单 overhaulorderm(userid,bf_receive_car_id,status)
     * 当前用户ID:userid,接车表ID:bf_receive_car_id,当前维修状态:status
     */
    private void overhaulorderm() {
        if (receiveInfo == null) {
            ToastUtil.showShort(mContext, "请选择车辆");
            return;
        }

        RequestParams params = creatParams("getlistsplitprojectparts");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", receiveInfo.getBf_receive_id());
//        params.addBodyParameter("status", receiveInfo.getStatus());

        HttpUtil.getInstance().post(params, ORMBean.PartVerhaulBean.class, new HttpUtil.HttpCallBack<ORMBean.PartVerhaulBean>() {
            @Override
            public void onSuccess(ORMBean.PartVerhaulBean result) {
                if (result.getRes().equals("1")) {
                    List<PartVerhaul> data = result.getData();
                    list_part.clear();
                    list_part.addAll(data);
                    adapter.notifyDataSetChanged();
                } else {
                    if (type == 1) {
                        ToastUtil.showShort(mContext, result.getMsg());
                    }
                }
                dialog_loding.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContent);
            }
        });

    }


    /**
     * 通过车牌号获取该车的车辆信息接口 getcarownerbycarno(userid,carno) 当前用户ID:userid,车牌号:carno
     */
    public void getcarinfobycarno(final String no) {
        RequestParams params = creatParams("getcarinfobycarno");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, no);

        dialog_loding.show(mContext);

        HttpUtil.getInstance().post(params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                if (result.getRes().equals("1")) {
                    CarInfo carInfo1 = result.getData().get(0);
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

                    getsplitinfobypricemid();
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                    dialog_loding.dismiss(mContext);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == 2) {
                ItemVerhaul part = (ItemVerhaul) data.getSerializableExtra(Key.OBJECT);
                list_itemVerhaul.remove(select_position);
                list_itemVerhaul.add(select_position, part);
            } else if (resultCode == -1) {
                ItemVerhaul part = (ItemVerhaul) data.getSerializableExtra(Key.OBJECT);
                list_itemVerhaul.add(part);
                    LogUtil.i("verhaulfrag "+part.getParts().size());
            }
            verhaulExpandableAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 2-51 保存拆检配件清单接口 savelistoverhaulorderm(userid,bf_receive_car_id,overhaulist)
     * 当前用户ID:userid,接车表ID:bf_receive_car_id,拆检配件清单列表:overhaulist
     */
    private void savelistoverhaulorderm() {
        if (TextUtils.isEmpty(getText(carNunber))) {
            ToastUtil.showShort(mContext, "请选择车牌号");
            return;
        }

        RequestParams params = creatParams("savelistoverhaulorderm");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", receiveInfo.getBf_receive_id());
        params.addBodyParameter("overhaulist", "{\"data\":" + list_itemVerhaul.toString() + "}");
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
                startActivity(new Intent(mContext, DispatchingActivity.class));
                mContent.finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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

        list_itemVerhaul.clear();
        verhaulExpandableAdapter.notifyDataSetChanged();
    }

    /**
     * 5-8 拆检单发送给接车员接口 sendsplittoreceive(userid,receiver_id,json_project)
     * 当前用户ID:userid,接车单ID:receiver_id,维修项目清单:json_project
     */
    private void sendsplittoreceive() {
        RequestParams params = creatParams("sendsplittoreceive");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("receiver_id", receiveInfo.getBf_receive_id());
        params.addBodyParameter("json_project", "{\"data\":" + list_itemVerhaul.toString() + "}");

        HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
                mContent.setResult(Activity.RESULT_OK);
                mContent.finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * * 2-90 新的根据接车表id查询出拆检的配件清单接口 getlistsplitprojectparts(userid,bf_receive_car_id)
     * 当前用户ID:userid,接车表ID:bf_receive_car_id
     */
    private void getlistsplitprojectparts() {
        RequestParams params = creatParams("getlistsplitprojectparts");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("bf_receive_car_id", receiveInfo.getBf_receive_id());

        HttpUtil.getInstance().post(params, ORMBean.ItemVerhaulBean.class, new HttpUtil.HttpCallBack<ORMBean.ItemVerhaulBean>() {
            @Override
            public void onSuccess(ORMBean.ItemVerhaulBean result) {
                if (result.getRes().equals("1")) {
                    List<ItemVerhaul> data = result.getData();
                    list_itemVerhaul.clear();
                    list_itemVerhaul.addAll(data);
                    verhaulExpandableAdapter.notifyDataSetChanged();
                }
                dialog_loding.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContent);
            }
        });
    }


}
