package com.xolo.gzqc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.xolo.gzqc.R;
import com.xolo.gzqc.utils.PhotoUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/21.
 */
public class OrderPhotoAdapter extends  CommonAdapter<String> {
    Context context;
    public OrderPhotoAdapter(Context context, int itemLayoutId, List<String> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.context=context;
    }
    HashMap<String,Bitmap> hashMap=new HashMap<>();
    @Override
    public void convert(final ViewHolder helper, final String item) {
        Glide.with(context).load(item).asBitmap().into(new ImageViewTarget<Bitmap>(((ImageView)helper.getView(R.id.img))) {
            @Override
            protected void setResource(Bitmap bitmap) {
                ((ImageView)helper.getView(R.id.img)).setImageBitmap(bitmap);
                hashMap.put(item,PhotoUtils.getZoomImage(bitmap,100));
            }
        });

    }

    public HashMap<String, Bitmap> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Bitmap> hashMap) {
        this.hashMap = hashMap;
    }
}
