package it.molis.newsBot;

import java.util.HashSet;
import java.util.Set;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import it.molis.baionetta.beans.Articolo;
import it.molis.model.Model;

public class BaioNewsBot extends TelegramLongPollingBot {

	Model model = new Model();

	Set<IsAttivo> attivi = new HashSet<>();

	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			long chat_id = update.getMessage().getChatId();

			SendMessage message = new SendMessage();

			message.setChatId(chat_id).setText("Ciao! \n" + "Sono il NewsBot de La Baionetta, ogni sera alle 21 "
					+ "ti invierò gli ultimi aggiornamenti del tuo sito preferito");

			try {
				sendMessage(message); // Sending our message object to user
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}

			if (update.getMessage().getText().equals("/start")) {

				IsAttivo isAttivo = new IsAttivo(chat_id);

				if(!attivi.contains(isAttivo))
					attivi.add(isAttivo);

				for (IsAttivo ia : attivi) {
					if (ia.getChat_id() == chat_id) {
						if (ia.isAttivo()) {
							String message_notification = "Sono già attivo su questa chat!\n";

							message.setText(message_notification);

							try {
								sendMessage(message);

							} catch (TelegramApiException e) {
								e.printStackTrace();
							}
						}

						if (!ia.isAttivo()) {
							ia.setAttivo(true);
							TimerExecutor te = new TimerExecutor();
							CustomTimerTask ctt = new CustomTimerTask("invia", 365) {

								@Override
								public void execute() {
									sendNotification(message);
								}
							};
							te.startExecutionEveryDayAt(ctt, 19, 00, 00);
						}
					}
				}
			}
			if (update.getMessage().getText().equals("/aggiornami")) {

				for (Articolo a : model.sendNotification(message)) {
					String message_notification = "L'articolo " + a.getTitolo() + "\nsi trova al link " + a.getLink()
							+ "\ned è stato scritto da " + a.getPenna() + "\n\n";

					message.setText(message_notification);

					try {
						sendMessage(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void sendNotification(SendMessage message) {
		String message_text = "Sono le 21 e vi avviso di tutti gli ultimi articoli scritti!!\n\n";

		message.setText(message_text);

		try {
			sendMessage(message); // Sending our message object to user
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

		for (Articolo a : model.sendNotification(message)) {
			String message_notification = "L'articolo " + a.getTitolo() + "\nsi trova al link " + a.getLink()
					+ "\ned è stato scritto da " + a.getPenna() + "\n\n";

			message.setText(message_notification);

			try {
				sendMessage(message); // Sending our message object to user
			} catch (TelegramApiException e) {
				e.printStackTrace();
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
