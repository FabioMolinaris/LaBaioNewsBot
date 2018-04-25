package it.molis.baionetta.timerTask;

import java.util.TimerTask;

import it.molis.baionetta.beans.Chat;
import it.molis.baionetta.newsBot.BaioNewsBot;

public class TimerTaskOggi extends TimerTask {

	BaioNewsBot baioNewsBot;
	Chat chat;

	public TimerTaskOggi(BaioNewsBot baioNewsBot, Chat c) {
		this.baioNewsBot = baioNewsBot;
		this.chat = c;
	}

	public void run() {
		baioNewsBot.sendNotificationTimerOggi();
	}
}
