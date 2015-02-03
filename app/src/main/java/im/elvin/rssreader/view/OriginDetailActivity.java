package im.elvin.rssreader.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import im.elvin.rssreader.R;

public class OriginDetailActivity extends ActionBarActivity {

    private WebView originDetailWebView;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origin_detail);

        url = (String) getIntent().getExtras().get("url");
        setTitle((String) getIntent().getExtras().get("title"));

        originDetailWebView = (WebView) findViewById(R.id.origin_detail_webview);
        originDetailWebView.loadUrl(url);
        WebSettings settings = originDetailWebView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_origin_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_copy_url) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(url);
            } else {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("链接已复制", url);
                clipboard.setPrimaryClip(clipData);
            }
            return true;
        } else if (id == R.id.action_menu_open_url_with_other_browser) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
