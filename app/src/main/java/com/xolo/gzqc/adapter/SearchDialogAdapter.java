package com.xolo.gzqc.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */
public class SearchDialogAdapter<T> extends CommonAdapter<T> {
    public SearchDialogAdapter(Context context, int itemLayoutId, List<T> mDatas) {
        super(context, itemLayoutId, mDatas);
    }

    @Override
    public void convert(ViewHolder helper, T item) {
        searchDialogAdapterIface.setItemData(helper, item);
    }

    SearchDialogAdapterIface searchDialogAdapterIface;

    public SearchDialogAdapterIface getSearchDialogAdapterIface() {
        return searchDialogAdapterIface;
    }

    public void setSearchDialogAdapterIface(SearchDialogAdapterIface searchDialogAdapterIface) {
        this.searchDialogAdapterIface = searchDialogAdapterIface;
    }

    public interface SearchDialogAdapterIface<T> {
        void setItemData(ViewHolder helper, T item);
        void onItemClick(int position,T item);
        void afterTextChanged(String text,SearchDialogAdapter searchDialogAdapter);
        void onDialogDismiss(SearchDialogAdapter searchDialogAdapter);
    }
}
