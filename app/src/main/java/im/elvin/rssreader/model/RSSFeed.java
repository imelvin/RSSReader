package im.elvin.rssreader.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by elvin on 15/1/19.
 */
public class RSSFeed {
    private String feedId;
    private String title;
    private String link;
    private String description;
    private Date pubDate;
    private Date lastBuildDate;

    public RSSFeed(String feedId, String title, String link, String description, Date pubDate, Date lastBuildDate) {
        this.feedId = feedId;
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.lastBuildDate = lastBuildDate;
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

    public String getDescription() {
        return description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public Date getLastBuildDate() {
        return lastBuildDate;
    }
}
