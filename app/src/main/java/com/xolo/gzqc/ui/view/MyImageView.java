package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.xutils.x;

public class MyImageView extends ImageView {



	String url;

	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyImageView(final Context context,final String url) {
		super(context);
		this.url=url;
		this.post(new Runnable() {
			@Override
			public void run() {
				x.image().bind(MyImageView.this,url);
			}
		});
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	
}
