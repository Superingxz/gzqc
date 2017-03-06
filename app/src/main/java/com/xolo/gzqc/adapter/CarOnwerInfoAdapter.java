package com.xolo.gzqc.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.xolo.gzqc.R;
import com.xolo.gzqc.utils.PhotoUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wei on 2017/2/24.
 */
public class CarOnwerInfoAdapter extends CommonAdapter<String>{
    Context context;

    public CarOnwerInfoAdapter(Context context, int itemLayoutId, List<String> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.context = context;
    }
    HashMap<String,Bitmap> hashMap=new HashMap<>();
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void convert(final ViewHolder helper, final String item) {
        if(item.equals("default")){
            Glide.with(context).load(R.mipmap.tj).into(((ImageView)helper.getView(R.id.img)));
        }else{
            Glide.with(context).load(item).asBitmap().into(new ImageViewTarget<Bitmap>(((ImageView)helper.getView(R.id.img))) {
                @Override
                protected void setResource(Bitmap bitmap) {
                    ((ImageView)helper.getView(R.id.img)).setImageBitmap(bitmap);
                    hashMap.put(item, PhotoUtils.getZoomImage(bitmap,200));
//                    hashMap.put(item, bitmap);
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
