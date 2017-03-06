package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xolo.gzqc.R;

import org.xutils.x;

public class SplashPageFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_splash, container, false);
		ImageView image = (ImageView) v.findViewById(R.id.view_pager_image);

		Bundle bundle = getArguments();
		if (bundle != null) {
			String upImageId = bundle.getString("upImageId");
			x.image().bind(image,upImageId);
		}
		return v;
	}
}
