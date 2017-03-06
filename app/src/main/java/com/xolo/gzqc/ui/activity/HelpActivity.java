package com.xolo.gzqc.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.PersonPickFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        init();

        String role = getIntent().getStringExtra("role");
        if (role.equals(PersonPickFragment.PICK_CAR)){
            mWebView.loadUrl("http://17ac5d70b7b5.ih5.cn/idea/f7jp9dk");
            return;
        }
        if (role.equals(PersonPickFragment.TRAM)){
            mWebView.loadUrl("http://17ac5d70b7b5.ih5.cn/idea/CglZkib");
            return;
        }
        if (role.equals(PersonPickFragment.PROCUREMENT)){
            mWebView.loadUrl("http://17ac5d70b7b5.ih5.cn/idea/1Y4QcJR");
            return;
        }
        if (role.equals("carowners")){
            mWebView.loadUrl("http://17ac5d70b7b5.ih5.cn/idea/vzzmaRe");
            return;
        }
    }

    private void init() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    class MyWebViewClient extends WebViewClient {

        ProgressDialog progressDialog;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//网页页面开始加载的时候
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(HelpActivity.this);
                progressDialog.show();
                mWebView.setEnabled(false);// 当加载网页的时候将网页进行隐藏
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {//网页加载结束的时候
            //super.onPageFinished(view, url);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                mWebView.setEnabled(true);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { //网页加载时的连接的网址
            view.loadUrl(url);
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else
            finish();
    }


}
