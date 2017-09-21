package com.hongliang.li.ans;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Administrator on 2017/9/21.
 */

public class FFwebView extends WebView{

    ProgressBar progressbar;

    public FFwebView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        progressbar
        progressbar = new ProgressBar(context, null,android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,10, 0, 0));
        addView(progressbar);
        setWebViewClient(mWebViewClient);
        setWebChromeClient(mWebChromeClient);
//        false
        getSettings().setSupportZoom(false);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setJavaScriptEnabled(true);
    }

    private WebViewClient mWebViewClient = new WebViewClient(){//TODO
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }};

    private WebChromeClient mWebChromeClient = new WebChromeClient(){//TODO

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (progressbar ==null)
                return;
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
        }
    };
}
