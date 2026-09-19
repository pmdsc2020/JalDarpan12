package com.dsc.jaldarpan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Jaldarpan — village water budget tool.
 * The whole app is one offline HTML file in assets/index.html.
 * This activity hosts it in a WebView and adds Android printing.
 *
 * No external libraries are used, so the build has no dependencies to clash.
 */
public class MainActivity extends Activity {

    private WebView web;

    // Injected after every page load so the board's print button uses Android's print dialog.
    private static final String PRINT_HOOK =
        "(function(){ if (window.__androidPrintReady) return; window.__androidPrintReady = true;" +
        "  window.print = function(){ try { AndroidBridge.printPage(); } catch(e){} };" +
        "})();";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        web = new WebView(this);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);        // localStorage — the app saves village data here
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setSupportZoom(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(false);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        web.addJavascriptInterface(new JsBridge(), "AndroidBridge");
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri u = request.getUrl();
                String scheme = u.getScheme() == null ? "" : u.getScheme();
                if (scheme.equals("file")) return false;          // stay inside the app
                if (scheme.equals("http") || scheme.equals("https")
                        || scheme.equals("tel") || scheme.equals("mailto")) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, u));
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, R.string.no_app, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript(PRINT_HOOK, null);
            }
        });

        if (savedInstanceState != null) {
            web.restoreState(savedInstanceState);
        } else {
            web.loadUrl("file:///android_asset/index.html");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        web.saveState(outState);
    }

    /** Back button: go back inside the app first, then leave. */
    @Override
    public void onBackPressed() {
        if (web != null && web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }

    /** Called from the page when the user taps छापा / PDF. */
    public class JsBridge {
        @JavascriptInterface
        public void printPage() {
            runOnUiThread(new Runnable() {
                @Override public void run() { doPrint(); }
            });
        }
    }

    private void doPrint() {
        PrintManager pm = (PrintManager) getSystemService(PRINT_SERVICE);
        if (pm == null) return;
        PrintDocumentAdapter adapter = web.createPrintDocumentAdapter("Jaldarpan");
        PrintAttributes attrs = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A3.asLandscape())
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();
        pm.print(getString(R.string.print_job), adapter, attrs);
    }
}
