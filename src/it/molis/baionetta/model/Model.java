package it.molis.baionetta.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.molis.baionetta.beans.Articolo;
import it.molis.baionetta.beans.Chat;
import it.molis.baionetta.beans.Mostrina;
import it.molis.baionetta.beans.ParolaChiave;
import it.molis.baionetta.beans.Penna;
import it.molis.baionetta.dao.ArticoloDAO;
import it.molis.baionetta.dao.ChatDAO;

public class Model {

	private ArticoloDAO dao = new ArticoloDAO();
	private ChatDAO chatDao = new ChatDAO();

	private Set<Articolo> articoli = new HashSet<>();
	private Set<Penna> penne = new HashSet<>();
	private Set<Mostrina> mostrine = new HashSet<>();
	Set<Chat> chat = new HashSet<>();

	SimpleGraph<Articolo, DefaultWeightedEdge> grafo;

	public Model() {
		articoli.addAll(dao.getAll());
		mostrine.addAll(dao.getAllMostrine());
		penne.addAll(dao.getAllPenne());
		chat.addAll(chatDao.getAll());
		creaGrafo();
	}

	public List<Articolo> getArticoliOggi() {

		LocalDate ieri = LocalDate.now().minusDays(1);
		Set<Articolo> articoliOggi = new HashSet<>();
		for (Articolo a : articoli) {
			if (a.getData().isAfter(ieri)) {
				articoliOggi.add(a);
			}
		}
		return orderByDate(articoliOggi);
	}

	public List<Articolo> getAccaddeIeri() {

		LocalDate oggi = LocalDate.now();
		Set<Articolo> articoliIeri = new HashSet<>();
		for (Articolo a : articoli) {
			if (!a.getMostrina().getNome().equals("Dispaccio")
					&& !a.getMostrina().getNome().equals("Gerarchia parallela")
					&& !a.getMostrina().getNome().equals("Salmerìa")) {
				if (a.getData().getMonth().equals(oggi.getMonth())
						&& a.getData().getDayOfMonth() == oggi.getDayOfMonth()
						&& a.getData().getYear() != oggi.getYear()) {
					articoliIeri.add(a);
				}
			}
		}
		return orderByDate(articoliIeri);
	}

	public Set<Penna> getAllPenne() {
		return penne;
	}

	public Set<Mostrina> getAllMostrine() {
		return mostrine;
	}

	/**
	 * crea un grafo i cui vertici sono <b>Articolo <\b> e gli archi sono pesati in
	 * funzione del numero e del peso delle parole chiave simili
	 */
	private void creaGrafo() {

		grafo = new SimpleWeightedGraph<Articolo, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		Graphs.addAllVertices(grafo, articoli);

		for (Articolo a1 : articoli) {
			for (Articolo a2 : articoli) {
				if (a1.hashCode() < a2.hashCode()) {

					int peso = 0;

					for (ParolaChiave pc1 : a1.getParoleChiave()) {
						for (ParolaChiave pc2 : a2.getParoleChiave()) {
							if (pc1.hashCode() < pc2.hashCode()) {
								if (pc1.isSimele(pc2)) {
									peso += pc1.getPeso();
								}
							}
						}
						if (peso > 0) {
							Graphs.addEdge(grafo, a1, a2, peso);
						}
					}
				}
			}
		}
	}

	public Set<Articolo> getAllArticoliFromPenna(Penna p) {
		return p.getAllArticoli();
	}

	public Set<Articolo> getAllArticoliFromMostrina(Mostrina m) {
		return m.getAllArticoli();
	}

	public List<Articolo> getArticoloFromTitolo(String titolo) {
		Set<Articolo> trovati = new HashSet<>();
		for (Articolo a : articoli) {
			if (a.getTitolo().contains(titolo)) {
				trovati.add(a);
			}
		}
		return orderByDate(trovati);
	}

	public List<Articolo> orderByDate(Set<Articolo> art) {
		List<Articolo> articoliOrdinati = new ArrayList<>(art);
		Collections.sort(articoliOrdinati);
		return articoliOrdinati;
	}

	public List<Articolo> getAllArticoliOrderByDate() {
		List<Articolo> articoliOrdinati = new ArrayList<>();
		articoliOrdinati.addAll(articoli);
		Collections.sort(articoliOrdinati);
		return articoliOrdinati;
	}

	public Set<Chat> getAllChat() {
		return chat;
	}

	public void addChat(Chat chat) {
		chatDao.addChat(chat);
	}
}
