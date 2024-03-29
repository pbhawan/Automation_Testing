package com.viome.components;

import java.util.Properties;
import org.json.simple.JSONObject;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	/*staging DB credential*/
//	static final String dbURL = "jdbc:redshift://viome-metacube-cluster.cmcvkrhcw0ac.us-east-2.redshift.amazonaws.com:5439/viomedev";
//	static final String MasterUsername = "viome";
//	static final String MasterUserPassword = "METAViomecube3";
	/*prod DB credential*/
	static final String dbURL = "jdbc:redshift://viome-integration-cluster.cmcvkrhcw0ac.us-east-2.redshift.amazonaws.com:5439/webhooks";
	static final String MasterUsername = "viome";
	static final String MasterUserPassword = "ViomeMetacube1";
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
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Cart":
				id = JsonData.get("id").toString();
				sql="SELECT  o.external_id,o.token,o.note,l.* FROM public.orders as o INNER JOIN public.line_items as l ON o.external_id = l.mapping_id WHERE o.type='Cart' and o.external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Checkout":
				id = JsonData.get("id").toString();
				sql="SELECT o.*,l.external_id, l.admin_graphql_api_id, l.applied_discount_amount, l.applied_discount_description, l.applied_discount_title, l.applied_discount_value, l.applied_discount_value_type, l.custom, l.discount_allocations, l.discounted_price_set, l.discounts, l.id, l.line_price_set, l.original_line_price_set, l.price_set, l.properties, l.total_discount_set, l.fulfillable_quantity, l.fulfillment_service, l.fulfillment_status, l.gift_card, l.grams, l.line_price, l.name, l.order_id, l.original_line_price, l.original_price, l.price, l.product_exists, l.product_id, l.product_id_pk, l.quantity, l.refund_line_item_id, l.requires_shipping, l.sku, l.taxable, l.title, l.total_discount,l.variant_id, l.variant_inventory_management, l.variant_title, l.vendor, l.mapping_id, l.compare_at_price, l.country_code_of_origin, l.province_code_of_origin, l.harmonized_system_code, l.country_hs_codes, l.unit_price_measurement, l.discounted_price, l.key, l.applied_discounts, l.pre_tax_price, l.pre_tax_price_set FROM public.orders as o INNER JOIN public.line_items as l ON o.external_id = l.mapping_id WHERE o.type='Checkout' and o.external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "DraftOrder":
				id = JsonData.get("id").toString();
				sql="SELECT  o.*,l.custom,l.admin_graphql_api_id,l.total_discount_set,l.tax_lines,l.applied_discount_amount,l.applied_discount_description,l.applied_discount_title,l.applied_discount_value,l.applied_discount_value_type,l.discount_allocations,l.external_id,l.fulfillable_quantity,l.fulfillment_service,l.fulfillment_status,l.gift_card,l.grams,l.id,l.id,l.mapping_id,l.name as line_name,l.order_id,l.price,l.price_set,l.product_exists,l.product_id,l.properties,l.properties,l.quantity,l.refund_line_item_id,l.requires_shipping,l.sku,l.tax_lines,l.taxable,l.title,l.total_discount,l.variant_id,l.variant_inventory_management,l.variant_title,l.vendor FROM public.orders as o INNER JOIN public.line_items as l ON o.external_id = l.mapping_id WHERE o.type='Draft_Order' and o.external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Fulfillment":
				id = JsonData.get("id").toString();
				sql="SELECT  f.*,l.custom,l.pre_tax_price_set as line_pre_tax_price_set,l.pre_tax_price as line_pre_tax_price,l.admin_graphql_api_id,l.total_discount_set,l.tax_lines,l.applied_discount_amount,l.applied_discount_description,l.applied_discount_title,l.applied_discount_value,l.applied_discount_value_type,l.discount_allocations,l.external_id,l.fulfillable_quantity,l.fulfillment_service,l.fulfillment_status,l.gift_card,l.grams,l.id,l.id,l.mapping_id,l.name as line_name,l.order_id,l.price,l.price_set,l.product_exists,l.product_id,l.properties,l.properties,l.quantity,l.refund_line_item_id,l.requires_shipping,l.sku,l.tax_lines,l.taxable,l.title,l.total_discount,l.variant_id,l.variant_inventory_management,l.variant_title,l.vendor FROM public.fulfillment as f INNER JOIN public.line_items as l ON f.external_id = l.mapping_id WHERE f.external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Location":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.location where id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Theme":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.theme where id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Order":
				id = JsonData.get("id").toString();
				sql="SELECT  o.*,l.custom,l.admin_graphql_api_id,l.total_discount_set,l.tax_lines,l.applied_discount_amount,l.applied_discount_description,l.applied_discount_title,l.applied_discount_value,l.applied_discount_value_type,l.discount_allocations,l.external_id,l.fulfillable_quantity,l.fulfillment_service,l.fulfillment_status,l.gift_card,l.grams,l.id,l.id,l.mapping_id,l.name as line_name,l.order_id,l.price,l.price_set,l.product_exists,l.product_id,l.properties,l.properties,l.quantity,l.refund_line_item_id,l.requires_shipping,l.sku,l.tax_lines,l.taxable,l.title,l.total_discount,l.variant_id,l.variant_inventory_management,l.variant_title,l.vendor FROM public.orders as o INNER JOIN public.line_items as l ON o.external_id = l.mapping_id WHERE o.type='Order' and o.external_id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Order_Transaction":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.order_transaction where id=" + id + ";";
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Product":
				id = JsonData.get("id").toString();
				sql="SELECT var.*, img.*, pro.admin_graphql_api_id,pro.body_html,pro.handle,pro.product_type,pro.published_scope,pro.tags,pro.template_suffix,pro.template_suffix,pro.title as product_title,pro.vendor FROM public.product as pro JOIN public.options as op ON pro.id = op.product_id JOIN public.images as img ON img.id = op.product_id JOIN public.variants as var ON var.id = op.product_id where pro.id="+ id +";";;
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Refund":
				id = JsonData.get("id").toString();
				sql="SELECT o.order_adjustments,o.order_transactions,o.note,o.order_id,o.restock,o.type,o.user_id,rli.*,li.* from public.orders o,refund_line_items rli,line_items li where rli.mapping_id = o.external_id and rli.refund_line_item_id = li.external_id and type='Refund_Order' and o.external_id=" + id +";"; 
				System.out.println(sql);
				_CP.rs = _CP.stmt.executeQuery(sql);
				break;
				
			case "Shop":
				id = JsonData.get("id").toString();
				sql = "SELECT * FROM public.shop where id=" + id + ";";
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
