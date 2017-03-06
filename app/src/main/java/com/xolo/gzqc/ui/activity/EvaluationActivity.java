package com.xolo.gzqc.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.child.Order;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单评论
 */
public class EvaluationActivity extends BaseActivity {

    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.lv)
    ListView lv;

    private String orderid;

    private List<Order.GoodsBean> goodsBeanList = new ArrayList<>();
    private CommenAdapter<Order.GoodsBean> objectCommenAdapter;
    private boolean isCommened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initIntent();
        initLv();

    }

    private void initLv() {
        objectCommenAdapter = new CommenAdapter<>(R.layout.item_evaluation, mContext, goodsBeanList, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final Order.GoodsBean goodsBean = goodsBeanList.get(position);

                holder.loadUrl(R.id.iv,goodsBean.getPic_path());
                holder.setText(R.id.tv_title,goodsBean.getGoodsname());
                holder.setText(R.id.tv_price,goodsBean.getFactprice());
                holder.setText(R.id.tv_qty,goodsBean.getQty());

                holder.addTextChange(R.id.et_comment, new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                             goodsBean.setComment(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        lv.setAdapter(objectCommenAdapter);
    }

    private void initIntent() {
        //        orderid = getIntent().getStringExtra(IntentConstant.ORDERID);
        Intent intent = getIntent();
        goodsBeanList = intent.getParcelableArrayListExtra(IntentConstant.GOOD);
    }

    @OnClick(R.id.btn_commit)
    public void onClick() {

        mLoad.show(mContext);
        isCommened = false;
        savecomment(0);
    }

    /**
     * 7-25 提交商品评价信息
     * savecomment(userid,orderid,comment)
     * 当前用户ID:userid,订单子表id:orderid,评价内容:comment
     */
    private void savecomment(final int position) {
        if (position>=goodsBeanList.size()){

            if (isCommened){
                ToastUtil.showShort(mContext,"发表评论成功");
                setResult(RESULT_OK);
                finish();
            }else {
                ToastUtil.showShort(mContext,"至少要评论一个商品");
            }

            mLoad.dismiss(mContext);
            return;
        }

        Order.GoodsBean goods = goodsBeanList.get(position);

        String comment = goods.getComment();
        if (!TextUtils.isEmpty(comment)){
            isCommened = true;

            RequestParams params = creatParams("savecomment");
            params.addBodyParameter("orderid", goods.getGoodsid());
            params.addBodyParameter("comment", comment);

            HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
                @Override
                public void onSuccess(BaseBean result) {

            }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    mLoad.dismiss(mContext);
                }
            });
        }

        savecomment(position+1);

    }

}
