package com.xolo.gzqc.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.BrandListAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Brand;
import com.xolo.gzqc.bean.child.Car;
import com.xolo.gzqc.bean.child.TypeCode;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.rong.CharacterParser;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ScreenUtils;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.mention.SideBar;

/**
 * 车品牌选择
 */
public class CarBrandActivity extends BaseActivity {

    @BindView(R.id.search)
    EditText mSearch;
    @BindView(R.id.listview)
    ListView mListview;
    @BindView(R.id.group_dialog)
    TextView mGroupDialog;
    @BindView(R.id.sidrbar)
    SideBar mSidrbar;
    @BindView(R.id.fl_contain)
    FrameLayout flContain;


    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
     * 全部数据
     */
    private List<Brand> list_brand = new ArrayList<>();
    /**
     * 过滤后的数据
     */
    private List<Brand> list_filter = new ArrayList<>();

    private List<TypeCode> list_typecode = new ArrayList<>();

    private BrandListAdapter adapter;

    private Brand brand_select;
    private CommenAdapter<TypeCode> typeCodeCommenAdapter;

    private PopupWindow popupWindow;

    public static final int GET_BRAND = 98;

    public static final int RETURN_BRAND = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();

        initView();
        initLV();
        initPop();
        getbrands();
    }

    private void initPop() {
        int width = ScreenUtils.getScreenWidth(mContext) / 2;

        ListView listView = new ListView(mContext);
        listView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        typeCodeCommenAdapter = new CommenAdapter<>(R.layout.item_brand_typecode, mContext, list_typecode, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.typecode, list_typecode.get(position).getTypecode());
            }
        });
        listView.setAdapter(typeCodeCommenAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(IntentConstant.TYPE_CODE,list_typecode.get(position));
                intent.putExtra(IntentConstant.BRAND,brand_select);
                setResult(RETURN_BRAND,intent);
                finish();
            }
        });

        popupWindow = new PopupWindow(listView, width, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.activity_bg)));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    private void initLV() {
        adapter = new BrandListAdapter(mContext,list_filter);
        mListview.setAdapter(adapter);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brand_select = list_filter.get(position);
                gettypecodebybrandsid();
            }
        });
    }

    private void initView() {
        mSidrbar.setTextView(mGroupDialog);
        //设置右侧触摸监听
        mSidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListview.setSelection(position);
                }

            }
        });
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                     filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 2-93 获取车辆品牌接口 getbrands (userid,brands)
     * 当前用户ID:userid,车辆品牌名称（查询时使用）：brands
     */
    private void getbrands() {
        RequestParams params = creatParams("getbrands");

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.BrandBean.class, new HttpUtil.HttpCallBack<ORMBean.BrandBean>() {
            @Override
            public void onSuccess(ORMBean.BrandBean result) {
                list_brand.clear();
                list_brand.addAll(result.getData());

                list_filter.addAll(list_brand);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_brand.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 2-94 获取车辆型号接口 gettypecodebybrandsid(userid,brands_id)
     * 当前用户ID:userid,车辆品牌ID：brands_id
     */
    private void gettypecodebybrandsid() {
        RequestParams params = creatParams("gettypecodebybrandsid");
        params.addBodyParameter("brands_id", brand_select.getBrands_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.TypeCodeBean.class, new HttpUtil.HttpCallBack<ORMBean.TypeCodeBean>() {
            @Override
            public void onSuccess(ORMBean.TypeCodeBean result) {
                list_typecode.clear();
                list_typecode.addAll(result.getData());

                backgroundAlpha(0.5f);
                popupWindow.showAtLocation(flContain, Gravity.RIGHT, 0, 0);
                typeCodeCommenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr 需要过滤的 String
     */
    private void filterData(String filterStr) {
        ArrayList<Brand> list = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)){
            list.addAll(list_brand);
        }else {
            for (Brand b:list_brand) {
                String brands = b.getBrands();
                if (brands.contains(filterStr) || mCharacterParser.getSpelling(brands).startsWith(filterStr) ){
                    list.add(b);
                }
            }
        }

        list_filter.clear();
        list_filter.addAll(list);
        adapter.notifyDataSetChanged();
    }

}
