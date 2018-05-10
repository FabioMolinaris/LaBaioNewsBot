package it.molis.baionetta.timerTask;

import java.util.TimerTask;

import it.molis.baionetta.dao.ArticoloDAO;

public class KeepAliveTask extends TimerTask {

	private ArticoloDAO dao;

	public KeepAliveTask(ArticoloDAO dao) {
		this.dao = dao;
	}

	public void run() {
		dao.getAll().size();
	}
}