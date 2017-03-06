package com.xolo.gzqc.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 万能适配器
 * 
 * @author yinbiao
 * 
 * @date 2015-6-9下午2:02:38
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;

	public CommonAdapter(Context context, int itemLayoutId, List<T> mDatas) {
		this.mContext = context;
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	public CommonAdapter(Context context, int itemLayoutId, T[] mDatas) {
		this.mContext = context;
		this.mDatas = Arrays.asList(mDatas);
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size();
		}
		return 0;
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = getViewHolder(position, convertView, parent);
		viewHolder.setPosition(position);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();
	}

	private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
	}

	public abstract void convert(ViewHolder helper, final T item);

	public void add(List<T> list) {
		if (list == null || list.size() == 0)
			return;
		this.mDatas.addAll(list);
		notifyDataSetChanged();
	}

	public void add(T t) {
		this.mDatas.add(t);
		notifyDataSetChanged();
	}

	public void add(int position, T t) {
		this.mDatas.add(position, t);
		notifyDataSetChanged();
	}

	public void clearAll() {
		this.mDatas.clear();
		notifyDataSetChanged();
	}
	public void removeItem(T t) {
		this.mDatas.remove(t);
		notifyDataSetChanged();
	}

	public void removeItem(int position) {
		this.mDatas.remove(position);
		notifyDataSetChanged();
	}

	public List<T> getData() {
		return mDatas;
	}

	/**
	 * 公共ViewHolder对象
	 * 
	 * @author yinbiao
	 * 
	 * @date 2015-6-9下午2:16:04
	 */
	public static class ViewHolder {
		private final SparseArray<View> mViews;
		private View mConvertView;
		private int position;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
			this.mViews = new SparseArray<View>();
			mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
			mConvertView.setTag(this);
		}

		/**
		 * 拿到一个ViewHolder对象
		 * 
		 * @param context
		 * @param convertView
		 * @param parent
		 * @param layoutId
		 * @param position
		 * @return
		 */
		public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
			if (convertView == null) {
				return new ViewHolder(context, parent, layoutId, position);
			}
			return (ViewHolder) convertView.getTag();
		}

		public View getConvertView() {
			return mConvertView;
		}

		/**
		 * 通过控件的Id获取对于的控件，如果没有则加入views
		 * 
		 * @param viewId
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public <T extends View> T getView(int viewId) {
			View view = mViews.get(viewId);
			if (view == null) {
				view = mConvertView.findViewById(viewId);
				mViews.put(viewId, view);
			}
			return (T) view;
		}

		/**
		 * 为TextView设置字符串
		 * 
		 * @param viewId
		 * @param text
		 * @return
		 */
		public void setText(int viewId, String text) {
			TextView view = getView(viewId);
			view.setText(text);
		}

		/**
		 * 为ImageView设置图片
		 * 
		 * @param viewId
		 * @param drawableId
		 * @return
		 */
		public void setImageResource(int viewId, int drawableId) {
			ImageView view = getView(viewId);
			view.setImageResource(drawableId);
		}

		/**
		 * 为ImageView设置图片
		 * 
		 * @param viewId
		 * @param
		 * @return
		 */
		public void setImageBitmap(int viewId, Bitmap bm) {
			ImageView view = getView(viewId);
			view.setImageBitmap(bm);
		}
	}

}
