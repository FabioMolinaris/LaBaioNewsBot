package it.molis.baionetta.dao;

import java.sql.Connection;
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
}
