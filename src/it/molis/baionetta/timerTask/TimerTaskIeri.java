package it.molis.baionetta.timerTask;

import java.util.TimerTask;

import it.molis.baionetta.beans.Chat;
import it.molis.baionetta.newsBot.BaioNewsBot;

public class TimerTaskIeri extends TimerTask {

	BaioNewsBot baioNewsBot;
	Chat chat;

	public TimerTaskIeri(BaioNewsBot baioNewsBot, Chat c) {
		this.baioNewsBot = baioNewsBot;
		this.chat = c;
	}

	public void run() {
		baioNewsBot.sendNotificationTimerIeri();
	}
}