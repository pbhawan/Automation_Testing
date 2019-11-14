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


public class Order {
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
		//CJ = new CSVFileHandling();	
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", WH.Order);
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
	public void VerifyProductData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			JSONObject OrderJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Order);
			TimeUnit.SECONDS.sleep(10);
			
			_CP = DB.GetRecordFromDB(OrderJsonData,"Order");
			if (_CP.rs.next()) {
				try {					
					Assert.assertEquals(OrderJsonData.get("email").toString(),
							_CP.rs.getString("email").toString(), "email not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("closed_at").toString(),
							_CP.rs.getString("closed_at").toString(), "closed_at not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("checkout_id").toString(),
							_CP.rs.getString("checkout_id").toString(), "checkout_id not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("checkout_token").toString(),
							_CP.rs.getString("checkout_token").toString(), "checkout_token not Match in Row" + Iteration);
					
//					Assert.assertEquals(OrderJsonData.get("created_at").toString(),
//datevalue							_CP.rs.getString("created_at").toString(), "created_at not Match in Row" + Iteration);
					
//					Assert.assertEquals(OrderJsonData.get("updated_at").toString(),
//datevalue							_CP.rs.getString("updated_at").toString(), "updated_at not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("number").toString(), 
							_CP.rs.getString("number").toString(),"number not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("note").toString(),
							_CP.rs.getString("note").toString(),"note not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("token").toString(),
							_CP.rs.getString("token").toString(), "token not Match in Row" + Iteration);
									
					Assert.assertEquals(OrderJsonData.get("gateway").toString(),
							_CP.rs.getString("gateway").toString(), "gateway not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("test").toString(),
							_CP.rs.getString("test").toString(), "test not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("total_price").toString(),
							_CP.rs.getString("total_price").toString(), "total_price not Match in Row" + Iteration);

//jsonarray					Assert.assertEquals(OrderJsonData.get("total_price_set").toString(),
//							_CP.rs.getString("total_price_set").toString(), "total_price_set not Match in Row" + Iteration);

					
				    Assert.assertEquals(OrderJsonData.get("subtotal_price").toString(),
							_CP.rs.getString("subtotal_price").toString(), "subtotal_price not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("total_weight").toString(),
					_CP.rs.getString("total_weight").toString(), "total_weight not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("total_tax").toString(),
					_CP.rs.getString("total_tax").toString(), "total_tax not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("taxes_included").toString(),
					_CP.rs.getString("taxes_included").toString(), "taxes_included not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("currency").toString(),
					_CP.rs.getString("currency").toString(), "currency not Match in Row" + Iteration);
								
					Assert.assertEquals(OrderJsonData.get("financial_status").toString(),
					_CP.rs.getString("financial_status").toString(), "financial_status not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("confirmed").toString(),
					_CP.rs.getString("confirmed").toString(), "confirmed not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("total_discounts").toString(),
							_CP.rs.getString("total_discounts").toString(), "total_discounts not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("total_line_items_price").toString(),
							_CP.rs.getString("total_line_items_price").toString(), "total_line_items_price not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("cart_token").toString(),
							_CP.rs.getString("cart_token").toString(), "cart_token not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("buyer_accepts_marketing").toString(),
							_CP.rs.getString("buyer_accepts_marketing").toString(), "buyer_accepts_marketing not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("name").toString(),
							_CP.rs.getString("name").toString(), "name not Match in Row" + Iteration);
										
					Assert.assertEquals(OrderJsonData.get("referring_site").toString(),
							_CP.rs.getString("referring_site").toString(), "referring_site not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("landing_site").toString(),
							_CP.rs.getString("landing_site").toString(), "landing_site not Match in Row" + Iteration);
					
//  				Assert.assertEquals(OrderJsonData.get("cancelled_at").toString(),
//	null value				_CP.rs.getString("cancelled_at").toString(), "cancelled_at not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("cancel_reason").toString(),
							_CP.rs.getString("cancel_reason").toString(), "cancel_reason not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("total_price_usd").toString(),
							_CP.rs.getString("total_price_usd").toString(), "total_price_usd not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("checkout_token").toString(),
							_CP.rs.getString("checkout_token").toString(), "checkout_token not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("reference").toString(),
							_CP.rs.getString("reference").toString(), "reference not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("user_id").toString(),
//null value							_CP.rs.getString("user_id").toString(), "user_id not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("location_id").toString(),
							_CP.rs.getString("location_id").toString(), "location_id not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("source_identifier").toString(),
							_CP.rs.getString("source_identifier").toString(), "source_identifier not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("source_url").toString(),
							_CP.rs.getString("source_url").toString(), "source_url not Match in Row" + Iteration);
					
