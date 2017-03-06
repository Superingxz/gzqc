package com.xolo.gzqc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.Co_deNameAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.child.Dept;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class UpdateDepActivity extends BaseActivity {
    ListView lv;
    List<Dept> deptlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dep);
        deptlist = (List<Dept>) getIntent().getSerializableExtra("deptList");
        initView();

    }

    Co_deNameAdapter coadatper;

    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        coadatper = new Co_deNameAdapter(this, R.layout.item_depte, deptlist);
        lv.setAdapter(coadatper);
        coadatper.setDeptDelteIface(new Co_deNameAdapter.DeptDelteIface() {
            @Override
            public void delete(int postion, String id) {
                deleteDep(postion, id);
            }

            @Override
            public void choseDept(int postion, String id) {
                updateRepairDept(deptlist.get(postion).getDept_name(),id);
            }
        });
    }
    void updateRepairDept(final String name, final String id) {
        LoadDialog.show(UpdateDepActivity.this);
        RequestParams requestParams = creatParams("updaterepairdept");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("dept_id", id);
        requestParams.addBodyParameter("dept_name", name);
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    user.setDept_id(id);
                    user.setDept_name(name);
                    SPManager.saveUser(UpdateDepActivity.this, user);
                    finish();
                }
                ToastUtil.showShort(mContext, result.getMsg());
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
        });

    }
    void deleteDep(final int postion, String dept_id) {
        LoadDialog.show(this);
        RequestParams requestParams = creatParams("deleterepairdeptbyownerid");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("dept_id", dept_id);
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {

                if (result.getRes().equals("1")) {
                    deptlist.remove(postion);
                    coadatper.notifyDataSetChanged();

                }
                ToastUtil.showShort(mContext, result.getMsg());
                LoadDialog.dismiss(mContext);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
        });
    }
}
