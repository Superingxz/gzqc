package com.xolo.gzqc.utils.adapterUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
public class CommenRecycleAdapter<T> extends RecyclerView.Adapter<ComRecyclerViewHolder> {
    private final LayoutInflater inflater;
    private  int layoutID;
    private Context context;
    private List<T> list;
    private RecyclerAdapterCallback callback;

    public CommenRecycleAdapter(int layoutID, Context context, List<T> list, RecyclerAdapterCallback callback) {
        this.layoutID = layoutID;
        this.list = list;
        this.context = context;
        this.callback = callback;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ComRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(layoutID, parent, false);
        return new ComRecyclerViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ComRecyclerViewHolder holder, int position) {
                    callback.setView(holder,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerAdapterCallback {
        void setView(ComRecyclerViewHolder holder, int position);
    }
}
