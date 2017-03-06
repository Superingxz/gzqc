package com.xolo.gzqc.ui.activity.pickcar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.ReceiveInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.ExScrollView;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ScreenUtils;
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
 * 进度查询
 */
public class ProgressActivity extends BaseActivity {


    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.esv)
    ExScrollView esv;
    @BindView(R.id.btn_return_top)
    ImageView btnReturnTop;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;

    private List<CarInfo> list_carno = new ArrayList<>();
    private CommenAdapter<CarInfo> adapter;

    private Dialog dialog;

    private List<Control> list_control = new ArrayList<>();

    private LicenseKeyboardUtil keyboardUtil;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        type = getIntent().getIntExtra("type", 0);
        keyboardUtil = new LicenseKeyboardUtil(mContext, carNunber, keyboardView);
        initLv();
        initEsv();
        initTitle();
        initDialog();
        initIntent();
    }

    private void initDialog() {
        dialog = creatListDialog("", list_control, tvStatus, new ListDialogCallBack<Control>() {
            @Override
            public String setText(Control control) {
                return control.getDisplay_data();
            }

            @Override
            public void onClick(Control control) {

            }
        });
    }

    private void initIntent() {
        String no = getIntent().getStringExtra(Key.CARNO);
        carNunber.setText(no);
        if (!TextUtils.isEmpty(no)) {
            getlistrepairingcar();
        }
    }


    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_maintenance_list, mContext, list_carno, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarInfo carInfo = list_carno.get(position);

                holder.setText(R.id.item1, "车牌号：" + carInfo.getCarno());
                holder.setText(R.id.item2, "车型：" + carInfo.getBrands() + "  " + carInfo.getTypecode());
                holder.setText(R.id.item4, "到厂时间： " + carInfo.getIn_time());
                holder.setText(R.id.item5, "当前状态： " + carInfo.getTeam_name());
                holder.setText(R.id.item7, "接车员： " + carInfo.getOperatoname());
                holder.setVisibility(R.id.item7, View.VISIBLE);
                holder.loadUrl(R.id.iv, carInfo.getBrands_path());

                holder.setVisibility(R.id.item3, View.GONE);
            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getnewreceiveinfo(list_carno.get(position));
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(mContext).setMessage("是否删除").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletereceivecar(list_carno.get(position).getBf_receive_id(), position);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
                return true;
            }
        });

    }

    private void initTitle() {
        titleview.setRightText("查询");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlistrepairingcar();
            }
        });
    }

    /**
     * 2-56 获取本部门正在维修车辆列表接口 getlistrepairingcar(userid,dept_id,carno,owner_name,owner_phone)
     * 当前用户ID:userid,维修厂ID:dept_id,车牌号:carno,车主姓名:owner_name,车主手机号:owner_phone
     */
    private void getlistrepairingcar() {
        RequestParams params = creatParams("getlistrepairingcar");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.CARNO, carNunber.getText().toString().trim());
        params.addBodyParameter("owner_name", name.getText().toString().trim());
        params.addBodyParameter("owner_phone", phone.getText().toString().trim());
        params.addBodyParameter("cur_status", tvStatus.getText().toString().trim());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                List<CarInfo> data = result.getData();
                list_carno.clear();
                list_carno.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_carno.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 2-29 通过车牌号获取本维修厂最新的接车信息接口 getnewreceiveinfo(userid,carno,dept_id) 当前用户ID:userid,车牌号:carno,维修厂ID:dept_id
     */
    private void getnewreceiveinfo(final CarInfo carInfo) {
        RequestParams params = creatParams("getnewreceiveinfo");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.CARNO, carInfo.getCarno());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ReceiveInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.ReceiveInfoBean>() {
            @Override
            public void onSuccess(ORMBean.ReceiveInfoBean result) {
                ReceiveInfo receiveInfo = result.getData().get(0);

                Intent intent = new Intent(mContext, MaintenanceList_2Activity.class);
                intent.putExtra(Key.OBJECT, receiveInfo);

                if (!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))) {
                    intent.putExtra("type", 1);
                } else {
                    if (type == 0) {
//                        String team_name = carInfo.getTeam_name();
//                        LogUtil.i(team_name);
//                        if (team_name.equals("已完工") || team_name.equals("待结算")) {
                            intent.putExtra("type", 0);
//                        } else
//                            intent.putExtra("type", 1);
                    } else
                        intent.putExtra("type", 1);
                }

                startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }


    /**
     * 2-77 接车员删除接车单接口 deletereceivecar(userid,receive_id)
     * 当前用户ID:userid,接车单ID：receive_id
     */
    private void deletereceivecar(String id, final int position) {
        RequestParams params = creatParams("deletereceivecar");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("receive_id", id);

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
                list_carno.remove(position);
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
     */
    private void getcontrolbytype(final String type) {
        RequestParams params = creatParams("getcontrolbytype");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("type", type);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ControlBean.class, new HttpUtil.HttpCallBack<ORMBean.ControlBean>() {
            @Override
            public void onSuccess(ORMBean.ControlBean result) {
                List<Control> data = result.getData();
                list_control.addAll(data);
                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void initEsv() {
        final int limitHeight = ScreenUtils.getScreenHeight(mContext) / 2;

        esv.setOnScrollChangedListener(new ExScrollView.OnScrollchangedListener() {
            @Override
            public void change(int l, int t, int oldl, int oldt) {
                if (t > limitHeight) {
                    btnReturnTop.setVisibility(View.VISIBLE);
                } else {
                    btnReturnTop.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.tv_status, R.id.btn_return_top})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_status:
                if (list_control.size() == 0) {
                    getcontrolbytype("10");
                } else
                    dialog.show();
                break;
            case R.id.btn_return_top:
                esv.scrollTo(0, 0);
                break;
        }
    }
}
