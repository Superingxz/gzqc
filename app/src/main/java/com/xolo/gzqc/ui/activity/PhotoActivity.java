package com.xolo.gzqc.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.configuration.Key;

import org.xutils.x;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.photoview.PhotoView;

public class PhotoActivity extends BaseActivity {

    @BindView(R.id.iv)
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Bitmap bitmap1 = getIntent().getParcelableExtra("bitmap");
        if (bitmap1!=null){
            iv.setImageBitmap(bitmap1);
            return;
        }

        byte[] byteArrayExtra = getIntent().getByteArrayExtra(Key.OBJECT);
        if(byteArrayExtra!=null&&byteArrayExtra.length>0){
            Bitmap b = BitmapFactory.decodeByteArray(byteArrayExtra,0,byteArrayExtra.length);
            iv.setImageBitmap(b);
            return;
        }

        String path = getIntent().getStringExtra("path");
        if (path!=null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(path,options);
            iv.setImageBitmap(bitmap);
            return;
        }

        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)){
            Glide.with(mContext).load(url).into(iv);
        }

    }

}
