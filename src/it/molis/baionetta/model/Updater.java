package it.molis.baionetta.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import it.molis.baionetta.beans.Articolo;
import it.molis.baionetta.beans.Mostrina;
import it.molis.baionetta.beans.ParolaChiave;
import it.molis.baionetta.beans.Penna;
import it.molis.baionetta.dao.ArticoloDAO;
import it.molis.baionetta.feed.ArticoloFeed;
import it.molis.baionetta.feed.BackupText;

public class Updater {
	
	private ArticoloFeed rss = new ArticoloFeed();
	private ArticoloDAO dao = new ArticoloDAO();
	
	private Set<Articolo> articoliRSS = new HashSet<>();
	private Set<Articolo> articoliDB = new HashSet<>();
	private Set<Penna> penne = new HashSet<>();
	private Set<Mostrina> mostrine = new HashSet<>();
	
	public Updater() {
		this.mostrine.addAll(dao.getAllMostrine());
		this.penne.addAll(dao.getAllPenne());
	}
	
	public Set<Penna> getAllPenne() {
		this.penne.clear();
		this.penne.addAll(dao.getAllPenne());
		return penne;
	}

	public Set<Mostrina> getAllMostrine() {
		this.mostrine.clear();
		this.mostrine.addAll(dao.getAllMostrine());
		return mostrine;
	}
	
	public void getArticoliFromRss() {
		articoliRSS.clear();
		articoliRSS.addAll(rss.getArticoliFromRss());
	}

	public void getAllArticoliDB() {
		articoliDB.clear();
		articoliDB.addAll(dao.getAllArticoli());
	}

	public void update() throws IOException {

		getAllArticoliDB();
		getArticoliFromRss();
		getAllPenne();
		getAllMostrine();

		Set<Articolo> articoliNew = new HashSet<>();
		if (!articoliRSS.isEmpty()) {
			for (Articolo a : articoliRSS) {
				if (!articoliDB.contains(a)) {
					dao.addArticolo(a);
					articoliNew.add(a);
				}
				if (!penne.contains(a.getPenna())) {
					dao.addPenna(a.getPenna());
					penne.add(a.getPenna());
				}
				if (!mostrine.contains(a.getMostrina())) {
					dao.addMostrina(a.getMostrina());
					mostrine.add(a.getMostrina());
				}
			}
		}
		for (Articolo a : articoliNew) {
			Set<ParolaChiave> paroleChiave = new HashSet<>();
			// ottengo le parole chiave da web
			paroleChiave.addAll(rss.getParoleGold(a));
			// ottengo le parole chiave da db
			paroleChiave.addAll(dao.getAllParoleChiave(a));
			// ottengo le parole chiave dal titolo (valgono doppio)
			paroleChiave.addAll(getParoleTitolo(a));
			// aggiungo parole del titolo se non già presenti
			a.setParoleChiave(paroleChiave);
			for (ParolaChiave pca : a.getParoleChiave()) {
				if (!dao.getAllParoleChiave(a).contains(pca))
					dao.addParoleChiave(pca);
			}
			BackupText bt = new BackupText();
			bt.backupText(a);
		}
	}

	private Set<ParolaChiave> getParoleTitolo(Articolo a) {

		Set<ParolaChiave> paroleChiave = new HashSet<>();

		for (String p : a.getTitolo().split(" ")) {
			if (p.length() > 3) {
				String parola = p.replaceAll("[^a-zA-Z0-9]", "");
				paroleChiave.add(new ParolaChiave(parola.toLowerCase(), a.getLink(), 2));
			}
		}
		return paroleChiave;
	}

}
