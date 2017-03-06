package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.HttpUtil;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarInfoFragment extends BaseFragment {

    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.brand)
    EditText brand;
    @BindView(R.id.models)
    EditText models;
    @BindView(R.id.displacement)
    TextView displacement;
    @BindView(R.id.year)
    EditText year;
    @BindView(R.id.drive)
    TextView drive;
    @BindView(R.id.fuel)
    TextView fuel;
    @BindView(R.id.operation)
    TextView operation;
    @BindView(R.id.vin_code)
    EditText vinCode;
    @BindView(R.id.engine)
    EditText engine;
    @BindView(R.id.login_data)
    TextView loginData;

    private  String id;


    public CarInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_info, container, false);
        ButterKnife.bind(this, view);

        CarInfo carInfo = (CarInfo) getArguments().getSerializable(Key.OBJECT);
        id =  carInfo.getBc_car_info_id();


        return view;
    }

    @Override
    protected void init() {
        getcarinfobycarinfoid(id);
    }

    @Override
    public void load() {
        super.load();
        getcarinfobycarinfoid(id);
    }

    /**
     * 通过车辆信息ID得到该车的车辆详细信息接口
     * @param id
     */
    private void getcarinfobycarinfoid(String id) {
        RequestParams params = creatParams("getcarinfobycarinfoid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CAR_INFO_ID, id);

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                CarInfo carInfo = result.getData().get(0);
                carNunber.setText(carInfo.getCarno());
                brand.setText(carInfo.getBrands());
                models.setText(carInfo.getTypecode());
                year.setText(carInfo.getProductyear());
                vinCode.setText(carInfo.getVincode());
                engine.setText(carInfo.getEnginecode());
                loginData.setText(carInfo.getReg_date());

                displacement.setText(carInfo.getOutput());
                drive.setText(carInfo.getDrive_type());
                fuel.setText(carInfo.getFuel());
                operation.setText(carInfo.getOperation_type());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

}
