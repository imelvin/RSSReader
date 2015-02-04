package im.elvin.rssreader.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import im.elvin.rssreader.R;
import im.elvin.rssreader.constant.Constant;
import im.elvin.rssreader.dao.RSSFeedDao;
import im.elvin.rssreader.dao.RSSFeedDaoImpl;
import im.elvin.rssreader.helper.RSSHelper;
import im.elvin.rssreader.model.RSSFeed;

public class AddFeedActivity extends Activity {

    private EditText feedAddressInput;
    private ProgressDialog progressDialog;

    private RSSFeedDao feedDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);

        feedAddressInput = (EditText) findViewById(R.id.feed_address_input);
        this.createProgressDialog();

        feedDao = new RSSFeedDaoImpl(getApplicationContext());

        feedAddressInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String feedAddress = feedAddressInput.getText().toString();
                    if (feedAddress != null && feedAddress.length() > 0) {
                        if (feedDao.isFeedExist(feedAddress)) {
                            Toast.makeText(getApplicationContext(), R.string.feed_address_exist, Toast.LENGTH_SHORT).show();
                        } else {
                            ParseFeedTask parseFeedTask = new ParseFeedTask();
                            parseFeedTask.execute(feedAddress);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("请等待");
        progressDialog.setMessage("正在获取订阅源...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class ParseFeedTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String feedAddress = params[0];
            RSSFeed feed = null;
            String feedId = null;
            try {
                feed = RSSHelper.parseFeed(feedAddress, AddFeedActivity.this, progressDialog);
                feedId = feedDao.createFeed(feed);
                feedDao.addItems(feedId, feed.getItemList());
                progressDialog.setProgress(100);
            } catch (IOException e) {
                Log.e("parse", e.getMessage());
            } catch (XmlPullParserException e) {
                Log.e("parse", e.getMessage());
            }
            return feedId;
        }

        @Override
        protected void onPostExecute(String feedId) {
            progressDialog.dismiss();

            if (feedId != null && feedId.length() > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("feed_id", feedId);
                getIntent().putExtras(bundle);
                setResult(Constant.RESULT_OK, getIntent());

                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.parse_error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
