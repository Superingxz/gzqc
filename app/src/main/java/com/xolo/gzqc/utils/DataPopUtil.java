package com.xolo.gzqc.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xolo.gzqc.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/9/1.
 */
public class DataPopUtil {

    public  static  void initDate( DataCallback  callback) {
        Calendar data = Calendar.getInstance();

        String enddate = ""+data.get(Calendar.YEAR)+"年";
        enddate = enddate+ (data.get(Calendar.MONTH)+1)+"月";
        enddate = enddate+ data.get(Calendar.DAY_OF_MONTH)+"日";

        data.add(Calendar.MONTH, -3);
        String startdate = ""+data.get(Calendar.YEAR)+"年";
        startdate = startdate+ (data.get(Calendar.MONTH)+1)+"月";
        startdate = startdate+ data.get(Calendar.DAY_OF_MONTH)+"日";

        callback.setData(startdate,enddate);
    }

    public interface  DataCallback{
        void  setData(String start,String end);
    }

    public  static Dialog creatStartDateDialog(Context context,final TextView tv) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -1);
        tv.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(calendar.MONTH) + 1) + "-" + calendar.get(calendar.DAY_OF_MONTH));

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
        return dialog;
    }


    public  static Dialog creatEndDateDialog(Context context,final TextView tv) {
        Calendar calendar = Calendar.getInstance();

        tv.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(calendar.MONTH) + 1) + "-" + calendar.get(calendar.DAY_OF_MONTH));

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
        return dialog;
    }

}
