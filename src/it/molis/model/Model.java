package it.molis.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.molis.baionetta.beans.Articolo;
import it.molis.baionetta.feed.Feed;
import it.molis.baionetta.feed.FeedMessage;
import it.molis.baionetta.feed.FeedReader;

public class Model {

	private Set<Articolo> articoli = new HashSet<>();

	private String feedUrl = "https://labaionetta.blogspot.com/feeds/posts/default?alt=rss";

	public void getArticoliFromRss() {

		FeedReader fd = new FeedReader(feedUrl);

		if (fd.read() == null) {
			System.out.println("Internet!!");
		} else {
			Feed feed = fd.readFeed();

			for (FeedMessage message : feed.getMessages()) {

				if (message.getLink() != null) {

					LocalDateTime date = LocalDateTime.parse(message.getPubDate(), DateTimeFormatter.RFC_1123_DATE_TIME);
					Articolo a = new Articolo(message.getTitle(), message.getCategory(), creaPenna(message.getAuthor()),
							message.getLink(), date);

					articoli.add(a);
				}
			}
		}
	}

	private String creaPenna(String s) {
		if(s.contains("Baionetta")){
			return "La Baionetta";
		}
		if(s.contains("Fabio")){
			return "Fabio Molinaris";
		}
		if(s.contains("Daniele")){
			return "Daniele Barale";
		}
		if(s.contains("Darth")){
			return "Darth Gender";
		}
		if(s.contains("Federico")){
			return "Federico Montagnani";
		}
		return null;
	}

	public List<Articolo> sendNotification() {
		getArticoliFromRss();
		LocalDateTime ieri = LocalDateTime.now().minusDays(1);
		List <Articolo> articoliOggi = new ArrayList<>();
		for(Articolo a : articoli){
			if(a.getData().isAfter(ieri)){
				articoliOggi.add(a);
			}
		}
		return articoliOggi;
	}
}
