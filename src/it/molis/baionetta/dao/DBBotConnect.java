package it.molis.baionetta.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class DBBotConnect {

	private static String jdbcURL = "jdbc:mysql://localhost/baionettaBot?autoReconnect=true";

	private static DataSource ds;

	public static Connection getConnection() {

		if (ds == null) {
			// initialize DataSource
			try {
				ds = DataSources.pooledDataSource(DataSources.unpooledDataSource(jdbcURL, "fabio", " "));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
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