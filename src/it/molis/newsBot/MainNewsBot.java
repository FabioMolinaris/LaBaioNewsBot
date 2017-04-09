package it.molis.newsBot;

import java.io.IOException;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MainNewsBot {

	public static void main(String[] args) {

		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();

		BaioNewsBot bnb = new BaioNewsBot();

		try {
			bnb.ripristinaBackup();
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			botsApi.registerBot(bnb);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
