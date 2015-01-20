package im.elvin.rssreader.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import im.elvin.rssreader.model.RSSFeed;
import im.elvin.rssreader.model.RSSItem;

/**
 * Created by elvin on 15/1/19.
 */
public class RSSFeedDaoImpl implements RSSFeedDao {

    private final DBHelper dbHelper;
    private final SQLiteDatabase db;

    public RSSFeedDaoImpl(Context context) {
        this.dbHelper = new DBHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    @Override
    public List<RSSFeed> getAllFeedList() {
        return null;
    }

    @Override
    public List<RSSItem> getItemListByFeedId(int feedId) {
        return null;
    }

    @Override
    public RSSItem getItemByItemId(int itemId) {
        return null;
    }

    @Override
    public void createFeed(RSSFeed feed) {

    }

    @Override
    public void addItems(int feedId, List<RSSItem> itemList) {

    }

    @Override
    public void close() {
        db.close();
    }
}
