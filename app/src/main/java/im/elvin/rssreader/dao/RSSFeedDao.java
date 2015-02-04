package im.elvin.rssreader.dao;

import java.util.List;

import im.elvin.rssreader.model.RSSFeed;
import im.elvin.rssreader.model.RSSItem;

/**
 * Created by elvin on 15/1/19.
 */
public interface RSSFeedDao {

    public List<RSSFeed> getAllFeedList();

    public RSSFeed getFeedByFeedId(String feedId);

    public boolean isFeedExist(String address);

    public List<RSSItem> getItemListByFeedId(String feedId, int pageStart, int pageLimit);

    public List<RSSItem> getNewItemListByFeedId(String feedId, String itemId);

    public List<RSSItem> getOldItemListByFeedId(String feedId, String itemId, int pageLimit);

    public RSSItem getItemByItemId(String itemId);

    public List<RSSItem> getFavoriteItem();

    public String createFeed(RSSFeed feed);

    public void addItems(String feedId, List<RSSItem> itemList);

    public void addFavorite(String itemId);

}
