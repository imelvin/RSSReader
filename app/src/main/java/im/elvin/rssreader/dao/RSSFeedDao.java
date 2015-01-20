package im.elvin.rssreader.dao;

import java.util.List;

import im.elvin.rssreader.model.RSSFeed;
import im.elvin.rssreader.model.RSSItem;

/**
 * Created by elvin on 15/1/19.
 */
public interface RSSFeedDao {

    public List<RSSFeed> getAllFeedList();

    public List<RSSItem> getItemListByFeedId(int feedId);

    public RSSItem getItemByItemId(int itemId);

    public void createFeed(RSSFeed feed);

    public void addItems(int feedId, List<RSSItem> itemList);

    public void close();

}
