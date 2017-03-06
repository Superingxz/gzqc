package com.xolo.gzqc.ui.activity.supplier;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.GoodsSellParameterAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.SaveGoodType;
import com.xolo.gzqc.bean.child.SupplierSellGoods;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollGridView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ImageSelectUtils;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 产品基本参数
 * Created by Administrator on 2016/12/9.
 */
public class GoodsSellParameterFragemnt extends BaseFragment {
    View view;
    Spinner spinner1;
    Spinner spinner2;
    EditText goods_name;
    EditText goods_standard;
    EditText goods_price_et;
    EditText goods_describe_et;
    Button submit;
    ImageView compress_img;
    EditText goods_unit;
    String firsttypeid, firsttype, secondtypeid, secondtype, firstvalue, secondvalue;
    List<SaveGoodType> saveGoodTypes = new ArrayList<>();
    List<SaveGoodType> saveGoodTypes1 = new ArrayList<>();
    //    List<String> type1 = new ArrayList<>();
//    List<String> type2 = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    GoodsSellParameterAdapter goodsSellParameterAdapter;
    ImageSelectUtils imageSelectUtils;
    SaveGoodsResultIfce SaveGoodsResultIfce;
    RelativeLayout typ1_layout, typ1_layout2;
    TextView typ1, typ2;
    int UPLOAD = 5;
    ArrayList<String> imgpath = new ArrayList<>();

    public GoodsSellParameterFragemnt.SaveGoodsResultIfce getSaveGoodsResultIfce() {
        return SaveGoodsResultIfce;
    }

    public void setSaveGoodsResultIfce(GoodsSellParameterFragemnt.SaveGoodsResultIfce saveGoodsResultIfce) {
        SaveGoodsResultIfce = saveGoodsResultIfce;
    }

    public interface SaveGoodsResultIfce {
        void retrunGoodsId(String id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
    }

    String goodsid;

    public void setData(final SupplierSellGoods supplierSellGoods) {
        goodsid = supplierSellGoods.getC_goodsinfor_id();
        typ1.setText(supplierSellGoods.getFirst_type());
        typ2.setText(supplierSellGoods.getSecond_type());
        goods_name.setText(supplierSellGoods.getGoods_name());
        goods_standard.setText(supplierSellGoods.getSecond_type());
        goods_unit.setText(supplierSellGoods.getSpecification_model());
        firsttype = supplierSellGoods.getFirst_type();
        firsttypeid = supplierSellGoods.getFirst_type_id();
        firstvalue = supplierSellGoods.getFirst_value_data();
        secondtype = supplierSellGoods.getSecond_type();
        secondtypeid = supplierSellGoods.getSecond_type_id();
        secondvalue = supplierSellGoods.getSecond_value_data();
        goods_price_et.setText(supplierSellGoods.getSales_price());
        goods_describe_et.setText(supplierSellGoods.getGoods_description());
        imgpath.remove("default");
        for (int i = 0; i < supplierSellGoods.getPicture().size(); i++) {
            imgpath.add(supplierSellGoods.getPicture().get(i).getPic_path());
        }
        if (imgpath.size() < UPLOAD && !imgpath.contains("default")) {
            imgpath.add("default");
        }
        goodsSellParameterAdapter.notifyDataSetChanged();
        getGoodsType(firstvalue, true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragemnt_goods_up, null);
            user = SPManager.getUser(mContext);
            ButterKnife.bind(getActivity(), view);
            initView(view);
            getGoodsType("", false);
        }
        return view;
    }


