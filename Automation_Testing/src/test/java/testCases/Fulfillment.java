package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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


public class Fulfillment {
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
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", WH.Fulfillment);
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
	public void VerifyFullfillmentData() throws IOException, InterruptedException, SQLException, ParseException {
	System.out.println("<-------------Fullfillment Verification Started------------->");
		for (Object record : JsonRecords)
		{	

         	Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			JSONObject JsonArrayObject;
			JSONArray JsonArray;
         	//map.put("external_id", new Date().getTime());
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			JSONObject FufillmentJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Fulfillment);
			TimeUnit.SECONDS.sleep(40);
			_CP = DB.GetRecordFromDB(FufillmentJsonData,"Fulfillment");
			if (_CP.rs.next()) {
				try {					
					Assert.assertEquals(FufillmentJsonData.get("admin_graphql_api_id").toString(),_CP.rs.getString("admin_graphql_api_id").toString(), "admin_graphql_api_id not Match in Row" + Iteration);
					//Object under object handling
					JSONObject destination = (JSONObject) FufillmentJsonData.get("destination");
					Assert.assertEquals(destination.get("address1").toString(),_CP.rs.getString("destination_address1"),"destination_address1 not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("address2").toString(),_CP.rs.getString("destination_address2"),"destination_address2 not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("city").toString(),_CP.rs.getString("destination_city"),"destination_city not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("company").toString(),_CP.rs.getString("destination_company"),"destination_company not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("country").toString(),_CP.rs.getString("destination_country"),"destination_country not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("country_code").toString(),_CP.rs.getString("destination_country_code"),"destination_country_code not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("first_name").toString(),_CP.rs.getString("destination_first_name"),"destination_first_name not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("last_name").toString(),_CP.rs.getString("destination_last_name"),"destination_last_name not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("latitude").toString(),_CP.rs.getString("destination_latitude"),"destination_latitude not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("longitude").toString(),_CP.rs.getString("destination_longitude"),"destination_longitude not Match in Row"+Iteration);
					
					Assert.assertEquals(destination.get("name").toString(),_CP.rs.getString("destination_name"),"destination_name not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("phone").toString(),_CP.rs.getString("destination_phone"),"destination_phone not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("province").toString(),_CP.rs.getString("destination_province"),"destination_province not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("province_code").toString(),_CP.rs.getString("destination_province_code"),"destination_province_code not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("zip").toString(),_CP.rs.getString("destination_zip"),"destination_zip not Match in Row"+Iteration);
					
					Assert.assertEquals(FufillmentJsonData.get("email").toString(),_CP.rs.getString("email").toString(),"email not Match in Row" + Iteration);

    			    JsonArray = (JSONArray) FufillmentJsonData.get("line_items");
    				for (int i = 0; i < JsonArray.size(); i++) {
    				JsonArrayObject = (JSONObject) JsonArray.get(i);
//    				Assert.assertEquals(JsonArrayObject.get("admin_graphql_api_id").toString(),_CP.rs.getString("admin_graphql_api_id").toString(), "admin_graphql_api_id not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("discount_allocations").toString(),_CP.rs.getString("discount_allocations").toString(), "discount_allocations not Match in Row" + Iteration);    				
//int    			Assert.assertEquals(JsonArrayObject.get("fulfillable_quantity").toString(),_CP.rs.getString("fulfillable_quantity").toString(),	"fulfillable_quantity not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("fulfillment_status").toString(),_CP.rs.getString("fulfillment_status").toString(),	"fulfillment_status not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("fulfillment_service").toString(),_CP.rs.getString("fulfillment_service").toString(), "fulfillment_service not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("gift_card").toString(),_CP.rs.getString("gift_card").toString(), "gift_card not Match in Row" + Iteration);
//int    			Assert.assertEquals(JsonArrayObject.get("grams").toString(),_CP.rs.getString("grams").toString(),"grams" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("name").toString(),_CP.rs.getString("line_name").toString(),"name not Match in Row" + Iteration);
 
//askv   			Assert.assertEquals(JsonArrayObject.get("pre_tax_price").toString(),_CP.rs.getString("pre_tax_price").toString(),"pre_tax_price not Match in Row" + Iteration);
//askv    			Assert.assertEquals(JsonArrayObject.get("pre_tax_price_set").toString(),_CP.rs.getString("pre_tax_price_set").toString(),"pre_tax_price_set not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("price").toString(),_CP.rs.getString("price").toString(),"price not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("price_set").toString(),_CP.rs.getString("price_set").toString(), "price_set not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("product_exists").toString(),_CP.rs.getString("product_exists").toString(),"product_exists not Match in Row" + Iteration);

//    				Assert.assertEquals(JsonArrayObject.get("product_id").toString(),_CP.rs.getString("product_id").toString(),"product_id not Match in Row" + Iteration);
//[]handle    		Assert.assertEquals(JsonArrayObject.get("properties").toString(),_CP.rs.getString("properties").toString(),"properties not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("quantity").toString(),_CP.rs.getString("quantity").toString(),"quantity not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("requires_shipping").toString(),_CP.rs.getString("requires_shipping").toString(),"quantity not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("sku").toString(),_CP.rs.getString("sku").toString(),"sku not Match in Row" + Iteration);
//askv    			Assert.assertEquals(JsonArrayObject.get("tax_code").toString(),_CP.rs.getString("tax_code").toString(),"tax_code not Match in Row" + Iteration);
//[]handle    		Assert.assertEquals(JsonArrayObject.get("tax_lines").toString(),_CP.rs.getString("tax_lines").toString(),"tax_lines not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("taxable").toString(),_CP.rs.getString("taxable").toString(),"taxable not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("title").toString(),_CP.rs.getString("title").toString(),"title not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("total_discount").toString(),_CP.rs.getString("total_discount").toString(),"total_discount not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("total_discount_set").toString(),_CP.rs.getString("total_discount_set").toString(),"total_discount_set not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("variant_id").toString(),_CP.rs.getString("variant_id").toString(),"variant_id not Match in Row" + Iteration);
//null    			Assert.assertEquals(JsonArrayObject.get("variant_inventory_management").toString(),_CP.rs.getString("variant_inventory_management").toString(),"variant_inventory_management not Match in Row" + Iteration);
//null    			Assert.assertEquals(JsonArrayObject.get("variant_title").toString(),_CP.rs.getString("variant_title").toString(),"variant_title not Match in Row" + Iteration);
    			Assert.assertEquals(JsonArrayObject.get("vendor").toString(),_CP.rs.getString("vendor").toString(),"vendor not Match in Row" + Iteration);
    				}

//					Assert.assertEquals(FufillmentJsonData.get("location_id").toString(),_CP.rs.getString("location_id").toString(),"location_id not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("name").toString(),_CP.rs.getString("name").toString(),"name not Match in Row" + Iteration);
//					Assert.assertEquals(FufillmentJsonData.get("order_id").toString(),_CP.rs.getString("order_id").toString(),"order_id not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("receipt").toString(),_CP.rs.getString("receipt").toString(),"receipt not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("service").toString(),_CP.rs.getString("service").toString(),"service not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("shipment_status").toString(),_CP.rs.getString("shipment_status").toString(),"shipment_status not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("tracking_company").toString(),_CP.rs.getString("tracking_company").toString(),"tracking_company not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("tracking_number").toString(),_CP.rs.getString("tracking_number").toString(),"tracking_number not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("tracking_numbers").toString(),_CP.rs.getString("tracking_numbers").toString(),"tracking_numbers not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("tracking_url").toString(),_CP.rs.getString("tracking_url").toString(),"tracking_url not Match in Row" + Iteration);
//same[]			Assert.assertEquals(FufillmentJsonData.get("tracking_urls").toString(),_CP.rs.getString("tracking_urls").toString(),"tracking_urls not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("variant_inventory_management").toString(),_CP.rs.getString("variant_inventory_management").toString(),"variant_inventory_managements not Match in Row" + Iteration);

				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Fulfillment);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,WH.Fulfillment);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration, WH.Fulfillment);
			}

		}
		System.out.println("<------------- Fullfillment Verification Started ------------->");

	}

}