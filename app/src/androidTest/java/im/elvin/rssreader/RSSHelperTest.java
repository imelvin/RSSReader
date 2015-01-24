package im.elvin.rssreader;

import android.test.AndroidTestCase;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import im.elvin.rssreader.helper.RSSHelper;
import im.elvin.rssreader.model.RSSFeed;

/**
 * Created by elvin on 15/1/24.
 */
public class RSSHelperTest extends AndroidTestCase {

    public void testParseFeed() throws IOException, XmlPullParserException {
        String address = "http://blog.csdn.net/cym492224103/rss/list";
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream inputStream = conn.getInputStream();

        RSSFeed feed = RSSHelper.parseFeed(address, inputStream);

        assertNotNull(feed);
    }
}
