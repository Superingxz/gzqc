package com.xolo.gzqc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Role;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.administrator.AdministratorMainActivity;
import com.xolo.gzqc.ui.activity.boss.BossMainActivity;
import com.xolo.gzqc.ui.activity.boss.DataAnalysisActivity;
import com.xolo.gzqc.ui.activity.pickcar.FuntionActivity;
import com.xolo.gzqc.ui.activity.procurement.ProcurementMainActivity;
import com.xolo.gzqc.ui.activity.team.TeamMainActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;

import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 角色选择
 */
public class RoleActivity extends BaseActivity {

    @BindView(R.id.btn_1)
    LinearLayout btn1;
    @BindView(R.id.btn_2)
    LinearLayout btn2;
    @BindView(R.id.btn_3)
    LinearLayout btn3;
    @BindView(R.id.btn_4)
    LinearLayout btn4;
    @BindView(R.id.btn_5)
    LinearLayout btn5;
    @BindView(R.id.btn_6)
    LinearLayout btn6;
    private LoadDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        ButterKnife.bind(this);

        getRole();
    }

    /**
     * 根据用户的ID，获取当前用户的所有角色ID及角色名称
     */
    private void getRole() {
        RequestParams params = creatParams("getListRolesByUserId");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("use_version", user.getUse_version());

        loadDialog.show(mContext);

        HttpUtil.getInstance().post(params, ORMBean.RoleBean.class, new HttpUtil.HttpCallBack<ORMBean.RoleBean>() {
            @Override
            public void onSuccess(ORMBean.RoleBean result) {
                if (result.getRes().equals("1")) {
                    List<Role> data = result.getData();
                    for (Role r : data) {
                        if (r.getRole_no().equals("95")) {
                            btn1.setVisibility(View.VISIBLE);
                        }
                        if (r.getRole_no().equals("96")) {
                            btn2.setVisibility(View.VISIBLE);
                        }
                        if (r.getRole_no().equals("97")) {
                            btn3.setVisibility(View.VISIBLE);
                        }
                        if (r.getRole_no().equals("98")) {
                            btn4.setVisibility(View.VISIBLE);
                        }
                        if (r.getRole_no().equals("90")) {
                            btn5.setVisibility(View.VISIBLE);
                        }
                        if (r.getRole_no().equals("85")) {
                            btn6.setVisibility(View.VISIBLE);
                        }
                    }
                }
                loadDialog.dismiss(mContext);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
            }
        });
    }


    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5,R.id.btn_6})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                startActivity(new Intent(RoleActivity.this, FuntionActivity.class));
                break;
            case R.id.btn_2:
                startActivity(new Intent(mContext, ProcurementMainActivity.class));
                break;
            case R.id.btn_3:
                startActivity(new Intent(mContext, TeamMainActivity.class));
                break;
            case R.id.btn_4:
                startActivity(new Intent(RoleActivity.this, CarOwnersActivity.class));
                break;
            case R.id.btn_5:
//                startActivity(new Intent(RoleActivity.this, DataAnalysisActivity.class));
                startActivity(new Intent(RoleActivity.this, BossMainActivity.class));
                break;
            case R.id.btn_6:
                startActivity(new Intent(RoleActivity.this, AdministratorMainActivity.class));
                break;
        }
        finish();
    }


}
