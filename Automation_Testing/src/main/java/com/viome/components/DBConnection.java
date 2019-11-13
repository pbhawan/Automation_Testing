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

	public ConnectionProperties GetRecordFromDB(JSONObject JsonData, String webhook) throws SQLException {
		System.out.println("Listing system tables...");
		_CP.stmt = _CP.conn.createStatement();
		String sql;
		String id;
		try {
			switch (webhook) {
			case "Customer":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.customer where id=" + id + ";";
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
			case "Location":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.location where id=" + id + ";";
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
			case "Theme":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.theme where id=" + id + ";";
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
			case "Product":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.product where id=" + id + ";";
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
			case "DraftOrder":
				id = JsonData.get("external_id").toString();
				sql = "SELECT * FROM public.orders where external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
			case "Order":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.orders where external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
			case "Order_Transaction":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.order_transaction where id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
			case "Cart":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.orders where external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
			case "Checkout":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.orders where external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
			case "Fulfillment":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.fulfillment where external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Tender_Transaction":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.tender_transaction where id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
				
			}
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
