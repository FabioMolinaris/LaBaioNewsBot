package it.molis.baionetta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import it.molis.baionetta.beans.Chat;

public class ChatDAO {

	private Set<Chat> chat = new HashSet<>();

	public Set<Chat> getAll() {

		final String sql = "SELECT idChat, active FROM baionettaBot.chat";

		try {
			Connection conn = DBBotConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Chat c = new Chat(rs.getLong("idChat"));
				c.setAttivo(true);
				System.out.println(c.getChatId());
				chat.add(c);
			}
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database del BOT.");
		}
		return chat;
	}

	public void addChat(Chat chat) {

		try {
			Connection conn = DBBotConnect.getConnection();

			final String sql = "INSERT INTO baionettaBot.chat (idChat)" + " VALUES (?)";

			PreparedStatement st = conn.prepareStatement(sql);
			st.setLong(1, chat.getChatId());
			st.executeUpdate();
			st.close();

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore di connessione al Database.");
		}
	}
}
