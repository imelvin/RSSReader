package im.elvin.rssreader;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

import im.elvin.rssreader.dao.RSSFeedDao;
import im.elvin.rssreader.dao.RSSFeedDaoImpl;
import im.elvin.rssreader.model.RSSFeed;
import im.elvin.rssreader.model.RSSItem;

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

        List<RSSItem> itemList = new ArrayList<RSSItem>();
        for (int i = 0; i < 5; i++) {
            RSSItem item = new RSSItem(null, "Test Title " + i, null, "Test Description Test Description Test Test.", null, null);
            itemList.add(item);
        }

        feedDao.addItems("1", itemList);

        itemList = feedDao.getItemListByFeedId("1");
        assertNotNull(itemList);
        assertEquals(itemList.size() > 0, true);

        feedDao.close();
    }

}
