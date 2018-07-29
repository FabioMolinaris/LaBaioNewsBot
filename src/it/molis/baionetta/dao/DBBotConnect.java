package it.molis.baionetta.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class DBBotConnect {

	private static String jdbcURL = "jdbc:mysql://localhost/baionettaBot?tcpKeepAlive=true&autoReconnect=true";

	private static DataSource ds;

	public static Connection getConnection() {

		if (ds == null) {
			try {
				ds = DataSources.pooledDataSource(DataSources.unpooledDataSource(jdbcURL, "fabio", "gtik9328"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			Connection c = ds.getConnection();
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}