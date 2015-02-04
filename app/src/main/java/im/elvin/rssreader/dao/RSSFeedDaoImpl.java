package im.elvin.rssreader.dao;

import android.content.ContentValues;
import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import im.elvin.rssreader.constant.Constant;
import im.elvin.rssreader.model.RSSFeed;
import im.elvin.rssreader.model.RSSItem;

/**
 * Created by elvin on 15/1/19.
 */
public class RSSFeedDaoImpl implements RSSFeedDao {

    private final DBHelper dbHelper;

    public RSSFeedDaoImpl(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    @Override
    public boolean isFeedExist(String address) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(Constant.DB_PASSWORD);
        Cursor cursor = db.query("rss_feed", new String[] {"id"}, "feed_address=?", new String[] {address.toLowerCase()}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    @Override
    public List<RSSFeed> getAllFeedList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase(Constant.DB_PASSWORD);
        Cursor cursor = db.query("rss_feed", new String[] {"id", "feed_title", "feed_address", "feed_link", "feed_description"}, null, null, null, null, null);
        List<RSSFeed> feedList = new ArrayList<RSSFeed>();

        while (cursor.moveToNext()) {
            String feedId = cursor.getString(0);
            String feedTitle = cursor.getString(1);
            String feedAddress = cursor.getString(2);
            String feedLink = cursor.getString(3);
            String feedDescription = cursor.getString(4);

            RSSFeed feed = new RSSFeed(feedId, feedTitle, feedAddress, feedLink, feedDescription, null);
            feedList.add(feed);
        }
        cursor.close();
        db.close();
        return feedList;
    }

    @Override
    public List<RSSItem> getItemListByFeedId(String feedId, int pageStart, int pageLimit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(Constant.DB_PASSWORD);
        Cursor cursor = db.query("rss_item", new String[] {"id", "item_title", "item_link", "item_description", "item_category", "item_author"}, "feed_id=" + feedId, null, null, null, "id desc", pageStart + "," + pageLimit);
        List<RSSItem> itemList = new ArrayList<RSSItem>();

        while (cursor.moveToNext()) {
            String itemId = cursor.getString(0);
            String itemTitle = cursor.getString(1);
            String itemLink = cursor.getString(2);
            String itemDescription = cursor.getString(3);
            String itemCategory = cursor.getString(4);
            String itemAuthor = cursor.getString(5);

            boolean isFavorite = false;
            Cursor cursor2 = db.query("rss_favorite", new String[] {"item_id"}, "item_id=?", new String[] {itemId}, null, null, null);
            if (cursor2.moveToNext()) {
                isFavorite = true;
            }
            cursor2.close();

            RSSItem item = new RSSItem(itemId, itemTitle, itemLink, itemDescription, itemCategory, itemAuthor, isFavorite);
            itemList.add(item);
        }
        cursor.close();
        db.close();
        return itemList;
    }

    @Override
    public List<RSSItem> getNewItemListByFeedId(String feedId, String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(Constant.DB_PASSWORD);
        Cursor cursor = db.query("rss_item", new String[] {"id", "item_title", "item_link", "item_description", "item_category", "item_author"}, "feed_id=? and id > ?", new String[] {feedId, id}, null, null, "id desc");
        List<RSSItem> itemList = new ArrayList<RSSItem>();

        while (cursor.moveToNext()) {
            String itemId = cursor.getString(0);
            String itemTitle = cursor.getString(1);
            String itemLink = cursor.getString(2);
            String itemDescription = cursor.getString(3);
            String itemCategory = cursor.getString(4);
            String itemAuthor = cursor.getString(5);

            boolean isFavorite = false;
            Cursor cursor2 = db.query("rss_favorite", new String[] {"item_id"}, "item_id=?", new String[] {itemId}, null, null, null);
            if (cursor2.moveToNext()) {
                isFavorite = true;
            }
            cursor2.close();

            RSSItem item = new RSSItem(itemId, itemTitle, itemLink, itemDescription, itemCategory, itemAuthor, isFavorite);
            itemList.add(item);
        }
        cursor.close();
        db.close();
        return itemList;
    }

    @Override
    public List<RSSItem> getOldItemListByFeedId(String feedId, String id, int pageLimit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(Constant.DB_PASSWORD);
        Cursor cursor = db.query("rss_item", new String[] {"id", "item_title", "item_link", "item_description", "item_category", "item_author"}, "feed_id=? and id < ?", new String[] {feedId, id}, null, null, "id desc", "0," + pageLimit);
        List<RSSItem> itemList = new ArrayList<RSSItem>();

        while (cursor.moveToNext()) {
            String itemId = cursor.getString(0);
            String itemTitle = cursor.getString(1);
            String itemLink = cursor.getString(2);
            String itemDescription = cursor.getString(3);
            String itemCategory = cursor.getString(4);
            String itemAuthor = cursor.getString(5);

            boolean isFavorite = false;
            Cursor cursor2 = db.query("rss_favorite", new String[] {"item_id"}, "item_id=?", new String[] {itemId}, null, null, null);
            if (cursor2.moveToNext()) {
                isFavorite = true;
            }
            cursor2.close();

            RSSItem item = new RSSItem(itemId, itemTitle, itemLink, itemDescription, itemCategory, itemAuthor, isFavorite);
            itemList.add(item);
        }
        cursor.close();
        db.close();
        return itemList;
    }

    @Override
    public RSSItem getItemByItemId(String itemId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(Constant.DB_PASSWORD);
        Cursor cursor = db.query("rss_item", new String[]{"id", "item_title", "item_link", "item_description", "item_category", "item_author"}, "id=?", new String[]{itemId}, null, null, null);
        List<RSSItem> itemList = new ArrayList<RSSItem>();

        cursor.moveToNext();
        String id = cursor.getString(0);
        String itemTitle = cursor.getString(1);
        String itemLink = cursor.getString(2);
        String itemDescription = cursor.getString(3);
        String itemCategory = cursor.getString(4);
        String itemAuthor = cursor.getString(5);
        cursor.close();

        boolean isFavorite = false;
        cursor = db.query("rss_favorite", new String[] {"item_id"}, "item_id=?", new String[] {itemId}, null, null, null);
        if (cursor.moveToNext()) {
            isFavorite = true;
        }
        cursor.close();

        RSSItem item = new RSSItem(id, itemTitle, itemLink, itemDescription, itemCategory, itemAuthor, isFavorite);

        db.close();
        return item;
    }

    @Override
    public String createFeed(RSSFeed feed) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(Constant.DB_PASSWORD);
        ContentValues values = new ContentValues();
        values.put("feed_title", feed.getTitle());
        values.put("feed_address", feed.getAddress().toLowerCase());
        values.put("feed_link", feed.getLink());
        values.put("feed_description", feed.getDescription());
        long id = db.insert("rss_feed", null, values);
        db.close();
        return String.valueOf(id);
    }

