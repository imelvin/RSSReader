package im.elvin.rssreader.model;

/**
 * Created by elvin on 15/1/19.
 */
public class RSSItem {
    private String itemId;
    private String title;
    private String link;
    private String description;
    private String category;
    private String author;

    public RSSItem(String itemId, String title, String link, String description, String category, String author) {
        this.itemId = itemId;
        this.title = title;
        this.link = link;
        this.description = description;
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

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }
}
