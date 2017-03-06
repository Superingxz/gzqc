package com.xolo.gzqc.ui.activity.administrator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.DataPermission;
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

import static android.app.Activity.RESULT_OK;

public class AddDataPermissionActivity extends BaseActivity {
    private Employee employee;
    private CommenAdapter<DataPermission> adapter;

    @BindView(R.id.mListView)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_permission);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initIntent();
        initListView();
        getlistdeptrole();
    }

    private void initListView() {
        adapter = new CommenAdapter<DataPermission>(R.layout.item_add_permission, mContext, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                DataPermission item = adapter.getItem(position);

                holder.setText(R.id.tv_num, (position + 1) + ".    ");
                holder.setText(R.id.tv_content, item.getDtype_name() );
                if (item.is_select()) {
                    holder.setCheck(R.id.cb, true);
                } else
                    holder.setCheck(R.id.cb, false);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataPermission item = adapter.getItem(position);
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
     * 获取该员工已存在的数据权限列表接口
     */
    private void getlistuserrole(final List<DataPermission> list) {
        RequestParams params = creatParams("getlistuserauthority");
        params.addBodyParameter("emp_id", employee.getUser_id());

        HttpUtil.getInstance().post(params, ORMBean.DataPermissionBean.class, new HttpUtil.HttpCallBack<ORMBean.DataPermissionBean>() {
            @Override
            public void onSuccess(ORMBean.DataPermissionBean result) {
                if (result.getRes().equals("1")) {
                    List<DataPermission> data = result.getData();
                    for (DataPermission r : data) {
                        for (DataPermission r2 : list) {
                            if (r.getDtype_id().equals(r2.getDtype_id())) {
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
     * 获取维修厂的数据权限列表接口
     */
    private void getlistdeptrole() {
        RequestParams params = creatParams("getlistdeptauthority");

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, ORMBean.DataPermissionBean.class, new HttpUtil.HttpCallBack<ORMBean.DataPermissionBean>() {
            @Override
            public void onSuccess(ORMBean.DataPermissionBean result) {
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
     * userid 	是 	string 	当前用户ID
     * emp_id 	是 	string 	员工ID
     * dept_id 	否 	string 	维修厂ID
     * dept_name 	否 	string 	维修厂名称
     * role_id 	是 	string 	角色ID,多个使用英文逗号隔开
     * dtype_id 	否 	string 	数据权限ID,多个使用英文逗号隔开
     */
    private void setroleandauthority() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
            DataPermission item = adapter.getItem(i);
            if (item.is_select()) {
                stringBuffer.append(item.getDtype_id() + ",");
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
        params.addBodyParameter("dtype_id", s);

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
