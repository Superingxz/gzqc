package com.xolo.gzqc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.Key;

import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import com.xolo.gzqc.utils.Interface.ListDialogCallBack;


import org.xutils.http.RequestParams;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.rong.imageloader.core.ImageLoader;


/**
 * Created by Administrator on 2016/8/29.
 */
public abstract class BaseFragment extends Fragment{

    protected BaseActivity mContent;

    protected   static  final int REQUEST_CODE =  12;

    protected boolean isLoaded;

    protected Context mContext;
    protected User user;

    protected LoadDialog mLoad;

    private boolean isInit;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        mContent = (BaseActivity) context;
        user = getUser();
    }

    private  User   getUser(){
        if (mContent.user!=null){
            return   mContent.user;
        }else {
            return  SPManager.getUser(mContext);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    protected abstract void init();

    public   void   load(){}

    public   void   clear(){}

    public    RequestParams  creatParams(String action){
        RequestParams requestParams = new RequestParams(Constant.BASE_URL);
        requestParams.addBodyParameter(Key.ACTION,action);
        if (!TextUtils.isEmpty(user.getDept_id())){
            requestParams.addBodyParameter(Key.DEPT_ID,user.getDept_id());
        }
        if (!TextUtils.isEmpty(user.getUser_id())){
            requestParams.addBodyParameter(Key.USER_ID,user.getUser_id());
        }
        return  requestParams;
    }

      public   interface listDialogFace{
          void retrunTest(String text);
      }
    protected Dialog creatListDialog(String title, final List<String> list,final listDialogFace listDialogFace){
        View inflate = View.inflate(getActivity(),R.layout.dialog_list, null);
        TextView tv = (TextView) inflate.findViewById(R.id.title_dialog);
        tv.setVisibility(View.VISIBLE);
        tv.setText(title);
        ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);

        lv.setAdapter(new CommenAdapter<String>(R.layout.item_dialog_list, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.item1,String.valueOf(position+1));
                holder.setText(R.id.item2,list.get(position));
            }
        }));

        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(inflate)
                .setNegativeButton("取消", null).create();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 listDialogFace.retrunTest(list.get(position));
                dialog.dismiss();
            }
        });
        return dialog;
    }

    protected Dialog creatDateDialog(final TextView tv){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(mContent, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
        return dialog;
    }
    protected Dialog creatDateDialog(final listDialogFace dateDialogFace){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateDialogFace.retrunTest(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
        return dialog;
    }

    protected Dialog creatListDialog(String title, final List<String> list, final TextView targer){
        View inflate = LayoutInflater.from(mContent).inflate(R.layout.dialog_list, null);
        TextView tv = (TextView) inflate.findViewById(R.id.title_dialog);
        tv.setText(title);
        ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);

        lv.setAdapter(new CommenAdapter<String>(R.layout.item_dialog_list, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.item1,String.valueOf(position+1));
                holder.setText(R.id.item2,list.get(position));
            }
        }));

        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(inflate)
                .setNegativeButton("取消", null).create();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                targer.setText(list.get(position));
                dialog.dismiss();
            }
        });
        return dialog;
    }


    protected <T> Dialog creatListDialog(String title, final List<T> list, final TextView targer, final ListDialogCallBack<T> callBack){
        View inflate = LayoutInflater.from(mContent).inflate(R.layout.dialog_list, null);
        TextView tv = (TextView) inflate.findViewById(R.id.title_dialog);

//        tv.setText(title);

        ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);

        lv.setAdapter(new CommenAdapter<T>(R.layout.item_dialog_list, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.item1,String.valueOf(position+1));
                holder.setText(R.id.item2,callBack.setText(list.get(position)));
            }
        }));

        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(inflate)
                .create();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (targer!=null) {
                    targer.setText(callBack.setText(list.get(position)));
                }
                callBack.onClick(list.get(position));
                dialog.dismiss();
            }
        });
        return dialog;
    }

    protected Dialog creatListDialog(String title, final String[] list, final TextView targer){
        List<String> strings = Arrays.asList(list);
        return   creatListDialog(title,strings,targer);
    }

    protected  String  getText(TextView tv){
        return  tv.getText().toString().trim();
    }

    protected void setEditable(EditText[] eds){
        int length = eds.length;
        for(int i = 0;i<length;i++){
            eds[i].setEnabled(false);
        }
    }

    protected void setClickale(View[] eds){
        int length = eds.length;
        for(int i = 0;i<length;i++){
            eds[i].setClickable(false);
        }
    }

    protected void setCompoundDrawables(TextView[] eds){
        int length = eds.length;
        for(int i = 0;i<length;i++){
            eds[i].setCompoundDrawables(null, null, null, null);
        }
    }

    protected void setGone(View[] views){
        int length = views.length;
        for(int i = 0;i<length;i++){
            views[i].setVisibility(View.GONE);
        }
    }

    protected void setHint(TextView[] eds){
        int length = eds.length;
        for(int i = 0;i<length;i++){
            eds[i].setHint("无");
        }
    }

    protected void setVisibility(View[] eds,int visible){
        int length = eds.length;
        for(int i = 0;i<length;i++){
            eds[i].setVisibility(visible);
        }
    }

    public void loadPhoto(String url, ImageView iv){
        Glide.with(mContent).load(url).error(R.mipmap.img_error).into(iv);
//        ImageLoader.getInstance().displayImage(url, iv, App.getOptions());
    }

}
