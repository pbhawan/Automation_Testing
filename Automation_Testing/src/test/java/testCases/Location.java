package testCases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.viome.components.ConnectionProperties;
import com.viome.components.DBConnection;
import com.viome.components.DataProviderClass;
import com.viome.components.ExcelToJSONConvertor;
import com.viome.components.HTTPConnection;



public class Location {
	HTTPConnection HC;
	DBConnection DB;
	ResultSet rs;
	ConnectionProperties _CP;
	ExcelToJSONConvertor EJ;


	private static ObjectMapper mapper = new ObjectMapper();
	List<String> JsonRecords;
	int Iteration = 0;

	@BeforeTest
	public void Setup() throws Exception {
			
		HC = new HTTPConnection();
		DB = new DBConnection();
		EJ = new ExcelToJSONConvertor();
		//CJ = new CSVFileHandling();	
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", "Location");
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

		for (String record : JsonRecords)
		{		
         	@SuppressWarnings("unchecked")
			Map<String, Long> map = mapper.readValue(record, Map.class);
			map.put("id", new Date().getTime());
			JSONObject LocationJsonData = HC.PostJson(mapper.writeValueAsString(map), "Location");
			TimeUnit.SECONDS.sleep(30);
			_CP = DB.GetRecordFromDB(LocationJsonData,"Location");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(LocationJsonData.get("first_name").toString(),
							_CP.rs.getString("first_name").toString(), "First Name not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("last_name").toString(),
							_CP.rs.getString("last_name").toString(), "Last Name not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("email").toString(), _CP.rs.getString("email").toString(),
							"Email not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("accepts_marketing").toString(),
							_CP.rs.getString("accepts_marketing").toString(),
							"accepts_marketing not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("orders_count").toString(),
							_CP.rs.getString("orders_count").toString(), "orders_count not Match in Row" + Iteration);
					/*
					 * Assert.assertEquals(CustomerJsonData.get("total_spent").toString(),
					 * _CP.rs.getString("total_spent").toString(), "total_spent not Match in Row" +
					 * Iteration);
					 * Assert.assertEquals(CustomerJsonData.get("last_order_id").toString(),
					 * _CP.rs.getString("last_order_id").toString(),
					 * "last_order_id not Match in Row" + Iteration);
					 */
					Assert.assertEquals(LocationJsonData.get("note").toString(), _CP.rs.getString("note").toString(),
							"note not Match in Row" + Iteration);
					Assert.assertEquals(LocationJsonData.get("verified_email").toString(),
							_CP.rs.getString("verified_email").toString(),
							"verified_email not Match in Row" + Iteration);
					// Assert.assertEquals(CustomerJsonData.get("multipass_identifier").toString(),_CP.rs.getString("multipass_identifier").toString(),
					// "multipass_identifier not Match in Row"+Iteration);
					// Assert.assertEquals(CustomerJsonData.get("tax_exempt").toString(),_CP.rs.getString("tax_exempt").toString(),
					// "tax_exempt not Match in Row"+Iteration);
					// Assert.assertEquals(CustomerJsonData.get("phone").toString(),
					// _CP.rs.getString("phone").toString(), "phone not Match in Row"+Iteration);
					Assert.assertEquals(LocationJsonData.get("tags").toString(), _CP.rs.getString("tags").toString(),
							"tags not Match in Row" + Iteration);
					// Assert.assertEquals(CustomerJsonData.get("last_order_name").toString(),_CP.rs.getString("last_order_name").toString(),"last_order_name
					// not Match in Row"+Iteration);
					Assert.assertEquals(LocationJsonData.get("currency").toString(),
							_CP.rs.getString("currency").toString(), "currency not Match in Row" + Iteration);
					// Assert.assertEquals(CustomerJsonData.get("accepts_marketing_updated_at").toString(),_CP.rs.getString("accepts_marketing_updated_at").toString(),
					// "accepts_marketing_updated_at not Match in Row"+Iteration);
					// Assert.assertEquals(CustomerJsonData.get("marketing_opt_in_level").toString(),_CP.rs.getString("marketing_opt_in_level").toString(),"marketing_opt_in_level
					// not Match in Row"+Iteration);

				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration);
			}

		}
	}

}