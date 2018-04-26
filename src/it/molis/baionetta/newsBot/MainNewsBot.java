package it.molis.baionetta.newsBot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import it.molis.baionetta.model.Model;

public class MainNewsBot {

	public static void main(String[] args) {

		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();

		Model model = new Model();

		BaioNewsBot bnb = new BaioNewsBot(model);

		model.setBot(bnb);
		model.getAttivi();
		model.newTask();
		System.out.println("v2.1");

		try {
			botsApi.registerBot(bnb);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
