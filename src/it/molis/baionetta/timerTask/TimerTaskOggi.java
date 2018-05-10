package it.molis.baionetta.timerTask;

import java.util.TimerTask;

import it.molis.baionetta.newsBot.BaioNewsBot;

public class TimerTaskOggi extends TimerTask {

	private BaioNewsBot baioNewsBot;

	public TimerTaskOggi(BaioNewsBot baioNewsBot) {
		this.baioNewsBot = baioNewsBot;
	}

	public void run() {
		baioNewsBot.sendNotificationTimerOggi();
	}
}
