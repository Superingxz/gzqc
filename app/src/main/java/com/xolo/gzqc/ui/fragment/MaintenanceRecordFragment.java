package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Ordermbuyparts;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.OnKeyDownListener;
import com.xolo.gzqc.utils.Interface.TabChangeListener;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 最近维修查询 - 构件预约
 */
public class MaintenanceRecordFragment extends BaseFragment implements OnKeyDownListener{


    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;
    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.select)
    Button select;


    private List<Ordermbuyparts> list = new ArrayList<>();
    private CommenAdapter<Ordermbuyparts> adapter;

    private LicenseKeyboardUtil keyboardUtil;

    private TabChangeListener tabchagelistener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tabchagelistener = ((TabChangeListener) context);
    }



    public MaintenanceRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maintenance_record, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        keyboardUtil = new LicenseKeyboardUtil(mContext, carNunber, keyboardView);
        initLv();
    }

    @Override
    public void load() {
        super.load();
        if (!isLoaded) {
            listrecentordermrepair();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardUtil.hideKeyboard();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_info_1, mContent, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Ordermbuyparts ordermbuyparts = list.get(position);

                holder.setText(R.id.item1, "姓名电话：" + ordermbuyparts.getName() + "/" + ordermbuyparts.getPhone());
                holder.setText(R.id.item2, "车牌号：" + ordermbuyparts.getCarno());
                holder.setText(R.id.item3, "预约时间：" + ordermbuyparts.getOperatdate());
                holder.setText(R.id.item4, "到厂时间：" + ordermbuyparts.getPlan_com_time());
                holder.setText(R.id.item5, "接车员：" + ordermbuyparts.getOperatoname());
                holder.setVisibility(R.id.item5, View.VISIBLE);
                holder.loadUrl(R.id.iv,ordermbuyparts.getBrands_path());
            }
        });
        lv.setAdapter(adapter);
    }


    /**
     * 2-10 B 本维修厂最近10天预约购件维修记录接口 listrecentordermrepair(userid,dept_id) 当前用户ID：userid,维修厂id：dept_id
     */
    private void listrecentordermrepair() {
        RequestParams params = creatParams("listrecentordermrepair");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.CARNO, getText(carNunber));

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.OrdermbuypartsBean.class, new HttpUtil.HttpCallBack<ORMBean.OrdermbuypartsBean>() {
            @Override
            public void onSuccess(ORMBean.OrdermbuypartsBean result) {
                List<Ordermbuyparts> data = result.getData();

                list.clear();
                tabchagelistener.change(2,data.size());
                list.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.select)
    public void onClick() {
        listrecentordermrepair();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil != null && keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
                return true;
            } else
                return false;
        }
        return false;
    }
}
