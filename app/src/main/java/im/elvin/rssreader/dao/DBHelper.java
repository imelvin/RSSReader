package im.elvin.rssreader.dao;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.List;

import im.elvin.rssreader.constant.Constant;
import im.elvin.rssreader.model.RSSFeed;
import im.elvin.rssreader.model.RSSItem;

/**
 * Created by elvin on 15/1/19.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_FEED = " create table rss_feed ( " +
                                    "     id integer primary key autoincrement, " +
                                    "     feed_title varchar(50), " +
                                    "     feed_link varchar(50), " +
                                    "     feed_description varchar(200), " +
                                    "     feed_pubdate date, " +
                                    "     feed_lastbuilddate date " +
                                    " ) ";

    public DBHelper(Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FEED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
