package it.molis.newsBot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import it.molis.baionetta.beans.Articolo;
import it.molis.model.Model;

public class BaioNewsBot extends TelegramLongPollingBot {

	Model model = new Model();
	SendMessage message = new SendMessage();
	private long chat_id;

	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			// Set variables
			chat_id = update.getMessage().getChatId();

			message.setChatId(chat_id).setText("Ciao! \n " + "Sono il NewsBot de La Baionetta, ogni sera alle 21 "
					+ "ti invierò gli ultimi aggiornamenti del tuo sito preferito");

			TimerExecutor te = new TimerExecutor();
			CustomTimerTask ctt = new CustomTimerTask("invia", 365) {

				@Override
				public void execute() {
					sendNotification();
				}
			};
			te.startExecutionEveryDayAt(ctt, 19, 00, 00);

			try {
				sendMessage(message); // Sending our message object to user
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendNotification() {
		String message_text = "Sono le 21 e vi avviso di tutti gli ultimi articoli scritti!!\n\n";
		for (Articolo a : model.sendNotification()) {
			message_text += "L'articolo " + a.getTitolo() + "\n si trova al link " + a.getLink()
					+ "\n ed è stato scritto da " + a.getPenna() + "\n\n";
		}
		message.setText(message_text);
		try {
			sendMessage(message); // Sending our message object to user
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getBotUsername() {
		return "BaioNewsBot";
	}

	@Override
	public String getBotToken() {
		return "332251488:AAEdzEqn_ILhv7vHlfK6YqrLDSVwS9BL9_A";
	}

}
