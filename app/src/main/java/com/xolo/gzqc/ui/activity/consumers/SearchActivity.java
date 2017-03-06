package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xolo.gzqc.App;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.Db.SearchContent;
import com.xolo.gzqc.bean.child.Logistics;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.ui.view.FlowLayout;
import com.xolo.gzqc.utils.DbUtils;
import com.xolo.gzqc.utils.DensityUtils;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.DbManager;
import org.xutils.common.util.DensityUtil;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;

    private PopupWindow dialog_search;

    private String supplierinfor_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initDialog();
        initIntent();
        initHistroy();
    }

    /**
     * 初始化搜索历史
     */
    private void initHistroy() {
           flowLayout.removeAllViews();

            DbUtils dbUtils = new DbUtils(mContext);
            List<SearchContent> all = dbUtils.get(SearchContent.class);

        if (all !=null){
            int size = all.size();
            LogUtil.i("size:"+size);
            int limitSize = 0;
            if (size>10){
                limitSize = size - 10;
            }

            for (int i = size-1;i>-1;i--){
                SearchContent searchContent = all.get(i);
                if (i >= limitSize){
                    final TextView textView = new TextView(mContext);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(searchContent.getContent());
                    textView.setBackgroundResource(R.drawable.shape_et_bg);
                    textView.setTextColor(ContextCompat.getColor(mContext,R.color.text1));
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etSearch.setText(textView.getText());
                        }
                    });
                    textView.setPadding(15,10,15,10);

                    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.leftMargin = 20;
                    lp.rightMargin = 20;
                    lp.topMargin = 20;
                    lp.bottomMargin = 20;

                    textView.setLayoutParams(lp);

                    flowLayout.addView(textView);
                }else {
                    WhereBuilder builder = WhereBuilder.b();
                    builder.and("content", "=",searchContent.getContent());
                    dbUtils.delete(SearchContent.class,builder);
                }
            }
            dbUtils.close();

        }

    }

    private void initIntent() {
        Intent intent = getIntent();
        supplierinfor_id = intent.getStringExtra(IntentConstant.SUPPLIERINFOR_ID);
        if (!TextUtils.isEmpty(supplierinfor_id)) {
            tvSearch.setVisibility(View.GONE);
        }
    }

    private void initDialog() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_seach, null);

        dialog_search = new PopupWindow(inflate, DensityUtils.dp2px(mContext, 70), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_search.setBackgroundDrawable(new BitmapDrawable());
        dialog_search.setFocusable(true);
        dialog_search.setOutsideTouchable(true);

        inflate.findViewById(R.id.tv_select_good).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSearch.setText("商品");
                dialog_search.dismiss();
            }
        });
        inflate.findViewById(R.id.tv_select_supplier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSearch.setText("供应商");
                dialog_search.dismiss();
            }
        });
    }

    @OnClick({R.id.tv_search, R.id.btn_search, R.id.iv_back,R.id.btn_delect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                dialog_search.showAsDropDown(tvSearch, 0, 30);
                break;
            case R.id.btn_search:
                if (TextUtils.isEmpty(getText(etSearch))) {
                    ToastUtil.showShort(mContext, "搜索内容不能为空");
                    return;
                }
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                intent.putExtra(IntentConstant.TYPE, getText(tvSearch).equals("商品") ? 1 : 2);
                intent.putExtra(IntentConstant.SEARCH_CONTETN, getText(etSearch));
                if (!TextUtils.isEmpty(supplierinfor_id)) {
                    intent.putExtra(IntentConstant.SUPPLIERINFOR_ID, supplierinfor_id);
                }

                /**
                 * 保存搜索内容
                 */
                DbUtils dbUtils = new DbUtils(mContext);

                WhereBuilder builder = WhereBuilder.b();
                builder.and("content", "=",getText(etSearch));
                dbUtils.delete(SearchContent.class,builder);

                SearchContent searchContent = new SearchContent();
                searchContent.setContent(getText(etSearch));
                 dbUtils.save(searchContent);

                dbUtils.close();

                    startActivity(intent);
                    finish();

                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_delect:
                DbUtils dbUtils_delect = new DbUtils(mContext);
                dbUtils_delect.delete(SearchContent.class);
                initHistroy();
                break;
        }
    }

}
