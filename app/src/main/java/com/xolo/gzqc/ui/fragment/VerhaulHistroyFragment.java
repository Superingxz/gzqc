package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.VerhaulHistroyActivity;
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
 * 历史拆检
 */
public class VerhaulHistroyFragment extends BaseFragment implements OnKeyDownListener{

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.tb_carno)
    TableLayout tbCarno;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;

    private List<CarInfo> list_carno = new ArrayList<>();
    private CommenAdapter<CarInfo> adapter;

    private LicenseKeyboardUtil keyboardUtil;

    private TabChangeListener tabchagelistener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tabchagelistener = ((TabChangeListener) context);
    }



    public VerhaulHistroyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verhaul_histroy, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        keyboardUtil = new LicenseKeyboardUtil(mContext, carNunber, keyboardView);
        initLv();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_maintenance_list, mContext, list_carno, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarInfo carInfo = list_carno.get(position);

                holder.setText(R.id.item1, "车牌号：" + carInfo.getCarno());
                holder.setText(R.id.item2, "车型：" + carInfo.getBrands() + "  " + carInfo.getTypecode());
                holder.setText(R.id.item4, "来车时间： " + carInfo.getIn_time());
//                holder.setText(R.id.item5, "当前班组： " + carInfo.getTeam_name());
                holder.getView(R.id.item3).setVisibility(View.GONE);
                holder.getView(R.id.item5).setVisibility(View.GONE);
                holder.setText(R.id.item7, "接车员：" + carInfo.getOperatoname());
                holder.setVisibility(R.id.item7, View.VISIBLE);

                holder.loadUrl(R.id.iv,carInfo.getBrands_path());
            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContent, VerhaulHistroyActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra(Key.CARNO, list_carno.get(position).getCarno());
                intent.putExtra("id", list_carno.get(position).getBf_receive_car_id());
                startActivity(intent);
            }
        });

    }


    @Override
    public void load() {
        super.load();
        getlistsplit();
    }

    /**
     * 2-80 本维修厂历史拆检接口 getlistsplit(userid,dept_id)
     * 当前用户ID:userid,维修厂ID：dept_id
     */
    private void getlistsplit() {
        RequestParams params = creatParams("getlistsplit");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.CARNO,getText(carNunber));


        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                List<CarInfo> data = result.getData();
                list_carno.clear();
                tabchagelistener.change(1,data.size());
                list_carno.addAll(data);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_carno.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.select)
    public void onClick() {
        getlistsplit();
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardUtil.hideKeyboard();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil != null && keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
            } else {
                mContent.finish();
            }
            return false;
        }
        return true;
    }

}
