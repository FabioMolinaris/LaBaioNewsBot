package it.molis.baionetta.timerTask;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

import it.molis.baionetta.dao.ArticoloDAO;
import it.molis.baionetta.dao.ChatDAO;

public class KeepAliveTask extends TimerTask {

	private ArticoloDAO dao;
	private ChatDAO daoBot;
	private ScheduledExecutorService timerFeed;
	private TimerTaskFeed ttFeed;

	public KeepAliveTask(ArticoloDAO dao, ChatDAO daoBot, ScheduledExecutorService timerFeed, TimerTaskFeed ttFeed) {
		this.dao = dao;
		this.daoBot = daoBot;
		this.timerFeed = timerFeed;
		this.ttFeed = ttFeed;
	}

	public void run() {
		System.out.println("keep alive size of articolo" + dao.getAll().size());
		System.out.println("keep alive size of chat" + daoBot.getAll().size());
		
		if (dao.getAll().isEmpty()) {
			this.ttFeed.run();
			this.timerFeed.execute(ttFeed);
		}
	}
}