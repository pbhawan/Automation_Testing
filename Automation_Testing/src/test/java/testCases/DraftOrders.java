package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

//import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viome.components.ConnectionProperties;
import com.viome.components.DBConnection;

import com.viome.components.ExcelToJSONConvertor;
import com.viome.components.HTTPConnection;
import com.viome.enums.webhooks;

//Draft Order Added 123 /

public class DraftOrders {
	HTTPConnection HC;
	DBConnection DB;
	ResultSet rs;
	ConnectionProperties _CP;
	ExcelToJSONConvertor EJ;
	webhooks WH;


	private static ObjectMapper mapper = new ObjectMapper();
	ArrayList<Object> JsonRecords;
	int Iteration = 0;

	@SuppressWarnings("static-access")
	@BeforeTest
	public void Setup() throws Exception {
			
		HC = new HTTPConnection();
		DB = new DBConnection();
		EJ = new ExcelToJSONConvertor();
		//CJ = new CSVFileHandling()  ;	
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", WH.DraftOrder);
		@SuppressWarnings("unused")
		ConnectionProperties _CP = DB.DBConnection();
	}

	@AfterTest
	public void Teardown() throws SQLException {

		_CP.rs.close();
		_CP.conn.close();
		_CP.stmt.close();

	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	@Test
	public void VerifyDraftOrderData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());
			map.put("created_at", new Date());
			map.put("completed_at", new Date());
			map.put("updated_at", new Date());	
			JSONObject DraftOrderJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.DraftOrder);
			TimeUnit.SECONDS.sleep(10);
			_CP = DB.GetRecordFromDB(DraftOrderJsonData,"DraftOrder");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(DraftOrderJsonData.get("admin_graphql_api_id").toString(),_CP.rs.getString("admin_graphql_api_id").toString(), "admin_graphql_api_id not Match in Row" + Iteration);					

					//Object under object handling
					JSONObject applied_discount = (JSONObject) DraftOrderJsonData.get("applied_discount");
					Assert.assertEquals(applied_discount.get("amount"),_CP.rs.getString("applied_discount_amount"),"applied_discount_amount not Match in Row"+Iteration);
					Assert.assertEquals(applied_discount.get("description"),_CP.rs.getString("applied_discount_description"),"applied_discount_description not Match in Row"+Iteration);
					Assert.assertEquals(applied_discount.get("title"),_CP.rs.getString("applied_discount_title"),"applied_discount_title not Match in Row"+Iteration);
					Assert.assertEquals(applied_discount.get("value"),_CP.rs.getString("applied_discount_value"),"applied_discount_value not Match in Row"+Iteration);
					Assert.assertEquals(applied_discount.get("value_type"),_CP.rs.getString("applied_discount_value_type"),"applied_discount_value_type not Match in Row"+Iteration);
					
					//Object under object handling
					JSONObject billing_address = (JSONObject) DraftOrderJsonData.get("billing_address");
					Assert.assertEquals(billing_address.get("address1"),_CP.rs.getString("billing_address_address1"),"billing_address_address1 not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("address2"),_CP.rs.getString("billing_address_address2"),"billing_address_address2 not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("city"),_CP.rs.getString("billing_address_city"),"billing_address_city not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("company"),_CP.rs.getString("billing_address_company"),"billing_address_company not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("country"),_CP.rs.getString("billing_address_country"),"billing_address_country not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("country_code"),_CP.rs.getString("billing_address_country_code"),"billing_address_country_code not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("first_name"),_CP.rs.getString("billing_address_first_name"),"billing_address_first_name not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("last_name"),_CP.rs.getString("billing_address_last_name"),"billing_address_last_name not Match in Row"+Iteration);
//null				Assert.assertEquals(billing_address.get("latitude"),_CP.rs.getString("billing_address_latitude"),"billing_address_latitude not Match in Row"+Iteration);
//handl				Assert.assertEquals(billing_address.get("longitude"),_CP.rs.getString("billing_address_longitude"),"billing_address_longitude not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("name"),_CP.rs.getString("billing_address_name"),"billing_address_name not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("phone"),_CP.rs.getString("billing_address_phone"),"billing_address_phone not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("province"),_CP.rs.getString("billing_address_province"),"billing_address_province not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("province_code"),_CP.rs.getString("billing_address_province_code"),"billing_address_province_code not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("zip"),_CP.rs.getString("billing_address_zip"),"billing_address_zip not Match in Row"+Iteration);	
					Assert.assertEquals(DraftOrderJsonData.get("currency").toString(),_CP.rs.getString("currency").toString(),"currency not Match in Row" + Iteration);

					//Object under object handling	  
					JSONObject customer = (JSONObject) DraftOrderJsonData.get("customer");
					Assert.assertEquals(customer.get("accepts_marketing").toString(),_CP.rs.getString("customer_accepts_marketing"),"customer_accepts_marketing not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("currency"),_CP.rs.getString("customer_currency"),"customer_currency not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("email"),_CP.rs.getString("customer_email"),"customer_email not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("first_name"),_CP.rs.getString("customer_first_name"),"customer_first_name not Match in Row"+Iteration);
					//Assert.assertEquals(customer.get("id"),_CP.rs.getString("customer_id"),"customer_id not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("last_name"),_CP.rs.getString("customer_last_name"),"customer_last_name not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("last_order_id"),_CP.rs.getString("customer_last_order_id"),"customer_last_order_id not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("last_order_name"),_CP.rs.getString("customer_last_order_name"),"customer_last_order_name not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("marketing_opt_in_level"),_CP.rs.getString("customer_marketing_opt_in_level"),"customer_marketing_opt_in_level not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("multipass_identifier"),_CP.rs.getString("customer_multipass_identifier"),"customer_multipass_identifier not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("note"),_CP.rs.getString("customer_note"),"customer_note not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("orders_count").toString(),_CP.rs.getString("customer_orders_count"),"customer_orders_count not Match in Row"+Iteration);
//null				Assert.assertEquals(customer.get("phone"),_CP.rs.getString("customer_phone"),"customer_phone not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("state"),_CP.rs.getString("customer_state"),"customer_state not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("tags"),_CP.rs.getString("customer_tags"),"customer_tags not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("tax_exempt").toString(),_CP.rs.getString("customer_tax_exempt"),"customer_tax_exempt not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("total_spent"),_CP.rs.getString("customer_total_spent"),"customer_total_spent not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("verified_email").toString(),_CP.rs.getString("customer_verified_email"),"customer_verified_email not Match in Row"+Iteration);														
																			
					//Object under object handling
					JSONObject default_address = (JSONObject) customer.get("default_address");
					Assert.assertEquals(default_address.get("address1"),_CP.rs.getString("customer_default_address_address1"),"customer_default_address_address1 not Match in Row"+Iteration);
//null				Assert.assertEquals(default_address.get("address2"),_CP.rs.getString("customer_default_address_address2"),"customer_default_address_address2 not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("city"),_CP.rs.getString("customer_default_address_city"),"customer_default_address_city not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("company"),_CP.rs.getString("customer_default_address_company"),"customer_default_address_company not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("country"),_CP.rs.getString("customer_default_address_country"),"customer_default_address_country not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("country_code"),_CP.rs.getString("customer_default_address_country_code"),"customer_default_address_country_code not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("country_name"),_CP.rs.getString("customer_default_address_country_name"),"customer_default_address_country_name not Match in Row"+Iteration);
//					Assert.assertEquals(default_address.get("customer_id"),_CP.rs.getString("customer_default_address_customer_id"),"customer_default_address_customer_id not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("default").toString(),_CP.rs.getString("customer_default_address_default"),"customer_default_address_default not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("first_name"),_CP.rs.getString("customer_default_address_first_name"),"customer_default_address_first_name not Match in Row"+Iteration);
//					Assert.assertEquals(default_address.get("id"),_CP.rs.getString("customer_default_address_id"),"customer_default_address_id not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("last_name"),_CP.rs.getString("customer_default_address_last_name"),"customer_default_address_last_name not Match in Row"+Iteration);
////null			Assert.assertEquals(default_address.get("name"),_CP.rs.getString("customer_default_address_name"),"customer_default_address_name not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("phone"),_CP.rs.getString("customer_default_address_phone"),"customer_default_address_phone not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("address_province"),_CP.rs.getString("customer_default_address_province"),"customer_default_address_province not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("province_code"),_CP.rs.getString("customer_default_address_province_code"),"customer_default_address_province_code not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("zip"),_CP.rs.getString("customer_default_address_zip"),"customer_default_address_zip not Match in Row"+Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("email").toString(),_CP.rs.getString("email").toString(), "email not Match in Row" + Iteration);
    				Assert.assertEquals(DraftOrderJsonData.get("invoice_url").toString(),_CP.rs.getString("invoice_url").toString(), "invoice_url not Match in Row" + Iteration);
//					Assert.assertEquals(DraftOrderJsonData.get("line_items").toString(),_CP.rs.getString("line_items").toString(), "line_items not Match in Row" + Iteration);	
					Assert.assertEquals(DraftOrderJsonData.get("name").toString(),_CP.rs.getString("name").toString(), "name not Match in Row" + Iteration);
					Assert.assertEquals(DraftOrderJsonData.get("note").toString(),_CP.rs.getString("note").toString(), "note not Match in Row" + Iteration);
					Assert.assertEquals(DraftOrderJsonData.get("note_attributes").toString(),_CP.rs.getString("note_attributes").toString(), "note_attributes not Match in Row" + Iteration);
					
					//Object under object handling
					JSONObject shipping_address = (JSONObject) DraftOrderJsonData.get("shipping_address");
					Assert.assertEquals(shipping_address.get("address1"),_CP.rs.getString("shipping_address_address1"),"shipping_address_address1 not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("address2"),_CP.rs.getString("shipping_address_address2"),"shipping_address_address2 not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("city"),_CP.rs.getString("shipping_address_city"),"shipping_address_city not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("company"),_CP.rs.getString("shipping_address_company"),"shipping_address_company not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("country"),_CP.rs.getString("shipping_address_country"),"shipping_address_country not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("country_code"),_CP.rs.getString("shipping_address_country_code"),"shipping_address_country_code not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("first_name"),_CP.rs.getString("shipping_address_first_name"),"shipping_address_first_name not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("last_name"),_CP.rs.getString("shipping_address_last_name"),"shipping_address_last_name not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("latitude"),_CP.rs.getString("shipping_address_latitude"),"shipping_address_latitude not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("longitude"),_CP.rs.getString("shipping_address_longitude"),"shipping_address_longitude not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("name"),_CP.rs.getString("shipping_address_name"),"shipping_address_name not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("phone"),_CP.rs.getString("shipping_address_phone"),"shipping_address_phone not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("province"),_CP.rs.getString("shipping_address_province"),"shipping_address_province not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("province_code"),_CP.rs.getString("shipping_address_province_code"),"shipping_address_province_code not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("zip"),_CP.rs.getString("shipping_address_zip"),"shipping_address_zip not Match in Row"+Iteration);
					
					//Object under object handling
					JSONObject shipping_line = (JSONObject) DraftOrderJsonData.get("shipping_line");
					Assert.assertEquals(shipping_line.get("handle"),_CP.rs.getString("shipping_line_handle"),"shipping_line_handle not Match in Row"+Iteration);
					Assert.assertEquals(shipping_line.get("price"),_CP.rs.getString("shipping_line_price"),"shipping_line_price not Match in Row"+Iteration);
					Assert.assertEquals(shipping_line.get("title"),_CP.rs.getString("shipping_line_title"),"shipping_line_title not Match in Row"+Iteration);

					Assert.assertEquals(DraftOrderJsonData.get("status").toString(),_CP.rs.getString("status").toString(),"status not Match in Row" + Iteration);
					Assert.assertEquals(DraftOrderJsonData.get("subtotal_price").toString(),_CP.rs.getString("subtotal_price").toString(),"subtotal_price not Match in Row" + Iteration);
					Assert.assertEquals(DraftOrderJsonData.get("taxes_included").toString(),_CP.rs.getString("taxes_included").toString(),"taxes_included not Match in Row" + Iteration);
					Assert.assertEquals(DraftOrderJsonData.get("tags").toString(),_CP.rs.getString("tags").toString(), "tags not Match in Row" + Iteration);
					Assert.assertEquals(DraftOrderJsonData.get("tax_exempt").toString(),_CP.rs.getString("tax_exempt").toString(), "tax_exempt not Match in Row" + Iteration);

//jsonarray			Assert.assertEquals(DraftOrderJsonData.get("tax_lines").toString(),_CP.rs.getString("tax_lines").toString(), "tax_lines not Match in Row" + Iteration);
					Assert.assertEquals(DraftOrderJsonData.get("taxes_included").toString(),_CP.rs.getString("taxes_included").toString(), "taxes_included not Match in Row" + Iteration);				
					Assert.assertEquals(DraftOrderJsonData.get("total_price").toString(),_CP.rs.getString("total_price").toString(), "total_price not Match in Row" + Iteration);
					Assert.assertEquals(DraftOrderJsonData.get("total_tax").toString(),_CP.rs.getString("total_tax").toString(), "total_tax not Match in Row" + Iteration);

				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.DraftOrder);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,WH.DraftOrder);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration, WH.DraftOrder);
			}

		}
	}

}