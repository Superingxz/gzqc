package com.xolo.gzqc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.ui.view.LoadDialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Created by Administrator on 2016/9/12.
 */
public class HttpUtil {
       private static  HttpUtil instance;
       private static  Gson  gson;
       private static  Context mContent;
       private static  final  int timeOut = 30*1000;

    public static HttpUtil getInstance(){
        if (instance == null){
            instance = new HttpUtil();
            gson = new Gson();
            mContent = x.app();
        }
        return  instance;
    }

    /**
     *
     * @param params
     * @param tClass
     * @param callBack
     * @param <T>
     */
    public   <T extends BaseBean> void post(RequestParams params, final Class<T> tClass, final HttpCallBack<T> callBack){
        LogUtil.i("params:"+params.getStringParams().toString());

        params.setConnectTimeout(timeOut);
        params.setCharset("gbk");

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                    LogUtil.i("result:"+result);

                try {
                    T t = gson.fromJson(result, tClass);
                        callBack.onSuccess(t);

                }catch (JsonSyntaxException s){
                    LogUtil.i("序列化失败");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                 if (!NetUtils.isConnected(mContent)){
                     ToastUtil.showShort(mContent,"无网络");
                 }else {
                     ToastUtil.showShort(mContent,"网络异常");
                 }

                callBack.onError(ex,isOnCallback);
                if(!TextUtils.isEmpty(ex.getMessage())){
                    LogUtil.d(ex.getMessage());
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 带读取提示框
     * @param params
     * @param tClass
     * @param callBack
     * @param <T>
     */
    public   <T extends BaseBean> void postLoading(final Activity context, RequestParams params , final Class<T> tClass , final HttpCallBack<T> callBack){

        LogUtil.i("params:"+params.getStringParams().toString());

        LoadDialog.show(context);

        params.setConnectTimeout(timeOut);
        params.setCharset("gbk");

        final Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.i("result:"+result);

                T t = null;
                try {
                     t= gson.fromJson(result, tClass);
                }catch (JsonSyntaxException s){
                    LogUtil.i("序列化失败");
                }

                if (t.getRes().equals("1")){
                    callBack.onSuccess(t);
                }else{
                    ToastUtil.showShort(mContent,t.getMsg());
                    callBack.onError(null,true);
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!NetUtils.isConnected(mContent)){
                    ToastUtil.showShort(mContent,"网络连接异常");
                }else {
                    ToastUtil.showShort(mContent,"网络异常");
                }
                if(!TextUtils.isEmpty(ex.getMessage())){
                    LogUtil.d(ex.getMessage());
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
           LoadDialog.dismiss(context);
            }
        });

        LoadDialog.setDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (cancelable.isCancelled()){
                    cancelable.cancel();
                }
            }
        });
    }

//    public static  void  cancelAll(){
//
//        for (Callback.Cancelable c:cancelableList) {
//            c.cancel();
//        }
//        cancelableList.clear();
//    }

    public  interface  HttpCallBack<T>{
        void  onSuccess(T result);
        void  onError(Throwable ex, boolean isOnCallback);
    }

}
