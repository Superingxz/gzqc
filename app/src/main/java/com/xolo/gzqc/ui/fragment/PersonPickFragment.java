package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.CheckVersionActivity;
import com.xolo.gzqc.ui.activity.EditPasswordActivity;
import com.xolo.gzqc.ui.activity.HelpActivity;
import com.xolo.gzqc.ui.activity.RoleActivity;
import com.xolo.gzqc.ui.activity.pickcar.FuntionActivity;
import com.xolo.gzqc.ui.activity.procurement.ProcurementMainActivity;
import com.xolo.gzqc.ui.activity.team.TeamMainActivity;
import com.xolo.gzqc.ui.view.ExEditText;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心-接车员
 */
public class PersonPickFragment extends BaseFragment {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.dept)
    TextView dept;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_show)
    CheckBox cbShow;
    @BindView(R.id.edit_password)
    Button editPassword;
    @BindView(R.id.edit_change_role)
    ExEditText editChangeRole;
    @BindView(R.id.ocupation)
    TextView ocupation;
    @BindView(R.id.tr_ocu)
    TableRow trOcu;

    private  boolean  isTeam;
    private  String role;

    public static  final  String  TRAM = "team";
    public static  final  String  PICK_CAR = "pick_car";
    public static  final  String  PROCUREMENT= "procurement";

    public PersonPickFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TeamMainActivity) {
                  isTeam = true;
            role = TRAM;
        }
        if (context instanceof FuntionActivity){
            role = PICK_CAR;
        }
        if (context instanceof ProcurementMainActivity){
            role = PROCUREMENT;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_pick, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        titleview.setLeftGone();

        if (user.getRole_count().equals("1")) {
            editChangeRole.setVisibility(View.GONE);
        }

        phone.setText(user.getPhone_code());
        name.setText(user.getUser_name());
        dept.setText(user.getDept_name());

        if (user.getSex().equals("1")) {
            sex.setText("男");
        } else {
            sex.setText("女");
        }

//        cbShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                } else {
//                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//            }
//        });

        if (isTeam){
            trOcu.setVisibility(View.VISIBLE);
            ocupation.setText(user.getRole_name());
        }

    }

    /**
     * 2-6 维修厂员工修改密码接口 setrepairmodifypassword(userid,pws) 当前用户ID:userid 密码:pws
     */
    private void setrepairmodifypassword() {
        String s = etPassword.getText().toString();
        if (s.length() < 6) {
            ToastUtil.showShort(mContent, "请输入6位新密码");
            return;
        }

        RequestParams params = creatParams("setrepairmodifypassword");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("pws", s);

        HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContent, result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @OnClick({R.id.edit_password, R.id.edit_change_role,R.id.eet_help,R.id.eet_password,R.id.eet_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_password:
                setrepairmodifypassword();
                break;
            case R.id.edit_change_role:
                startActivity(new Intent(mContent, RoleActivity.class));
                mContent.finish();
                break;
            case R.id.eet_help:
                Intent intent = new Intent(mContent, HelpActivity.class);
                intent.putExtra("role",role);
                startActivity(intent);
                break;
            case R.id.eet_password:
                startActivity(new Intent(mContent, EditPasswordActivity.class));
                break;
            case R.id.eet_version:
                startActivity(new Intent(mContext, CheckVersionActivity.class));
                break;
        }
    }

}
