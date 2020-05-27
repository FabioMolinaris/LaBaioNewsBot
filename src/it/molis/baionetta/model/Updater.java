package it.molis.baionetta.model;

import java.io.File;
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
	private Set<Articolo> articoliSenzaParoleChiave = new HashSet<>();
	private Set<Penna> penne = new HashSet<>();
	private Set<Mostrina> mostrine = new HashSet<>();
	private Set<Articolo> articoliNew = new HashSet<>();
	private Set<ParolaChiave> paroleChiave = new HashSet<>();
	private BackupText bt = new BackupText();

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
		getAllarticoliSenzaParoleChiave();
		//getAllArticoliSenzaBackupTxt();
		
		articoliNew.clear();

		if (!articoliRSS.isEmpty()) {
			for (Articolo a : articoliRSS) {
				if (!mostrine.contains(a.getMostrina())) {
					dao.addMostrina(a.getMostrina());
					mostrine.add(a.getMostrina());
				}
				if (!penne.contains(a.getPenna())) {
					dao.addPenna(a.getPenna());
					penne.add(a.getPenna());
				}
				if (!articoliDB.contains(a)) {
					dao.addArticolo(a);
					articoliNew.add(a);
					bt.backupText(a);
				}
			}
		}
		for (Articolo a : articoliNew) {
			paroleChiave.clear();
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
		}
		for (Articolo a : articoliSenzaParoleChiave) {
			System.out.println("articoliSenzaParoleChiave "+ articoliSenzaParoleChiave.size());
			paroleChiave.clear();
			// ottengo le parole chiave da web
			paroleChiave.addAll(rss.getParoleGold(a));
			// ottengo le parole chiave da db
			paroleChiave.addAll(dao.getAllParoleChiave(a));
			// ottengo le parole chiave dal titolo (valgono doppio)
			paroleChiave.addAll(getParoleTitolo(a));
			// aggiungo parole del titolo se non già presenti
			a.setParoleChiave(paroleChiave);
			System.out.println("paroleChiave "+ a.getParoleChiave().size());
			for (ParolaChiave pca : a.getParoleChiave()) {
				if (!dao.getAllParoleChiave(a).contains(pca))
					dao.addParoleChiave(pca);
			}
		}
	}

	private Set<ParolaChiave> getParoleTitolo(Articolo a) {
		for (String p : a.getTitolo().split(" ")) {
			if (p.length() > 3) {
				String parola = p.replaceAll("[^a-zA-Z0-9]", "");
				paroleChiave.add(new ParolaChiave(parola.toLowerCase(), a.getLink(), 2));
			}
		}
		return paroleChiave;
	}
	
	private void getAllarticoliSenzaParoleChiave() {
		articoliSenzaParoleChiave.clear();
		Set<Articolo> articoligetAll = new HashSet<>();
		articoligetAll.addAll(dao.getAll());
		getAllArticoliDB();
		for(Articolo a : articoliDB) {
			if (!articoligetAll.contains(a)) {
				articoliSenzaParoleChiave.add(a);
				System.out.println("###Aggiunto "+ a.getTitolo());
			}
		}	
		System.out.println("articoliDB "+ articoliDB.size());
		System.out.println("getAll "+ dao.getAll().size());
	}
	
	private void getAllArticoliSenzaBackupTxt() {
		File f;
		String titolo;
		getAllArticoliDB();
		for(Articolo a : articoliDB) {
			
			titolo = a.getTitolo().replace("/", " ");
			titolo = titolo.replace(" ", "_");
			titolo = titolo.replace("ì", "i");
			titolo = titolo.replace("ò", "o");
			titolo = titolo.replace("à", "a");
			titolo = titolo.replace("á", "a");
			titolo = titolo.replace("ù", "u");
			titolo = titolo.replace("è", "e");
			titolo = titolo.replace("é", "e");
			titolo = titolo.replace("È", "E");
			titolo = titolo.replace("'", "");
			titolo = titolo.replace("\"", "");
			titolo = titolo.replace("«", "");
			titolo = titolo.replace("»", "");
			titolo = titolo.replace("“", "");
			titolo = titolo.replace("”", "");
			titolo = titolo.replace("–", "-");
			titolo = titolo.replace("’", "");
			titolo = titolo.replace(":", "");
			
		    if (titolo.length() > 50) 
		    	titolo = titolo.substring(0, 50); 
		    
			f = new File(("/volume1/homes/fabio/Drive/Dropbox/La baionetta - munizioni/BaioBackupAutomatico/" 
					+ a.getPenna() + "/" + a.getData() + "-" + titolo + ".txt"), "UTF-8");
			
			if(!f.exists()) {
				bt.backupText(a);
				System.out.println("###NO_TXT "+ a.getTitolo());
			}
		}	
	}
}
