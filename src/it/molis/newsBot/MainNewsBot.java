package it.molis.newsBot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MainNewsBot {

	public static void main(String[] args) {

		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();

		BaioNewsBot bnb = new BaioNewsBot();

		try {
			botsApi.registerBot(bnb);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
