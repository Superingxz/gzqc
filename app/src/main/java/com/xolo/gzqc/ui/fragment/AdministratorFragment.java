package com.xolo.gzqc.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Employee;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.activity.administrator.AddEmployActivity;
import com.xolo.gzqc.ui.activity.administrator.EditEmployeeActivity;
import com.xolo.gzqc.ui.view.ExEditText;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdministratorFragment extends BaseFragment {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.btn_add)
    ImageView btnAdd;
    @BindView(R.id.mListView)
    ListView mListView;
    @BindView(R.id.eet_jian_ban)
    ExEditText eetJianBan;

    private CommenAdapter<Employee> employeeCommenAdapter;

    private Dialog dialog_jian_ban;

    private String is_jian_ban;

    public AdministratorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_administrator, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 管理员首页-获取本部门的员工列表接口
     */
    private void getlistemployee() {
        RequestParams params = creatParams("getlistemployee");

        mLoad.show(mContent);
        HttpUtil.getInstance().post(params, ORMBean.EmployeeBean.class, new HttpUtil.HttpCallBack<ORMBean.EmployeeBean>() {
            @Override
            public void onSuccess(ORMBean.EmployeeBean result) {
                if (result.getRes().equals("1")) {
                    employeeCommenAdapter.upDt(result.getData());
                } else {
                    employeeCommenAdapter.clearAll();
                }
                mLoad.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContent);
            }
        });
    }

    @Override
    protected void init() {
        initListview();
        initTitle();
        initDialog();
        getlistemployee();
    }

    private void initDialog() {
        is_jian_ban = user.getUse_version();
        boolean use_version = is_jian_ban.equals("1");
        if (use_version){
            eetJianBan.setText("是");
        }else eetJianBan.setText("否");

        dialog_jian_ban = new AlertDialog.Builder(mContext)
                .setTitle("是否简版")
                .setSingleChoiceItems(new String[]{"是", "否"}, use_version?0:1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            is_jian_ban = "1";
                        } else {
                            is_jian_ban = "0";
                        }
                    }
                })
                .setPositiveButton("取消",null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (is_jian_ban.equals("1")) {
                            eetJianBan.setText("是");
                        } else {
                            eetJianBan.setText("否");
                        }
                        setuseversion();
                    }
                })
                .create();
    }

    private void initTitle() {
        titleview.setLeftGone();
        titleview.setTitle(user.getDept_name() + "   管理员");
    }

    private void initListview() {
        employeeCommenAdapter = new CommenAdapter<>(R.layout.item_employy, mContent, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Employee employee = employeeCommenAdapter.getItem(position);

                holder.setText(R.id.tv_number, String.valueOf(position + 1));
                holder.setText(R.id.tv_name, employee.getUser_name());
                holder.setText(R.id.tv_zh, employee.getUser_code());
                holder.setText(R.id.tv_brand, employee.getDept_name());
            }
        });
        mListView.setAdapter(employeeCommenAdapter);
    }

    @OnItemClick(R.id.mListView)
    public void onItemClick(int position) {
        Intent intent = new Intent(mContext, EditEmployeeActivity.class);
        intent.putExtra(IntentConstant.EMPLOYEE, employeeCommenAdapter.getItem(position));
        startActivityForResult(intent, 99);
    }

    @OnClick({R.id.btn_add,R.id.eet_jian_ban})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                startActivityForResult(new Intent(mContext, AddEmployActivity.class), 99);
                break;
            case R.id.eet_jian_ban:
                  dialog_jian_ban.show();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            getlistemployee();
        }
    }

    /**
     * 使用版本设置接口
     */
    private void setuseversion(){
        if (eetJianBan.getText().equals("是")){
            is_jian_ban = "1";
        }else  is_jian_ban = "0";

        RequestParams params = creatParams("setuseversion");
        params.addBodyParameter("use_version",is_jian_ban);

         HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
             @Override
             public void onSuccess(BaseBean result) {
                 ToastUtil.showShort(mContent,result.getMsg());

                 user.setUse_version(is_jian_ban);
                 SPManager.saveUser(mContent,user);
             }

             @Override
             public void onError(Throwable ex, boolean isOnCallback) {

             }
         });
    }
}
