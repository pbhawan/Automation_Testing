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
	public void VerifyOrderData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			JSONObject JsonArrayObject;
			JSONArray JsonArray;
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			JSONObject OrderJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Order);
			TimeUnit.SECONDS.sleep(10);
			_CP = DB.GetRecordFromDB(OrderJsonData,"Order");
			if (_CP.rs.next()) {
				try {				
					Assert.assertEquals(OrderJsonData.get("admin_graphql_api_id").toString(),_CP.rs.getString("admin_graphql_api_id").toString(), "admin_graphql_api_id not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("app_id").toString(),_CP.rs.getString("app_id").toString(), "app_id not Match in Row" + Iteration);
				
					//Object under object handling
					JSONObject billing_address = (JSONObject) OrderJsonData.get("billing_address");
					Assert.assertEquals(billing_address.get("address1"),_CP.rs.getString("billing_address_address1"),"billing_address_address1 not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("address2"),_CP.rs.getString("billing_address_address2"),"billing_address_address2 not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("city"),_CP.rs.getString("billing_address_city"),"billing_address_city not Match in Row"+Iteration);
//					Assert.assertEquals(billing_address.get("company"),_CP.rs.getString("billing_address_company"),"billing_address_company not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("country"),_CP.rs.getString("billing_address_country"),"billing_address_country not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("country_code"),_CP.rs.getString("billing_address_country_code"),"billing_address_country_code not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("first_name"),_CP.rs.getString("billing_address_first_name"),"billing_address_first_name not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("last_name"),_CP.rs.getString("billing_address_last_name"),"billing_address_last_name not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("latitude"),_CP.rs.getString("billing_address_latitude"),"billing_address_latitude not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("longitude"),_CP.rs.getString("billing_address_longitude"),"billing_address_longitude not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("name"),_CP.rs.getString("billing_address_name"),"billing_address_name not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("phone"),_CP.rs.getString("billing_address_phone"),"billing_address_phone not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("province"),_CP.rs.getString("billing_address_province"),"billing_address_province not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("province_code"),_CP.rs.getString("billing_address_province_code"),"billing_address_province_code not Match in Row"+Iteration);
					Assert.assertEquals(billing_address.get("zip"),_CP.rs.getString("billing_address_zip"),"billing_address_zip not Match in Row"+Iteration);	
					
					Assert.assertEquals(OrderJsonData.get("browser_ip").toString(),_CP.rs.getString("browser_ip").toString(), "browser_ip not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("buyer_accepts_marketing").toString(),_CP.rs.getString("buyer_accepts_marketing").toString(), "buyer_accepts_marketing not Match in Row" + Iteration);
//null				Assert.assertEquals(OrderJsonData.get("cancel_reason").toString(),_CP.rs.getString("cancel_reason").toString(), "cancel_reason not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("cart_token").toString(),_CP.rs.getString("cart_token").toString(), "cart_token not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("checkout_id").toString(),_CP.rs.getString("checkout_id").toString(), "checkout_id not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("checkout_token").toString(),_CP.rs.getString("checkout_token").toString(), "checkout_token not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("client_details").toString(),_CP.rs.getString("client_details").toString(), "client_details not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("confirmed").toString(),_CP.rs.getString("confirmed").toString(), "confirmed not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("contact_email").toString(),_CP.rs.getString("contact_email").toString(), "contact_email not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("currency").toString(),_CP.rs.getString("currency").toString(),"currency not Match in Row" + Iteration);

					//Object under object handling	  
					JSONObject customer = (JSONObject) OrderJsonData.get("customer");
					Assert.assertEquals(customer.get("accepts_marketing").toString(),_CP.rs.getString("customer_accepts_marketing"),"customer_accepts_marketing not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("currency"),_CP.rs.getString("customer_currency"),"customer_currency not Match in Row"+Iteration);

					//Object under object handling
					JSONObject default_address = (JSONObject) customer.get("default_address");
					Assert.assertEquals(default_address.get("address1"),_CP.rs.getString("customer_default_address_address1"),"customer_default_address_address1 not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("address2"),_CP.rs.getString("customer_default_address_address2"),"customer_default_address_address2 not Match in Row"+Iteration);
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
					Assert.assertEquals(default_address.get("name"),_CP.rs.getString("customer_default_address_name"),"customer_default_address_name not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("phone").toString(),_CP.rs.getString("customer_default_address_phone"),"customer_default_address_phone not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("province"),_CP.rs.getString("customer_default_address_province"),"customer_default_address_province not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("province_code"),_CP.rs.getString("customer_default_address_province_code"),"customer_default_address_province_code not Match in Row"+Iteration);
					Assert.assertEquals(default_address.get("zip"),_CP.rs.getString("customer_default_address_zip"),"customer_default_address_zip not Match in Row"+Iteration);

					
					Assert.assertEquals(customer.get("email"),_CP.rs.getString("customer_email"),"customer_email not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("first_name"),_CP.rs.getString("customer_first_name"),"customer_first_name not Match in Row"+Iteration);
					//Assert.assertEquals(customer.get("id"),_CP.rs.getString("customer_id"),"customer_id not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("last_name"),_CP.rs.getString("customer_last_name"),"customer_last_name not Match in Row"+Iteration);
//					Assert.assertEquals(customer.get("last_order_id"),_CP.rs.getString("customer_last_order_id"),"customer_last_order_id not Match in Row"+Iteration);
//					Assert.assertEquals(customer.get("last_order_name"),_CP.rs.getString("customer_last_order_name"),"customer_last_order_name not Match in Row"+Iteration);
//					Assert.assertEquals(customer.get("marketing_opt_in_level"),_CP.rs.getString("customer_marketing_opt_in_level"),"customer_marketing_opt_in_level not Match in Row"+Iteration);
//					Assert.assertEquals(customer.get("multipass_identifier"),_CP.rs.getString("customer_multipass_identifier"),"customer_multipass_identifier not Match in Row"+Iteration);
//					Assert.assertEquals(customer.get("note"),_CP.rs.getString("customer_note"),"customer_note not Match in Row"+Iteration);
//int				Assert.assertEquals(customer.get("orders_count").toString(),_CP.rs.getString("customer_orders_count"),"customer_orders_count not Match in Row"+Iteration);
//null				Assert.assertEquals(customer.get("phone"),_CP.rs.getString("customer_phone"),"customer_phone not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("state"),_CP.rs.getString("customer_state"),"customer_state not Match in Row"+Iteration);
//null				Assert.assertEquals(customer.get("tags"),_CP.rs.getString("customer_tags"),"customer_tags not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("tax_exempt").toString(),_CP.rs.getString("customer_tax_exempt"),"customer_tax_exempt not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("total_spent"),_CP.rs.getString("customer_total_spent"),"customer_total_spent not Match in Row"+Iteration);
					Assert.assertEquals(customer.get("verified_email").toString(),_CP.rs.getString("customer_verified_email"),"customer_verified_email not Match in Row"+Iteration);														
																			

					Assert.assertEquals(OrderJsonData.get("customer_locale").toString(),_CP.rs.getString("customer_locale").toString(), "customer_locale not Match in Row" + Iteration);
//					Assert.assertEquals(OrderJsonData.get("device_id").toString(),_CP.rs.getString("device_id").toString(), "device_id not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("discount_applications").toString(),_CP.rs.getString("discount_applications").toString(), "discount_applications not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("discount_codes").toString(),_CP.rs.getString("discount_codes").toString(), "discount_codes not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("email").toString(),_CP.rs.getString("email").toString(), "email not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("financial_status").toString(),_CP.rs.getString("financial_status").toString(), "financial_status not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("fulfillment_status").toString(),_CP.rs.getString("fulfillment_status").toString(), "fulfillment_status not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("fulfillments").toString(),_CP.rs.getString("fulfillments").toString(), "fulfillments not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("gateway").toString(),_CP.rs.getString("gateway").toString(), "gateway not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("landing_site").toString(),_CP.rs.getString("landing_site").toString(), "landing_site not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("landing_site_ref").toString(),_CP.rs.getString("landing_site_ref").toString(), "landing_site_ref not Match in Row" + Iteration);

	   			    JsonArray = (JSONArray) OrderJsonData.get("line_items");
    				for (int i = 0; i < JsonArray.size(); i++) {
    				JsonArrayObject = (JSONObject) JsonArray.get(i);
	    			Assert.assertEquals(JsonArrayObject.get("discount_allocations").toString(),_CP.rs.getString("discount_allocations").toString(), "discount_allocations not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("fulfillable_quantity").toString(),_CP.rs.getString("fulfillable_quantity").toString(),	"fulfillable_quantity not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("fulfillment_service").toString(),_CP.rs.getString("fulfillment_service").toString(), "fulfillment_service not Match in Row" + Iteration);
//null    			Assert.assertEquals(JsonArrayObject.get("fulfillment_status").toString(),_CP.rs.getString("fulfillment_status").toString(), "fulfillment_status not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("gift_card").toString(),_CP.rs.getString("gift_card").toString(), "gift_card not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("grams").toString(),_CP.rs.getString("grams").toString(),"grams" + Iteration);
//	    			Assert.assertEquals(JsonArrayObject.get("name").toString(),_CP.rs.getString("name").toString(),"name not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("price_set").toString(),_CP.rs.getString("price_set").toString(),"price_set not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("price").toString(),_CP.rs.getString("price").toString(),"price not Match in Row" + Iteration);
//	    			Assert.assertEquals(JsonArrayObject.get("product_id").toString(),_CP.rs.getString("product_id").toString(),"product_id not Match in Row" + Iteration);
//	    			Assert.assertEquals(JsonArrayObject.get("properties").toString(),_CP.rs.getString("properties").toString(),"properties not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("quantity").toString(),_CP.rs.getString("quantity").toString(),"quantity not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("requires_shipping").toString(),_CP.rs.getString("requires_shipping").toString(),"quantity not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("sku").toString(),_CP.rs.getString("sku").toString(),"sku not Match in Row" + Iteration);
//	    			Assert.assertEquals(JsonArrayObject.get("tax_lines").toString(),_CP.rs.getString("tax_lines").toString(),"tax_lines not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("taxable").toString(),_CP.rs.getString("taxable").toString(),"taxable not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("title").toString(),_CP.rs.getString("title").toString(),"title not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("total_discount_set").toString(),_CP.rs.getString("total_discount_set").toString(),"total_discount_set not Match in Row" + Iteration);
//	    			Assert.assertEquals(JsonArrayObject.get("variant_id").toString(),_CP.rs.getString("variant_id").toString(),"variant_id not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("variant_title").toString(),_CP.rs.getString("variant_title").toString(),"variant_title not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("vendor").toString(),_CP.rs.getString("vendor").toString(),"vendor not Match in Row" + Iteration);
    				}
    				
//					Assert.assertEquals(OrderJsonData.get("location_id").toString(),_CP.rs.getString("location_id").toString(), "location_id not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("name").toString(),_CP.rs.getString("name").toString(), "name not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("note").toString(),_CP.rs.getString("note").toString(),"note not Match in Row" + Iteration);
//					Assert.assertEquals(OrderJsonData.get("note_attributes").toString(),_CP.rs.getString("note_attributes").toString(), "note_attributes not Match in Row" + Iteration);
//int				Assert.assertEquals(OrderJsonData.get("number").toString(),_CP.rs.getString("number").toString(),"number not Match in Row" + Iteration);
//int				Assert.assertEquals(OrderJsonData.get("order_number").toString(),_CP.rs.getString("order_number").toString(), "order_number not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("order_status_url").toString(),_CP.rs.getString("order_status_url").toString(), "order_status_url not Match in Row" + Iteration);
//checkp			Assert.assertEquals(OrderJsonData.get("payment_gateway_names").toString(),_CP.rs.getString("payment_gateway_names").toString(), "payment_gateway_names not Match in Row" + Iteration);
//					Assert.assertEquals(OrderJsonData.get("phone").toString(),_CP.rs.getString("phone").toString(), "phone not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("presentment_currency").toString(),_CP.rs.getString("presentment_currency").toString(), "presentment_currency not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("processing_method").toString(),_CP.rs.getString("processing_method").toString(), "processing_method not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("reference").toString(),_CP.rs.getString("reference").toString(), "reference not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("referring_site").toString(),_CP.rs.getString("referring_site").toString(), "referring_site not Match in Row" + Iteration);
//					Assert.assertEquals(OrderJsonData.get("refunds").toString(),_CP.rs.getString("refunds").toString(), "refunds not Match in Row" + Iteration);
					
					//Object under object handling
					JSONObject shipping_address = (JSONObject) OrderJsonData.get("shipping_address");
					Assert.assertEquals(shipping_address.get("address1"),_CP.rs.getString("shipping_address_address1"),"shipping_address_address1 not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("address2"),_CP.rs.getString("shipping_address_address2"),"shipping_address_address2 not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("city"),_CP.rs.getString("shipping_address_city"),"shipping_address_city not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("company"),_CP.rs.getString("shipping_address_company"),"shipping_address_company not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("country"),_CP.rs.getString("shipping_address_country"),"shipping_address_country not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("country_code"),_CP.rs.getString("shipping_address_country_code"),"shipping_address_country_code not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("first_name"),_CP.rs.getString("shipping_address_first_name"),"shipping_address_first_name not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("last_name"),_CP.rs.getString("shipping_address_last_name"),"shipping_address_last_name not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("latitude").toString(),_CP.rs.getString("shipping_address_latitude"),"shipping_address_latitude not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("longitude").toString(),_CP.rs.getString("shipping_address_longitude"),"shipping_address_longitude not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("name"),_CP.rs.getString("shipping_address_name"),"shipping_address_name not Match in Row"+Iteration);
//					Assert.assertEquals(shipping_address.get("phone"),_CP.rs.getString("shipping_address_phone"),"shipping_address_phone not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("province"),_CP.rs.getString("shipping_address_province"),"shipping_address_province not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("province_code"),_CP.rs.getString("shipping_address_province_code"),"shipping_address_province_code not Match in Row"+Iteration);
					Assert.assertEquals(shipping_address.get("zip"),_CP.rs.getString("shipping_address_zip"),"shipping_address_zip not Match in Row"+Iteration);
					
					Assert.assertEquals(OrderJsonData.get("shipping_lines").toString(),_CP.rs.getString("shipping_lines").toString(), "shipping_lines not Match in Row" + Iteration);

 					//Object under object handling
//					JSONObject shipping_line = (JSONObject) OrderJsonData.get("shipping_line");
//nullk				Assert.assertEquals(shipping_line.get("handle"),_CP.rs.getString("shipping_line_handle"),"shipping_line_handle not Match in Row"+Iteration);
//inthandle			Assert.assertEquals(shipping_line.get("price").toString(),_CP.rs.getString("shipping_line_price"),"shipping_line_price not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("title"),_CP.rs.getString("shipping_line_title"),"shipping_line_title not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("code"),_CP.rs.getString("shipping_line_code"),"shipping_line_code not Match in Row"+Iteration);
//nullk				Assert.assertEquals(shipping_line.get("phone"),_CP.rs.getString("shipping_line_phone"),"shipping_line_phone not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("requested_fulfillment_service_id"),_CP.rs.getString("shipping_line_requested_fulfillment_service_id"),"shipping_line_requested_fulfillment_service_id not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("delivery_category"),_CP.rs.getString("shipping_line_delivery_category"),"shipping_line_delivery_category not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("carrier_identifier"),_CP.rs.getString("shipping_line_carrier_identifier"),"shipping_line_carrier_identifier not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("discount_allocations"),_CP.rs.getString("shipping_line_discount_allocations"),"shipping_line_delivery_category not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("discounted_price"),_CP.rs.getString("shipping_line_discounted_price"),"shipping_line_delivery_category not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("discounted_price_set"),_CP.rs.getString("shipping_line_discounted_price_set"),"shipping_line_delivery_category not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("discounted_price"),_CP.rs.getString("shipping_line_discounted_price"),"shipping_line_delivery_category not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("price"),_CP.rs.getString("shipping_line_price"),"shipping_line_delivery_category not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("price_set"),_CP.rs.getString("shipping_line_price_set"),"shipping_line_delivery_category not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("source"),_CP.rs.getString("shipping_line_source"),"shipping_line_delivery_category not Match in Row"+Iteration);
//k					Assert.assertEquals(shipping_line.get("tax_lines"),_CP.rs.getString("shipping_line_tax_lines"),"shipping_line_delivery_category not Match in Row"+Iteration);


					Assert.assertEquals(OrderJsonData.get("source_identifier").toString(),_CP.rs.getString("source_identifier").toString(), "source_identifier not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("source_name").toString(),_CP.rs.getString("source_name").toString(), "source_name not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("source_url").toString(),_CP.rs.getString("source_url").toString(), "source_url not Match in Row" + Iteration);
				    Assert.assertEquals(OrderJsonData.get("subtotal_price").toString(),_CP.rs.getString("subtotal_price").toString(), "subtotal_price not Match in Row" + Iteration);
				    Assert.assertEquals(OrderJsonData.get("subtotal_price_set").toString(),_CP.rs.getString("subtotal_price_set").toString(), "subtotal_price not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("tags").toString(),_CP.rs.getString("tags").toString(), "tags not Match in Row" + Iteration);
//null				Assert.assertEquals(OrderJsonData.get("tax_lines").toString(),_CP.rs.getString("tax_lines").toString(), "tax_lines not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("taxes_included").toString(),_CP.rs.getString("taxes_included").toString(), "taxes_included not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("test").toString(),_CP.rs.getString("test").toString(), "test not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("token").toString(),_CP.rs.getString("token").toString(), "token not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_discounts").toString(),_CP.rs.getString("total_discounts").toString(), "total_discounts not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_discounts_set").toString(),_CP.rs.getString("total_discounts_set").toString(), "total_discounts not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_line_items_price").toString(),_CP.rs.getString("total_line_items_price").toString(), "total_line_items_price not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_line_items_price_set").toString(),_CP.rs.getString("total_line_items_price_set").toString(), "total_line_items_price_set not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_price_usd").toString(),_CP.rs.getString("total_price_usd").toString(), "total_price_usd not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_price").toString(),_CP.rs.getString("total_price").toString(), "total_price not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_price_set").toString(),_CP.rs.getString("total_price_set").toString(), "total_price_set not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_shipping_price_set").toString(),_CP.rs.getString("total_shipping_price_set").toString(), "total_shipping_price_set not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_tax").toString(),_CP.rs.getString("total_tax").toString(), "total_tax not Match in Row" + Iteration);
					Assert.assertEquals(OrderJsonData.get("total_tax_set").toString(),_CP.rs.getString("total_tax_set").toString(), "total_tax_set not Match in Row" + Iteration);
//int				Assert.assertEquals(OrderJsonData.get("total_weight").toString(),_CP.rs.getString("total_weight").toString(), "total_weight not Match in Row" + Iteration);

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