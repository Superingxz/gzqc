package com.xolo.gzqc.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.Traces;

import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */
public class LogisticsAdapter extends CommonAdapter<Traces> {
    Context context;

    public LogisticsAdapter(Context context, int itemLayoutId, List<Traces> mDatas) {
        super(context, itemLayoutId, mDatas);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void convert(ViewHolder helper, Traces item) {
        helper.setText(R.id.content, item.getAcceptTime() + "  " + item.getAcceptStation()+"");
        if (helper.getPosition() == getData().size() - 1 && helper.getPosition() != 0) {
            //最后一条
            ((View) helper.getView(R.id.line_bootom)).setVisibility(View.GONE);
            ((View) helper.getView(R.id.line_top)).setVisibility(View.VISIBLE);
            ((ImageView) helper.getView(R.id.val_img)).setBackground(ContextCompat.getDrawable(context,R.drawable.logistics_val_bule));
            ((TextView) helper.getView(R.id.content)).setTextColor(Color.WHITE);
            helper.getView(R.id.content).setBackground(ContextCompat.getDrawable(context,R.mipmap.logosotocs_img_bule));
        } else if (getData().size() > 3 && helper.getPosition() != getData().size() - 1 && helper.getPosition() != 0) {
            //中间
            ((View) helper.getView(R.id.line_bootom)).setVisibility(View.VISIBLE);
            ((View) helper.getView(R.id.line_top)).setVisibility(View.VISIBLE);
            ((ImageView) helper.getView(R.id.val_img)).setBackground(ContextCompat.getDrawable(context,R.drawable.logistics_val_white));
            ((TextView) helper.getView(R.id.content)).setTextColor(Color.BLACK);
            helper.getView(R.id.content).setBackground(ContextCompat.getDrawable(context,R.mipmap.logosotocs_img));
        } else if (getData().size() == 1&&helper.getPosition()==0) {
            //第一条且只有一条
            ((View) helper.getView(R.id.line_bootom)).setVisibility(View.GONE);
            ((View) helper.getView(R.id.line_top)).setVisibility(View.GONE);
            ((ImageView) helper.getView(R.id.val_img)).setBackground(ContextCompat.getDrawable(context,R.drawable.logistics_val_white));
            ((TextView) helper.getView(R.id.content)).setTextColor(Color.BLACK);
            helper.getView(R.id.content).setBackground(context.getDrawable(R.mipmap.logosotocs_img));
        }else if(helper.getPosition()==0&&getData().size()>1){
            //第一条
            ((View) helper.getView(R.id.line_bootom)).setVisibility(View.VISIBLE);
            ((View) helper.getView(R.id.line_top)).setVisibility(View.GONE);
            ((ImageView) helper.getView(R.id.val_img)).setBackground(ContextCompat.getDrawable(context,R.drawable.logistics_val_white));
            ((TextView) helper.getView(R.id.content)).setTextColor(Color.BLACK);
            helper.getView(R.id.content).setBackground(ContextCompat.getDrawable(context,R.mipmap.logosotocs_img));
        }
    }
}
