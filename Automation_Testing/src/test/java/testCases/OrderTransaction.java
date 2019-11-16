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

public class OrderTransaction {
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
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls",WH.Order_Transaction);

		@SuppressWarnings("unused")
		ConnectionProperties _CP = DB.DBConnection();
	}

	@AfterTest
	public void Teardown() throws SQLException {

		_CP.rs.close();
		_CP.conn.close();
		_CP.stmt.close();

	}
	
	@SuppressWarnings("static-access")
	@Test
	public void VerifyOrderTransactionData() throws IOException, InterruptedException, SQLException, ParseException {

		for (Object record : JsonRecords)
		{		
         	@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());			
			map.put("created_at", new Date());
			map.put("processed_at", new Date());
			JSONObject OrderTransactionJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Order_Transaction);
			TimeUnit.SECONDS.sleep(10);
			_CP = DB.GetRecordFromDB(OrderTransactionJsonData,"Order_Transaction");
			if (_CP.rs.next()) {
				try {
					Assert.assertEquals(OrderTransactionJsonData.get("admin_graphql_api_id").toString(),_CP.rs.getString("admin_graphql_api_id").toString(), "admin_graphql_api_id not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("amount").toString(),_CP.rs.getString("amount").toString(), "amount not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("authorization").toString(),_CP.rs.getString("authorization_").toString(),"authorization not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("currency").toString(),_CP.rs.getString("currency").toString(),"currency not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("device_id").toString(),_CP.rs.getString("device_id").toString(),"device_id not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("error_code").toString(),_CP.rs.getString("error_code").toString(),"error_code not Match in Row" + Iteration);
 				    Assert.assertEquals(OrderTransactionJsonData.get("gateway").toString(),_CP.rs.getString("gateway").toString(),"gateway not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("kind").toString(),_CP.rs.getString("kind").toString(),"kind not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("location_id").toString(),_CP.rs.getString("location_id").toString(),"location_id not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("message").toString(),_CP.rs.getString("message").toString(),"message not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("order_id").toString(),_CP.rs.getString("order_id").toString(),"order_id not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("parent_id").toString(),_CP.rs.getString("parent_id").toString(),"parent_id not Match in Row" + Iteration);
					//JSON Object Handling of type {} splitting in different columns in db.
					JSONObject payment_details = (JSONObject) OrderTransactionJsonData.get("payment_details");
					Assert.assertEquals(payment_details.get("avs_result_code"),_CP.rs.getString("payment_details_avs_result_code"),"avs_result_code not Match in Row"+Iteration);
					Assert.assertEquals(payment_details.get("credit_card_bin"),_CP.rs.getString("payment_details_credit_card_bin"),"credit_card_bin not Match in Row"+Iteration);
					Assert.assertEquals(payment_details.get("credit_card_company"),_CP.rs.getString("payment_details_credit_card_company"),"credit_card_company not Match in Row"+Iteration);
					Assert.assertEquals(payment_details.get("credit_card_number"),_CP.rs.getString("payment_details_credit_card_number"),"credit_card_number not Match in Row"+Iteration);
					Assert.assertEquals(payment_details.get("cvv_result_code"),_CP.rs.getString("payment_details_cvv_result_code"),"cvv_result_code not Match in Row"+Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("receipt").toString(),_CP.rs.getString("receipt").toString(),"receipt not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("source_name").toString(),_CP.rs.getString("source_name").toString(),"source_name not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("status").toString(),_CP.rs.getString("status").toString(),"status not Match in Row" + Iteration);
					Assert.assertEquals(OrderTransactionJsonData.get("test").toString(),_CP.rs.getString("test").toString(),"test not Match in Row" + Iteration);
//null				Assert.assertEquals(OrderTransactionJsonData.get("user_id").toString(),_CP.rs.getString("user_id").toString(),"user_id not Match in Row" + Iteration);
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Order_Transaction);
					break;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,  WH.Order_Transaction);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration,  WH.Order_Transaction);
			}

		}
	}

}
