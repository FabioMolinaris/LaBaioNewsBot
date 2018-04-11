package it.molis.newsBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

			if (update.getMessage().getText().equals("/start")
					|| update.getMessage().getText().equals("/start@BaioNewsBot")) {

				IsAttivo isAttivo = new IsAttivo(chat_id);

				if (!attivi.contains(isAttivo)){
					attivi.add(isAttivo);
					try {
						updateFileBackup();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				for (IsAttivo ia : attivi) {

					if (ia.getChat_id() == chat_id) {

						if (ia.isAttivo()) {

							String message_notification = "Sono già attivo su questa chat!\n";

							message.setText(message_notification);

							try {
								execute(message);

							} catch (TelegramApiException e) {
								e.printStackTrace();
							}
						}

						if (!ia.isAttivo()) {
							ia.setAttivo(true);

							message.setChatId(chat_id).setText("Ciao! \n" + "Sono il NewsBot de La Baionetta, ogni sera alle 21 "
									+ "ti invierò gli ultimi aggiornamenti del tuo sito preferito");

							try {
								execute(message); // Sending our message object to user
							} catch (TelegramApiException e) {
								e.printStackTrace();
							}

							TimerExecutor te = new TimerExecutor();
							CustomTimerTask ctt = new CustomTimerTask("invia", 365) {

								@Override
								public void execute() {
									sendNotificationTimer(message);
								}
							};
							te.startExecutionEveryDayAt(ctt, 21, 00, 00);
						}
					}
				}
			}
			if (update.getMessage().getText().equals("/aggiornami")
					|| update.getMessage().getText().equals("/aggiornami@BaioNewsBot")) {

				sendNotificationAggiornami(message);
			}

		}
	}

	private void updateFileBackup() throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/BaioBackup.molis")));
		for(IsAttivo ia : attivi){
			String content = ""+ia.getChat_id()+"\n";
			out.append(content);
		}
		out.close();
	}

	public void ripristinaBackup() throws NumberFormatException, IOException{
		String s;
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader("/BaioBackup.molis"));
			while( (s = reader.readLine()) != null ){
				IsAttivo ia = new IsAttivo(Long.valueOf(s.split("\n")[0]).longValue());
				attivi.add(ia);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for(IsAttivo ia : attivi){
			ia.setAttivo(true);
			SendMessage message = new SendMessage().setChatId(ia.getChat_id());
			TimerExecutor te = new TimerExecutor();
			CustomTimerTask ctt = new CustomTimerTask("backup", 365) {

				@Override
				public void execute() {
					sendNotificationTimer(message);
				}
			};
			te.startExecutionEveryDayAt(ctt, 21, 00, 00);
		}
	}

	public void sendNotificationTimer(SendMessage message) {

		String message_text = "Sono le 21 e vi avviso di tutti gli ultimi articoli scritti!!\n\n";

		message.setText(message_text);

		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

		sendNotification(message);
	}

	public void sendNotificationAggiornami(SendMessage message) {

		String message_text = "Eccomi! \nOra ti avviso di tutti gli ultimi articoli scritti!!\n\n";

		message.setText(message_text);

		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}

		sendNotification(message);

	}

	public void sendNotification(SendMessage message) {

		if (model.sendNotification(message).size() > 0) {
			for (Articolo a : model.sendNotification(message)) {

				String message_notification = "L'articolo " + a.getTitolo() + "\nsi trova al link " + a.getLink()
						+ "\ned è stato scritto da " + a.getPenna() + "\n\n";

				message.setText(message_notification);

				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		} else {

			message.setText("Oggi non è stato scritto niente\n");

			try {
				execute(message); // Sending our message object to user
			} catch (TelegramApiException e) {
				e.printStackTrace();
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
