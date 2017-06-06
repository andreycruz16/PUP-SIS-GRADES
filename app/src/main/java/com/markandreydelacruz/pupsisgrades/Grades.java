package com.markandreydelacruz.pupsisgrades;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Grades extends AppCompatActivity {

    AdView adView;
    AdRequest adRequest;

    private WebView webViewSisGrades;
    private ProgressDialog dialog;

    private String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle("Grades");
        urlString = getIntent().getExtras().getString("urlString");
        loadGrades();
        //show ads
        adView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void loadGrades() {
        dialog = new ProgressDialog(Grades.this);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
//                Toast.makeText(getApplicationContext(), "Process Canceled.", Toast.LENGTH_LONG).show();
//                finish(); //If you want to finish the activity.
                    Toast.makeText(getApplicationContext(), "Loading....", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();

        webViewSisGrades  = (WebView) findViewById(R.id.webViewSisGrades);
        webViewSisGrades.getSettings().setJavaScriptEnabled(true); // enable javascript
        webViewSisGrades.getSettings().setLoadWithOverviewMode(true);
        webViewSisGrades.getSettings().setUseWideViewPort(true);
        webViewSisGrades.getSettings().setBuiltInZoomControls(true);
        webViewSisGrades.getSettings().setDisplayZoomControls(false);
        webViewSisGrades.setWebViewClient(new WebViewClient() {
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
//            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                Toast.makeText(getApplicationContext(), "Connection Lost.", Toast.LENGTH_LONG).show();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
//                finish();
                String htmlData ="<h1>LOST CONNECTION</h1>";

                webViewSisGrades.loadUrl("about:blank");
                webViewSisGrades.loadDataWithBaseURL(null,htmlData, "text/html", "UTF-8",null);
                webViewSisGrades.invalidate();
            }

            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url){
                //True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
                Toast.makeText(getApplicationContext(), "Redirected. Try Again.", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Check Login Details.", Toast.LENGTH_LONG).show();
////                loadGrades();
////                finish();
//
//                while(count < 3) {
//                    if (dialog.isShowing()) {
//                        dialog.dismiss();
//                        webViewSisGrades .loadUrl(urlString);
//                    }
//                    count++;
////                    loadGrades();
//                }
//
//                Toast.makeText(getApplicationContext(), "Invalid login details.", Toast.LENGTH_LONG).show();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                finish();

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });


        webViewSisGrades.clearCache(true);
        webViewSisGrades.clearHistory();
        this.clearCookies(getApplication());

        webViewSisGrades .loadUrl(urlString);
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grades, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
//            new LowStocks.BackgroundTaskLowStocks().execute();
            loadGrades();
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
