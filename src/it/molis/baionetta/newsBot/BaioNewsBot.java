package it.molis.baionetta.newsBot;

import java.util.HashSet;
import java.util.Set;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import it.molis.baionetta.beans.Articolo;
import it.molis.baionetta.beans.Chat;
import it.molis.baionetta.model.Model;

public class BaioNewsBot extends TelegramLongPollingBot {

	Model model;
	Set<Chat> attivi;
	SendMessage message;
	Set<Articolo> artInviati = new HashSet<>();

	public BaioNewsBot(Model model) {
		attivi = new HashSet<>();
		message = new SendMessage();
		this.model = model;
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			long chat_id = update.getMessage().getChatId();

			if (update.getMessage().getText().equals("/start")
					|| update.getMessage().getText().equals("/start@BaioNewsBot")) {

				Chat isAttivo = new Chat(chat_id);
				boolean nuovo = true;

				for (Chat ia : attivi) {
					if (ia.getChatId() == chat_id) {

						String message_notification = "Sono già attivo su questa chat!\n";
						nuovo = false;
						message.setChatId(chat_id);
						message.setText(message_notification);
						try {
							execute(message);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
					}
				}
				if (nuovo) {
					message.setChatId(chat_id);
					message.setText("Ciao! \n" + "Sono il NewsBot de La Baionetta, ogni sera alle 21 "
							+ "ti invierò gli ultimi aggiornamenti del tuo sito preferito");
					try {
						execute(message); // Sending our message object to user
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}

					attivi.add(isAttivo);
					updateDBAttivi(isAttivo);
				}
			}
			if (update.getMessage().getText().equals("/oggi")) {
				sendNotificationTimerOggi();
			}
			if (update.getMessage().getText().equals("/ieri")) {
				sendNotificationTimerIeri();
			}
		}
	}

	public void setAttivi(Set<Chat> attivi) {
		this.attivi.addAll(attivi);
	}

	private void updateDBAttivi(Chat isAttivo) {
		model.addChat(isAttivo);
	}

	public void sendNotificationTimerOggi() {
		Set<Articolo> articoliDaInviare = new HashSet<>();

		if (model.getArticoliOggi().size() > 0)
			for (Articolo a : model.getArticoliOggi())
				if (!artInviati.contains(a))
					articoliDaInviare.add(a);

		if (!articoliDaInviare.isEmpty()) {
			for (Chat c : attivi) {
				String message_text = "Sono le 21 e vi avviso di tutti gli ultimi articoli scritti!!\n\n";
				message.setChatId(c.getChatId());
				message.setText(message_text);
				for (Articolo a : articoliDaInviare) {
					message_text = message_text + "\nL'articolo " + a.getTitolo() + "\nsi trova al link " + a.getLink()
							+ "\ned è stato scritto da " + a.getPenna() + "\n\n";
					message.setText(message_text);
				}
				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void sendNotificationTimerIeri() {

		if (model.getAccaddeIeri().size() > 0) {
			for (Chat c : attivi) {
				String message_text = "Sono le 17 e sono nostalgico, oggi (ma negli anni passati) è stato scritto: \n\n";
				message.setChatId(c.getChatId());
				message.setText(message_text);
				for (Articolo a : model.getAccaddeIeri()) {
					message_text = message_text + "L'articolo " + a.getTitolo() + "\nsi trova al link " + a.getLink()
							+ "\ned è stato scritto da " + a.getPenna() + "\nnel lontano " + a.getData().getYear()
							+ "\n";
					message.setText(message_text);
				}
				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
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
