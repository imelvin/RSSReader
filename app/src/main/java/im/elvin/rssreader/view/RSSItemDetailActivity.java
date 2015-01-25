package im.elvin.rssreader.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import im.elvin.rssreader.R;
import im.elvin.rssreader.dao.RSSFeedDao;
import im.elvin.rssreader.dao.RSSFeedDaoImpl;
import im.elvin.rssreader.model.RSSItem;

public class RSSItemDetailActivity extends Activity {

    private TextView titleView;
    private TextView descriptionView;
    private Button showOriginButton;

    private RSSFeedDao feedDao;

    private String itemId;
    private RSSItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_detail);

        titleView = (TextView) findViewById(R.id.detail_title);
        descriptionView = (TextView) findViewById(R.id.detail_description);
        showOriginButton = (Button) findViewById(R.id.show_origin_button);

        feedDao = new RSSFeedDaoImpl(this.getApplicationContext());
        itemId = getIntent().getExtras().getString("rss_item_id");

        item = feedDao.getItemByItemId(itemId);
        this.loadData(item);

        if (item.getLink() != null && item.getLink().length() > 0) {
            showOriginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(item.getLink()));
                    startActivity(intent);
                }
            });

            showOriginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        feedDao.close();
        super.onDestroy();
    }

    public void loadData(RSSItem item) {
        if (item != null) {
            titleView.setText(Html.fromHtml(item.getTitle()));
            descriptionView.setText(Html.fromHtml(item.getDescription() != null ? item.getDescription().replaceAll("<img.+?>", "") : ""));
        }
    }

}
