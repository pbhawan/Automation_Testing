package testCases;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
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

		for (Object record : JsonRecords)
		{		
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
			@SuppressWarnings("static-access")
			JSONObject ProductJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Product);
			TimeUnit.SECONDS.sleep(30);
			
			_CP = DB.GetRecordFromDB(ProductJsonData,"Product");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(ProductJsonData.get("body_html").toString(),
							_CP.rs.getString("body_html").toString(), "body_html not Match in Row" + Iteration);
					
					Assert.assertEquals(ProductJsonData.get("handle").toString(),
							_CP.rs.getString("handle").toString(), "handle not Match in Row" + Iteration);
					
//					Assert.assertEquals(ProductJsonData.get("images").toString(), 
//jsonobj							_CP.rs.getString("images").toString(),"images not Match in Row" + Iteration);
					
//jsonobj					Assert.assertEquals(ProductJsonData.get("options").toString(),
//							_CP.rs.getString("options").toString(),"options not Match in Row" + Iteration);
					
					Assert.assertEquals(ProductJsonData.get("product_type").toString(),
							_CP.rs.getString("product_type").toString(), "product_type not Match in Row" + Iteration);
					
				    Assert.assertEquals(ProductJsonData.get("published_scope").toString(),
							_CP.rs.getString("published_scope").toString(), "published_scope not Match in Row" + Iteration);
				
					Assert.assertEquals(ProductJsonData.get("tags").toString(),
							_CP.rs.getString("tags").toString(), "tags not Match in Row" + Iteration);
					
					Assert.assertEquals(ProductJsonData.get("template_suffix").toString(),
							_CP.rs.getString("template_suffix").toString(), "template_suffix not Match in Row" + Iteration);
					
					Assert.assertEquals(ProductJsonData.get("title").toString(),
							_CP.rs.getString("title").toString(), "title not Match in Row" + Iteration);
					
//					Assert.assertEquals(ProductJsonData.get("variants").toString(),
//jsonobj							_CP.rs.getString("variants").toString(), "variants not Match in Row" + Iteration);
					
					Assert.assertEquals(ProductJsonData.get("vendor").toString(),
							_CP.rs.getString("vendor").toString(), "vendor not Match in Row" + Iteration);
					
										
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
	}

}