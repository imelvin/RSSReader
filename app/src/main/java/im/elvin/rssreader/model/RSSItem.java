package im.elvin.rssreader.model;

import java.util.Date;

/**
 * Created by elvin on 15/1/19.
 */
public class RSSItem {
    private String itemId;
    private String title;
    private String link;
    private String description;
    private Date pubDate;
    private String category;
    private String author;

    public RSSItem(String itemId, String title, String link, String description, Date pubDate, String category, String author) {
        this.itemId = itemId;
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.category = category;
        this.author = author;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getItemId() {
        return itemId;
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

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }
}
