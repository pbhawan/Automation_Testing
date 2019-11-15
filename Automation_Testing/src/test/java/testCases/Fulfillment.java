package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

		for (Object record : JsonRecords)
		{		
         	Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			//map.put("external_id", new Date().getTime());
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			JSONObject FufillmentJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Fulfillment);
			TimeUnit.SECONDS.sleep(10);
			_CP = DB.GetRecordFromDB(FufillmentJsonData,"Fulfillment");
			if (_CP.rs.next()) {
				try {					
					Assert.assertEquals(FufillmentJsonData.get("admin_graphql_api_id").toString(),_CP.rs.getString("admin_graphql_api_id").toString(), "admin_graphql_api_id not Match in Row" + Iteration);
					//Object under object handling
					JSONObject destination = (JSONObject) FufillmentJsonData.get("destination");
					Assert.assertEquals(destination.get("address1"),_CP.rs.getString("destination_address1"),"destination_address1 not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("address2"),_CP.rs.getString("destination_address2"),"destination_address2 not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("city"),_CP.rs.getString("destination_city"),"destination_city not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("company"),_CP.rs.getString("destination_company"),"destination_company not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("country"),_CP.rs.getString("destination_country"),"destination_country not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("country_code"),_CP.rs.getString("destination_country_code"),"destination_country_code not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("first_name"),_CP.rs.getString("destination_first_name"),"destination_first_name not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("last_name"),_CP.rs.getString("destination_last_name"),"destination_last_name not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("latitude").toString(),_CP.rs.getString("destination_latitude"),"destination_latitude not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("longitude").toString(),_CP.rs.getString("destination_longitude"),"destination_longitude not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("name"),_CP.rs.getString("destination_name"),"destination_name not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("phone"),_CP.rs.getString("destination_phone"),"destination_phone not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("province"),_CP.rs.getString("destination_province"),"destination_province not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("province_code"),_CP.rs.getString("destination_province_code"),"destination_province_code not Match in Row"+Iteration);
					Assert.assertEquals(destination.get("zip"),_CP.rs.getString("destination_zip"),"destination_zip not Match in Row"+Iteration);
					Assert.assertEquals(FufillmentJsonData.get("email").toString(),_CP.rs.getString("email").toString(),"email not Match in Row" + Iteration);
//json array	 	Assert.assertEquals(FufillmentJsonData.get("line_items").toString(),_CP.rs.getString("line_items").toString(),"line_items not Match in Row" + Iteration);
//					Assert.assertEquals(FufillmentJsonData.get("location_id").toString(),_CP.rs.getString("location_id").toString(),"location_id not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("name").toString(),_CP.rs.getString("name").toString(),"name not Match in Row" + Iteration);
//					Assert.assertEquals(FufillmentJsonData.get("order_id").toString(),_CP.rs.getString("order_id").toString(),"order_id not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("receipt").toString(),_CP.rs.getString("receipt").toString(),"receipt not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("service").toString(),_CP.rs.getString("service").toString(),"service not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("shipment_status").toString(),_CP.rs.getString("shipment_status").toString(),"shipment_status not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("tracking_company").toString(),_CP.rs.getString("tracking_company").toString(),"tracking_company not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("tracking_number").toString(),_CP.rs.getString("tracking_number").toString(),"tracking_number not Match in Row" + Iteration);
//json array		Assert.assertEquals(FufillmentJsonData.get("tracking_numbers").toString(),_CP.rs.getString("tracking_numbers").toString(),"tracking_numbers not Match in Row" + Iteration);
					Assert.assertEquals(FufillmentJsonData.get("tracking_url").toString(),_CP.rs.getString("tracking_url").toString(),"tracking_url not Match in Row" + Iteration);
//json array		Assert.assertEquals(FufillmentJsonData.get("tracking_urls").toString(),_CP.rs.getString("tracking_urls").toString(),"tracking_urls not Match in Row" + Iteration);
					
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
	}

}