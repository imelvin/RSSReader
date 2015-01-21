package im.elvin.rssreader.view;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import im.elvin.rssreader.R;
import im.elvin.rssreader.dao.RSSFeedDao;
import im.elvin.rssreader.dao.RSSFeedDaoImpl;
import im.elvin.rssreader.model.RSSItem;

public class RSSItemDetailActivity extends Activity {

    private TextView titleView;
    private TextView descriptionView;

    private RSSFeedDao feedDao;

    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_detail);

        titleView = (TextView) findViewById(R.id.detail_title);
        descriptionView = (TextView) findViewById(R.id.detail_description);

        feedDao = new RSSFeedDaoImpl(this.getApplicationContext());
        itemId = getIntent().getExtras().getString("rss_item_id");

        this.loadData(feedDao.getItemByItemId(itemId));
    }

    @Override
    protected void onDestroy() {
        feedDao.close();
        super.onDestroy();
    }

    public void loadData(RSSItem item) {
        titleView.setText(item.getTitle());
        descriptionView.setText(item.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rssitem_detail, menu);
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
}
