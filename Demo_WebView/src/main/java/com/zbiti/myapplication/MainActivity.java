package com.zbiti.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DownloadListener, View.OnClickListener {
    private TextView mtitle;
    private Button btn_back, btn_refresh;
    private WebView webView;
    private ImageView error_img;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        mtitle = (TextView) findViewById(R.id.title);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        webView = (WebView) findViewById(R.id.webView);
        error_img = (ImageView) findViewById(R.id.error);
    }

    private void initData() {
        btn_back.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);

        webView.getSettings().setJavaScriptEnabled(true); //设置WebView属性，能够执行Javascript脚本

        webView.getSettings().setBlockNetworkImage(true); // 将图片下载阻塞

        webView.loadUrl("http://shouji.baidu.com/"); // 加载网络页面
//        webView.loadDataWithBaseURL("", "html页面代码", "text/html", "utf8", "404");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mtitle.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
                if(newProgress == 100){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.getSettings().setBuiltInZoomControls(true);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(MainActivity.this, "同步失败，请稍候再试", Toast.LENGTH_SHORT).show();
                // 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。

//                webView.setVisibility(View.GONE);
//                error_img.setVisibility(View.VISIBLE);

//                webView.loadUrl("file:///android_asset/error.html"); // 404跳转页面

//                String errorHtml = "<html><body><h1>Page not find!</h1></body></html>";
//                webView.loadData(errorHtml, "text/html", "UTF-8");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.getSettings().setBlockNetworkImage(false); // 通过图片的延迟载入，让网页能更快地显示
            }
        });

        webView.setDownloadListener(this); // 设置下载监听
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
//              finish();
                webView.goBack();
                break;
            case R.id.btn_refresh:
                webView.reload();
                break;
        }
    }
}
