package im.elvin.rssreader;

import android.test.AndroidTestCase;

import java.util.List;

import im.elvin.rssreader.dao.RSSFeedDao;
import im.elvin.rssreader.dao.RSSFeedDaoImpl;
import im.elvin.rssreader.model.RSSFeed;

/**
 * Created by elvin on 15/1/19.
 */
public class RSSFeedDaoTest extends AndroidTestCase {

    public void testGetItemListByFeed() {
        RSSFeedDao feedDao = new RSSFeedDaoImpl(getContext());

        RSSFeed feed1 = new RSSFeed(null, "Test Feed 1", null, null, null);
        feedDao.createFeed(feed1);

        List<RSSFeed> feedList = feedDao.getAllFeedList();

        assertNotNull(feedList);
        assertEquals(feedList.size() > 0, true);

        feedDao.close();
    }

}
