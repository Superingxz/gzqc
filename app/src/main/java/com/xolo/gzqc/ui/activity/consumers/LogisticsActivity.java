package com.xolo.gzqc.ui.activity.consumers;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.ConsumerLogistics;
import com.xolo.gzqc.bean.child.Order;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 物流详情
 */
public class LogisticsActivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView lv;

    private Order order;

    private List<ConsumerLogistics.TracesBean>  list_traces = new ArrayList<>();

    private CommenAdapter<ConsumerLogistics.TracesBean> tracesBeanCommenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);
        ButterKnife.bind(this);

        order = (Order) getIntent().getParcelableExtra(IntentConstant.ORDER);

        initLV();

        getlogisticsinfo();
    }

    private void initLV() {
        tracesBeanCommenAdapter = new CommenAdapter<>(R.layout.item_logistics, mContext, list_traces, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                ConsumerLogistics.TracesBean tracesBean = list_traces.get(position);

                holder.setText(R.id.tv_content,tracesBean.getAcceptStation());
                holder.setText(R.id.tv_time,tracesBean.getAcceptTime());

                if (list_traces.size() == (position+1)){
                    holder.setTextColor(R.id.tv_content,R.color.green);
                    holder.setTextColor(R.id.tv_time,R.color.green);
                    holder.setImage(R.id.iv,R.mipmap.dot);
                }else {
                    holder.setTextColor(R.id.tv_content,R.color.gray1);
                    holder.setTextColor(R.id.tv_time,R.color.gray1);
                    holder.setImage(R.id.iv,R.drawable.shape_dot_logistics);
                }

                if ((position+1) == list_traces.size()){
                               holder.setVisibility(R.id.ll_divider, View.INVISIBLE);
                        }    else {
                            holder.setVisibility(R.id.ll_divider, View.VISIBLE);
                        }
            }
        });

        lv.setAdapter(tracesBeanCommenAdapter);
    }


    /**
     * 7-7 查看物流信息接口
     应用的界面：应用于供应商和消费者查看订单的物流信息。
     getlogisticsinfo(userid,emsno,shipper_code)
     当前用户ID:userid,快递单号:emsno,快递公司编码:shipper_code
     */
      private  void getlogisticsinfo(){
          RequestParams params = creatParams("getlogisticsinfo");
          params.addBodyParameter("emsno","518336682846");
          params.addBodyParameter("shipper_code","UC");
//          params.addBodyParameter("emsno",order.getEmsno());
//          params.addBodyParameter("shipper_code",order.getCompany_name());

          HttpUtil.getInstance().postLoading(mContext,params, ORMBean.ConsumerLogisticsBean.class, new HttpUtil.HttpCallBack<ORMBean.ConsumerLogisticsBean>() {
              @Override
              public void onSuccess(ORMBean.ConsumerLogisticsBean result) {
                  ConsumerLogistics logistics = result.getData().get(0);

                  List<ConsumerLogistics.TracesBean> traces = logistics.getTraces();
                    list_traces.addAll(traces);
                  tracesBeanCommenAdapter.notifyDataSetChanged();
              }

              @Override
              public void onError(Throwable ex, boolean isOnCallback) {
              }
          });
      }

}
