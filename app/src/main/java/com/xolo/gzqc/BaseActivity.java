package com.xolo.gzqc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.adapter.SearchDialogAdapter;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static final int REQUEST_CODE = 12;

    protected Activity mContext;
    protected User user;

    protected LoadDialog mLoad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        user = SPManager.getUser(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public RequestParams creatParams(String action) {
        RequestParams requestParams = new RequestParams(Constant.BASE_URL);
        requestParams.addBodyParameter(Key.ACTION, action);
        if (!TextUtils.isEmpty(user.getDept_id())) {
            requestParams.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        }
        if (!TextUtils.isEmpty(user.getUser_id())) {
            requestParams.addBodyParameter(Key.USER_ID, user.getUser_id());
        }
        return requestParams;
    }

    protected Dialog creatListDialog(String title, final List<String> list, final TextView targer) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tv = (TextView) inflate.findViewById(R.id.title_dialog);
        tv.setText(title);
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


    protected <T> Dialog creatListDialog(String title, final List<T> list, final TextView targer, final ListDialogCallBack<T> callBack) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tv = (TextView) inflate.findViewById(R.id.title_dialog);

        tv.setText(title);

        ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);

        lv.setAdapter(new CommenAdapter<T>(R.layout.item_dialog_list, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.item1, String.valueOf(position + 1));
                holder.setText(R.id.item2, callBack.setText(list.get(position)));
            }
        }));

        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(inflate)
                .create();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (targer != null) {
                    targer.setText(callBack.setText(list.get(position)));
                }
                callBack.onClick(list.get(position));
                dialog.dismiss();
            }
        });
        return dialog;
    }

    protected <T> Dialog creatSearchListDialog(Context context, String title,
                                               final List<T> list,
                                               final SearchDialogAdapter.SearchDialogAdapterIface<T> searchDialogAdapterIface) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.search_dialog_list, null);
        TextView tv = (TextView) inflate.findViewById(R.id.title_dialog);
        tv.setText(title);
        final ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);
        EditText serch_edit = (EditText) inflate.findViewById(R.id.serch_edit);
        final SearchDialogAdapter searchDialogAdapter = new SearchDialogAdapter(context, R.layout.item_dialog_list, list);
        searchDialogAdapter.setSearchDialogAdapterIface(searchDialogAdapterIface);
        lv.setAdapter(searchDialogAdapter);
        serch_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                searchDialogAdapterIface.afterTextChanged(s.toString().trim(),searchDialogAdapter);
            }
        });
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(inflate)
                .create();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchDialogAdapterIface.onItemClick(position, list.get(position));
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                searchDialogAdapterIface.onDialogDismiss(searchDialogAdapter);
            }
        });
        return dialog;
    }


    protected <T> Dialog creatListLeftDialog(String title, final List<T> list, final TextView targer, final ListDialogCallBack<T> callBack) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
        TextView tv = (TextView) inflate.findViewById(R.id.title_dialog);

        tv.setText(title);

        ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);

        lv.setAdapter(new CommenAdapter<T>(R.layout.item_dialog_list, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                TextView t = holder.getView(R.id.item2);
                t.setGravity(Gravity.LEFT);
                t.setText(callBack.setText(list.get(position)));
            }
        }));

        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(inflate)
                .create();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (targer != null) {
                    targer.setText(callBack.setText(list.get(position)));
                }
                callBack.onClick(list.get(position));
                dialog.dismiss();
            }
        });
        return dialog;
    }


    protected Dialog creatListDialog(String title, final String[] list, final TextView targer) {
        List<String> strings = Arrays.asList(list);
        return creatListDialog(title, strings, targer);
    }

    protected Dialog creatDateDialog(final TextView tv) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
        return dialog;
    }


    protected String getText(TextView tv) {
        return tv.getText().toString().trim();
    }


    listDialogFace listDialogFace;

    protected Dialog creatListDialog(String title, final List<String> list, final listDialogFace listDialogFace) {
        View inflate = View.inflate(mContext, R.layout.dialog_list, null);
        TextView tv = (TextView) inflate.findViewById(R.id.title_dialog);
        tv.setVisibility(View.VISIBLE);
        tv.setText(title);
        ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);

        lv.setAdapter(new CommenAdapter<String>(R.layout.item_dialog_list, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.item1, String.valueOf(position + 1));
                holder.setText(R.id.item2, list.get(position));
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

    public interface listDialogFace {
        void retrunTest(String text);
    }

    public void clear() {
    }

    protected void setEditable(EditText[] eds) {
        int length = eds.length;
        for (int i = 0; i < length; i++) {
            eds[i].setEnabled(false);
        }
    }

    protected void setClickale(View[] eds) {
        int length = eds.length;
        for (int i = 0; i < length; i++) {
            eds[i].setClickable(false);
        }
    }

    protected void setCompoundDrawables(TextView[] eds) {
        int length = eds.length;
        for (int i = 0; i < length; i++) {
            eds[i].setCompoundDrawables(null, null, null, null);
        }
    }

    protected void setHint(TextView[] eds) {
        int length = eds.length;
        for (int i = 0; i < length; i++) {
            eds[i].setHint("无");
        }
    }

    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
