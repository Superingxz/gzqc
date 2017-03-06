package com.xolo.gzqc.ui.activity.pickcar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.ExScrollView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.DataPopUtil;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ScreenUtils;
import com.xolo.gzqc.utils.ToastUtil;
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
 * 车辆综合信息
 */
public class CarInfoActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.date_start)
    TextView dateStart;
    @BindView(R.id.date_end)
    TextView dateEnd;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.btn_return_top)
    ImageView btnReturnTop;
    @BindView(R.id.esv)
    ExScrollView esv;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;

    private List<CarInfo> list_carno = new ArrayList<>();
    private CommenAdapter<CarInfo> adapter;

    private Dialog dialog_start;
    private Dialog dialog_end;
    private Dialog dialog;
    private CarInfo carInfo;
    private LicenseKeyboardUtil licenseKeyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        licenseKeyboardUtil = new LicenseKeyboardUtil(mContext,carNunber,keyboardView);

        initTitle();
        initEsv();
        initLv();
        initDialog();
        initIntent();

        /**
         * 车主端进来
         */
        if (!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))) {
            getCarNoData();
        }
    }

    void getCarNoData() {
        LoadDialog.show(CarInfoActivity.this);
        RequestParams requestParams = creatParams("getownercarinfo");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoCarInfo.class, new HttpUtil.HttpCallBack<ORMBean.CoCarInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }

            @Override
            public void onSuccess(ORMBean.CoCarInfo result) {
                if (result.getRes().equals("1") && result.getData().size() == 1) {
                    carNunber.setText(result.getData().get(0).getCarno());
                    listcarinfo();
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }


    private void initEsv() {
        final int limitHeight = ScreenUtils.getScreenHeight(mContext) / 2;
        esv.setOnScrollChangedListener(new ExScrollView.OnScrollchangedListener() {
            @Override
            public void change(int l, int t, int oldl, int oldt) {
                if (t > limitHeight) {
                    btnReturnTop.setVisibility(View.VISIBLE);
                } else {
                    btnReturnTop.setVisibility(View.GONE);
                }
            }
        });
    }


    private void initIntent() {
        String no = getIntent().getStringExtra(Key.CARNO);
        if (!TextUtils.isEmpty(no)) {
            carNunber.setText(no);
            listcarinfo();
        }
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_maintenance_list, mContext, list_carno, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarInfo carInfo = list_carno.get(position);

                holder.setText(R.id.item1, "车牌号：" + carInfo.getCarno());
                holder.setText(R.id.item2, "车型：" + carInfo.getBrands() + "  " + carInfo.getTypecode());
                holder.setText(R.id.item4, "来车时间： " + carInfo.getIn_time());
                holder.setText(R.id.item5, "维修状态： " + carInfo.getStatus());
                holder.loadUrl(R.id.iv, carInfo.getBrands_path());

                holder.setVisibility(R.id.item3, View.GONE);

                if (TextUtils.isEmpty(carInfo.getGive_date())) {
                    holder.setVisibility(R.id.item7, View.GONE);
                } else {
                    holder.setVisibility(R.id.item7, View.VISIBLE);
                    holder.setText(R.id.item7, "交车时间:" + carInfo.getGive_date());
                }

                if (TextUtils.isEmpty(carInfo.getQr_code())) {
                    holder.setVisibility(R.id.iv_1, View.GONE);
                } else
                    holder.setVisibility(R.id.iv_1, View.VISIBLE);
            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                carInfo = list_carno.get(position);
                dialog.show();
            }
        });
    }

    private void initDialog() {
        dialog_start = DataPopUtil.creatStartDateDialog(mContext,dateStart);
        dialog_end = DataPopUtil.creatEndDateDialog(mContext,dateEnd);
        final String[] strings;
        if (!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))) {
            strings = new String[]{"接车单", "报价单", "结算单", "维修进度", "车辆信息", "车主资料"};
        } else
            strings = new String[]{"接车单", "报价单", "结算单", "班组完工", "车辆信息", "车主资料"};

        final View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);

        ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);

        lv.setAdapter(new CommenAdapter<String>(R.layout.item_dialog_list, mContext, Arrays.asList(strings), new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.item2, strings[position]);
            }
        }));

        dialog = new AlertDialog.Builder(mContext).setView(inflate)
                .create();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 4:
                        intent = new Intent(mContext, AddCarInfoActivity.class);
                        break;
                    case 0:
                        intent = new Intent(mContext, ReadPickUpActivity.class);
                        break;
                    case 1:
                        if (TextUtils.isEmpty(carInfo.getBf_quoted_pricem_id())) {
                            ToastUtil.showShort(mContext, "该车未报价");
                            dialog.dismiss();
                            return;
                        }
                        intent = new Intent(mContext, OfferActivity.class);
                        break;
                    case 2:
                        if (TextUtils.isEmpty(carInfo.getBf_finished_id())) {
                            ToastUtil.showShort(mContext, "该车未结算");
                            dialog.dismiss();
                            return;
                        }
                        intent = new Intent(mContext, PayDetailsActivity.class);
                        intent.putExtra("type", 1);
                        break;
                    case 3:
                        if (!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))) {
                            intent = new Intent(mContext, ProgressActivity.class);
                            intent.putExtra(Key.CARNO, carInfo.getCarno());
                        } else
                            intent = new Intent(mContext, CompleteDetailActivity.class);
                        intent.putExtra("type", 1);
                        break;
                    case 5:
                        intent = new Intent(mContext, OwnerInfoActivity.class);
                        intent.putExtra(Key.IS_READ, true);
                        intent.putExtra("phone", carInfo.getPhone());
                        break;
                }
                intent.putExtra(Key.OBJECT, carInfo);
                if (!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))) {
                    intent.putExtra("carwoner", "carwoner");
                }
                startActivity(intent);

                dialog.dismiss();
            }
        });
    }

    private void initTitle() {
        titleview.setRightText("查询");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listcarinfo();
            }
        });
        if (!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))) {
            titleview.setTitle("历史维修记录");
        }
    }

    /**
     * 2-63 A 本维修厂车辆列表综合查询接口，含查询 listcarintegratedquery(userid,car_no,name,phone,dept_id,start_date,end_date,is_query)
     * 当前用户ID：userid,车牌号：car_no,车主姓名：name,车主手机号：phone,维修厂id：dept_id,维修开始日期:start_date,
     * 维修结束日期:end_date,是否查询.0 为不是查询，1为查询：is_query
     */
    private void listcarinfo() {
        RequestParams params = creatParams("listcarintegratedquery");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, carNunber.getText().toString().trim());
        params.addBodyParameter(Key.NAME, name.getText().toString().trim());
        params.addBodyParameter(Key.PHONE, phone.getText().toString().trim());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        if (!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))) {
            params.addBodyParameter("is_car_owner", "1");
        }
        params.addBodyParameter("start_date", dateStart.getText().toString().trim());
        params.addBodyParameter("end_date", dateEnd.getText().toString().trim());
        params.addBodyParameter("is_query", "1");

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


    @OnClick({R.id.date_start, R.id.date_end, R.id.btn_return_top})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_start:
                dialog_start.show();
                break;
            case R.id.date_end:
                dialog_end.show();
                break;
            case R.id.btn_return_top:
                esv.scrollTo(0, 0);
                break;
        }
    }

}
