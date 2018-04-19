package it.molis.baionetta.newsBot;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
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

	public BaioNewsBot() {
		model = new Model();
		attivi = new HashSet<>();
		message = new SendMessage();
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

					getAttivi();
				}
			}
			if (update.getMessage().getText().equals("ieri")
					|| update.getMessage().getText().equals("/aggiornami@BaioNewsBot")) {
				sendAccaddeIeri();
			}
		}
	}

	private void updateDBAttivi(Chat isAttivo) {
		model.addChat(isAttivo);
	}

	public void getAttivi() {
		attivi.addAll(model.getAllChat());
		for (Chat c : attivi) {

			c.setAttivo(true);
			message.setChatId(c.getChatId());
			TimerExecutor teOggi = new TimerExecutor();
			CustomTimerTask cttOggi = new CustomTimerTask("Oggi", 365) {

				@Override
				public void execute() {
					sendNotificationTimerOggi();
				}
			};
			teOggi.startExecutionEveryDayAt(cttOggi, 21, 16, 00);
			System.out.println(LocalDateTime.now());

			TimerExecutor teIeri = new TimerExecutor();
			CustomTimerTask cttIeri = new CustomTimerTask("Ieri", 365) {

				@Override
				public void execute() {
					sendNotificationTimerIeri();
				}
			};
			teIeri.startExecutionEveryDayAt(cttIeri, 17, 00, 00);

		}
	}

	public void sendNotificationTimerOggi() {
		System.out.println(LocalDateTime.now());

		if (model.getArticoliOggi().size() > 0) {
			String message_text = "Sono le 21 e vi avviso di tutti gli ultimi articoli scritti!!\n\n";
			//message.setChatId(chat_id);
			message.setText(message_text);

			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}

			for (Articolo a : model.getArticoliOggi()) {

				String message_notification = "L'articolo " + a.getTitolo() + "\nsi trova al link " + a.getLink()
						+ "\ned è stato scritto da " + a.getPenna() + "\n\n";
				//message.setChatId(chat_id);
				message.setText(message_notification);

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

			String message_text = "Sono le 17 e sono nostalgico, oggi (ma negli anni passati) è stato scritto: \n\n";
			//message.setChatId(chat_id);
			message.setText(message_text);

			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}

			sendAccaddeIeri();
		}
	}

	public void sendAccaddeIeri() {

		if (model.getAccaddeIeri().size() > 0) {
			for (Articolo a : model.getAccaddeIeri()) {

				String message_notification = "L'articolo " + a.getTitolo() + "\nsi trova al link " + a.getLink()
						+ "\ned è stato scritto da " + a.getPenna() + "\nnel lontano " + a.getData().getYear() + "\n";

				//message.setChatId(chat_id);
				message.setText(message_notification);

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
		return "BaioNewsBetaBot";
	}

	@Override
	public String getBotToken() {
		return "549518382:AAHV_bpxepsJY9s1DnIKCq5bi0sRlQH5qCQ";
	}

}
