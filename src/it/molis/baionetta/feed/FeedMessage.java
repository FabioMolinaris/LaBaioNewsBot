package it.molis.baionetta.feed;

public class FeedMessage {

	private String title;
	private String category;
	private String link;
	private String author;
    private String pubDate;

    public String getTitle() {
            return title;
    }

    public void setTitle(String title) {
            this.title = title;
    }

    public String getCategory() {
            return category;
    }

    public void setCategory(String category) {
            this.category = category;
    }

    public String getLink() {
            return link;
    }

    public void setLink(String link) {
            this.link = link;
    }

    public String getAuthor() {
            return author;
    }

    public void setAuthor(String author) {
            this.author = author;
    }

    @Override
    public String toString() {
            return "FeedMessage title=" + title + ", category=" + category
                            + ", link=" + link + ", author=" + author + "data="+pubDate+"";
    }

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public void setDate(String pubdate) {
		this.pubDate = pubdate;
	}
}
