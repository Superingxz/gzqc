package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Store;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.ui.fragment.consumers.ConsumersMainFragment;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.DensityUtils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ScreenUtils;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 店铺
 */
public class StoreActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.tv_announcement)
    TextView tvAnnouncement;

    private List<Store.ContentBean> list_type = new ArrayList<>();
    private CommenAdapter<Store.ContentBean> adapter;

    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTitle();
        initSlv();
        Intent intent = getIntent();
        supplierhome(intent.getStringExtra(IntentConstant.SUPPLIERINFOR_ID));
    }

    private void initTitle() {
        titleview.setRightBg(R.mipmap.search_white);
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra(IntentConstant.SUPPLIERINFOR_ID,store.getC_supplierinfor_id());
                startActivity(intent);
            }
        });
    }


    /**
     * 7-22 供应商店铺首页接口
     * 应用的界面：应用于供应商店铺首页
     * supplierhome(userid,c_supplierinfor_id)
     * 当前用户ID:userid ,供应商ID:c_supplierinfor_id
     */
    private void supplierhome(String id) {
        RequestParams params = creatParams("supplierhome");
        params.addBodyParameter("c_supplierinfor_id", id);

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, ORMBean.StoreBean.class, new HttpUtil.HttpCallBack<ORMBean.StoreBean>() {
            @Override
            public void onSuccess(ORMBean.StoreBean result) {
                if (result.getRes().equals("1")) {
                    store = result.getData().get(0);
                    titleview.setTitle(store.getCompany_name());
                    tvAnnouncement.setText(store.getAnnouncement());

                    list_type.addAll(store.getContent());
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                mLoad.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }

    private void initSlv() {
        final int ivHeight = ScreenUtils.getScreenWidth(mContext) / 2 - DensityUtils.dp2px(mContext, 30);

        adapter = new CommenAdapter<>(R.layout.item_consumer_maingood, mContext, list_type, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final Store.ContentBean typeBean = list_type.get(position);
                holder.loadUrl(R.id.iv_good, typeBean.getPic_path());
                holder.setText(R.id.tv_price, "￥" + typeBean.getSales_price());
                holder.setText(R.id.tv_qty, "销量：" + typeBean.getQty());
                holder.setText(R.id.tv_title, typeBean.getGoods_name());

                ImageView iv = holder.getView(R.id.iv_good);
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                layoutParams.height = ivHeight;
                iv.setLayoutParams(layoutParams);
            }
        });

        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                intent.putExtra(IntentConstant.GOODSINFOR_ID, list_type.get(position).getC_goodsinfor_id());
                startActivity(intent);
            }
        });

    }

}
