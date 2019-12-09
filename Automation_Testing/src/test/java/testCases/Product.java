package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
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



public class Product {
	HTTPConnection HC;
	DBConnection DB;
	ResultSet rs;
	ConnectionProperties _CP;
	ExcelToJSONConvertor EJ;
	webhooks WH;


	private static ObjectMapper mapper = new ObjectMapper();
	List<Object> JsonRecords;
	int Iteration = 0;

	@SuppressWarnings("static-access")
	@BeforeTest
	public void Setup() throws Exception {
			
		HC = new HTTPConnection();
		DB = new DBConnection();
		EJ = new ExcelToJSONConvertor();
		//CJ = new CSVFileHandling();	
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", WH.Product);
		@SuppressWarnings("unused")
		ConnectionProperties _CP = DB.DBConnection();
	}

	@AfterTest
	public void Teardown() throws SQLException {

		_CP.rs.close();
		_CP.conn.close();
		_CP.stmt.close();

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	@Test
	public void VerifyProductData() throws IOException, InterruptedException, SQLException, ParseException {
		System.out.println("<------------- Product Verification Started ------------->");

		for (Object record : JsonRecords)
		{		
			JSONObject JsonArrayObject;
			JSONArray JsonArray;
         	Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			long id = new Date().getTime();
			map.put("id", id);
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			if (null != map.get("variants")) {
			List<Map> variants = (List<Map>) map.get("variants");
		for (Map m : variants) {
			m.put("product_id" ,id);
			m.put("id",  new Date().getTime());
		}
			}
			if (null != map.get("variants")) {
				List<Map> options = (List<Map>) map.get("options");
			for (Map m : options) {
				m.put("product_id" ,id);
				m.put("id",  new Date().getTime());
			}
				}
			
			if (null != map.get("variants")) {
				List<Map> images = (List<Map>) map.get("images");
			for (Map m : images) {
				m.put("product_id" ,id);
				m.put("id",  new Date().getTime());
			}
				}	
			JSONObject ProductJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Product);
			TimeUnit.SECONDS.sleep(30);
			_CP = DB.GetRecordFromDB(ProductJsonData,"Product");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(ProductJsonData.get("body_html").toString(),_CP.rs.getString("body_html").toString(), "body_html not Match in Row" + Iteration);
					Assert.assertEquals(ProductJsonData.get("handle").toString(),_CP.rs.getString("handle").toString(), "handle not Match in Row" + Iteration);
					
	   			    JsonArray = (JSONArray) ProductJsonData.get("images");
    				for (int i = 0; i < JsonArray.size(); i++) {
    				JsonArrayObject = (JSONObject) JsonArray.get(i);
//inthand    		Assert.assertEquals(JsonArrayObject.get("height").toString(),_CP.rs.getString("height").toString(),"height not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("id").toString(),_CP.rs.getString("id").toString(), "id not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("position").toString(),_CP.rs.getString("position").toString(), "position not Match in Row" + Iteration);
//   				Assert.assertEquals(JsonArrayObject.get("product_id").toString(),_CP.rs.getString("product_id").toString(), "product_id not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("src").toString(),_CP.rs.getString("src").toString(),"src" + Iteration);
//null	    		Assert.assertEquals(JsonArrayObject.get("variant_ids").toString(),_CP.rs.getString("variant_ids").toString(),"variant_ids not Match in Row" + Iteration);
//int handle	    Assert.assertEquals(JsonArrayObject.get("width").toString(),_CP.rs.getString("width").toString(),"width not Match in Row" + Iteration);
    				}
					
	   			    JsonArray = (JSONArray) ProductJsonData.get("options");
    				for (int i = 0; i < JsonArray.size(); i++) {
    				JsonArrayObject = (JSONObject) JsonArray.get(i);
//	    			Assert.assertEquals(JsonArrayObject.get("id").toString(),_CP.rs.getString("id").toString(), "id not Match in Row" + Iteration);
System.out.println(JsonArrayObject.get("name"));
//    				Assert.assertEquals(JsonArrayObject.get("name").toString(),_CP.rs.getString("name").toString(),"name not Match in Row" + Iteration);
//    				Assert.assertEquals(JsonArrayObject.get("product_id").toString(),_CP.rs.getString("product_id").toString(), "product_id not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("position").toString(),_CP.rs.getString("position").toString(), "position not Match in Row" + Iteration);    				
    				}
					
					Assert.assertEquals(ProductJsonData.get("product_type").toString(),_CP.rs.getString("product_type").toString(), "product_type not Match in Row" + Iteration);
				    Assert.assertEquals(ProductJsonData.get("published_scope").toString(),_CP.rs.getString("published_scope").toString(), "published_scope not Match in Row" + Iteration);
					Assert.assertEquals(ProductJsonData.get("tags").toString(),_CP.rs.getString("tags").toString(), "tags not Match in Row" + Iteration);
					Assert.assertEquals(ProductJsonData.get("template_suffix").toString(),_CP.rs.getString("template_suffix").toString(), "template_suffix not Match in Row" + Iteration);
					Assert.assertEquals(ProductJsonData.get("title").toString(),_CP.rs.getString("product_title").toString(), "title not Match in Row" + Iteration);
					
	   			    JsonArray = (JSONArray) ProductJsonData.get("variants");
    				for (int i = 0; i < JsonArray.size(); i++) {
    				JsonArrayObject = (JSONObject) JsonArray.get(i);
//	    			Assert.assertEquals(JsonArrayObject.get("product_id").toString(),_CP.rs.getString("product_id").toString(), "product_id not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("title").toString(),_CP.rs.getString("title").toString(),"title not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("price").toString(),_CP.rs.getString("price").toString(), "price not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("sku").toString(),_CP.rs.getString("sku").toString(), "sku not Match in Row" + Iteration);
   				    Assert.assertEquals(JsonArrayObject.get("position").toString(),_CP.rs.getString("position").toString(), "position not Match in Row" + Iteration);
    				Assert.assertEquals(JsonArrayObject.get("inventory_policy").toString(),_CP.rs.getString("inventory_policy").toString(),"inventory_policy" + Iteration);
	    			Assert.assertEquals(JsonArrayObject.get("compare_at_price").toString(),_CP.rs.getString("compare_at_price").toString(),"compare_at_price not Match in Row" + Iteration);
	    			Assert.assertEquals(JsonArrayObject.get("fulfillment_service").toString(),_CP.rs.getString("fulfillment_service").toString(),"fulfillment_service not Match in Row" + Iteration);
	    			Assert.assertEquals(JsonArrayObject.get("inventory_management").toString(),_CP.rs.getString("inventory_management").toString(),"inventory_management not Match in Row" + Iteration);
	    			Assert.assertEquals(JsonArrayObject.get("option1").toString(),_CP.rs.getString("option1").toString(),"option1 not Match in Row" + Iteration);
//	    			Assert.assertEquals(JsonArrayObject.get("option2").toString(),_CP.rs.getString("option2").toString(),"option2 not Match in Row" + Iteration);
//	    			Assert.assertEquals(JsonArrayObject.get("option3").toString(),_CP.rs.getString("option3").toString(),"option3 not Match in Row" + Iteration);
	    			Assert.assertEquals(JsonArrayObject.get("barcode").toString(),_CP.rs.getString("barcode").toString(),"barcode not Match in Row" + Iteration);
//int	    		Assert.assertEquals(JsonArrayObject.get("grams").toString(),_CP.rs.getString("grams").toString(),"grams not Match in Row" + Iteration);
//	    			Assert.assertEquals(JsonArrayObject.get("image_id").toString(),_CP.rs.getString("image_id").toString(),"image_id not Match in Row" + Iteration);
	    			Assert.assertEquals(JsonArrayObject.get("weight_unit").toString(),_CP.rs.getString("weight_unit").toString(),"weight_unit not Match in Row" + Iteration);
	    			Assert.assertEquals(JsonArrayObject.get("inventory_item_id").toString(),_CP.rs.getString("inventory_item_id").toString(),"inventory_item_id not Match in Row" + Iteration);
//int	    		Assert.assertEquals(JsonArrayObject.get("inventory_quantity").toString(),_CP.rs.getString("inventory_quantity").toString(),"inventory_quantity not Match in Row" + Iteration);
//int	    		Assert.assertEquals(JsonArrayObject.get("old_inventory_quantity").toString(),_CP.rs.getString("old_inventory_quantity").toString(),"old_inventory_quantity not Match in Row" + Iteration);
	    			Assert.assertEquals(JsonArrayObject.get("requires_shipping").toString(),_CP.rs.getString("requires_shipping").toString(),"requires_shipping not Match in Row" + Iteration);
	    			Assert.assertEquals(JsonArrayObject.get("inventory_quantity").toString(),_CP.rs.getString("inventory_quantity").toString(),"inventory_quantity not Match in Row" + Iteration);
    				}
					Assert.assertEquals(ProductJsonData.get("vendor").toString(),_CP.rs.getString("vendor").toString(), "vendor not Match in Row" + Iteration);
					
										
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Product);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,WH.Product);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration, WH.Product);
			}

		}
		System.out.println("<------------- Product Verification Ended ------------->");

	}

}