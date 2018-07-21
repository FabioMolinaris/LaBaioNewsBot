package it.molis.baionetta.timerTask;

import java.io.IOException;
import java.util.TimerTask;

import it.molis.baionetta.model.Updater;

public class TimerTaskFeed extends TimerTask {

	private Updater updater;
	
	public TimerTaskFeed (Updater updater) {
		this.updater = updater;
	}
	
	public void run() {
		try {
			updater.update();
		} catch (IOException e) {
			System.out.println("Errore nel task update");
			e.printStackTrace();
		}
	}

}
