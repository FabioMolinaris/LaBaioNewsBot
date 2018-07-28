package it.molis.baionetta.timerTask;

import java.util.TimerTask;

import it.molis.baionetta.dao.ArticoloDAO;
import it.molis.baionetta.dao.ChatDAO;

public class KeepAliveTask extends TimerTask {

	private ArticoloDAO dao;
	private ChatDAO daoBot;

	public KeepAliveTask(ArticoloDAO dao, ChatDAO daoBot) {
		this.dao = dao;
		this.daoBot = daoBot;
	}

	public void run() {
		System.out.println("keep alive size of articolo" + dao.getAll().size());
		System.out.println("keep alive size of chat" + daoBot.getAll().size());
	}
}