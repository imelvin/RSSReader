package im.elvin.rssreader.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elvin on 15/1/19.
 */
public class RSSFeed {
    private String feedId;
    private String title;
    private String address;
    private String link;
    private String description;
    private List<RSSItem> itemList;

    public RSSFeed(String feedId, String title, String address, String link, String description, List<RSSItem> itemList) {
        this.feedId = feedId;
        this.title = title;
        this.link = link;
        this.address = address;
        this.description = description;
        this.itemList = itemList != null ? itemList : new ArrayList<RSSItem>();
    }

    @Override
    public String toString() {
        return title;
    }

    public String getFeedId() {
        return feedId;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public List<RSSItem> getItemList() {
        return itemList;
    }
}
