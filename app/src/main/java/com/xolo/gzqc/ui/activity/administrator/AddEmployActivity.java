package com.xolo.gzqc.ui.activity.administrator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Dept;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.ExEditText;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEmployActivity extends BaseActivity {

    @BindView(R.id.eet_name)
    ExEditText eetName;
    @BindView(R.id.eet_zh)
    ExEditText eetZh;
    @BindView(R.id.eet_dept)
    ExEditText eetDept;
    @BindView(R.id.eet_dety)
    ExEditText eetDety;
    @BindView(R.id.eet_sex)
    ExEditText eetSex;
    @BindView(R.id.eet_phone)
    ExEditText eetPhone;
    @BindView(R.id.eet_leader)
    ExEditText eetLeader;
    @BindView(R.id.eet_isnovalid)
    ExEditText eetIsnovalid;
    @BindView(R.id.eet_remark)
    ExEditText eetRemark;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.activity_add_employ)
    LinearLayout activityAddEmploy;

    private Dialog dialog_sex ;
    private Dialog dialog_leader ;
    private Dialog dialog_isnovalid ;
    private Dialog dialog_dept ;

    private String select_sex= "1";
    private String select_leader= "0";
    private String select_isnovalid= "0";

    private Dept dept;

    private List<Dept> deptList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employ);
        ButterKnife.bind(this);
        eetPhone.setContentInput(InputType.TYPE_CLASS_PHONE,11);
        initDialog();
    }

    private void initDialog() {
        dialog_sex = new AlertDialog.Builder(mContext)
                .setTitle("选择性别")
                .setSingleChoiceItems(new String[]{"男", "女"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            select_sex = "1";
                        } else select_sex = "0";
                    }
                })
                .setPositiveButton("取消",null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eetSex.setText(select_sex.equals("0")?"女":"男");
                    }
                })
                .create();

        dialog_leader = new AlertDialog.Builder(mContext)
                .setTitle("是否领导")
                .setSingleChoiceItems(new String[]{"否", "是"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            select_leader = "0";
                        } else select_leader = "1";
                    }
                })
                .setPositiveButton("取消",null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eetLeader.setText(select_leader.equals("0")?"否":"是");
                    }
                })
                .create();
        dialog_isnovalid = new AlertDialog.Builder(mContext)
                .setTitle("是否注销")
                .setSingleChoiceItems(new String[]{"否", "是"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            select_isnovalid = "0";
                        } else select_isnovalid = "1";
                    }
                })
                .setPositiveButton("取消",null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eetIsnovalid.setText(select_isnovalid.equals("0")?"否":"是");
                    }
                })
                .create();
        dialog_dept = creatListDialog("", deptList, eetDept.getEt_contetn(), new ListDialogCallBack<Dept>() {
            @Override
            public String setText(Dept control) {
                return control.getDept_name();
            }

            @Override
            public void onClick(Dept control) {
                dept = control;
            }
        });
    }


    @OnClick({R.id.eet_dept, R.id.eet_sex, R.id.eet_leader, R.id.eet_isnovalid, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eet_dept:
                if (deptList.size()==0){
                    getlistdept();
                }else
                    dialog_dept.show();
                break;
            case R.id.eet_sex:
                dialog_sex.show();
                break;
            case R.id.eet_leader:
                dialog_leader.show();
                break;
            case R.id.eet_isnovalid:
                dialog_isnovalid.show();
                break;
            case R.id.btn_commit:
                addemployeeinfo();
                break;
        }
    }

    /**
     * 获取本维修厂的部门列列表接口，用于下拉选择
     */
    private void getlistdept(){
        RequestParams params = creatParams("getlistdept");

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.DeptBean.class, new HttpUtil.HttpCallBack<ORMBean.DeptBean>() {
            @Override
            public void onSuccess(ORMBean.DeptBean result) {
                deptList.addAll(result.getData());
                dialog_dept.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 新增员工信息接口
     */
        private void addemployeeinfo(){
            String ustCode = eetZh.getText();
            String ustName = eetName.getText();
            if (TextUtils.isEmpty(ustCode)){
                ToastUtil.showShort(mContext,"请输入员工账号");
                return;
            }
            if (TextUtils.isEmpty(ustName)){
                ToastUtil.showShort(mContext,"请输入员工姓名");
                return;
            }
            if (dept==null){
                ToastUtil.showShort(mContext,"请选择部门");
                return;
            }

            RequestParams requestParams = new RequestParams(Constant.BASE_URL);
            requestParams.addBodyParameter(Key.ACTION, "addemployeeinfo");
            requestParams.addBodyParameter("userid",user.getUser_id());
            requestParams.addBodyParameter("user_code",ustCode);
            requestParams.addBodyParameter("user_name",ustName);
            requestParams.addBodyParameter("dept_id",dept.getDept_id());
            requestParams.addBodyParameter("is_leader",select_leader);
            requestParams.addBodyParameter("duty",eetDety.getText());
            requestParams.addBodyParameter("is_novalid",select_isnovalid);
            requestParams.addBodyParameter("sex",select_sex);
            requestParams.addBodyParameter("memo",eetRemark.getText());

            HttpUtil.getInstance().postLoading(mContext, requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
                @Override
                public void onSuccess(BaseBean result) {
                    ToastUtil.showShort(mContext,result.getMsg());
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }
            });
        }
}
