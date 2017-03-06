package com.xolo.gzqc.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */
public class SelectYearDialog  {

    private static   List<String>  list = new ArrayList<>();

    public static Dialog  creat(Context mContext, final TextView targer){
        if (list.size()<30){
            list.clear();
            for (int i = 1990;i<2018;i++){
                list.add(String.valueOf(i));
            }
        }

        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_list, null);
        ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);

        lv.setAdapter(new CommenAdapter<String>(R.layout.item_dialog_list, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.item1, String.valueOf(position + 1));
                holder.setText(R.id.item2, list.get(position));
            }
        }));

        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(inflate).create();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                targer.setText(list.get(position));
                dialog.dismiss();
            }
        });

        return dialog;
    }



}
