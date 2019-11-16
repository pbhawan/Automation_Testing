package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
//import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
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
			JSONObject JsonArrayObject;
			JSONArray JsonArray;
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			JSONObject CheckoutJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Checkout);
			TimeUnit.SECONDS.sleep(10);
			
			_CP = DB.GetRecordFromDB(CheckoutJsonData,"Checkout");
			if (_CP.rs.next()) {
				try {					
					Assert.assertEquals(CheckoutJsonData.get("abandoned_checkout_url").toString(),_CP.rs.getString("abandoned_checkout_url").toString(), "abandoned_checkout_url not Match in Row" + Iteration);			
					Assert.assertEquals(CheckoutJsonData.get("buyer_accepts_marketing").toString(),_CP.rs.getString("buyer_accepts_marketing").toString(), "buyer_accepts_marketing not Match in Row" + Iteration);
					
					//Object array billing_address
					JSONObject billing_address = (JSONObject) CheckoutJsonData.get("billing_address");
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
					Assert.assertEquals(CheckoutJsonData.get("currency").toString(),_CP.rs.getString("currency").toString(),"currency not Match in Row" + Iteration);

					//Object under customer	  
					JSONObject customer = (JSONObject) CheckoutJsonData.get("customer");
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
					Assert.assertEquals(CheckoutJsonData.get("cart_token").toString(),_CP.rs.getString("cart_token").toString(), "cart_token not Match in Row" + Iteration);
			
					//Object inside customer default_address object handling
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
					Assert.assertEquals(default_address.get("phone").toString(),_CP.rs.getString("customer_default_address_phone"),"customer_default_address_phone not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("province"),_CP.rs.getString("customer_default_address_province"),"customer_default_address_province not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("province_code"),_CP.rs.getString("customer_default_address_province_code"),"customer_default_address_province_code not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("zip"),_CP.rs.getString("customer_default_address_zip"),"customer_default_address_zip not Match in Row"+Iteration);		

					Assert.assertEquals(CheckoutJsonData.get("discount_codes").toString(),_CP.rs.getString("discount_codes").toString(), "discount_codes not Match in Row" + Iteration);
//    				Assert.assertEquals(CheckoutJsonData.get("invoice_url").toString(),_CP.rs.getString("invoice_url").toString(), "invoice_url not Match in Row" + Iteration);															
					Assert.assertEquals(CheckoutJsonData.get("email").toString(),_CP.rs.getString("email").toString(), "email not Match in Row" + Iteration);					
					Assert.assertEquals(CheckoutJsonData.get("gateway").toString(),_CP.rs.getString("gateway").toString(), "gateway not Match in Row" + Iteration);					
					Assert.assertEquals(CheckoutJsonData.get("landing_site").toString(),_CP.rs.getString("landing_site").toString(), "landing_site not Match in Row" + Iteration);
					
    			    JsonArray = (JSONArray) CheckoutJsonData.get("line_items");
    				for (int i = 0; i < JsonArray.size(); i++) {
    				JsonArrayObject = (JSONObject) JsonArray.get(i);
//    				Assert.assertEquals(JsonArrayObject.get("applied_discount").toString(),_CP.rs.getString("applied_discount").toString(), "applied_discount not Match in Row" + Iteration);
//k    				Assert.assertEquals(JsonArrayObject.get("compare_at_price").toString(),_CP.rs.getString("compare_at_price").toString(), "compare_at_price not Match in Row" + Iteration);
//k    				Assert.assertEquals(JsonArrayObject.get("country_code_of_origin").toString(),_CP.rs.getString("country_code_of_origin").toString(), "country_code_of_origin not Match in Row" + Iteration);
//k    				Assert.assertEquals(JsonArrayObject.get("country_hs_codes").toString(),_CP.rs.getString("country_hs_codes").toString(), "country_hs_codes not Match in Row" + Iteration);   				
//    				Assert.assertEquals(JsonArrayObject.get("custom").toString(),_CP.rs.getString("custom").toString(),	"custom not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("fulfillment_service").toString(),_CP.rs.getString("fulfillment_service").toString(), "fulfillment_service not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("gift_card").toString(),_CP.rs.getString("gift_card").toString(), "gift_card not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("grams").toString(),_CP.rs.getString("grams").toString(),"grams" + Iteration);
//k    				Assert.assertEquals(JsonArrayObject.get("harmonized_system_code").toString(),_CP.rs.getString("harmonized_system_code").toString(),"harmonized_system_code" + Iteration);
//k    				Assert.assertEquals(JsonArrayObject.get("key").toString(),_CP.rs.getString("key").toString(),"key" + Iteration);
//k    				Assert.assertEquals(JsonArrayObject.get("line_price").toString(),_CP.rs.getString("line_price").toString(),"line_price" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("name").toString(),_CP.rs.getString("name").toString(),"name not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("price").toString(),_CP.rs.getString("price").toString(),"price not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("product_id").toString(),_CP.rs.getString("product_id").toString(),"product_id not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("properties").toString(),_CP.rs.getString("properties").toString(),"properties not Match in Row" + Iteration);
//k    				Assert.assertEquals(JsonArrayObject.get("province_code_of_origin").toString(),_CP.rs.getString("province_code_of_origin").toString(),"province_code_of_origin not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("quantity").toString(),_CP.rs.getString("quantity").toString(),"quantity not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("requires_shipping").toString(),_CP.rs.getString("requires_shipping").toString(),"quantity not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("sku").toString(),_CP.rs.getString("sku").toString(),"sku not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("tax_lines").toString(),_CP.rs.getString("tax_lines").toString(),"tax_lines not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("taxable").toString(),_CP.rs.getString("taxable").toString(),"taxable not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("title").toString(),_CP.rs.getString("title").toString(),"title not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("variant_id").toString(),_CP.rs.getString("variant_id").toString(),"variant_id not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("variant_title").toString(),_CP.rs.getString("variant_title").toString(),"variant_title not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("vendor").toString(),_CP.rs.getString("vendor").toString(),"vendor not Match in Row" + Iteration);
    				}


			        Assert.assertEquals(CheckoutJsonData.get("location_id").toString(), _CP.rs.getString("location_id").toString(), "location_id not Match in Row" + Iteration);
			        Assert.assertEquals(CheckoutJsonData.get("name").toString(),_CP.rs.getString("name").toString(), "name not Match in Row" + Iteration);
			        Assert.assertEquals(CheckoutJsonData.get("note").toString(), _CP.rs.getString("note").toString(), "note not Match in Row" + Iteration);			      
//k			        Assert.assertEquals(CheckoutJsonData.get("note_attributes").toString(),_CP.rs.getString("note_attributes").toString(), "note_attributes not Match in Row" + Iteration);
//null			    Assert.assertEquals(CheckoutJsonData.get("phone").toString(),_CP.rs.getString("phone").toString(), "phone not Match in Row" + Iteration);
			        Assert.assertEquals(CheckoutJsonData.get("presentment_currency").toString(),_CP.rs.getString("presentment_currency").toString(), "presentment_currency not Match in Row" + Iteration);
			        Assert.assertEquals(CheckoutJsonData.get("referring_site").toString(),_CP.rs.getString("referring_site").toString(), "referring_site not Match in Row" + Iteration);
			     
					//Object under object handling
					JSONObject shipping_address = (JSONObject) CheckoutJsonData.get("shipping_address");
					Assert.assertEquals(shipping_address.get("address1"),_CP.rs.getString("shipping_address_address1"),"shipping_address_address1 not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("address2"),_CP.rs.getString("shipping_address_address2"),"shipping_address_address2 not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("city"),_CP.rs.getString("shipping_address_city"),"shipping_address_city not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("company"),_CP.rs.getString("shipping_address_company"),"shipping_address_company not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("country"),_CP.rs.getString("shipping_address_country"),"shipping_address_country not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("country_code"),_CP.rs.getString("shipping_address_country_code"),"shipping_address_country_code not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("first_name"),_CP.rs.getString("shipping_address_first_name"),"shipping_address_first_name not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("last_name"),_CP.rs.getString("shipping_address_last_name"),"shipping_address_last_name not Match in Row"+Iteration);
//					Assert.assertEquals(shipping_address.get("latitude").toString(),_CP.rs.getString("shipping_address_latitude"),"shipping_address_latitude not Match in Row"+Iteration);
//					Assert.assertEquals(shipping_address.get("longitude").toString(),_CP.rs.getString("shipping_address_longitude"),"shipping_address_longitude not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("name"),_CP.rs.getString("shipping_address_name"),"shipping_address_name not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("phone"),_CP.rs.getString("shipping_address_phone"),"shipping_address_phone not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("province"),_CP.rs.getString("shipping_address_province"),"shipping_address_province not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("province_code"),_CP.rs.getString("shipping_address_province_code"),"shipping_address_province_code not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("zip"),_CP.rs.getString("shipping_address_zip"),"shipping_address_zip not Match in Row"+Iteration);
					
					Assert.assertEquals(CheckoutJsonData.get("shipping_lines").toString(),_CP.rs.getString("shipping_lines"),"shipping_lines not Match in Row"+Iteration);
//    			    JsonArray = (JSONArray) CheckoutJsonData.get("shipping_lines");
//    				for (int i = 0; i < JsonArray.size(); i++) {
//    				JsonArrayObject = (JSONObject) JsonArray.get(i);
////k    				Assert.assertEquals(JsonArrayObject.get("api_client_id").toString(),_CP.rs.getString("api_client_id").toString(),"api_client_id not Match in Row" + Iteration);
////k    				Assert.assertEquals(JsonArrayObject.get("applied_discounts").toString(),_CP.rs.getString("applied_discounts").toString(), "applied_discounts not Match in Row" + Iteration);
////null    			Assert.assertEquals(JsonArrayObject.get("carrier_identifier").toString(),_CP.rs.getString("carrier_identifier").toString(), "carrier_identifier not Match in Row" + Iteration);
////k    				Assert.assertEquals(JsonArrayObject.get("carrier_service_id").toString(),_CP.rs.getString("carrier_service_id").toString(), "carrier_service_id not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("code").toString(),_CP.rs.getString("code").toString(),"code not Match in Row" + Iteration);
////k    				Assert.assertEquals(JsonArrayObject.get("custom_tax_lines").toString(),_CP.rs.getString("custom_tax_lines").toString(),"custom_tax_lines not Match in Row" + Iteration);
////null    			Assert.assertEquals(JsonArrayObject.get("delivery_category").toString(),_CP.rs.getString("delivery_category").toString(),"delivery_category not Match in Row" + Iteration);
////k    				Assert.assertEquals(JsonArrayObject.get("markup").toString(),_CP.rs.getString("markup").toString(),"markup not Match in Row" + Iteration);
////null    			Assert.assertEquals(JsonArrayObject.get("phone").toString(),_CP.rs.getString("phone").toString(),"phone not Match in Row" + Iteration);
////k    				Assert.assertEquals(JsonArrayObject.get("presentment_title").toString(),_CP.rs.getString("presentment_title").toString(),"presentment_title not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("price").toString(),_CP.rs.getString("price").toString(),"price not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("source").toString(),_CP.rs.getString("source").toString(),"source not Match in Row" + Iteration);
////null    			Assert.assertEquals(JsonArrayObject.get("tax_lines").toString(),_CP.rs.getString("tax_lines").toString(),"tax_lines not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("title").toString(),_CP.rs.getString("title").toString(),"title not Match in Row" + Iteration);
////k    				Assert.assertEquals(JsonArrayObject.get("validation_context").toString(),_CP.rs.getString("validation_context").toString(),"validation_context not Match in Row" + Iteration);
//    				}											
				     Assert.assertEquals(CheckoutJsonData.get("source").toString(),_CP.rs.getString("source").toString(), "source not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("source_identifier").toString(),_CP.rs.getString("source_identifier").toString(), "source_identifier not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("source_name").toString(),_CP.rs.getString("source_name").toString(), "source_name not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("source_url").toString(),_CP.rs.getString("source_url").toString(), "source_url not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("subtotal_price").toString(),_CP.rs.getString("subtotal_price").toString(), "subtotal_price not Match in Row" + Iteration);			
//null		     	 Assert.assertEquals(CheckoutJsonData.get("tax_lines").toString(),_CP.rs.getString("tax_lines").toString(),"status not Match in Row" + Iteration);    				
				     Assert.assertEquals(CheckoutJsonData.get("taxes_included").toString(),_CP.rs.getString("taxes_included").toString(), "taxes_included not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("token").toString(),_CP.rs.getString("token").toString(), "token not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("total_discounts").toString(),_CP.rs.getString("total_discounts").toString(), "total_discounts not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("total_line_items_price").toString(),_CP.rs.getString("total_line_items_price").toString(), "total_line_items_price not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("total_price").toString(),_CP.rs.getString("total_price").toString(), "total_price not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("total_tax").toString(),_CP.rs.getString("total_tax").toString(), "total_tax not Match in Row" + Iteration);
				     Assert.assertEquals(CheckoutJsonData.get("total_weight").toString(),_CP.rs.getString("total_weight").toString(), "total_weight not Match in Row" + Iteration);
//				     Assert.assertEquals(CheckoutJsonData.get("user_id").toString(),_CP.rs.getString("user_id").toString(), "user_id not Match in Row" + Iteration);
					
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