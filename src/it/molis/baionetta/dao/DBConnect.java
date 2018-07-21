package it.molis.baionetta.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class DBConnect {

	private static String jdbcURL = "jdbc:mysql://192.168.1.166/baionetta?autoReconnect=true";

	private static DataSource ds;

	public static Connection getConnection() {

		if (ds == null) {
			try {
				ds = DataSources.pooledDataSource(DataSources.unpooledDataSource(jdbcURL, "fabio", "gtik9328"));
			} catch (SQLException e) {
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