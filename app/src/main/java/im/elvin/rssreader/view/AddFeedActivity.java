package im.elvin.rssreader.view;

import android.app.Activity;
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
    private ProgressBar progressBar;

    private RSSFeedDao feedDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);

        feedAddressInput = (EditText) findViewById(R.id.feed_address_input);
        progressBar = (ProgressBar) findViewById(R.id.add_feed_progressbar);

        feedDao = new RSSFeedDaoImpl(getApplicationContext());

        feedAddressInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String feedAddress = feedAddressInput.getText().toString();
                    if (feedAddress != null && feedAddress.length() > 0) {
                        ParseFeedTask parseFeedTask = new ParseFeedTask();
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        parseFeedTask.execute(feedAddress);
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        feedDao.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ParseFeedTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String feedAddress = params[0];
            RSSFeed feed = null;
            String feedId = null;
            try {
                feed = RSSHelper.parseFeed(feedAddress);
                feedId = feedDao.createFeed(feed);
                feedDao.addItems(feedId, feed.getItemList());
            } catch (IOException e) {
                Log.e("parse", e.getMessage());
            } catch (XmlPullParserException e) {
                Log.e("parse", e.getMessage());
            }
            return feedId;
        }

        @Override
        protected void onPostExecute(String feedId) {
            progressBar.setVisibility(ProgressBar.INVISIBLE);

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
