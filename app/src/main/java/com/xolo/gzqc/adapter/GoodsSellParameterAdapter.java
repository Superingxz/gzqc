package com.xolo.gzqc.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.StringBitmap;
import com.xolo.gzqc.utils.PhotoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2016/12/9.
 */
public class GoodsSellParameterAdapter extends CommonAdapter<String> {
    Context context;

    public GoodsSellParameterAdapter(Context context, int itemLayoutId, List<String> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.context = context;
    }
    HashMap<String,Bitmap> hashMap=new HashMap<>();
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void convert(final ViewHolder helper, final String item) {
        if(item.equals("default")){
            Glide.with(context).load(R.mipmap.tj).into(((ImageView)helper.getView(R.id.img_main)));
        }else{
            Glide.with(context).load(item).asBitmap().into(new ImageViewTarget<Bitmap>(((ImageView)helper.getView(R.id.img_main))) {
                @Override
                protected void setResource(Bitmap bitmap) {
                    ((ImageView)helper.getView(R.id.img_main)).setImageBitmap(bitmap);
                    hashMap.put(item,PhotoUtils.getZoomImage(bitmap,100));
                }
            });

        }
    }
    public HashMap<String, Bitmap> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Bitmap> hashMap) {
        this.hashMap = hashMap;
    }
}
