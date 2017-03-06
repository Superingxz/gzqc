package com.xolo.gzqc.ui.fragment.consumers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.GoodInfo;
import com.xolo.gzqc.ui.activity.consumers.GoodsInfoActivity;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情-商品评论
 */
public class CommentFragment extends BaseFragment implements GoodsInfoActivity.GoodInfoCallBack{

   private ScrollListView listView;

    private List<GoodInfo.CommentBean>  list_comment = new ArrayList<>();
    private CommenAdapter<GoodInfo.CommentBean> commentBeanCommenAdapter;

    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listView = new ScrollListView(mContent);
        return listView;
    }

    @Override
    protected void init() {
        initLv();
    }

    private void initLv() {
        commentBeanCommenAdapter = new CommenAdapter<>(R.layout.item_comment, mContent, list_comment, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                GoodInfo.CommentBean commentBean = list_comment.get(position);

                holder.loadUrl(R.id.iv,commentBean.getHead_portrait());
                holder.setText(R.id.tv_name,commentBean.getComment_name());
                holder.setText(R.id.tv_date,commentBean.getComment_time());
                holder.setText(R.id.tv_comment,commentBean.getComment_content());
            }
        });

        listView.setAdapter(commentBeanCommenAdapter);
    }

    @Override
    public void readInfo(GoodInfo goodInfo) {
        list_comment.addAll(goodInfo.getComment());
        commentBeanCommenAdapter.notifyDataSetChanged();
    }
}
