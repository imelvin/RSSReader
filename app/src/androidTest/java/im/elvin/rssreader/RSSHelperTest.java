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
        String address = "http://cnbeta.feedsportal.com/c/34306/f/624776/index.rss";

        RSSFeed feed = RSSHelper.parseFeed(address);

        assertNotNull(feed);
    }
}
