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


public class Checkout {
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
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", WH.Checkout);
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
	public void VerifyCheckoutData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			JSONObject CheckoutJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Checkout);
			TimeUnit.SECONDS.sleep(10);
			
			_CP = DB.GetRecordFromDB(CheckoutJsonData,"Checkout");
			if (_CP.rs.next()) {
				try {					
					Assert.assertEquals(CheckoutJsonData.get("abandoned_checkout_url").toString(),
							_CP.rs.getString("abandoned_checkout_url").toString(), "abandoned_checkout_url not Match in Row" + Iteration);
					
//					Assert.assertEquals(CheckoutJsonData.get("billing_address").toString(),
//jsonarray							_CP.rs.getString("billing_address").toString(), "billing_address not Match in Row" + Iteration);
					
					Assert.assertEquals(CheckoutJsonData.get("buyer_accepts_marketing").toString(),
							_CP.rs.getString("buyer_accepts_marketing").toString(), "buyer_accepts_marketing not Match in Row" + Iteration);
					
					Assert.assertEquals(CheckoutJsonData.get("cart_token").toString(),
							_CP.rs.getString("cart_token").toString(), "cart_token not Match in Row" + Iteration);
					
//					Assert.assertEquals(CheckoutJsonData.get("closed_at").toString(),
//null							_CP.rs.getString("closed_at").toString(), "closed_at not Match in Row" + Iteration);
					
//					Assert.assertEquals(CheckoutJsonData.get("completed_at").toString(),
//null							_CP.rs.getString("completed_at").toString(), "completed_at not Match in Row" + Iteration);
										
//					Assert.assertEquals(CheckoutJsonData.get("customer").toString(),
//jsonarray							_CP.rs.getString("customer").toString(), "customer not Match in Row" + Iteration);
					
//					Assert.assertEquals(CheckoutJsonData.get("discount_codes").toString(),
//jsonarray							_CP.rs.getString("discount_codes").toString(), "discount_codes not Match in Row" + Iteration);

					Assert.assertEquals(CheckoutJsonData.get("email").toString(),
							_CP.rs.getString("email").toString(), "email not Match in Row" + Iteration);
					
					Assert.assertEquals(CheckoutJsonData.get("gateway").toString(),
							_CP.rs.getString("gateway").toString(), "gateway not Match in Row" + Iteration);
					
					Assert.assertEquals(CheckoutJsonData.get("landing_site").toString(),
							_CP.rs.getString("landing_site").toString(), "landing_site not Match in Row" + Iteration);
					
//					Assert.assertEquals(CheckoutJsonData.get("line_items").toString(),
//jsonarray							_CP.rs.getString("line_items").toString(), "line_items not Match in Row" + Iteration);

			        Assert.assertEquals(CheckoutJsonData.get("location_id").toString(),
						    _CP.rs.getString("location_id").toString(), "location_id not Match in Row" + Iteration);

			        Assert.assertEquals(CheckoutJsonData.get("name").toString(),
						    _CP.rs.getString("name").toString(), "name not Match in Row" + Iteration);

			        Assert.assertEquals(CheckoutJsonData.get("note").toString(),
						    _CP.rs.getString("note").toString(), "note not Match in Row" + Iteration);
			        
			        Assert.assertEquals(CheckoutJsonData.get("presentment_currency").toString(),
						    _CP.rs.getString("presentment_currency").toString(), "presentment_currency not Match in Row" + Iteration);
			        
//jsonarray			        Assert.assertEquals(CheckoutJsonData.get("note_attributes").toString(),
//						    _CP.rs.getString("note_attributes").toString(), "note_attributes not Match in Row" + Iteration);

//			      Assert.assertEquals(CheckoutJsonData.get("phone").toString(),
//null			    		  _CP.rs.getString("phone").toString(), "phone not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("presentment_currency").toString(),
			    		   _CP.rs.getString("presentment_currency").toString(), "presentment_currency not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("referring_site").toString(),
			        		_CP.rs.getString("referring_site").toString(), "referring_site not Match in Row" + Iteration);
			     
//			     Assert.assertEquals(CheckoutJsonData.get("shipping_address").toString(),
//json array			        		_CP.rs.getString("shipping_address").toString(), "shipping_address not Match in Row" + Iteration);

//			     Assert.assertEquals(CheckoutJsonData.get("shipping_lines").toString(),
//json array			        		_CP.rs.getString("shipping_lines").toString(), "shipping_lines not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("source").toString(),
			        		_CP.rs.getString("source").toString(), "source not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("source_identifier").toString(),
			        		_CP.rs.getString("source_identifier").toString(), "source_identifier not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("source_name").toString(),
			        		_CP.rs.getString("source_name").toString(), "source_name not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("source_url").toString(),
			        		_CP.rs.getString("source_url").toString(), "source_url not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("subtotal_price").toString(),
			        		_CP.rs.getString("subtotal_price").toString(), "subtotal_price not Match in Row" + Iteration);
			     
//			     Assert.assertEquals(CheckoutJsonData.get("tax_lines").toString(),
//json array			        		_CP.rs.getString("tax_lines").toString(), "tax_lines not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("taxes_included").toString(),
			        		_CP.rs.getString("taxes_included").toString(), "taxes_included not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("token").toString(),
			        		_CP.rs.getString("token").toString(), "token not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("total_discounts").toString(),
			        		_CP.rs.getString("total_discounts").toString(), "total_discounts not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("total_line_items_price").toString(),
			        		_CP.rs.getString("total_line_items_price").toString(), "total_line_items_price not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("total_price").toString(),
			        		_CP.rs.getString("total_price").toString(), "total_price not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("total_tax").toString(),
			        		_CP.rs.getString("total_tax").toString(), "total_tax not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("total_weight").toString(),
			        		_CP.rs.getString("total_weight").toString(), "total_weight not Match in Row" + Iteration);

			     Assert.assertEquals(CheckoutJsonData.get("user_id").toString(),
			        		_CP.rs.getString("user_id").toString(), "user_id not Match in Row" + Iteration);

					
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Checkout);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,WH.Checkout);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration, WH.Checkout);
			}

		}
	}

}