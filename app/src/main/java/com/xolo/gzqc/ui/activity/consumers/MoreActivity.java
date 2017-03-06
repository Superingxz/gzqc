package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.ConsumerMain;
import com.xolo.gzqc.bean.child.GoodMore;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.DensityUtils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ScreenUtils;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 更多商品分类
 */
public class MoreActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.lv)
    ListView lv;

    private List<GoodMore> list_type = new ArrayList<>();
    private CommenAdapter<GoodMore> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initLv();
        initIntent();
    }

    private void initLv() {
        final int ivHeight = ScreenUtils.getScreenWidth(mContext) / 2 - DensityUtils.dp2px(mContext, 30);


        adapter = new CommenAdapter<>(R.layout.item_type_good, mContext, list_type, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                GoodMore type = list_type.get(position);
                final List<GoodMore.GoodsBean> goods = type.getGoods();

                holder.setText(R.id.tv_title, type.getDisplay_data());
                holder.setVisibility(R.id.btn_more,View.GONE);

                if (goods.size() > 0) {
                    GridView gv = (GridView) holder.getView(R.id.sgv);
                    gv.setAdapter(new CommenAdapter<GoodMore.GoodsBean>(R.layout.item_consumer_maingood, mContext, goods, new CommenAdapter.AdapterCallback() {
                        @Override
                        public void setView(ViewHolder holder, int position) {
                            GoodMore.GoodsBean goodsBean = goods.get(position);

                            holder.loadUrl(R.id.iv_good, goodsBean.getPic_path());
                            holder.setText(R.id.tv_price, "￥" + goodsBean.getSales_price());
                            holder.setText(R.id.tv_qty, "销量：" + goodsBean.getQty());
                            holder.setText(R.id.tv_title,goodsBean.getGoods_name());

                            ImageView iv =  holder.getView(R.id.iv_good);
                            ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                            layoutParams.height = ivHeight;
                            iv.setLayoutParams(layoutParams);
                        }
                    }));
                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            GoodMore.GoodsBean goodsBean = goods.get(position);

                            Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                            intent.putExtra(IntentConstant.GOODSINFOR_ID,goodsBean.getC_goodsinfor_id());
                            startActivity(intent);
                        }
                    });
                }


            }
        });

        lv.setAdapter(adapter);
    }

    private void initIntent() {
        Intent intent = getIntent();

        titleview.setTitle( intent.getStringExtra(IntentConstant.TITLE));

        String value_data = intent.getStringExtra(IntentConstant.VALUE_DATA);
        getmoregoodsbytypeid(value_data);
    }


    /**
     * 7-3 按商品类别获取更多对应类别的数据接口
     应用的界面：应用于一级商品类别和二级商品类别，点击【更多】按钮，获取更多商品。
     getmoregoodsbytypeid(userid,value_data,type)
     当前用户ID:userid,商品类别显示值：value_data,商品类别（1为商品一级类别；2为商品二级类别）：type
     */
    private  void getmoregoodsbytypeid(String value_data){
        RequestParams params = creatParams("getmoregoodsbytypeid");
        params.addBodyParameter("value_data",value_data);
        params.addBodyParameter("type","2");

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.GoodMoreBean.class, new HttpUtil.HttpCallBack<ORMBean.GoodMoreBean>() {
            @Override
            public void onSuccess(ORMBean.GoodMoreBean result) {
                list_type.addAll(result.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }
}
