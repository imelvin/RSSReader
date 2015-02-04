package im.elvin.rssreader.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import im.elvin.rssreader.model.RSSFeed;
import im.elvin.rssreader.model.RSSItem;

/**
 * Created by elvin on 15/1/23.
 */
public class RSSHelper {

    public static final String TAG_CHANNEL = "channel";
    public static final String TAG_TITLE = "title";
    public static final String TAG_LINK = "link";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_ITEM = "item";
    public static final String TAG_CATEGORY = "category";
    public static final String TAG_AUTHOR = "author";

    public static RSSFeed parseFeed(String address, Context context, ProgressDialog progressDialog) throws IOException, XmlPullParserException {
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int fileSize = conn.getContentLength();
        InputStream inputStream = conn.getInputStream();
        String tmpFileName = "tmp.xml";
        OutputStream fileOutputStream = context.openFileOutput(tmpFileName, Context.MODE_PRIVATE);
        byte [] buffer = new byte [4096];

        int length, downloadedSize = 0;
        while((length = inputStream.read(buffer)) > 0){
            fileOutputStream.write(buffer,0,length);
            if (fileSize > 0) {
                downloadedSize += length;
                if (progressDialog != null) {
                    progressDialog.setProgress(90 * downloadedSize / fileSize);
                }
            }
        }
        fileOutputStream.close();
        inputStream.close();

        if (progressDialog != null) {
            progressDialog.setProgress(90);
        }

        inputStream = context.openFileInput(tmpFileName);
        RSSFeed feed = RSSHelper.parseFeed(address, inputStream);

        if (progressDialog != null) {
            progressDialog.setProgress(95);
        }

        return feed;
    }

    public static RSSFeed parseFeed(String address, InputStream inputStream) throws XmlPullParserException, IOException {
        RSSFeed feed = null;
        RSSItem item = null;
        String feedTitle = null, feedLink = null, feedDescription = null;
        String itemTitle = null, itemLink = null, itemDescription = null, itemCategory = null, itemAuthor = null;
        List<RSSItem> itemList = new ArrayList<RSSItem>();

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "utf-8");
        int event = parser.getEventType();
        boolean itemLevel = false;

        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (TAG_TITLE.equalsIgnoreCase(parser.getName())) {
                        if (itemLevel) {
                            itemTitle = parser.nextText();
                        } else {
                            feedTitle = parser.nextText();
                        }
                    } else if (TAG_LINK.equalsIgnoreCase(parser.getName())) {
                        if (itemLevel) {
                            itemLink = parser.nextText();
                        } else {
                            feedLink = parser.nextText();
                        }
                    } else if (TAG_DESCRIPTION.equalsIgnoreCase(parser.getName())) {
                        if (itemLevel) {
                            itemDescription = parser.nextText();
                        } else {
                            feedDescription = parser.nextText();
                        }
                    } else if (TAG_ITEM.equalsIgnoreCase(parser.getName())) {
                        itemLevel = true;
                    } else if (TAG_CATEGORY.equalsIgnoreCase(parser.getName())) {
                        if (itemLevel) {
                            itemCategory = parser.nextText();
                        }
                    } else if (TAG_AUTHOR.equalsIgnoreCase(parser.getName())) {
                        if (itemLevel) {
                            itemAuthor = parser.nextText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (TAG_CHANNEL.equalsIgnoreCase(parser.getName())) {
                        feed = new RSSFeed(null, feedTitle, address, feedLink, feedDescription, itemList);
                        feedTitle = null;
                        feedLink = null;
                        feedDescription = null;
                        itemList = new ArrayList<RSSItem>();
                    } else if (TAG_ITEM.equalsIgnoreCase(parser.getName())) {
                        itemLevel = false;
                        item = new RSSItem(null, itemTitle, itemLink, itemDescription, itemCategory, itemAuthor, false);
                        itemList.add(item);
                        itemTitle = null;
                        itemLink = null;
                        itemDescription = null;
                        itemCategory = null;
                        itemAuthor = null;
                    }
                    break;
            }
            event = parser.next();
        }

        return feed;
    }

    public static void writeFeed(RSSFeed feed, OutputStream outputStream) {

    }

}
