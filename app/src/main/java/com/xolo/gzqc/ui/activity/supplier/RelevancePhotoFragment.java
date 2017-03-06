package com.xolo.gzqc.ui.activity.supplier;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.OrderPhotoAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.child.StringBitmap;
import com.xolo.gzqc.bean.child.SupplierSellGoods;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.fragment.LazyFragment;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ImageSelectUtils;
import com.xolo.gzqc.utils.PhotoUtils;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 相关图片
 * Created by Administrator on 2016/12/16.
 */
public class RelevancePhotoFragment extends LazyFragment {
    ImageView add_img;
    ImageSelectUtils imageSelectUtils;
    ArrayList<String> imgPath = new ArrayList<>();
    String goodsid;
    Button submit;
    OrderPhotoAdapter orderPhotoAdapter;
    ScrollListView lv;

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_goods_relevance_photo, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

    }


    public void setData(final SupplierSellGoods supplierSellGoods) {
        goodsid = supplierSellGoods.getC_goodsinfor_id();
        if (!TextUtils.isEmpty(supplierSellGoods.getPic_1())) {
            imgPath.add(supplierSellGoods.getPic_1());
        }
        if (!TextUtils.isEmpty(supplierSellGoods.getPic_2())) {
            imgPath.add(supplierSellGoods.getPic_2());
        }
        if (!TextUtils.isEmpty(supplierSellGoods.getPic_3())) {
            imgPath.add(supplierSellGoods.getPic_3());
        }
        orderPhotoAdapter.notifyDataSetChanged();
    }

    StringBuffer stringBuffer = new StringBuffer();

    void submit() {
        if (TextUtils.isEmpty(getGoodsid())) {
            ToastUtil.showShort(getContext(), "请先上传商品");
            return;
        }
        LoadDialog.show(mContext);
        stringBuffer = new StringBuffer();
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("savegoodsimages");
        requestParams.addBodyParameter("userid", "jxstar6852949");
        requestParams.addBodyParameter("goodsid", getGoodsid());
        for (int i = 0; i < imgPath.size(); i++) {
            Bitmap bitmap = PhotoUtils.compressImage(orderPhotoAdapter.getHashMap().get(imgPath.get(i)));
            stringBuffer.append(Bitmap2StrByBase64(bitmap) + ",");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        requestParams.addBodyParameter("pictures", stringBuffer.toString());
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(BaseBean result) {
                LoadDialog.dismiss(mContext);
                ToastUtil.showShort(getActivity(), result.getMsg());
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

    void init(View view) {
        lv = (ScrollListView) view.findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("是否删除？").setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderPhotoAdapter.getHashMap().remove(imgPath.get(position));
                        orderPhotoAdapter.removeItem(position);
                    }
                }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        orderPhotoAdapter = new OrderPhotoAdapter(getContext(), R.layout.order_photo_item, imgPath);
        lv.setAdapter(orderPhotoAdapter);
        submit = (Button) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgPath.size() == 0) {
                    ToastUtil.showShort(getContext(), "请选择图片");
                    return;
                }
                submit();
            }
        });
        imageSelectUtils = new ImageSelectUtils(getActivity());
        add_img = (ImageView) view.findViewById(R.id.add_img);
        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgPath.size() == 3) {
                    ToastUtil.showShort(getContext(), "最多只能上传3张相关图片");
                    return;
                }
                imageSelectUtils.openGalleryMuti(imgPath, 3);
            }
        });
        imageSelectUtils.setResultCallBack(new ImageSelectUtils.ResultCallBack() {
            @Override
            public void onHanlderSuccess(List<PhotoInfo> resultList) {
                for (PhotoInfo p : resultList) {
                    imgPath.add(p.getPhotoPath());
                }
                orderPhotoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onHanlderFailure(String errorMsg) {
                ToastUtil.showShort(getContext(), errorMsg);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void init() {

    }
}
