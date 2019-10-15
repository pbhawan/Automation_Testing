package com.viome.components;

import java.util.Properties;
import org.json.simple.JSONObject;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

	// Redshift driver:
	// "jdbc:redshift://x.y.us-west-2.redshift.amazonaws.com:5439/dev";
	static final String dbURL = "jdbc:redshift://viome-metacube-cluster.cmcvkrhcw0ac.us-east-2.redshift.amazonaws.com:5439/viomedev";
	static final String MasterUsername = "viome";
	static final String MasterUserPassword = "METAViomecube3";
	ResultSet rs;
	ConnectionProperties _CP = new ConnectionProperties();
	public ConnectionProperties GetCustomerFromDB(JSONObject CustomerJsonData) throws SQLException {
	
		String id = CustomerJsonData.get("id").toString();
		try {
			// Try a simple query.
			System.out.println("Listing system tables...");
			_CP.stmt = _CP.conn.createStatement();
			String sql;
			sql = "SELECT * FROM public.customer where id=" + id + ";";
			_CP.rs = _CP.stmt.executeQuery(sql);

		} catch (Exception ex) {
			// For convenience, handle all errors here.
			ex.printStackTrace();
		} finally {

		}

		return _CP;
	}
	
	public ConnectionProperties DBConnection() throws SQLException {
		try {
			// Dynamically load driver at runtime.
			// Redshift JDBC 4.1 driver: com.amazon.redshift.jdbc41.Driver
			// Redshift JDBC 4 driver: com.amazon.redshift.jdbc4.Driver
			Class.forName("com.amazon.redshift.jdbc.Driver");

			// Open a connection and define properties.
			System.out.println("Connecting to database...");
			Properties props = new Properties();

			// Uncomment the following line if using a keystore.
			// props.setProperty("ssl", "true");
			props.setProperty("user", MasterUsername);
			props.setProperty("password", MasterUserPassword);
			_CP.conn = DriverManager.getConnection(dbURL, props);

			// Try a simple query.
			System.out.println("Listing system tables...");
			_CP.stmt = _CP.conn.createStatement();
	}catch (Exception ex) {
		// For convenience, handle all errors here.
		ex.printStackTrace();
	} finally {

	}
		return _CP;

	}
	
	
}
