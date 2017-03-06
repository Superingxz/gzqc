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
import com.xolo.gzqc.ui.activity.pickcar.PayDetailsActivity;
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
 * 待交车-交车
 */
public class TurningCarFragment extends BaseFragment implements OnKeyDownListener{

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

    private List<CarInfo> list_info = new ArrayList<>();
    private CommenAdapter<CarInfo> adapter;

    private int type;

    private LicenseKeyboardUtil keyboardUtil;

    private TabChangeListener tabchagelistener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tabchagelistener = ((TabChangeListener) context);
    }

    public TurningCarFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_turning_car, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        type = getArguments().getInt("type", 0);

        keyboardUtil = new LicenseKeyboardUtil(mContext, carNunber, keyboardView);

        initLv();
        if (type == 1) {
            getlistwaitreturncar();
            tbCarno.setVisibility(View.GONE);
        }
    }


    @Override
    public void load() {
        super.load();
        getlistwaitreturncar();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_maintenance_list, mContext, list_info, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarInfo carInfo = list_info.get(position);

                holder.setText(R.id.item1, "车牌号：" + carInfo.getCarno());
                holder.setText(R.id.item2, "车型：" + carInfo.getBrands() + "  " + carInfo.getTypecode());
                holder.setText(R.id.item4, "到厂时间： " + carInfo.getIn_time());
                holder.setText(R.id.item5, "接车员：" + carInfo.getOperatoname());

                holder.getView(R.id.item3).setVisibility(View.GONE);
                holder.loadUrl(R.id.iv,carInfo.getBrands_path());
            }
        });

        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarInfo carInfo = list_info.get(position);
                Intent intent = new Intent(mContext, PayDetailsActivity.class);
                intent.putExtra(Key.OBJECT, carInfo);
                if (type == 1) {
                    intent.putExtra("type", 2);
                } else if (type == 2) {
                    intent.putExtra("type", 1);
                }
                startActivity(intent);
            }
        });

    }


    /**
     * 2-71 获取本维修厂的待交车车辆列表接口 getlistwaitreturncar(userid,dept_id)
     当前用户ID:userid,维修厂ID：dept_id
     */
    /**
     * 2-72 获取本维修厂的最近3天已交车辆列表接口 getlistalreadyreturncar(userid,dept_id)
     * 当前用户ID:userid,维修厂ID：dept_id
     */
    private void getlistwaitreturncar() {
        RequestParams params = null;
        if (type == 1) {
            params = creatParams("getlistwaitreturncar");
        } else {
            params = creatParams("getlistalreadyreturncar");
        }
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.CARNO, getText(carNunber));

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                List<CarInfo> data = result.getData();
                list_info.clear();
                tabchagelistener.change(type-1,data.size());
                list_info.addAll(data);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_info.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }

    @OnClick(R.id.select)
    public void onClick() {
        getlistwaitreturncar();
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
                return true;
            }else
                return false;
        }
        return false;
    }



}
