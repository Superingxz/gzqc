package com.xolo.gzqc.ui.activity.pickcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 未付结账
 */
public class PaymentActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.lv)
    ListView lv;

    private List<CarInfo> list_carno = new ArrayList<>();

    private CommenAdapter<CarInfo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTitle();
        initLv();

        getlistunsettledcar();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_maintenance_list, mContext, list_carno, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarInfo carInfo = list_carno.get(position);

                holder.setText(R.id.item2,"车型："+carInfo.getBrands()+"  "+carInfo.getTypecode());
                holder.setText(R.id.item4,"到厂时间： "+carInfo.getIn_time());
                holder.setText(R.id.item1,"车牌号： "+carInfo.getCarno());
                holder.setText(R.id.item5, "接车员：" + carInfo.getOperatoname());

                holder.loadUrl(R.id.iv,carInfo.getBrands_path());

                holder.getView(R.id.item3).setVisibility(View.GONE);

              }

        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarInfo carInfo = list_carno.get(position);
                Intent intent = new Intent(mContext, PayDetailsActivity.class);
                intent.putExtra(Key.OBJECT,carInfo);
                startActivity(intent);
            }
        });
    }


    private void initTitle() {
        titleview.setRightText("查询");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlistunsettledcar();
            }
        });
    }


/**
 * 2-64 获取本维修厂的未结算车辆列表接口 getlistunsettledcar(userid,dept_id)
 当前用户ID:userid,维修厂ID：dept_id
 */
    private  void  getlistunsettledcar(){
        RequestParams params = creatParams("getlistunsettledcar");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                List<CarInfo> data = result.getData();
                list_carno.clear();
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


}
