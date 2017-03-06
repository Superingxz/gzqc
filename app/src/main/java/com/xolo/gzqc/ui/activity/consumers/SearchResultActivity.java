package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xolo.gzqc.App;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.Db.SearchContent;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.ConsumerMain;
import com.xolo.gzqc.bean.child.SearchResult;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索
 */
public class SearchResultActivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView lv;
    private int type;
    private String key_word;
    private String supplier_id;

    private List<SearchResult> list_result = new ArrayList<>();
    private CommenAdapter<SearchResult> searchResultCommenAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        init();
    }

    private void init() {


        initIntent();
        initLv();
        clientsearch();
    }

    /**
     * 1为搜索消费者首页商品，2为搜索消费者首页供应商，3为搜索店铺商品
     */
    private void initIntent() {
        Intent intent = getIntent();
        type = intent.getIntExtra(IntentConstant.TYPE, 0);
        key_word = intent.getStringExtra(IntentConstant.SEARCH_CONTETN);
        supplier_id = intent.getStringExtra(IntentConstant.SUPPLIERINFOR_ID);

    }

    private void initLv() {
        if (type != 2) {
            searchResultCommenAdapter = new CommenAdapter<>(R.layout.item_good_search, mContext, list_result, new CommenAdapter.AdapterCallback() {
                @Override
                public void setView(ViewHolder holder, int position) {
                    SearchResult searchResult = list_result.get(position);

                    holder.loadUrl(R.id.iv, searchResult.getPic_path());
                    holder.setText(R.id.tv_title, searchResult.getGoods_name());
                    holder.setText(R.id.tv_price, searchResult.getSales_price());
                    holder.setText(R.id.tv_qty, "销量："+searchResult.getQty());
                }
            });
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SearchResult searchResult = list_result.get(position);

                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra(IntentConstant.GOODSINFOR_ID, searchResult.getC_goodsinfor_id());
                    startActivity(intent);
                }
            });
        } else  {
            searchResultCommenAdapter = new CommenAdapter<>(R.layout.item_supply_search, mContext, list_result, new CommenAdapter.AdapterCallback() {
                @Override
                public void setView(ViewHolder holder, int position) {
                    SearchResult searchResult = list_result.get(position);

                    holder.setText(R.id.tv_title, searchResult.getCompany_name());
                }
            });
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(mContext, StoreActivity.class);
                    intent1.putExtra(IntentConstant.SUPPLIERINFOR_ID,list_result.get(position).getC_supplierinfor_id());
                    startActivity(intent1);
                }
            });
        }



          lv.setAdapter(searchResultCommenAdapter);
    }


    /**
     * 7-2 消费者搜索商品和供应商接口
     * 应用的界面：消费者首页商品、供应商搜索和店铺首页的搜索。
     * clientsearch(userid,type,key_word,supplierinfor_id)
     * 当前用户ID:userid,搜索类别(1为搜索消费者首页商品，2为搜索消费者首页供应商，3为搜索店铺商品):type,搜索关键字:key_word,
     * 供应商ID(店铺首页搜索时候使用):supplierinfor_id
     */
    private void clientsearch() {
        RequestParams params = creatParams("clientsearch");
        params.addBodyParameter("type", TextUtils.isEmpty(supplier_id)?String.valueOf(type):"3");
        params.addBodyParameter("key_word", key_word);
        params.addBodyParameter("supplierinfor_id",supplier_id);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.SearchResultBean.class, new HttpUtil.HttpCallBack<ORMBean.SearchResultBean>() {
            @Override
            public void onSuccess(ORMBean.SearchResultBean result) {
                      list_result.addAll(result.getData());
                searchResultCommenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

}
