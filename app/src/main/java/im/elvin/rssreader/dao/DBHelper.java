package im.elvin.rssreader.dao;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import im.elvin.rssreader.constant.Constant;

/**
 * Created by elvin on 15/1/19.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_FEED = " create table rss_feed ( " +
                                    "     id integer primary key autoincrement, " +
                                    "     feed_title varchar(50), " +
                                    "     feed_address varchar(200), " +
                                    "     feed_link varchar(50), " +
                                    "     feed_description text " +
                                    " ) ";

    private static final String SQL_CREATE_ITEM = " create table rss_item ( " +
                                    "     id integer primary key autoincrement, " +
                                    "     item_title varchar(50), " +
                                    "     item_link varchar(50), " +
                                    "     item_description text, " +
                                    "     item_category varchar(50), " +
                                    "     item_author varchar(30), " +
                                    "     feed_id integer " +
                                    " ) ";

    public DBHelper(Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FEED);
        db.execSQL(SQL_CREATE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists rss_item");
        db.execSQL("drop table if exists rss_feed");
        db.execSQL(SQL_CREATE_FEED);
        db.execSQL(SQL_CREATE_ITEM);
    }

}
