package it.molis.baionetta.timerTask;

import java.util.TimerTask;

import it.molis.baionetta.newsBot.BaioNewsBot;

public class TimerTaskIeri extends TimerTask {

	BaioNewsBot baioNewsBot;

	public TimerTaskIeri(BaioNewsBot baioNewsBot) {
		this.baioNewsBot = baioNewsBot;
	}

	public void run() {
		baioNewsBot.sendNotificationTimerIeri();
	}
}