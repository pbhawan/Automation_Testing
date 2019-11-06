package testCases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.viome.components.ExcelToJSONConvertor;
import com.viome.components.HTTPConnection;
import com.viome.enums.webhooks;
import com.viome.utilites.DataProviderClass;

/* git changes commit - PB1*/

public class Customer {
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
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls", WH.Customer);
		ConnectionProperties _CP = DB.DBConnection();
	}

	@AfterTest
	public void Teardown() throws SQLException {

		_CP.rs.close();
		_CP.conn.close();
		_CP.stmt.close();

	}
	
	@Test
	public void VerifyCustomerData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	@SuppressWarnings("unchecked")
			Map<String, Long> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());
			JSONObject CustomerJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Customer);
			TimeUnit.SECONDS.sleep(30);
			_CP = DB.GetRecordFromDB(CustomerJsonData,"Customer");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(CustomerJsonData.get("first_name").toString(),
							_CP.rs.getString("first_name").toString(), "First Name not Match in Row" + Iteration);
					Assert.assertEquals(CustomerJsonData.get("last_name").toString(),
							_CP.rs.getString("last_name").toString(), "Last Name not Match in Row" + Iteration);
					Assert.assertEquals(CustomerJsonData.get("email").toString(), _CP.rs.getString("email").toString(),
							"Email not Match in Row" + Iteration);
					Assert.assertEquals(CustomerJsonData.get("accepts_marketing").toString(),
							_CP.rs.getString("accepts_marketing").toString(),
							"accepts_marketing not Match in Row" + Iteration);
					Assert.assertEquals(CustomerJsonData.get("orders_count").toString(),
							_CP.rs.getString("orders_count").toString(), "orders_count not Match in Row" + Iteration);

					/*
					 * Assert.assertEquals(CustomerJsonData.get("total_spent").toString(),
					 * _CP.rs.getString("total_spent").toString(), "total_spent not Match in Row" +
					 * Iteration);
					 * Assert.assertEquals(CustomerJsonData.get("last_order_id").toString(),
					 * _CP.rs.getString("last_order_id").toString(),
					 * "last_order_id not Match in Row" + Iteration);
					 */
					Assert.assertEquals(CustomerJsonData.get("note").toString(), _CP.rs.getString("note").toString(),
							"note not Match in Row" + Iteration);
					Assert.assertEquals(CustomerJsonData.get("verified_email"),
							_CP.rs.getString("verified_email"),
							"verified_email not Match in Row" + Iteration);
					// Assert.assertEquals(CustomerJsonData.get("multipass_identifier").toString(),_CP.rs.getString("multipass_identifier").toString(),
					// "multipass_identifier not Match in Row"+Iteration);
					// Assert.assertEquals(CustomerJsonData.get("tax_exempt").toString(),_CP.rs.getString("tax_exempt").toString(),
					// "tax_exempt not Match in Row"+Iteration);
					// Assert.assertEquals(CustomerJsonData.get("phone").toString(),
					// _CP.rs.getString("phone").toString(), "phone not Match in Row"+Iteration);
					Assert.assertEquals(CustomerJsonData.get("tags").toString(), _CP.rs.getString("tags").toString(),
							"tags not Match in Row" + Iteration);
					// Assert.assertEquals(CustomerJsonData.get("last_order_name").toString(),_CP.rs.getString("last_order_name").toString(),"last_order_name
					// not Match in Row"+Iteration);
					Assert.assertEquals(CustomerJsonData.get("currency").toString(),
							_CP.rs.getString("currency").toString(), "currency not Match in Row" + Iteration);
					// Assert.assertEquals(CustomerJsonData.get("accepts_marketing_updated_at").toString(),_CP.rs.getString("accepts_marketing_updated_at").toString(),
					// "accepts_marketing_updated_at not Match in Row"+Iteration);
					// Assert.assertEquals(CustomerJsonData.get("marketing_opt_in_level").toString(),_CP.rs.getString("marketing_opt_in_level").toString(),"marketing_opt_in_level
					// not Match in Row"+Iteration);

				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Customer);
					continue;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration, WH.Customer);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration, WH.Customer);
			}

		}
	}

}