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

public class TenderTransaction {
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
		JsonRecords = EJ.CreteJSONFileFromExcel("./src/test/resources/DataSet.xls",WH.Tender_Transaction);

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
	public void VerifyTenderTransactionData() throws IOException, InterruptedException, SQLException, ParseException {
		System.out.println("<------------- Tender Transaction Verification Started ------------->");

		for (Object record : JsonRecords)
		{		
         	@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(record.toString(), Map.class);
			map.put("id", new Date().getTime());			
			map.put("processed_at", new Date());
			JSONObject TenderTransactionJsonData = HC.PostJson(mapper.writeValueAsString(map), WH.Tender_Transaction);
			TimeUnit.SECONDS.sleep(20);
			_CP = DB.GetRecordFromDB(TenderTransactionJsonData,"Tender_Transaction");
			if (_CP.rs.next()) {
				try {					
					Assert.assertEquals(TenderTransactionJsonData.get("amount").toString(),_CP.rs.getString("amount").toString(), "amount not Match in Row" + Iteration);
					Assert.assertEquals(TenderTransactionJsonData.get("currency").toString(),_CP.rs.getString("currency").toString(),"currency not Match in Row" + Iteration);					
//					Assert.assertEquals(TenderTransactionJsonData.get("order_id").toString(),_CP.rs.getString("order_id").toString(),"order_id not Match in Row" + Iteration);
				
					//JSON Object Handling of type {} splitting in different columns in db.
					JSONObject payment_details = (JSONObject) TenderTransactionJsonData.get("payment_details");
//null				Assert.assertEquals(payment_details.get("avs_result_code"),_CP.rs.getString("payment_details_avs_result_code"),"avs_result_code not Match in Row"+Iteration);
//null				Assert.assertEquals(payment_details.get("credit_card_bin"),_CP.rs.getString("payment_details_credit_card_bin"),"credit_card_bin not Match in Row"+Iteration);					
					Assert.assertEquals(payment_details.get("credit_card_company"),_CP.rs.getString("payment_details_credit_card_company"),"credit_card_company not Match in Row"+Iteration);					
					Assert.assertEquals(payment_details.get("credit_card_number"),_CP.rs.getString("payment_details_credit_card_number"),"credit_card_number not Match in Row"+Iteration);					
//null				Assert.assertEquals(payment_details.get("cvv_result_code"),_CP.rs.getString("payment_details_cvv_result_code"),"cvv_result_code not Match in Row"+Iteration);					
					
					Assert.assertEquals(TenderTransactionJsonData.get("payment_method").toString(), _CP.rs.getString("payment_method").toString(),"payment_method not Match in Row" + Iteration);
					Assert.assertEquals(TenderTransactionJsonData.get("remote_reference").toString(),_CP.rs.getString("remote_reference").toString(),"remote_reference not Match in Row" + Iteration);
					Assert.assertEquals(TenderTransactionJsonData.get("test").toString(),_CP.rs.getString("test").toString(),"test not Match in Row" + Iteration);
//null				Assert.assertEquals(OrderTransactionJsonData.get("user_id").toString(),_CP.rs.getString("user_id").toString(),"user_id not Match in Row" + Iteration);
				
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Iteration = Iteration + 1;
					EJ.SetFailureStatus(Iteration, WH.Tender_Transaction,ex.getMessage());
					continue;
				}
				Iteration = Iteration + 1;
				EJ.SetPassStatus(Iteration,  WH.Tender_Transaction);

			} else {
				Iteration = Iteration + 1;
				EJ.SetFailureStatus(Iteration,  WH.Tender_Transaction,null);
			}

		}
		System.out.println("<------------- Tender Transaction Verification Ended ------------->");

	}

}
