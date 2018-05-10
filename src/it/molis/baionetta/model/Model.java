package it.molis.baionetta.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.molis.baionetta.beans.Articolo;
import it.molis.baionetta.beans.Chat;
import it.molis.baionetta.beans.Mostrina;
import it.molis.baionetta.beans.Penna;
import it.molis.baionetta.dao.ArticoloDAO;
import it.molis.baionetta.dao.ChatDAO;
import it.molis.baionetta.newsBot.BaioNewsBot;
import it.molis.baionetta.timerTask.KeepAliveTask;
import it.molis.baionetta.timerTask.TimerTaskIeri;
import it.molis.baionetta.timerTask.TimerTaskOggi;

public class Model {

	private BaioNewsBot baioNewsBot;

	private ArticoloDAO dao = new ArticoloDAO();
	private ChatDAO chatDao = new ChatDAO();

	private Set<Articolo> articoli = new HashSet<>();
	private Set<Penna> penne = new HashSet<>();
	private Set<Mostrina> mostrine = new HashSet<>();
	private Set<Chat> chat = new HashSet<>();
	private Set<Chat> attivi = new HashSet<>();

	public Model() {
		this.articoli.addAll(dao.getAll());
		this.mostrine.addAll(dao.getAllMostrine());
		this.penne.addAll(dao.getAllPenne());
		this.chat.addAll(chatDao.getAll());
	}

	public List<Articolo> getArticoliOggi() {
		LocalDate oggi = LocalDate.now();
		LocalDate ieri = LocalDate.now().minusDays(1);
		Set<Articolo> articoliOggi = new HashSet<>();
		for (Articolo a : dao.getAll()) {
			if (a.getData().equals(ieri) || a.getData().equals(oggi)) {
				articoliOggi.add(a);
			}
		}
		return orderByDate(articoliOggi);
	}

	public List<Articolo> getAccaddeIeri() {

		LocalDate oggi = LocalDate.now();
		Set<Articolo> articoliIeri = new HashSet<>();
		for (Articolo a : dao.getAll()) {
			if (!a.getMostrina().getNome().equals("Dispaccio")
					&& !a.getMostrina().getNome().equals("Gerarchia parallela")
					&& !a.getMostrina().getNome().equals("Salmerìa")
					&& !a.getMostrina().getNome().equals("")) {
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

	public Set<Articolo> getAllArticoliFromPenna(Penna p) {
		return p.getAllArticoli();
	}

	public Set<Articolo> getAllArticoliFromMostrina(Mostrina m) {
		return m.getAllArticoli();
	}

	public List<Articolo> getArticoloFromTitolo(String titolo) {
		Set<Articolo> trovati = new HashSet<>();
		for (Articolo a : dao.getAll()) {
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
		articoliOrdinati.addAll(dao.getAll());
		Collections.sort(articoliOrdinati);
		return articoliOrdinati;
	}

	public Set<Chat> getAllChat() {
		return chat;
	}

	public void addChat(Chat chat) {
		chatDao.addChat(chat);
	}

	public void newTask() {
		//Task 1
		TimerTaskOggi ttOggi = new TimerTaskOggi(baioNewsBot);
		ScheduledExecutorService timerOggi = Executors.newScheduledThreadPool(1);

		long dayInSeconds = 86400;
		long firstTimeOggi = Duration.between(LocalTime.now(), LocalTime.of(21, 00)).getSeconds();
		if(firstTimeOggi < 0)
			firstTimeOggi = dayInSeconds + firstTimeOggi;

		timerOggi.scheduleWithFixedDelay(ttOggi, firstTimeOggi, dayInSeconds, TimeUnit.SECONDS);
		//ttOggi.run();
		System.out.println("Task oggi "+firstTimeOggi);

		//Task 2
		TimerTaskIeri ttIeri = new TimerTaskIeri(baioNewsBot);
		ScheduledExecutorService timerIeri = Executors.newScheduledThreadPool(1);

		long firstTimeIeri = Duration.between(LocalTime.now(), LocalTime.of(17, 00)).getSeconds();
		if(firstTimeIeri < 0)
			firstTimeIeri = dayInSeconds + firstTimeIeri;

		timerIeri.scheduleWithFixedDelay(ttIeri, firstTimeIeri, dayInSeconds, TimeUnit.SECONDS);
		//ttIeri.run();
		System.out.println("Task ieri "+firstTimeIeri);

		//Task 3
		KeepAliveTask kATasck = new KeepAliveTask(dao);
		ScheduledExecutorService timerAlive = Executors.newScheduledThreadPool(1);

		long delay = 1800;

		timerAlive.scheduleAtFixedRate(kATasck, delay, delay, TimeUnit.SECONDS);
		kATasck.run();
	}

	public void getAttivi() {
		this.attivi.addAll(getAllChat());
		this.baioNewsBot.setAttivi(attivi);
	}

	public void setBot(BaioNewsBot bnb) {
		this.baioNewsBot = bnb;
	}
}
