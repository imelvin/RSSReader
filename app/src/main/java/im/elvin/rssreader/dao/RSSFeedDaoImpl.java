package im.elvin.rssreader.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
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
        String sql = "select * from rss_feed";
        Cursor cursor = db.rawQuery(sql, null);
        List<RSSFeed> feedList = new ArrayList<RSSFeed>();

        while (cursor.moveToNext()) {
            String feedId = cursor.getString(0);
            String feedTitle = cursor.getString(1);
            String feedAddress = cursor.getString(2);
            String feedLink = cursor.getString(3);
            String feedDescription = cursor.getString(4);

            RSSFeed feed = new RSSFeed(feedId, feedTitle, feedAddress, feedLink, feedDescription);
            feedList.add(feed);
        }
        return feedList;
    }

    @Override
    public List<RSSItem> getItemListByFeedId(int feedId) {
        String sql = "select * from rss_item where feed_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(feedId)});
        List<RSSItem> itemList = new ArrayList<RSSItem>();

        while (cursor.moveToNext()) {
            String itemId = cursor.getString(0);
            String itemTitle = cursor.getString(1);
            String itemLink = cursor.getString(2);
            String itemDescription = cursor.getString(3);
            String itemCategory = cursor.getString(4);
            String itemAuthor = cursor.getString(5);

            RSSItem item = new RSSItem(itemId, itemTitle, itemLink, itemDescription, itemCategory, itemAuthor);
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public RSSItem getItemByItemId(int itemId) {
        String sql = "select * from rss_item where id = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(itemId)});
        List<RSSItem> itemList = new ArrayList<RSSItem>();

        cursor.moveToNext();
        String id = cursor.getString(0);
        String itemTitle = cursor.getString(1);
        String itemLink = cursor.getString(2);
        String itemDescription = cursor.getString(3);
        String itemCategory = cursor.getString(4);
        String itemAuthor = cursor.getString(5);

        RSSItem item = new RSSItem(id, itemTitle, itemLink, itemDescription, itemCategory, itemAuthor);

        return item;
    }

    @Override
    public void createFeed(RSSFeed feed) {
        String sql = "insert into rss_feed (feed_title, feed_address, feed_link, feed_description) values (?, ?, ?, ?)";
        db.execSQL(sql, new Object[] {feed.getTitle(), feed.getAddress(), feed.getLink(), feed.getDescription()});
    }

    @Override
    public void addItems(int feedId, List<RSSItem> itemList) {
        String sql = "insert into rss_item (item_title, item_link, item_description, item_category, item_author, feed_id) values (?, ?, ?, ?, ?, ?)";

        for (RSSItem item : itemList) {
            db.execSQL(sql, new Object[] {item.getTitle(), item.getLink(), item.getDescription(), item.getCategory(), item.getAuthor(), feedId});
        }
    }

    @Override
    public void close() {
        db.close();
    }
}