    @Override
    public void addItems(String feedId, List<RSSItem> itemList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(Constant.DB_PASSWORD);
        String sql = "insert into rss_item (item_title, item_link, item_description, item_category, item_author, feed_id) values (?, ?, ?, ?, ?, ?)";

        for (RSSItem item : itemList) {
            Cursor cursor = db.query("rss_item", new String[] {"id"}, "feed_id=? and item_link=?", new String[] {feedId, item.getLink()}, null, null, null);
            if (!cursor.moveToNext()) {
                db.execSQL(sql, new Object[] {item.getTitle(), item.getLink(), item.getDescription(), item.getCategory(), item.getAuthor(), feedId});
            }
            cursor.close();
        }
        db.close();
    }

    @Override
    public List<RSSItem> getFavoriteItem() {
        String sql = "select i.* from rss_item i, rss_favorite f where i.id = f.item_id order by f.id desc";
        List<RSSItem> itemList = new ArrayList<RSSItem>();
        SQLiteDatabase db = dbHelper.getReadableDatabase(Constant.DB_PASSWORD);

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String itemId = cursor.getString(0);
            String itemTitle = cursor.getString(1);
            String itemLink = cursor.getString(2);
            String itemDescription = cursor.getString(3);
            String itemCategory = cursor.getString(4);
            String itemAuthor = cursor.getString(5);

            RSSItem item = new RSSItem(itemId, itemTitle, itemLink, itemDescription, itemCategory, itemAuthor, true);
            itemList.add(item);
        }

        cursor.close();
        db.close();

        return itemList;
    }

    @Override
    public void addFavorite(String itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(Constant.DB_PASSWORD);
        Cursor cursor = db.query("rss_favorite", new String[] {"id"}, "item_id=?", new String[] {itemId}, null, null, null);

        if (!cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put("item_id", itemId);
            db.insert("rss_favorite", null, values);
        }
        cursor.close();
        db.close();
    }

    @Override
    public void deleteItem(String itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(Constant.DB_PASSWORD);
        db.delete("rss_favorite", "item_id = ?", new String[] {itemId});
        db.delete("rss_item", "id = ?", new String[] {itemId});
        db.close();
    }
}
