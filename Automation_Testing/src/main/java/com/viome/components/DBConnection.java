package com.viome.components;

import java.util.Properties;
import org.json.simple.JSONObject;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

	static final String dbURL = "jdbc:redshift://viome-metacube-cluster.cmcvkrhcw0ac.us-east-2.redshift.amazonaws.com:5439/viomedev";
	static final String MasterUsername = "viome";
	static final String MasterUserPassword = "METAViomecube3";
	ResultSet rs;
	ConnectionProperties _CP = new ConnectionProperties();

	public ConnectionProperties GetRecordFromDB(JSONObject CustomerJsonData, String webhook) throws SQLException {

		String id = CustomerJsonData.get("id").toString();
		try {
			System.out.println("Listing system tables...");
			_CP.stmt = _CP.conn.createStatement();
			String sql;
			if (webhook == "Customer") {
				sql = "SELECT * FROM public.customer where id=" + id + ";";
				_CP.rs = _CP.stmt.executeQuery(sql);
			}
			if (webhook == "Location") {
				sql = "SELECT * FROM public.location where id=" + id + ";";
				_CP.rs = _CP.stmt.executeQuery(sql);
			}
			if (webhook == "Theme") {
				sql = "SELECT * FROM public.theme where id=" + id + ";";
				_CP.rs = _CP.stmt.executeQuery(sql);
			}
			if (webhook == "Product") {

				sql = "SELECT * FROM public.product where id=" + id + ";";
				_CP.rs = _CP.stmt.executeQuery(sql);
			}
//		
		} catch (Exception ex) {
			// For convenience, handle all errors here.
			ex.printStackTrace();
		} finally {

		}

		return _CP;
	}

	public ConnectionProperties DBConnection() throws SQLException {
		try {

			Class.forName("com.amazon.redshift.jdbc.Driver");
			System.out.println("Connecting to database...");
			Properties props = new Properties();
			props.setProperty("user", MasterUsername);
			props.setProperty("password", MasterUserPassword);
			_CP.conn = DriverManager.getConnection(dbURL, props);
			_CP.stmt = _CP.conn.createStatement();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
		return _CP;

	}

}
