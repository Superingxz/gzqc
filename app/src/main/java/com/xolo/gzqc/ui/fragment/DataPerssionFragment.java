package com.xolo.gzqc.ui.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.DataPermission;
import com.xolo.gzqc.bean.child.Role;
import com.xolo.gzqc.ui.activity.administrator.PermissionActivity;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import static com.xolo.gzqc.R.id.textView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataPerssionFragment extends BaseFragment {
private ListView lv;
private PermissionActivity  permissionActivity;

    private CommenAdapter<DataPermission>  roleCommenAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        permissionActivity = (PermissionActivity) context;
    }

    public DataPerssionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lv = new ListView(getActivity());
        return lv;
    }

    @Override
    protected void init() {
        roleCommenAdapter =  new CommenAdapter<>(R.layout.item_permission, mContent, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.tv_content,(position+1)+"      "+roleCommenAdapter.getItem(position).getDtype_name());
            }
        });
        lv.setAdapter(roleCommenAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final DataPermission role = roleCommenAdapter.getItem(position);

                new AlertDialog.Builder(mContent)
                        .setTitle("删除权限")
                        .setMessage(role.getDtype_name())
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteuserrole( role.getUser_data_id());
                            }
                        })
                        .show();
            }
        });

        getlistuserrole();
    }

    /**
     * 获取该员工已存在的角色列表接口
     */
    private void getlistuserrole(){
        RequestParams params = creatParams("getlistuserauthority");
        params.addBodyParameter("emp_id",permissionActivity.getEmployee().getUser_id());

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.DataPermissionBean.class, new HttpUtil.HttpCallBack<ORMBean.DataPermissionBean>() {
            @Override
            public void onSuccess(ORMBean.DataPermissionBean result) {
                roleCommenAdapter.upDt(result.getData());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 删除员工的角色接口
     */
    private void deleteuserrole(String  id){
        RequestParams params = creatParams("deleteuserauthority");
        params.addBodyParameter("user_data_id",id);

        HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                load();
                ToastUtil.showShort(mContent,result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    public void load() {
        super.load();
        getlistuserrole();
    }
}