//					Assert.assertEquals(OrderJsonData.get("processed_at").toString(),
//datevalue							_CP.rs.getString("processed_at").toString(), "processed_at not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("source_url").toString(),
							_CP.rs.getString("source_url").toString(), "source_url not Match in Row" + Iteration);
					
//					Assert.assertEquals(OrderJsonData.get("device_id").toString(),
//nullvalue							_CP.rs.getString("device_id").toString(), "device_id not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("phone").toString(),
							_CP.rs.getString("phone").toString(), "phone not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("customer_locale").toString(),
							_CP.rs.getString("customer_locale").toString(), "customer_locale not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("app_id").toString(),
							_CP.rs.getString("app_id").toString(), "app_id not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("browser_ip").toString(),
							_CP.rs.getString("browser_ip").toString(), "browser_ip not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("landing_site_ref").toString(),
							_CP.rs.getString("landing_site_ref").toString(), "landing_site_ref not Match in Row" + Iteration);
					
					Assert.assertEquals(OrderJsonData.get("order_number").toString(),
							_CP.rs.getString("order_number").toString(), "order_number not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("discount_applications").toString(),
//jsonarray							_CP.rs.getString("discount_applications").toString(), "discount_applications not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("discount_codes").toString(),
//jsonarray							_CP.rs.getString("discount_codes").toString(), "discount_codes not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("note_attributes").toString(),
//jsonarray							_CP.rs.getString("note_attributes").toString(), "note_attributes not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("payment_gateway_names").toString(),
//jsonarray							_CP.rs.getString("payment_gateway_names").toString(), "payment_gateway_names not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("processing_method").toString(),
							_CP.rs.getString("processing_method").toString(), "processing_method not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("checkout_id").toString(),
							_CP.rs.getString("checkout_id").toString(), "checkout_id not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("source_name").toString(),
							_CP.rs.getString("source_name").toString(), "source_name not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("fulfillment_status").toString(),
							_CP.rs.getString("fulfillment_status").toString(), "fulfillment_status not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("tax_lines").toString(),
//jsonarray							_CP.rs.getString("tax_lines").toString(), "tax_lines not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("tags").toString(),
							_CP.rs.getString("tags").toString(), "tags not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("contact_email").toString(),
							_CP.rs.getString("contact_email").toString(), "contact_email not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("order_status_url").toString(),
							_CP.rs.getString("order_status_url").toString(), "order_status_url not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("presentment_currency").toString(),
							_CP.rs.getString("presentment_currency").toString(), "presentment_currency not Match in Row" + Iteration);
					
//					Assert.assertEquals(OrderJsonData.get("total_line_items_price_set").toString(),
//jsonarray							_CP.rs.getString("total_line_items_price_set").toString(), "total_line_items_price_set not Match in Row" + Iteration);

					Assert.assertEquals(OrderJsonData.get("admin_graphql_api_id").toString(),
							_CP.rs.getString("admin_graphql_api_id").toString(), "admin_graphql_api_id not Match in Row" + Iteration);

					
//					Assert.assertEquals(OrderJsonData.get("line_items").toString(),
//jsonarray							_CP.rs.getString("line_items").toString(), "line_items not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("shipping_lines").toString(),
//jsonarray							_CP.rs.getString("shipping_lines").toString(), "shipping_lines not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("billing_address").toString(),
//jsonarray							_CP.rs.getString("billing_address").toString(), "billing_address not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("shipping_address").toString(),
//jsonarray							_CP.rs.getString("shipping_address").toString(), "shipping_address not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("fulfillments").toString(),
//jsonarray							_CP.rs.getString("fulfillments").toString(), "fulfillments not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("refunds").toString(),
//jsonarray							_CP.rs.getString("refunds").toString(), "refunds not Match in Row" + Iteration);

//					Assert.assertEquals(OrderJsonData.get("customer").toString(),
//jsonarray							_CP.rs.getString("customer").toString(), "customer not Match in Row" + Iteration);


					
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Order);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,WH.Order);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration, WH.Order);
			}

		}
	}

}