package im.elvin.rssreader.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import im.elvin.rssreader.R;
import im.elvin.rssreader.dao.RSSFeedDao;
import im.elvin.rssreader.dao.RSSFeedDaoImpl;
import im.elvin.rssreader.model.RSSItem;

public class FavoriteListActivity extends ActionBarActivity {

    private ListView favoriteListView;
    private LinkedList<Map<String, Object>> itemList;
    private SimpleAdapter itemListAdapter;

    private RSSFeedDao feedDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        favoriteListView = (ListView) findViewById(R.id.favorite_listview);

        feedDao = new RSSFeedDaoImpl(this.getApplicationContext());
        this.loadFavoriteList();
    }

    private void loadFavoriteList() {
        String [] mFrom = new String[] {"rss_item_title", "rss_item_description"};
        int [] mTo = new int[] {R.id.rss_item_title, R.id.rss_item_description};

        List<RSSItem> items = feedDao.getFavoriteItem();
        itemList = convertMapList(items);

        itemListAdapter = new SimpleAdapter(this.getApplicationContext(), itemList, R.layout.rssitem_list_item, mFrom, mTo);
        favoriteListView.setAdapter(itemListAdapter);
    }

    private LinkedList<Map<String, Object>> convertMapList(List<RSSItem> items) {
        LinkedList<Map<String, Object>> itemList = new LinkedList<Map<String, Object>>();
        Map<String,Object> mMap = null;
        for (RSSItem item : items) {
            mMap = new HashMap<String,Object>();
            mMap.put("rss_item_id", item.getItemId());
            mMap.put("rss_item_title", Html.fromHtml(item.getTitle()));
            mMap.put("rss_item_description", Html.fromHtml(item.getDescription()));
            itemList.add(mMap);
        }
        return itemList;
    }

}
