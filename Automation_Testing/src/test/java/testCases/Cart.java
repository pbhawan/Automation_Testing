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


public class Cart {
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
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", WH.Cart);
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
			//map.put("external_id", new Date().getTime());
			map.put("id", new Date().getTime());
			Map<String, Date> map1 = mapper.readValue(mapper.writeValueAsString(map), Map.class);
			map1.put("updated_at", new Date());
			map1.put("created_at", new Date());
			JSONObject CartJsonData = HC.PostJson(mapper.writeValueAsString(map1), WH.Cart);
			TimeUnit.SECONDS.sleep(10);
			
			_CP = DB.GetRecordFromDB(CartJsonData,"Cart");
			if (_CP.rs.next()) {
				try {					
					Assert.assertEquals(CartJsonData.get("token").toString(),
							_CP.rs.getString("token").toString(), "token not Match in Row" + Iteration);
					
					Assert.assertEquals(CartJsonData.get("note").toString(),
							_CP.rs.getString("note").toString(), "note not Match in Row" + Iteration);
										
//					Assert.assertEquals(OrderJsonData.get("line_items").toString(),
//datevalue							_CP.rs.getString("line_items").toString(), "line_items not Match in Row" + Iteration);
					
					
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Cart);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,WH.Cart);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration, WH.Cart);
			}

		}
	}

}