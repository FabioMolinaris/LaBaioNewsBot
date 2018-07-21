package it.molis.baionetta.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.molis.baionetta.beans.Articolo;
import it.molis.baionetta.beans.Mostrina;
import it.molis.baionetta.beans.ParolaChiave;
import it.molis.baionetta.beans.Penna;

public class ArticoloDAO {

	Map<Articolo, Articolo> articoli = new HashMap<>();
	private Set<Penna> penne = new HashSet<>();
	private Set<Mostrina> mostrine = new HashSet<>();

	public Set<Articolo> getAll() {

		final String sql = "SELECT titolo, mostrina, penna, articolo.link, parolaChiave.parola, data, peso FROM articolo, parolaChiave where parolaChiave.link = articolo.link";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Mostrina m = new Mostrina(rs.getString("mostrina"));
				Penna p = new Penna(rs.getString("penna"));
				ParolaChiave pc = new ParolaChiave(rs.getString("parola"), rs.getString("articolo.link"), rs.getInt("peso"));

				Articolo a = new Articolo(rs.getString("titolo"), m, p, rs.getString("link"), (rs.getDate("data").toLocalDate()));

				if (articoli.containsKey(a)) {
					articoli.get(a).setMostrina(m);
					articoli.get(a).setPenna(p);
					articoli.get(a).addParolaChiave(pc);
				}
				if (!articoli.containsKey(a)) {
					articoli.put(a, a);
					articoli.get(a).setMostrina(m);
					articoli.get(a).setPenna(p);
					articoli.get(a).addParolaChiave(pc);
				}

				penne.add(p);
				mostrine.add(m);

			}
			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return new HashSet<Articolo>(articoli.values());
	}

	public Set<Articolo> getAllArticoli() {

		if(!articoli.isEmpty())
			return new HashSet<Articolo>(articoli.values());
		else{
			getAll();
			return getAllArticoli();
		}
	}

	public Set<Penna> getAllPenne() {

		if(!penne.isEmpty())
			return penne;
		else{
			getAll();
			return getAllPenne();
		}
	}

	public Set<Mostrina> getAllMostrine() {

		if(!mostrine.isEmpty())
			return mostrine;
		else{
			getAll();
			return getAllMostrine();
		}
	}
	
	public void addArticolo(Articolo a) {

		final String sql = "INSERT INTO articolo (titolo, mostrina, penna, link, data)" + " VALUES (?, ?, ?, ?, ?)";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, a.getTitolo());
			st.setString(2, a.getMostrina().getNome());
			st.setString(3, a.getPenna().getNome());
			st.setString(4, a.getLink());
			String data = "" + a.getData().getYear() + "-" + a.getData().getMonthValue() + "-"
					+ a.getData().getDayOfMonth();
			st.setDate(5, Date.valueOf(data));

			st.executeUpdate();

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}
	}

	public void addParoleChiave(ParolaChiave pc) {

		final String sql = "INSERT INTO parolaChiave (parola, link, peso)" + " VALUES (?, ?, ?)";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, pc.getParola());
			st.setString(2, pc.getLink());
			st.setInt(3, pc.getPeso());

			st.executeUpdate();

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}
	}

	public Set<ParolaChiave> getAllParoleChiave(Articolo a) {

		final String sql = "SELECT parola, link, peso FROM parolaChiave WHERE link=?";

		Set<ParolaChiave> paroleChiave = new HashSet<ParolaChiave>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, a.getLink());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				ParolaChiave pc = new ParolaChiave(rs.getString("parola"), rs.getString("link"), rs.getInt("peso"));
				paroleChiave.add(pc);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return paroleChiave;

	}

	public void addPenna(Penna p) {

		final String sql = "INSERT INTO penna (nome) VALUES (?)";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, p.getNome());

			st.executeUpdate();

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}
	}
	
	public void addMostrina(Mostrina m) {

		final String sql = "INSERT INTO mostrina (nome) VALUES (?)";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, m.getNome());

			st.executeUpdate();

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}
	}
}
