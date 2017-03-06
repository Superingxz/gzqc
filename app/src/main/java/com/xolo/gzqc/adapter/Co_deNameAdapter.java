package com.xolo.gzqc.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.Dept;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class Co_deNameAdapter extends CommonAdapter<Dept> {

    Context context;

    public Co_deNameAdapter(Context context, int itemLayoutId, List<Dept> mDatas) {
        super(context, itemLayoutId, mDatas);
    }


    @Override
    public void convert(final ViewHolder helper, final Dept item) {
        ((TextView)helper.getView(R.id.dept_name)).setText(item.getDept_name());
        helper.setText(R.id.dept_name,item.getDept_name());
        if(item.isChecck()){
            ((TextView)helper.getView(R.id.delete_dept)).setVisibility(View.GONE);
            ((TextView)helper.getView(R.id.update_dept)).setVisibility(View.GONE);
        }
        ((Button)helper.getView(R.id.update_dept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deptDelteIface.choseDept(helper.getPosition(),item.getDept_id());
            }
        });
        ((Button)helper.getView(R.id.delete_dept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deptDelteIface.delete(helper.getPosition(),item.getDept_id());
            }
        });

    }
    DeptDelteIface deptDelteIface;
    public  interface  DeptDelteIface{
        void delete(int postion,String id);
        void choseDept(int postion,String id);
    }

    public DeptDelteIface getDeptDelteIface() {
        return deptDelteIface;
    }

    public void setDeptDelteIface(DeptDelteIface deptDelteIface) {
        this.deptDelteIface = deptDelteIface;
    }
}
