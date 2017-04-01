package it.molis.newsBot;

import java.time.LocalTime;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import it.molis.baionetta.beans.Articolo;
import it.molis.model.Model;

public class BaioNewsBot extends TelegramLongPollingBot {

	Model model = new Model();
	private long chat_id;

	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			// Set variables
			chat_id = update.getMessage().getChatId();

			SendMessage message = new SendMessage() // Create a message object
													// object
					.setChatId(chat_id).setText("Ciao! \n"
							+ "Sono il NewsBot de La Baionetta, ogni sera alle 21 ti invierò gli ultimi aggiornamenti del tuo sito preferito");
			try {
				sendMessage(message); // Sending our message object to user
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isTime() {
		for (LocalTime i = LocalTime.MIN; i.isBefore(LocalTime.MAX); i.plusMinutes(1)) {
			if (LocalTime.now().hashCode() == LocalTime.of(21, 00, 00, 0000).hashCode()) {
				return true;
			}
		}return false;
	}

	public void sendNotification(){
		SendMessage message = new SendMessage().setChatId(chat_id);
		String message_text = "Sono le 21 e vi avviso di tutti gli ultimi articoli scritti!!\n\n";
		if(isTime()){
			for (Articolo a : model.sendNotification()) {
				message_text += "L'articolo " + a.getTitolo() + "\n si trova al link " + a.getLink()
						+ "\n ed è stato scritto da " + a.getPenna() + "\n\n";
			}
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