    void getGoodsType(final String type, final boolean OnItemClick) {
        LoadDialog.show(mContext);
        RequestParams requestParams = creatParams("getmoregoodsbytypeid");
        if (TextUtils.isEmpty(type)) {
            requestParams.addBodyParameter("type", "1");
        } else {
            requestParams.addBodyParameter("type", "2");
            requestParams.addBodyParameter("value_data", type);
        }
        HttpUtil.getInstance().post(requestParams, ORMBean.SaveGoodTypeBean.class, new HttpUtil.HttpCallBack<ORMBean.SaveGoodTypeBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.SaveGoodTypeBean result) {
                if (result.getRes().equals("1")) {
                    if (TextUtils.isEmpty(type)) {
//                        typ1.setText(saveGoodTypes.get(0).getValue_data());
                        saveGoodTypes.clear();
                        saveGoodTypes.addAll(result.getData());
                    } else {
                        if (!OnItemClick) {
                            typ2.setText(result.getData().get(0).getDisplay_data());
                            secondvalue = result.getData().get(0).getValue_data();
                            secondtype = result.getData().get(0).getDisplay_data();
                            secondtypeid = result.getData().get(0).getControl_id();
                        }
                        saveGoodTypes1.clear();
                        saveGoodTypes1.addAll(result.getData());
                    }
                } else {
                    if (!TextUtils.isEmpty(type)) {
                        saveGoodTypes1.clear();
                        typ2.setText("");
                        secondvalue = "";
                        secondtype = "";
                        secondtypeid = "";
                    }
                    ToastUtil.showShort(getActivity(), result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }


    void initView(View view) {
        typ1 = (TextView) view.findViewById(R.id.typ1);
        typ2 = (TextView) view.findViewById(R.id.typ2);
        typ1_layout = (RelativeLayout) view.findViewById(R.id.typ1_layout);
        typ1_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatListDialog("", saveGoodTypes, typ1, new ListDialogCallBack<SaveGoodType>() {
                    @Override
                    public String setText(SaveGoodType saveGoodType) {
                        return saveGoodType.getDisplay_data();
                    }

                    @Override
                    public void onClick(SaveGoodType saveGoodType) {
                        firstvalue = saveGoodType.getValue_data();
                        firsttype = saveGoodType.getDisplay_data();
                        firsttypeid = saveGoodType.getControl_id();
                        getGoodsType(saveGoodType.getValue_data(), false);
                    }
                }).show();
            }
        });

        typ1_layout2 = (RelativeLayout) view.findViewById(R.id.typ2_layout);
        typ1_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatListDialog("", saveGoodTypes1, typ2, new ListDialogCallBack<SaveGoodType>() {
                    @Override
                    public String setText(SaveGoodType saveGoodType) {
                        return saveGoodType.getDisplay_data();
                    }

                    @Override
                    public void onClick(SaveGoodType saveGoodType) {
                        secondvalue = saveGoodType.getValue_data();
                        secondtype = saveGoodType.getDisplay_data();
                        secondtypeid = saveGoodType.getControl_id();
                    }
                }).show();
            }
        });
        goods_unit = (EditText) view.findViewById(R.id.goods_unit);
        compress_img = (ImageView) view.findViewById(R.id.compress_img);
        goods_standard = (EditText) view.findViewById(R.id.goods_standard);
        goods_describe_et = (EditText) view.findViewById(R.id.goods_describe_et);
        goods_price_et = (EditText) view.findViewById(R.id.goods_price_et);
        goods_name = (EditText) view.findViewById(R.id.goods_name);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        ScrollGridView gv = (ScrollGridView) view.findViewById(R.id.gv);
        imageSelectUtils = new ImageSelectUtils(getActivity());
        imgpath.add("default");
        goodsSellParameterAdapter = new GoodsSellParameterAdapter(getActivity(), R.layout.item_supplier_main_img, imgpath);
        gv.setAdapter(goodsSellParameterAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i("test", "onItemClick: "+position);

                if (goodsSellParameterAdapter.getData().get(position).equals("default")) {
                    imageSelectUtils.openGalleryMuti((ArrayList<String>) imgpath, UPLOAD + 1);
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("是否删除？").setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            imgpath.remove(position);
                            dialog.dismiss();
                            if (!goodsSellParameterAdapter.getData().contains("default") && goodsSellParameterAdapter.getData().size() < 5) {
                                imgpath.add("default");
                            }
                            goodsSellParameterAdapter.notifyDataSetChanged();
                        }
                    }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }
            }
        });
        imageSelectUtils.setResultCallBack(new ImageSelectUtils.ResultCallBack() {
            @Override
            public void onHanlderSuccess(List<PhotoInfo> resultList) {
                imgpath.remove("default");
                for (PhotoInfo photoInfo : resultList) {
                    imgpath.add(photoInfo.getPhotoPath());
                }
                if (goodsSellParameterAdapter.getData().size() < 5) {
                    imgpath.add("default");
                }
                goodsSellParameterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onHanlderFailure(String errorMsg) {
                ToastUtil.showShort(getActivity(), errorMsg);
          }
        });

        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("test", "onItemLongClick: "+position);

                return false;
            }
        });

    }

    @Override
    protected void init() {

    }


    void submit() {
        if (TextUtils.isEmpty(goods_name.getText().toString())
                || TextUtils.isEmpty(goods_standard.getText().toString())
                || TextUtils.isEmpty(goods_unit.getText().toString())
                || TextUtils.isEmpty(goods_price_et.getText().toString())
                || TextUtils.isEmpty(goods_describe_et.getText().toString())
                || TextUtils.isEmpty(typ2.getText().toString())
                || TextUtils.isEmpty(typ1.getText().toString())) {
            ToastUtil.showShort(getContext(), "请填写完整的商品信息");
            return;
        }
        if (goodsSellParameterAdapter.getData().size() == 1) {
            ToastUtil.showShort(getContext(), "至少上传一张图片");
            return;
        }
        LoadDialog.show(mContext);
        RequestParams requestParams = null;
        if (!TextUtils.isEmpty(goodsid)) {
            requestParams = creatParams("updategoodsinfo");
            requestParams.addBodyParameter("goodsid", goodsid);
        } else {
            requestParams = creatParams("savegoodsinfo");
        }

        requestParams.addBodyParameter("goodsname", goods_name.getText().toString());
        requestParams.addBodyParameter("firsttypeid", firsttypeid);
        requestParams.addBodyParameter("model", goods_standard.getText().toString());
        requestParams.addBodyParameter("unit", goods_unit.getText().toString());
        requestParams.addBodyParameter("firsttype", firsttype);
        requestParams.addBodyParameter("firstvalue", firstvalue);
        requestParams.addBodyParameter("secondvalue", secondvalue);
        requestParams.addBodyParameter("secondtypeid", secondtypeid);
        requestParams.addBodyParameter("secondtype", secondtype);
        requestParams.addBodyParameter("price", goods_price_et.getText().toString());
        requestParams.addBodyParameter("discript", goods_describe_et.getText().toString());
        imgString = new StringBuffer();
        for (int i = 0; i < goodsSellParameterAdapter.getData().size(); i++) {
            if (!goodsSellParameterAdapter.getData().get(i).equals("default")) {
                    imgString.append(Bitmap2StrByBase64(goodsSellParameterAdapter.getHashMap().get(goodsSellParameterAdapter.getItem(i))) + ",");
            }
        }
        imgString.deleteCharAt(imgString.length() - 1);
        requestParams.addBodyParameter("pictures", imgString.toString());
        HttpUtil.getInstance().post(requestParams, ORMBean.SaveGoodsResultBean.class, new HttpUtil.HttpCallBack<ORMBean.SaveGoodsResultBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.SaveGoodsResultBean result) {
                if (result.getRes().equals("1")) {
                    if (SaveGoodsResultIfce != null) {
                        if (null != result.getData()) {
                            SaveGoodsResultIfce.retrunGoodsId(result.getData().get(0).getGoodsid());
                            submit.setBackgroundResource(R.drawable.shape_grey);
                            submit.setTextColor(Color.parseColor("#cccccc"));
                            submit.setText("已提交");
                            submit.setClickable(false);

                        }
                    }
                }
                ToastUtil.showShort(getContext(), result.getMsg());
                LoadDialog.dismiss(mContext);
            }
        });
    }

    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        int lengths = bos.toByteArray().length;
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    StringBuffer imgString = new StringBuffer();
}
