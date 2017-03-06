package com.xolo.gzqc.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.LogUtil;

import org.xutils.http.RequestParams;

public abstract class LazyFragment extends  BaseFragment{

	protected boolean isPrepared = false;// 是否初始化完毕
	protected boolean isVisibleToUser = false;

	protected boolean mHasLoadedOnce = false; // 加载过一次数据

	@Override
	public boolean getUserVisibleHint() {
		return super.getUserVisibleHint();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// 显示的时候是false 隐藏的时候是true 针对show和hide,从另一个activity退回来不会执行
	public void onHiddenChanged(boolean hidden) {
		this.isVisibleToUser = !hidden;
	};

	// 针对viewpage中fragment显示或隐藏执行的方法
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		this.isVisibleToUser = isVisibleToUser;
		loadData();

}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		isPrepared = true;
		loadData();

	}
	/**
	 * 是否需要加载
	 * 
	 * @return
	 */
	public boolean needLoad() {
		return isPrepared && isVisibleToUser && !mHasLoadedOnce;
	}

	/**
	 * 加载数据
	 */
	protected abstract void loadData();

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}




}
