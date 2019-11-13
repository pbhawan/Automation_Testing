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
	public void VerifyProductData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	Map<String, Long> map = mapper.readValue(record.toString(), Map.class);
			map.put("external_id", new Date().getTime());
			JSONObject DraftOrderJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.DraftOrder);
			TimeUnit.SECONDS.sleep(30);
			
			_CP = DB.GetRecordFromDB(DraftOrderJsonData,"DraftOrder");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(DraftOrderJsonData.get("note").toString(),
							_CP.rs.getString("note").toString(), "note not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("email").toString(),
							_CP.rs.getString("email").toString(), "email not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("taxes_included").toString(), 
							_CP.rs.getString("taxes_included").toString(),"taxes_included not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("currency").toString(),
							_CP.rs.getString("currency").toString(),"currency not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("invoice_sent_at").toString(),
							_CP.rs.getString("invoice_sent_at").toString(), "invoice_sent_at not Match in Row" + Iteration);
									
					Assert.assertEquals(DraftOrderJsonData.get("tax_exempt").toString(),
							_CP.rs.getString("tax_exempt").toString(), "tax_exempt not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("name").toString(),
							_CP.rs.getString("name").toString(), "name not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("invoice_url").toString(),
							_CP.rs.getString("invoice_url").toString(), "invoice_url not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("tax_lines").toString(),
							_CP.rs.getString("tax_lines").toString(), "tax_lines not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("tags").toString(),
							_CP.rs.getString("tags").toString(), "tags not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("total_price").toString(),
							_CP.rs.getString("total_price").toString(), "total_price not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("subtotal_price").toString(),
							_CP.rs.getString("subtotal_price").toString(), "subtotal_price not Match in Row" + Iteration);
					
					Assert.assertEquals(DraftOrderJsonData.get("total_tax").toString(),
							_CP.rs.getString("total_tax").toString(), "total_tax not Match in Row" + Iteration);
										
					Assert.assertEquals(DraftOrderJsonData.get("admin_graphql_api_id").toString(),
							_CP.rs.getString("admin_graphql_api_id").toString(), "admin_graphql_api_id not Match in Row" + Iteration);					

					
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