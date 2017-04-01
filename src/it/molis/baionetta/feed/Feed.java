package it.molis.baionetta.feed;

import java.util.ArrayList;
import java.util.List;

public class Feed {

    private String title;
    private String link;
    private String category;
    private String pubDate;


    private List<FeedMessage> entries = new ArrayList<FeedMessage>();

    public Feed(String title, String link, String category, String pubDate) {
            this.title = title;
            this.link = link;
            this.category = category;
            this.pubDate = pubDate;
    }

    public List<FeedMessage> getMessages() {
            return entries;
    }

    public String getTitle() {
            return title;
    }

    public String getLink() {
            return link;
    }

    public String getCategory() {
            return category;
    }

    public String getPubDate() {
            return pubDate;
    }

    @Override
    public String toString() {
            return "Feed [description="+category+", link="+link+", pubDate="+pubDate+", title="+title+"]";
    }

}
