package com.xolo.gzqc.ui.activity.boss;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.SumByType;
import com.xolo.gzqc.bean.child.SumDetail;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.ui.view.ExEditText;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.DataPopUtil;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自主预约明细
 */
public class DealAppointmentDetailedActivity extends BaseActivity {
    private List<SumDetail>  list = new ArrayList<>();

    @BindView(R.id.lv)
    ListView lv;

    private CommenAdapter<SumDetail> sumDetailCommenAdapter;
    private String type;
    private String startTime;
    private String endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_appointment_detailed);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initIntent();
        initLv();
        getorderdetail();
    }

    private void initLv() {
        sumDetailCommenAdapter = new CommenAdapter<>(R.layout.item_info_1, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                SumDetail sumDetail = list.get(position);

                holder.setText(R.id.item1,"车牌号："+sumDetail.getCarno());
                holder.setText(R.id.item2,"预约人："+sumDetail.getOperator());
                holder.setText(R.id.item3,"手机号码："+sumDetail.getPhone());
                holder.setText(R.id.item4,"预约时间："+sumDetail.getOperatdate());
                holder.loadUrl(R.id.iv,sumDetail.getBrands_path());
            }
        });
        lv.setAdapter(sumDetailCommenAdapter);
    }

    private void initIntent() {
        Intent intent = getIntent();
        type = intent.getStringExtra(IntentConstant.TYPE);
        startTime = intent.getStringExtra(IntentConstant.START_TIME);
        endTime = intent.getStringExtra(IntentConstant.END_TIME);
    }

    private void  getorderdetail(){
        RequestParams params = creatParams("getorderdetail");
        if (!TextUtils.isEmpty(type)){
            params.addBodyParameter("dtype",type);
        }else {
            params.addBodyParameter("startdate",startTime);
            params.addBodyParameter("enddate",endTime);
        }

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.SumDetailBean.class, new HttpUtil.HttpCallBack<ORMBean.SumDetailBean>() {
            @Override
            public void onSuccess(ORMBean.SumDetailBean result) {
                List<SumDetail> data = result.getData();
                list.addAll(data);
                sumDetailCommenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

}
