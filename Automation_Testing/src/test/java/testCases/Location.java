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



public class Location {
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
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", WH.Location);
		@SuppressWarnings("unused")
		ConnectionProperties _CP = DB.DBConnection();
	}

	@AfterTest
	public void Teardown() throws SQLException {

		_CP.rs.close();
		_CP.conn.close();
		_CP.stmt.close();

	}
	
	@Test
	public void VerifyLocationData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());
			map.put("updated_at", new Date());
			map.put("created_at", new Date());
			@SuppressWarnings("static-access")
			JSONObject LocationJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Location);
			TimeUnit.SECONDS.sleep(30);
			_CP = DB.GetRecordFromDB(LocationJsonData,"Location");
			if (_CP.rs.next()) {
				try {

					Assert.assertEquals(LocationJsonData.get("name").toString(),_CP.rs.getString("name").toString(), "Name not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("address1").toString(),_CP.rs.getString("address1").toString(), "Address1 not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("address2").toString(), _CP.rs.getString("address2").toString(),"Address2 not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("city").toString(),_CP.rs.getString("city").toString(),"city not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("zip").toString(),	_CP.rs.getString("zip").toString(), "zip not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("province").toString(),_CP.rs.getString("province").toString(), "province not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("country").toString(),	_CP.rs.getString("country").toString(), "country not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("phone").toString(),_CP.rs.getString("phone").toString(), "phone not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("country_code").toString(),_CP.rs.getString("country_code").toString(), "country_code not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("country_name").toString(),_CP.rs.getString("country_name").toString(), "country_name not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("province_code").toString(), _CP.rs.getString("province_code").toString(),	"province_code not Match in Row" + Iteration);				
					Assert.assertEquals(LocationJsonData.get("active").toString(),	_CP.rs.getString("active").toString(), "active not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("legacy").toString(),	_CP.rs.getString("legacy").toString(),"legacy not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("admin_graphql_api_id").toString(),_CP.rs.getString("admin_graphql_api_id").toString(), "admin_graphql_api_id not Match in Row" + Iteration);
					

				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Location);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,  WH.Location);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration,  WH.Location);
			}

		}
	}

}