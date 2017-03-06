package com.xolo.gzqc.ui.activity.administrator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Employee;
import com.xolo.gzqc.bean.child.Role;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加角色权限
 */
public class AddRolePermissionActivity extends BaseActivity {

    private Employee employee;
    private CommenAdapter<Role> adapter;

    @BindView(R.id.mListView)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_role_permission);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initIntent();
        initListView();
        getlistdeptrole();
    }

    private void initListView() {
        adapter = new CommenAdapter<Role>(R.layout.item_add_permission, mContext, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Role item = adapter.getItem(position);

                holder.setText(R.id.tv_num, (position + 1) + ".    ");
                holder.setText(R.id.tv_content, item.getRole_name() + "(" + item.getRole_memo() + ")");
                if (item.is_select()) {
                    holder.setCheck(R.id.cb, true);
                } else
                    holder.setCheck(R.id.cb, false);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Role item = adapter.getItem(position);
                if (item.is_select()) {
                    item.setIs_select(false);
                } else {
                    item.setIs_select(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
        mListView.setAdapter(adapter);
    }

    private void initIntent() {
        employee = (Employee) getIntent().getSerializableExtra(IntentConstant.EMPLOYEE);
    }

    /**
     * 获取该员工已存在的角色列表接口
     */
    private void getlistuserrole(final List<Role> list) {
        RequestParams params = creatParams("getlistuserrole");
        params.addBodyParameter("emp_id", employee.getUser_id());

        HttpUtil.getInstance().post(params, ORMBean.RoleBean.class, new HttpUtil.HttpCallBack<ORMBean.RoleBean>() {
            @Override
            public void onSuccess(ORMBean.RoleBean result) {
                if (result.getRes().equals("1")) {
                    List<Role> data = result.getData();
                    for (Role r : data) {
                        for (Role r2 : list) {
                            if (r.getRole_no().equals(r2.getRole_no())) {
                                list.remove(r2);
                                break;
                            }
                        }
                    }
                }
                adapter.upDt(list);
                mLoad.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss();
            }
        });
    }

    /**
     * 获取维修厂的角色列表接口
     */
    private void getlistdeptrole() {
        RequestParams params = creatParams("getlistdeptrole");

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, ORMBean.RoleBean.class, new HttpUtil.HttpCallBack<ORMBean.RoleBean>() {
            @Override
            public void onSuccess(ORMBean.RoleBean result) {
                if (result.getRes().equals("1")) {
                    getlistuserrole(result.getData());
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                    mLoad.dismiss(mContext);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }


    /**
     * 给员工赋角色和权限接口
     */
    private void setroleandauthority() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
            Role item = adapter.getItem(i);
            if (item.is_select()) {
                stringBuffer.append(item.getRole_id() + ",");
            }
        }
        String s = stringBuffer.toString();
        if (TextUtils.isEmpty(s)){
            return;
        }
        int length = stringBuffer.length();
        stringBuffer.delete(length - 1, length);

        RequestParams params = creatParams("setroleandauthority");
        params.addBodyParameter("emp_id", employee.getUser_id());
        params.addBodyParameter("dept_name", user.getDept_name());
        params.addBodyParameter("role_id", s);

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @OnClick(R.id.btn)
    public void onClick() {
        setroleandauthority();
    }
}
