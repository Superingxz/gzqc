package com.xolo.gzqc.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.MaintainRecordAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Maintain_Record;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class MaintainRecordFragment extends LazyFragment {
    /**
     * 预约进度查询
     */
    ListView maintain_re_lv;
    List<Maintain_Record> maintain_records=new ArrayList<>();
    MaintainRecordAdapter maintainRecordAdapter;
    Dialog datedialog, plandatedialog;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view ==null){
            view=inflater.inflate(R.layout.fragment_maintain_record, null);
            initView(view);
        }
        return view;
    }

    void initData() {
        LoadDialog.show(mContext);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getorderlist");
        requestParams.addBodyParameter("is_deal", "0");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoMaintainRecord.class, new HttpUtil.HttpCallBack<ORMBean.CoMaintainRecord>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoMaintainRecord result) {
                if (result.getRes().equals("1")) {
                    maintain_records.clear();
                    maintain_records.addAll(result.getData());
                    maintainRecordAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }
    void updateInfo(String bf_orderm_id){
        LoadDialog.show(mContext);
        RequestParams requestParams = creatParams("updateordermbyid");
        requestParams.addBodyParameter("userid",user.getUser_id());
        requestParams.addBodyParameter("plan_com_time", time2_edt.getText().toString());
        requestParams.addBodyParameter("fault_des", fault_description.getText().toString());
        requestParams.addBodyParameter("bf_orderm_id",bf_orderm_id);
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    time1_edt.setText("");
                    fault_description.setText("");
                    time2_edt.setText("");
                    initData();
                }
                ToastUtil.showShort(mContext, result.getMsg());
                LoadDialog.dismiss(mContext);
            }
        });

    }
    EditText fault_description;
    RelativeLayout et_img_layout,et_img_layout1;

    TextView time1_edt,time2_edt;
    private View initDialogView(Maintain_Record maintain_Record) {
        View view = View.inflate(mContext, R.layout.dialog_subscribe_info, null);
        fault_description=(EditText) view.findViewById(R.id.fault_description);
        time1_edt=(TextView) view.findViewById(R.id.time_edt);
        time2_edt=(TextView) view.findViewById(R.id.time1_edt);
        time1_edt.setText(maintain_Record.getOperatdate());
        time2_edt.setText(maintain_Record.getPlan_com_time());
        fault_description.setText(maintain_Record.getFault_description());
//        et_img_layout=(RelativeLayout)view.findViewById(R.id.et_img_layout);
        et_img_layout1=(RelativeLayout)view.findViewById(R.id.et_img_layout1);
//        et_img_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (datedialog == null) {
//                    datedialog = creatDateDialog(new listDialogFace() {
//                        @Override
//                        public void retrunTest(String text) {
//                            time1_edt.setText(text);
//                        }
//                    });
//                }
//                datedialog.show();
//            }
//        });
        et_img_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plandatedialog == null) {
                    plandatedialog = creatDateDialog(new listDialogFace() {
                        @Override
                        public void retrunTest(String text) {
                            time2_edt.setText(text);
                        }
                    });
                }
                plandatedialog.show();
            }
        });
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initView(View view) {
        maintain_re_lv = (ListView) view.findViewById(R.id.maintain_re_lv);
        maintainRecordAdapter = new MaintainRecordAdapter(getActivity(), R.layout.item_maintain_record, maintain_records);
        maintain_re_lv.setAdapter(maintainRecordAdapter);
        maintain_re_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("编辑预约信息").setNeutralButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).setView(initDialogView(maintain_records.get(position))).setNegativeButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(TextUtils.isEmpty(fault_description.getText().toString())){
                                    ToastUtil.showShort(getActivity(),"请输入故障描述");
                                    return;
                                }
                                updateInfo(maintain_records.get(position).getBf_orderm_id());
                            }
                        }).create();
                        alertDialog.show();
                    }
                }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletesendmanbyid(maintain_records.get(position).getBf_orderm_id());
                    }
                }).create();
                dialog.show();
                return false;
            }
        });
    }

    void deletesendmanbyid(String bf_orderm_id){
        LoadDialog.show(mContext);
        RequestParams requestParams = creatParams("deleteordermbyid");
        requestParams.addBodyParameter("userid",user.getUser_id());
        requestParams.addBodyParameter("bf_orderm_id",bf_orderm_id);
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    initData();
                }
                ToastUtil.showShort(mContext, result.getMsg());
                LoadDialog.dismiss(mContext);
            }
        });
    }
    @Override
    protected void loadData() {
        if (needLoad()){
            initData();
        }

    }

    @Override
    protected void init() {

    }
}
