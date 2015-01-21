package im.elvin.rssreader.dao;

import java.util.List;

import im.elvin.rssreader.model.RSSFeed;
import im.elvin.rssreader.model.RSSItem;

/**
 * Created by elvin on 15/1/19.
 */
public interface RSSFeedDao {

    public List<RSSFeed> getAllFeedList();

    public List<RSSItem> getItemListByFeedId(String feedId);

    public RSSItem getItemByItemId(String itemId);

    public void createFeed(RSSFeed feed);

    public void addItems(String feedId, List<RSSItem> itemList);

    public void close();

}
